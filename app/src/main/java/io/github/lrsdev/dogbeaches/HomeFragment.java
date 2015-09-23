package io.github.lrsdev.dogbeaches;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.lrsdev.dogbeaches.R;

/**
 * Created by samuel on 17/05/15.
 */
public class HomeFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Hotline FAB direct dial
        FloatingActionButton callBtn = (FloatingActionButton)view.findViewById(R.id.homeFab);

        callBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent callIntent = new Intent();
                callIntent.setAction(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0800362468"));
                startActivity(callIntent);
            }
        });

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Home");
    }
}
