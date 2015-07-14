package bit.stewasc3.dogbeaches;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import UserAPI.Location;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationInfoFragment extends Fragment
{
    public static final String KEY_LOCATION = "dogapp.location";
    Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mLocation = (Location) args.getSerializable(KEY_LOCATION);
    }

    public static LocationInfoFragment newInstance(Location l)
    {
        Bundle args = new Bundle();
        args.putSerializable(KEY_LOCATION, l);
        LocationInfoFragment f = new LocationInfoFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_location_info, container, false);

        ImageView iv = (ImageView) v.findViewById(R.id.locationInfoImage);
        Picasso.with(getActivity()).load(mLocation.getImage().getMedium()).into(iv);

        TextView nameTextView = (TextView) v.findViewById(R.id.locationInfoNameTextView);
        nameTextView.setText(mLocation.getName());

        TextView blurbTextView = (TextView) v.findViewById(R.id.locationInfoBlurbTextView);
        blurbTextView.setText(mLocation.getBlurb());

        TextView guidelinesTextView = (TextView) v.findViewById(R.id.locationInfoDogGuidelinesTextView);
        guidelinesTextView.setText(mLocation.getDogStatus().getGuidelines());

        return v;
    }


}
