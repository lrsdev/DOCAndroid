package bit.stewasc3.dogbeaches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import UserAPI.Location;
import UserAPI.Sighting;
import UserAPI.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rickiekewene on 21/05/15.
 */

//This class will extend LocationFragment when implemented correctly
public class LocationFragment extends Fragment
{

    public static final String KEY_LOCATION = "dogapp.location";
    Location mLocation;
    ListView mReportListView;
    ArrayList<Sighting> mSightings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mLocation = (Location) args.getSerializable(KEY_LOCATION);

        // Debugging lifecycles
        Log.d("LocationFragment", "onCreate called for " + Integer.toString(mLocation.getId()));
    }

    public static LocationFragment newInstance(Location l)
    {
        Bundle args = new Bundle();
        args.putSerializable(KEY_LOCATION, l);
        LocationFragment f = new LocationFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: Find out what false is really doing
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        mReportListView = (ListView)v.findViewById(R.id.locationReportListView);
        mReportListView.setOnItemClickListener(new ReportListItemClick());

        ImageView iv = (ImageView)v.findViewById(R.id.locationImage);
        Picasso.with(getActivity()).load(mLocation.getImageMedium()).into(iv);

        TextView nameTextView = (TextView)v.findViewById(R.id.locationNameTextView);
        nameTextView.setText(mLocation.getName());

        TextView dogStatusTextView = (TextView)v.findViewById(R.id.locationDogStatusTextView);
        dogStatusTextView.setText(dogStatusTextView.getText() + mLocation.getDogStatus());

        RestClient.get().getReports(mLocation.getId(), new Callback<ArrayList<Sighting>>()
        {
            @Override
            public void success(ArrayList<Sighting> sightings, Response response)
            {
                mSightings = sightings;
                mReportListView.setAdapter(new ReportListAdapter(sightings));
            }

            @Override
            public void failure(RetrofitError error)
            {
            }
        });
        return v;
    }

    public class ReportListAdapter extends ArrayAdapter
    {
        public ReportListAdapter(ArrayList<Sighting> sightings) { super(getActivity(), 0, sightings);}

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.location_report_list_item, null);
            }

            Sighting r = (Sighting) getItem(position);

            TextView reportTitle = (TextView) convertView.findViewById(R.id.reportListTitleTextView);
            reportTitle.setText("Animal Name");

            TextView reportBlurb = (TextView) convertView.findViewById(R.id.reportListAnimalTextView);
            reportBlurb.setText(r.getBlurb());

            TextView reportDate = (TextView) convertView.findViewById(R.id.reportListDate);
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            reportDate.setText(sf.format(r.getSubmittedAt()));

            ImageView reportThumb = (ImageView) convertView.findViewById(R.id.reportListThumbImageView);
            Picasso.with(getActivity()).load(r.getImageThumb()).into(reportThumb);

            return convertView;
        }
    }

    public class ReportListItemClick implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            Intent i = new Intent(getActivity(), SightingPagerActivity.class);
            i.putExtra(SightingPagerActivity.KEY_SIGHTING_ARRAY, mSightings);
            i.putExtra(SightingPagerActivity.KEY_SIGHTING_INDEX, position);
            startActivity(i);
        }
    }
}
