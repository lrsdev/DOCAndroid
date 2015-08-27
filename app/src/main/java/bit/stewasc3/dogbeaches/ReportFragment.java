package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
import android.widget.SimpleCursorAdapter;
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
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class ReportFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor>
{
    private static final int REQUEST_IMAGE_CODE = 100;
    private static final int LOADER_ANIMAL = 1;
    private static final int LOADER_LOCATION = 2;

    /* Start obtaining users location when fragment instantiates. Populate Location list with
        locations close to the user. At the moment, this will jsut pull in the entire location list
        for testing purposes. Populate wildlife list with options. Take a photo, down sample the
        photo for transmission. Submit to server.
        ToDo: Backup plan if user location not available
     */

    private ArrayList<UserAPI.Location> mLocations;
    private ImageButton mImageButton;
    private ProgressDialog pd;
    private LocationCursorAdapter mLocationAdapter;
    private AnimalCursorAdapter mAnimalAdapter;
    private Location mLastLocation;
    private Spinner mLocationSpinner;
    private Spinner mAnimalSpinner;
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


        mLocationSpinner = (Spinner) view.findViewById(R.id.reportLocationSpinner);
        mLocationAdapter = new LocationCursorAdapter();
        mLocationSpinner.setAdapter(mLocationAdapter);

        mAnimalSpinner = (Spinner) view.findViewById(R.id.reportAnimalSpinner);
        mAnimalAdapter = new AnimalCursorAdapter();
        mAnimalSpinner.setAdapter(mAnimalAdapter);

        mBlurbEditText = (EditText) view.findViewById(R.id.reportNotesEditText);

        mImageButton = (ImageButton) view.findViewById(R.id.reportImageButton);
        mImageButton.setOnClickListener(new ImageButtonClick());

        Button submitButton = (Button) view.findViewById(R.id.reportSubmitButton);
        submitButton.setOnClickListener(new SubmitButtonClick());

        getLoaderManager().initLoader(LOADER_LOCATION, null, this);
        getLoaderManager().initLoader(LOADER_ANIMAL, null, this);
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
        long locationId = mLocationSpinner.getSelectedItemId();
        long animalId = mAnimalSpinner.getSelectedItemId();
        double lat = mLastLocation.getLatitude();
        double lon = mLastLocation.getLongitude();
        String blurb = mBlurbEditText.getText().toString();
        String submitted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(new Date());
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

    @Override
    public Loader onCreateLoader(int i, Bundle bundle)
    {
        String[] projection = null;
        Uri uri = null;
        switch(i)
        {
            case LOADER_ANIMAL:
                projection = new String[] { DogBeachesContract.Animals.COLUMN_ID,
                    DogBeachesContract.Animals.COLUMN_NAME };
                uri = DogBeachesContract.Animals.CONTENT_URI;
                break;
            case LOADER_LOCATION:
                projection = new String[] { DogBeachesContract.Locations.COLUMN_ID,
                    DogBeachesContract.Locations.COLUMN_NAME };
                uri = DogBeachesContract.Locations.CONTENT_URI;
                break;
        }

       return new CursorLoader(getActivity(), uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor c)
    {
        switch(loader.getId())
        {
            case LOADER_ANIMAL:
                mAnimalAdapter.changeCursor(c);
                break;
            case LOADER_LOCATION:
                mLocationAdapter.changeCursor(c);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader)
    {
        switch(loader.getId())
        {
            case LOADER_ANIMAL:
                mAnimalAdapter.changeCursor(null);
                break;
            case LOADER_LOCATION:
                mLocationAdapter.changeCursor(null);
                break;
        }
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

    private class AnimalCursorAdapter extends SimpleCursorAdapter
    {

        public AnimalCursorAdapter()
        {
            super(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    null,
                    new String[] {DogBeachesContract.Animals.COLUMN_NAME},
                    new int[] {android.R.id.text1},
                    SimpleCursorAdapter.NO_SELECTION);
        }
    }

    private class LocationCursorAdapter extends SimpleCursorAdapter
    {
        public LocationCursorAdapter()
        {
            super(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item,
                    null,
                    new String[] {DogBeachesContract.Locations.COLUMN_NAME},
                    new int[] {android.R.id.text1},
                    SimpleCursorAdapter.NO_SELECTION);
        }
    }
}
