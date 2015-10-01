package io.github.lrsdev.dogbeaches;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by rickiekewene on 29/09/15.
 */
public class DonateFragment extends Fragment
{
    private ImageButton yept;
    private ImageButton sea;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_donate,container, false);

        //Yellow-Eyed Penguin Trust & NZ Sealion Trust Intent(Open browser to donation url page)
        yept = (ImageButton)view.findViewById(R.id.yept_icon);
        sea = (ImageButton)view.findViewById(R.id.sea_icon);

        yept.setOnClickListener(clickListener);
        sea.setOnClickListener(clickListener);

        return view;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.yept_icon:
                    Uri url1 = Uri.parse("http://www.yellow-eyedpenguin.org.nz/passion/support-the-trusts-work");
                    Intent launchBrowser1 = new Intent(Intent.ACTION_VIEW, url1);
                    startActivity(launchBrowser1);
                    break;
                case R.id.sea_icon:
                    Uri url2 = Uri.parse("http://www.sealiontrust.org.nz/");
                    Intent launchBrowser2 = new Intent(Intent.ACTION_VIEW, url2);
                    startActivity(launchBrowser2);
                    break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Donate");
    }
}
