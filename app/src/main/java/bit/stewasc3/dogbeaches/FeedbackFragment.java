package bit.stewasc3.dogbeaches;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rickiekewene on 20/09/15.
 */
public class FeedbackFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Feedback");
    }
}
