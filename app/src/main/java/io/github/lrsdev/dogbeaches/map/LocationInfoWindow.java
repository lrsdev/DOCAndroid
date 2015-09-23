package io.github.lrsdev.dogbeaches.map;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.InfoWindow;
import com.mapbox.mapboxsdk.views.MapView;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.github.lrsdev.dogbeaches.LocationActivity;
import io.github.lrsdev.dogbeaches.R;

/**
 * Created by sam on 26/08/15.
 */
public class LocationInfoWindow extends InfoWindow
{
    private String imageLocation;

    public LocationInfoWindow(MapView mapView, final Integer id, String imageLocation)
    {
        super(R.layout.location_info_window, mapView);
        this.imageLocation = imageLocation;

        setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    Toast.makeText(mView.getContext(), "Info Window Pressed", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(mView.getContext(), LocationActivity.class);
                    i.putExtra(LocationActivity.KEY_LOCATION_ID, id);
                    mView.getContext().startActivity(i);
                }
                return true;
            }
        });
    }

    public LocationInfoWindow(View view, MapView mapView)
    {
        super(view, mapView);
    }

    @Override
    public void onOpen(Marker overlayItem)
    {
        ImageView iv = (ImageView) mView.findViewById(R.id.locationInfoWindowImage);
        TextView tv = (TextView) mView.findViewById(R.id.locationInfoWindowTitle);
        File f = new File(imageLocation);
        Picasso.with(mView.getContext()).load(f).into(iv);
        tv.setText(overlayItem.getTitle());
    }
}
