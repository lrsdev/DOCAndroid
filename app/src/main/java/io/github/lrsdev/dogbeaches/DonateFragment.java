package io.github.lrsdev.dogbeaches;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import io.github.lrsdev.dogbeaches.R;
/**
 * Created by rickiekewene on 29/09/15.
 */
public class DonateFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_donate, container, false);
/*
        //Yellow-Eyed Penguin Trust Intent(Open browser to donation url page)
        ImageButton yept = (ImageButton)findviewById(R.id.yept_icon);

        yept.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String url = "http//www.yellow-eyedpenguin.co.nz/passion/support-the-trusts-work/become-a-trust-supporter";
                Intent urlIntent1 = new Intent();
                urlIntent1.setAction(Intent.ACTION_VIEW);
                urlIntent1.setData(Uri.parse(url));
                startActivity(urlIntent1);
            }
        }); */

        return view;
    }



    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Donate");
    }
}
