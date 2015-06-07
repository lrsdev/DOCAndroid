package bit.stewasc3.dogbeaches;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import UserAPI.Report;

/**
 * Created by samuel on 5/06/15.
 */
public class SightingFragment extends Fragment
{
    public static final String KEY_REPORT = "dogapp.report";
    private Report mReport;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mReport = (Report)getArguments().getSerializable(KEY_REPORT);
    }

    public static Fragment newInstance(Report r)
    {
        Bundle args = new Bundle();
        args.putSerializable(KEY_REPORT, r);
        Fragment f = new SightingFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_sighting, container, false);

        TextView mLocationTextView = (TextView)v.findViewById(R.id.sightingLocationTextView);

        TextView mTypeTextView = (TextView)v.findViewById(R.id.sightingTypeTextView);

        TextView mDateTextView = (TextView)v.findViewById(R.id.sightingDateTextView);
        mDateTextView.setText("Date: " + mReport.getSubmittedAt().toString());

        ImageView mSightingImageView = (ImageView)v.findViewById(R.id.sightingImageView);
        Picasso.with(getActivity()).load(mReport.getImageMedium()).into(mSightingImageView);

        return v;
    }

}
