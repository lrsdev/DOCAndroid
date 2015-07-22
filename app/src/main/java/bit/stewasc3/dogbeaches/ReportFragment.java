package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import UserAPI.RestClient;
import UserAPI.Sighting;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ReportFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private static final int REQUEST_IMAGE_CODE = 100;

    /* Start obtaining users location when fragment instantiates. Populate Location list with
        locations close to the user. At the moment, this will jsut pull in the entire location list
        for testing purposes. Populate wildlife list with options. Take a photo, down sample the
        photo for transmission. Submit to server.
        ToDo: Backup plan if user location not available
     */

    private ArrayList<UserAPI.Location> mLocations;
    private ImageButton mImageButton;
    private ProgressDialog pd;
    private ArrayAdapter mLocationAdapter;
    private ArrayAdapter mWildlifeAdapter;
    private Location mLastLocation;
    private Spinner mLocationSpinner;
    private Spinner mWildlifeSpinner;
    private EditText mBlurbEditText;
    private GoogleApiClient mGoogleApiClient;
    private File mPhotoFile;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        mLocations = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        mLocationAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mLocations);
        mLocationSpinner = (Spinner) view.findViewById(R.id.reportLocationSpinner);
        mLocationSpinner.setAdapter(mLocationAdapter);

        // Wildlife type populated from resource string array for now.
        // ToDo: Implement server side wildlife model, store locally for selection.
        mWildlifeSpinner = (Spinner) view.findViewById(R.id.reportWildlifeTypeSpinner);
        mWildlifeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.wildlife_array,
                android.R.layout.simple_spinner_dropdown_item);
        mWildlifeSpinner.setAdapter(mWildlifeAdapter);

        // User entered notes/ blurb
        mBlurbEditText = (EditText) view.findViewById(R.id.reportNotesEditText);

        // Camera image button
        mImageButton = (ImageButton) view.findViewById(R.id.reportImageButton);
        mImageButton.setOnClickListener(new ImageButtonClick());

        Button submitButton = (Button) view.findViewById(R.id.reportSubmitButton);
        submitButton.setOnClickListener(new SubmitButtonClick());

        getLocations();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK)
        {
            Picasso.with(getActivity()).load(mPhotoFile).into(mImageButton);
        }
    }

    private void submitReport()
    {
        UserAPI.Location l = (UserAPI.Location) mLocationSpinner.getSelectedItem();

        Integer locationId = l.getId();
        double lat = mLastLocation.getLatitude();
        double longi = mLastLocation.getLongitude();
        TypedFile tf = new TypedFile("image/jpeg", mPhotoFile);
        String blurb = mBlurbEditText.getText().toString();
        String submitted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(new Date());
        String animalType = (String) mWildlifeSpinner.getSelectedItem();

        // Progress spinner while report uploads
        pd = new ProgressDialog(getActivity());
        pd.setTitle("Submitting...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();

        RestClient.get().createReport(tf, locationId, submitted, blurb, lat, longi, animalType, new Callback<Sighting>()
        {
            @Override
            public void success(Sighting s, Response response)
            {
                Toast.makeText(getActivity(), "Report uploaded!", Toast.LENGTH_LONG).show();
                pd.dismiss();
                getFragmentManager().popBackStackImmediate();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity(), "Report upload failed", Toast.LENGTH_LONG).show();
                Log.d("RETROFIT", error.toString());
                pd.dismiss();
            }
        });
    }

    private void takeAPhoto()
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            mPhotoFile = createImageFile();
        }
        catch (IOException e)
        {
            Log.d("Image File", e.toString());
        }

        if (mPhotoFile != null)
        {
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(i, REQUEST_IMAGE_CODE);
        }
    }

    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "REPORT_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists())
            storageDir.mkdir();
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        return image;
    }

    // Get locations to populate location list. This will be refined later to locations close to
    // user. If request is successful, add items to location array and notify the adapter.
    private void getLocations()
    {
        RestClient.get().getAllLocations(new Callback<ArrayList<UserAPI.Location>>()
        {
            @Override
            public void success(ArrayList<UserAPI.Location> locations, Response response)
            {
                mLocations.addAll(locations);
                mLocationAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error)
            {
                mLocations = new ArrayList<>();  // <-- create empty on failure for now
                Toast.makeText(getActivity(), "Couldn't obtain locations",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getAnimals()
    {

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
       mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    private class SubmitButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(mPhotoFile== null)
            {
                Toast.makeText(getActivity(), "Please take a photo", Toast.LENGTH_SHORT).show();
            }

            else if(mLastLocation == null)
                Toast.makeText(getActivity(), "Geo-Location cannot be obtained", Toast.LENGTH_SHORT)
                        .show();
            else
                submitReport();
        }
    }

    private class ImageButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            takeAPhoto();
        }
    }
}
