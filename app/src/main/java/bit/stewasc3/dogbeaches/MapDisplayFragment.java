package bit.stewasc3.dogbeaches;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.tileprovider.tilesource.ITileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MBTilesLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.TileLayer;
import com.mapbox.mapboxsdk.views.MapView;
import java.util.HashMap;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;


public class MapDisplayFragment extends Fragment
{
    private static final Integer MAX_ZOOM = 13;
    private static final Integer MIN_ZOOM = 11;
    private MapView mv;

    public static MapDisplayFragment newInstance()
    {
        MapDisplayFragment fragment = new MapDisplayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    // Once map is ready, pull locations, set up the map once locations have been pulled.
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_map_display, container, false);
        mv = (MapView) v.findViewById(R.id.mapActivityMap);

        /* For using local mmbtiles
        TileLayer mbTileLayer = new MBTilesLayer(getActivity().getDatabasePath("dunedin2.mbtiles"));
        mv.setTileSource(new ITileLayer[]{mbTileLayer});
        mv.setScrollableAreaLimit(mbTileLayer.getBoundingBox());
        */

        mv.setUserLocationEnabled(true);
        mv.goToUserLocation(true);
        addLocationMarkers();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public void setupMap()
    {
    }
    public void addLocationMarkers()
    {
        Cursor c = getActivity().getContentResolver().query(DogBeachesContract.Locations.CONTENT_URI, DogBeachesContract.Locations.PROJECTION_ALL, null, null, null);

        Integer nameIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_NAME);
        Integer idIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_ID);
        Integer latIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_LATITUDE);
        Integer longIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_LONGITUDE);
        Integer statusIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_DOG_STATUS);
        Integer imageIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_IMAGE);

        while(c.moveToNext())
        {
            com.mapbox.mapboxsdk.overlay.Marker m = new com.mapbox.mapboxsdk.overlay.Marker(c.getString(nameIndex), "",
                    new com.mapbox.mapboxsdk.geometry.LatLng(c.getDouble(latIndex),
                    c.getDouble(longIndex)));
            m.setMarker(getIconBitmap(c.getString(statusIndex)));
            m.setAnchor(new PointF(0.5f, 0.5f));
            m.setToolTip(new LocationInfoWindow(mv, c.getInt(idIndex), c.getString(imageIndex)));
            mv.addMarker(m);
        }
        c.close();
    }

    private Drawable getIconBitmap(String status)
    {
        int icon = 0;
        switch(status)
        {
            case "on_lead" : icon = R.drawable.dogonlead;
                break;
            case "off_lead" : icon = R.drawable.dogofflead;
                break;
            case "no_dogs" : icon = R.drawable.nodogs;
                break;
        }
        return getResources().getDrawable(icon);
    }
}
