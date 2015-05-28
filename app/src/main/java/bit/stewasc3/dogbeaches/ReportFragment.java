package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import UserAPI.ImageAttachment;
import UserAPI.ReportSubmit;
import UserAPI.RestClient;
import UserAPI.UserApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportFragment extends Fragment
{
    private static final int REQUEST_IMAGE_CODE = 100;

    private LocationManager lm;


    /* Start obtaining users location when fragment instantiates. Populate Location list with
        locations close to the user. At the moment, this will jsut pull in the entire location list
        for testing purposes. Populate wildlife list with options. Take a photo, down sample the
        photo for transmission. Submit to server.
        ToDo: Backup plan if user location not available
     */

    // Locations prefixed with UserAPI (Collision with android.location)
    private ArrayList<UserAPI.Location> mLocations;
    private ImageView mThumbImageView;
    private File mImage;
    private ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Start listening for location updates while user completes report
        lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new ReportLocationListener();
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        final ArrayAdapter locationAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, new ArrayList<UserAPI.Location>());
        final Spinner locationSpinner = (Spinner) view.findViewById(R.id.reportLocationSpinner);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        // Eventually, we will query the rest client for beaches close to current location, for now
        // populate list box with all locations from server. ToDo: refactor
        RestClient.get().getAllLocations(new Callback<ArrayList<UserAPI.Location>>()
        {
            @Override
            public void success(ArrayList<UserAPI.Location> locations, Response response)
            {
                for(UserAPI.Location l : locations)
                {
                    locationAdapter.add(l);
                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error)
            {
                mLocations = new ArrayList<UserAPI.Location>();  // <-- create empty on failure for now
                Toast.makeText(getActivity(), "Couldn't obtain locations",
                        Toast.LENGTH_LONG).show();
            }
        });

        // Wildlife type populated from resource string array for now.
        final Spinner wildlifeSpinner = (Spinner) view.findViewById(R.id.reportWildlifeTypeSpinner);
        ArrayAdapter wildlifeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.wildlife_array,
                android.R.layout.simple_spinner_item);
        wildlifeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wildlifeSpinner.setAdapter(wildlifeAdapter);

        // User entered notes/ blurb
        final EditText blurbEditText = (EditText) view.findViewById(R.id.reportNotesEditText);

        // Camera image button
        ImageButton photoButton = (ImageButton) view.findViewById(R.id.reportPhotoButton);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                takeAPhoto();
            }
        });

        // Thumbnail displaying image after being taken
        mThumbImageView = (ImageView) view.findViewById(R.id.reportImageView);

        ImageButton submitButton = (ImageButton) view.findViewById(R.id.reportSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mImage == null)
                {
                    Toast.makeText(getActivity(), "Please take a photo", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UserAPI.Location l = (UserAPI.Location) locationSpinner.getSelectedItem();
                    String animal = (String) wildlifeSpinner.getSelectedItem();
                    String blurb = blurbEditText.getText().toString();
                    submitReport(l, animal, blurb);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                // Set the thumnbail
                mThumbImageView.setImageBitmap(BitmapFactory.decodeFile(mImage.getPath()));
            }
        }
        else
        {
            // ToDo: Handle this
        }
    }

    // Encode image to base64, get user entered details, encode JSON, send.
    private void submitReport(UserAPI.Location l, String animal, String blurb)
    {
        ReportSubmit report = new ReportSubmit();

        if (mImage != null)
        {
            ImageAttachment img = new ImageAttachment();
            img.setImageData(getBase64(mImage));
            img.setImageContentType("image/jpeg");
            img.setImageFilename(mImage.getName());
            report.setImage(img);
        }

        // We'll use item ID's for now from locations, but this will need changing for considering
        // offline operation.
        Location lastKnown = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        report.setLocationId(l.getId());
        report.setBlurb(blurb);
        report.setUserId(1);
        //report.setAnimalId(2);  // Not implemented server side

        // Create a POINT for PostGIS datatype TODO: Tidy, embed in ReportSubmit class perhaps
        // Change api interface to accept seperate lat long values.
        String point = "POINT (" + Double.toString(lastKnown.getLatitude()) +
                " " + Double.toString(lastKnown.getLongitude()) + ")";
        report.setGeolocation(point);

        // Progress spinner while report uploads
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Submitting...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        RestClient.get().createReport(report, new Callback<ReportSubmit>()
        {
            @Override
            public void success(ReportSubmit reportSubmit, Response response)
            {
                Toast.makeText(getActivity(), "Report uploaded!", Toast.LENGTH_LONG).show();
                pd.dismiss();
                getFragmentManager().popBackStackImmediate();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), "Report upload failed", Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
        // Progress spinner
    }

    // SDK 8 + Only
    private String getBase64(File image)
    {
        Bitmap bm = BitmapFactory.decodeFile(image.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Todo: Quality constant (80)
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    private void takeAPhoto()
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            mImage = createImageFile();
        }
        catch (IOException e)
        {
            Log.d("Image File", e.toString());
        }

        if (mImage != null)
        {
            // Saves to mImage
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImage));
            startActivityForResult(i, REQUEST_IMAGE_CODE);
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "REPORT_" + timeStamp + "_";
        File folder = new File(Environment.getExternalStorageDirectory(), "DogAppImages");
        folder.mkdir();
        File image = File.createTempFile(imageFileName, ".jpg", folder);
        return image;
    }


    private class ReportLocationListener implements LocationListener
    {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }

        @Override
        public void onLocationChanged(Location location)
        {

        }
    }
}
