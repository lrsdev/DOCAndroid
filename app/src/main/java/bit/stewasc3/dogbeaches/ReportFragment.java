package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private ArrayAdapter mLocationAdapter;
    private ArrayAdapter mWildlifeAdapter;
    private ImageView mThumbImageView;
    private File mImage;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Start listening for location updates while user completes report
        lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 0, new ReportLocationListener());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        // Eventually, we will query the rest client for beaches close to current location, for now
        // populate list box with all locations from server.
        RestClient.get().getAllLocations(new Callback<ArrayList<UserAPI.Location>>()
        {
            @Override
            public void success(ArrayList<UserAPI.Location> locations, Response response)
            {
                mLocations = locations;
            }

            @Override
            public void failure(RetrofitError error)
            {
                mLocations = new ArrayList<UserAPI.Location>();  // <-- create empty on failure for now
                Toast.makeText(getActivity(), "Couldn't obtain locations",
                        Toast.LENGTH_LONG).show();
            }
        });

        final Spinner locationSpinner = (Spinner) view.findViewById(R.id.reportLocationSpinner);
        mLocationAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_item, mLocations);
        mLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(mLocationAdapter);

        // Wildlife type populated from resource string array for now.
        final Spinner wildlifeSpinner = (Spinner) view.findViewById(R.id.reportWildlifeTypeSpinner);
        mWildlifeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.wildlife_array,
                android.R.layout.simple_spinner_item);
        mWildlifeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wildlifeSpinner.setAdapter(mWildlifeAdapter);


        // Camera image button
        Button photoButton = (Button) view.findViewById(R.id.reportPhotoButton);
        photoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                takeAPhoto();
            }
        });

        mThumbImageView = (ImageView) view.findViewById(R.id.reportImageView);

        Button submitButton = (Button) view.findViewById(R.id.reportSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserAPI.Location l = (UserAPI.Location)locationSpinner.getSelectedItem();
                String animal = (String)wildlifeSpinner.getSelectedItem();
                submitReport(l, animal);
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
    private void submitReport(UserAPI.Location l, String animal)
    {
        if (mImage != null)
        {
            // Get a base64 string
        }

        // We'll use item ID's for now from locations, but this will need changing for considering
        // offline operation.

        int location_id = l.getId();
        Location lastKnown = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

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
