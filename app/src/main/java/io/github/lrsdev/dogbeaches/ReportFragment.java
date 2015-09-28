package io.github.lrsdev.dogbeaches;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.lrsdev.dogbeaches.R;
import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;
import io.github.lrsdev.dogbeaches.db.DBHelper;
import io.github.lrsdev.dogbeaches.db.ReportTable;

public class ReportFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "Report";
    private static final int REQUEST_IMAGE_CODE = 100;
    private static final int LOADER_ANIMAL = 1;
    private static final int LOADER_LOCATION = 2;
    private LocationCursorAdapter mLocationAdapter;
    private AnimalCursorAdapter mAnimalAdapter;
    private Location mLastLocation;
    private Spinner mLocationSpinner;
    private Spinner mAnimalSpinner;
    private EditText mBlurbEditText;
    private String mCurrentPhotoPath;
    private Button mPhotoButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLastLocation = LocationManager.get(getActivity()).getLocation();
        getLoaderManager().initLoader(LOADER_LOCATION, null, this);
        getLoaderManager().initLoader(LOADER_ANIMAL, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        mLocationSpinner = (Spinner) v.findViewById(R.id.reportLocationSpinner);
        mLocationAdapter = new LocationCursorAdapter();
        mLocationSpinner.setAdapter(mLocationAdapter);

        mAnimalSpinner = (Spinner) v.findViewById(R.id.reportAnimalSpinner);
        mAnimalAdapter = new AnimalCursorAdapter();
        mAnimalSpinner.setAdapter(mAnimalAdapter);

        mBlurbEditText = (EditText) v.findViewById(R.id.reportNotesEditText);

        Button submitButton = (Button) v.findViewById(R.id.reportSubmitButton);
        submitButton.setOnClickListener(new SubmitButtonClick());

        mPhotoButton = (Button) v.findViewById(R.id.photoButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                takeAPhoto();
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Report Wildlife");
    }

    private void submitReport()
    {
        // ToDo: Show a confirmation dialog
        mLastLocation = LocationManager.get(getActivity()).getLocation();

        long locationId = mLocationSpinner.getSelectedItemId();
        long animalId = mAnimalSpinner.getSelectedItemId();
        double lat = mLastLocation.getLatitude();
        double lon = mLastLocation.getLongitude();
        String blurb = mBlurbEditText.getText().toString();
        String created_at = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").format(new Date());

        // Copy image to internal dir so user cannot accidentally delete the image before sync
        // back to server.
        String imagePath = copyImage();

        SQLiteDatabase db = new DBHelper(getActivity()).getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ReportTable.COLUMN_ANIMAL_ID, animalId);
        cv.put(ReportTable.COLUMN_LOCATION_ID, locationId);
        cv.put(ReportTable.COLUMN_BLURB, blurb);
        cv.put(ReportTable.COLUMN_IMAGE, imagePath);
        cv.put(ReportTable.COLUMN_LATITUDE, lat);
        cv.put(ReportTable.COLUMN_LONGITUDE, lon);
        cv.put(ReportTable.COLUMN_CREATED_AT, created_at);

        db.insert(ReportTable.TABLE_NAME, null, cv);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(null, BuildConfig.AUTHORITY, bundle);

        Toast.makeText(getActivity(), "Thank you, we will notify you once your report has been uploaded.",
                Toast.LENGTH_LONG).show();

        getFragmentManager().popBackStackImmediate();
    }

    private void takeAPhoto()
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(i.resolveActivity(getActivity().getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException e)
            {
                Log.d("Image File", e.toString());
            }
            if (photoFile != null)
            {
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(i, REQUEST_IMAGE_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CODE && resultCode == Activity.RESULT_OK)
        {
            Toast.makeText(getActivity(), "Photo Taken", Toast.LENGTH_SHORT).show();
            mPhotoButton.setText("Retake Photo");
        }
    }

    public String copyImage()
    {
        InputStream in = null;
        OutputStream out = null;
        File current = new File(mCurrentPhotoPath);
        File newFile = new File(getActivity().getFilesDir(), current.getName());
        try
        {
            in = new FileInputStream(current);
            out = new FileOutputStream(newFile);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
            {
                out.write(buf, 0, len);
            }
        }
        catch(IOException e)
        {
            Log.d(TAG, e.toString());
        }
        finally
        {
            try
            {
                in.close();
                out.close();
            }
            catch (Exception e)
            {

            }
        }
        return newFile.getAbsolutePath();
    }

    /**
     * Creates a file to save captured image to
     * @return File
     * @throws IOException
     */
    private File createImageFile() throws IOException
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "REPORT_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists())
            storageDir.mkdir();
        File f = new File(storageDir, fileName);
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle)
    {
        String[] projection = null;
        Uri uri = null;
        String orderBy = null;

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
                Double lat = mLastLocation.getLatitude();
                Double lon = mLastLocation.getLongitude();
                orderBy = "abs(latitude - " + Double.toString(lat) + ") " +
                        "+ abs(longitude - " + Double.toString(lon) + ") LIMIT 5";
                break;
        }

       return new CursorLoader(getActivity(), uri, projection, null, null, orderBy);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor c)
    {
        Cursor otherCursor = addOtherToCursor(c);
        switch(loader.getId())
        {
            case LOADER_ANIMAL:
                mAnimalAdapter.changeCursor(otherCursor);
                break;
            case LOADER_LOCATION:
                mLocationAdapter.changeCursor(otherCursor);
                break;
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader)
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

    private Cursor addOtherToCursor(Cursor c)
    {
        MatrixCursor other = new MatrixCursor(new String[] {"_id", "name"});
        other.addRow(new String[] {null, "Other"});
        Cursor[] cursors = { c, other };
        return new MergeCursor(cursors);
    }

    private class SubmitButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(mCurrentPhotoPath == null)
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

    private class AnimalCursorAdapter extends SimpleCursorAdapter
    {

        public AnimalCursorAdapter()
        {
            super(getActivity(), android.R.layout.simple_spinner_dropdown_item, null, new String[]{DogBeachesContract.Animals.COLUMN_NAME}, new int[]{android.R.id.text1}, SimpleCursorAdapter.NO_SELECTION);
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
