package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
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
import UserAPI.Report;
import UserAPI.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ReportFragment extends Fragment
{
    private static final int REQUEST_IMAGE_CODE = 100;

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
    private Location mLastLocation;
    private CustomLocationService mLocationService;
    private BroadcastReceiver mLocationReceiver = new LocationReceiver()
    {
        @Override
        protected void onLocationReceived(Context context, Location l)
        {
            mLastLocation = l;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(mLocationReceiver,
                new IntentFilter(CustomLocationService.ACTION_LOCATION));
        mLocationService = CustomLocationService.get(getActivity());
        mLocationService.startUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_lodge_report, container, false);

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
                else if(mLastLocation == null)
                {
                    Toast.makeText(getActivity(), "Location could not be obtained", Toast.LENGTH_SHORT)
                            .show();
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
        Report report = new Report();

        if(mLastLocation == null)
        {
            Toast.makeText(getActivity(), "Cannot obtain GPS Location at this time.", Toast.LENGTH_SHORT)
                .show();
        }
        else
        {
            // TODO: Change User API to accept lat/long in JSON payload, rather than POINT() string
            String point = "POINT (" + Double.toString(mLastLocation.getLatitude()) +
                    " " + Double.toString(mLastLocation.getLongitude()) + ")";
            report.setGeolocation(point);
        }

        ImageAttachment img = new ImageAttachment();
        img.setImageData(getBase64(mImage));
        img.setImageContentType("image/jpeg");
        img.setImageFilename(mImage.getName());
        report.setImage(img);

        report.setLocationId(l.getId());
        report.setBlurb(blurb);
        report.setUserId(1); // No user registration avaiable yet, set to 1.
        //report.setAnimalId(2);  // Not implemented server side

        // Send UTC value to API
        Date d = new Date();
        report.setSubmittedAt(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(new Date()));

        // Progress spinner while report uploads
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Submitting...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        RestClient.get().createReport(report, new Callback<Report>()
        {
            @Override
            public void success(Report report, Response response)
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
    }

    // Base64 available for SDK 8 + Only
    // ToDo: Look into multipart post, quality constant
    private String getBase64(File image)
    {
        Bitmap bm = BitmapFactory.decodeFile(image.getPath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(mLocationReceiver);
    }
}
