package bit.stewasc3.dogbeaches;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import UserAPI.Location;
import UserAPI.Report;
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
    ArrayList<Report> mReports;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mLocation = (Location) args.getSerializable(KEY_LOCATION);

        RestClient.get().getReports(mLocation.getId(), new Callback<ArrayList<Report>>()
        {
            @Override
            public void success(ArrayList<Report> reports, Response response)
            {
                mReports = reports;
                mReportListView.setAdapter(new ReportListAdapter(mReports));
            }

            @Override
            public void failure(RetrofitError error)
            {
            }
        });
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

        ImageView iv = (ImageView)v.findViewById(R.id.locationImage);
        Picasso.with(getActivity()).load(mLocation.getImageMedium()).into(iv);

        TextView nameTextView = (TextView)v.findViewById(R.id.locationNameTextView);
        nameTextView.setText(mLocation.getName());

        TextView dogStatusTextView = (TextView)v.findViewById(R.id.locationDogStatusTextView);
        dogStatusTextView.setText(dogStatusTextView.getText() + mLocation.getDogStatus());

        mReportListView = (ListView)v.findViewById(R.id.locationReportListView);

        return v;
    }

    public class ReportListAdapter extends ArrayAdapter
    {
        public ReportListAdapter(ArrayList<Report>reports) { super(getActivity(), 0,reports);}

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.location_report_list_item, null);
            }

            Report r = (Report) getItem(position);

            TextView reportTitle = (TextView) convertView.findViewById(R.id.reportListTitleTextView);
            reportTitle.setText("Report #" + r.getId());

            TextView reportBlurb = (TextView) convertView.findViewById(R.id.reportListAnimalTextView);
            reportBlurb.setText(r.getBlurb());

            ImageView reportThumb = (ImageView) convertView.findViewById(R.id.reportListThumbImageView);
            Picasso.with(getActivity()).load(r.getImageThumb()).into(reportThumb);

            return convertView;
        }
    }
}
