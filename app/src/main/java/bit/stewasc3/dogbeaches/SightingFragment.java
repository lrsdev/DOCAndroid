package bit.stewasc3.dogbeaches;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import UserAPI.Sighting;

/**
 * Created by samuel on 5/06/15.
 */
public class SightingFragment extends Fragment
{
    public static final String KEY_SIGHTING = "dogapp.report";
    private Sighting mSighting;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mSighting = (Sighting)getArguments().getSerializable(KEY_SIGHTING);
    }

    public static Fragment newInstance(Sighting s)
    {
        Bundle args = new Bundle();
        args.putSerializable(KEY_SIGHTING, s);
        Fragment f = new SightingFragment();
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_sighting, container, false);

        TextView locationTextView = (TextView)v.findViewById(R.id.sightingLocationTextView);
        locationTextView.setText("Location: " + mSighting.getLocationName());

        TextView typeTextView = (TextView)v.findViewById(R.id.sightingTypeTextView);
        typeTextView.setText("Type: " + "Not yet implemented server side");

        TextView blurbTextView = (TextView)v.findViewById(R.id.sightingBlurbTextView);
        blurbTextView.setText("Blurb: " + mSighting.getBlurb());

        TextView dateTextView = (TextView)v.findViewById(R.id.sightingDateTextView);
        dateTextView.setText("Date: " + mSighting.getSubmittedAt().toString());

        ImageView sightingImageView = (ImageView)v.findViewById(R.id.sightingImageView);
        Picasso.with(getActivity()).load(mSighting.getImageMedium()).into(sightingImageView);

        return v;
    }

}
