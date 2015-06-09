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
import UserAPI.UserApi;
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

    private ArrayList<UserAPI.Location> mLocations;
    private ImageView mThumbImageView;
    private File mImage;
    private ProgressDialog pd;
    private ArrayAdapter mLocationAdapter;
    private ArrayAdapter mWildlifeAdapter;
    private Location mLastLocation;
    private CustomLocationService mLocationService;
    private Spinner mLocationSpinner;
    private Spinner mWildlifeSpinner;
    private EditText mBlurbEditText;
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
        mLocations = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_lodge_report, container, false);

        mLocationAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, mLocations);
        mLocationSpinner = (Spinner) view.findViewById(R.id.reportLocationSpinner);
        mLocationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLocationSpinner.setAdapter(mLocationAdapter);

        // Wildlife type populated from resource string array for now.
        // ToDo: Implement server side wildlife model, store locally for selection.
        mWildlifeSpinner = (Spinner) view.findViewById(R.id.reportWildlifeTypeSpinner);
        mWildlifeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.wildlife_array,
                android.R.layout.simple_spinner_item);
        mWildlifeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWildlifeSpinner.setAdapter(mWildlifeAdapter);

        // User entered notes/ blurb
        mBlurbEditText = (EditText) view.findViewById(R.id.reportNotesEditText);
        // Thumbnail displaying image after being taken
        mThumbImageView = (ImageView) view.findViewById(R.id.reportImageView);

        // Camera image button
        ImageButton photoButton = (ImageButton) view.findViewById(R.id.reportPhotoButton);
        photoButton.setOnClickListener(new CameraButtonClick());

        ImageButton submitButton = (ImageButton) view.findViewById(R.id.reportSubmitButton);
        submitButton.setOnClickListener(new SubmitButtonClick());

        getLocations();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                mThumbImageView.setImageBitmap(BitmapFactory.decodeFile(mImage.getPath()));
            }
        }
    }

    // Encode image to base64, get user entered details, encode JSON, send.
    private void submitReport()
    {
        Report report = new Report();
        UserAPI.Location l = (UserAPI.Location) mLocationSpinner.getSelectedItem();

        report.setLatitude(mLastLocation.getLatitude());
        report.setLongitude(mLastLocation.getLongitude());

        ImageAttachment img = new ImageAttachment();
        img.setImageData(getBase64(mImage));
        img.setImageContentType("image/jpeg");
        img.setImageFilename(mImage.getName());
        report.setImage(img);

        report.setLocationId(l.getId());
        report.setBlurb(mBlurbEditText.getText().toString());
        report.setUserId(1); // No user registration avaiable yet, set to 1.
        //report.setAnimalId(2);  // Not implemented server side

        // Format date to API specification. Includes TimeZone so API knows how to store in UTC.
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
    // ToDo: Look into multipart post later
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
                mLocations = new ArrayList<UserAPI.Location>();  // <-- create empty on failure for now
                Toast.makeText(getActivity(), "Couldn't obtain locations",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(mLocationReceiver);
    }

    private class SubmitButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(mImage == null)
                Toast.makeText(getActivity(), "Please take a photo", Toast.LENGTH_SHORT).show();
            else if(mLastLocation == null)
                Toast.makeText(getActivity(), "Geo-Location cannot be obtained", Toast.LENGTH_SHORT)
                        .show();
            else
                submitReport();
        }
    }

    private class CameraButtonClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            takeAPhoto();
        }
    }
}
