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

        return v;
    }
}
