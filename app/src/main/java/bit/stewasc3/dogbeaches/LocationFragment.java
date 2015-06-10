package bit.stewasc3.dogbeaches;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import UserAPI.Location;
import UserAPI.Report;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: Find out what false is really doing
        View v = inflater.inflate(R.layout.fragment_location, container, false);

        ImageView iv = (ImageView) v.findViewById(R.id.locationImage);
        Picasso.with(getActivity()).load(mLocation.getImageMedium()).into(iv);

        TextView nameTextView = (TextView) v.findViewById(R.id.locationNameTextView);
        nameTextView.setText(mLocation.getName());

        TextView guideLinesTextView = (TextView) v.findViewById(R.id.locationGuidelinesTextView);
        guideLinesTextView.setText(mLocation.getDogGuidelines());

        ImageView dogIconImageView = (ImageView) v.findViewById(R.id.locationDogIconImageView);

        // Move to a static helper method later
        switch(mLocation.getDogStatus())
        {
            case "no dogs":
                dogIconImageView.setImageResource(R.drawable.nodogs);
                break;
            case "on lead":
                dogIconImageView.setImageResource(R.drawable.dogonlead);
                break;
            case "off lead":
                dogIconImageView.setImageResource(R.drawable.dogofflead);
                break;
        }

        final LinearLayout reportContainer = (LinearLayout) v.findViewById(R.id.locationReportContainer);

        // If Location has reports, populate the report container.
        // Later, this will be limited to X amount of most recent reports.
        if(mLocation.getSightings() != null)
            populateReportContainer(reportContainer, mLocation.getSightings());

        // ToDo: Consider loading this with location data in location container, lazy loading here
        // can potentially cause issues if user scrolls too fast between screens.
        return v;
    }

    private void populateReportContainer(LinearLayout reportContainer, final ArrayList<Sighting> sightings)
    {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        for(int i = 0; i < sightings.size(); i++)
        {
            Sighting s = sightings.get(i);
            View convertView = inflater.inflate(R.layout.location_report_list_item, null);

            TextView reportTitle = (TextView) convertView.findViewById(R.id.reportListTitleTextView);
            reportTitle.setText("Animal Name");

            TextView reportBlurb = (TextView) convertView.findViewById(R.id.reportListAnimalTextView);
            reportBlurb.setText(s.getBlurb());

            TextView reportDate = (TextView) convertView.findViewById(R.id.reportListDate);
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            reportDate.setText(sf.format(s.getSubmittedAt()));

            ImageView reportThumb = (ImageView) convertView.findViewById(R.id.reportListThumbImageView);
            Picasso.with(getActivity()).load(s.getImageThumb()).into(reportThumb);

            // Hack so I can use the stupid value in inner class
            final int index = i;
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getActivity(), SightingPagerActivity.class);
                    intent.putExtra(SightingPagerActivity.KEY_SIGHTING_ARRAY, sightings);
                    intent.putExtra(SightingPagerActivity.KEY_SIGHTING_INDEX, index);
                    startActivity(intent);
                }
            });

            reportContainer.addView(convertView);
        }
    }
}
