package bit.stewasc3.dogbeaches;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rickiekewene on 12/08/15.
 */
public class DonateFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_donate, container, false);
        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Donate");
    }
}
