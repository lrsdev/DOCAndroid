package io.github.lrsdev.dogbeaches;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by samuel on 17/05/15.
 */
public class HomeFragment extends Fragment
{
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

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
        final FloatingActionMenu menu1 = (FloatingActionMenu) view.findViewById(R.id.menu1);

        menu1.setOnMenuButtonClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (menu1.isOpened())
                {
                    //Toast.makeText(menu1.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }
                menu1.toggle(true);
            }
        });

        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);

        createCustomAnimation();

        return view;
    }

    private void createCustomAnimation()
    {
        AnimatorSet set = new AnimatorSet();
    }

    private View.OnClickListener clickListener = new View.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            String text = "";

            switch (v.getId())
            {
                case R.id.fab1:
                    text = fab1.getLabelText();
                    Intent callIntent1 = new Intent();
                    callIntent1.setAction(Intent.ACTION_DIAL);
                    callIntent1.setData(Uri.parse("tel:03477400"));
                    startActivity(callIntent1);
                    break;
                case R.id.fab2:
                    text = fab2.getLabelText();
                    Intent callIntent2 = new Intent();
                    callIntent2.setAction(Intent.ACTION_DIAL);
                    callIntent2.setData(Uri.parse("tel:0800362468"));
                    startActivity(callIntent2);
                    break;
            }
            //           Toast.makeText(FloatingMenusActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Home");
    }
}
