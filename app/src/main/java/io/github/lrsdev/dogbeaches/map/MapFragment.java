package io.github.lrsdev.dogbeaches.map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PointF;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mapbox.mapboxsdk.api.ILatLng;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MBTilesLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.MapboxTileLayer;
import com.mapbox.mapboxsdk.tileprovider.tilesource.TileLayer;
import com.mapbox.mapboxsdk.views.MapView;
import com.mapbox.mapboxsdk.views.MapViewListener;

import java.util.HashMap;

import io.github.lrsdev.dogbeaches.Helpers;
import io.github.lrsdev.dogbeaches.LocationActivity;
import io.github.lrsdev.dogbeaches.LocationManager;
import io.github.lrsdev.dogbeaches.R;
import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;


/**
 * A fragment which displays the map.
 */
public class MapFragment extends Fragment
{
    private static final Integer OFFLINE_MAX_ZOOM = 13;
    private static final Integer OFFLINE_MIN_ZOOM = 13;
    private static final Integer ONLINE_MAX_ZOOM = 16;
    private static final LatLng DUNEDIN_LATLNG = new LatLng(-45.874372, 170.504186);
    private static final String MAP_DB_NAME = "otago.mbtiles";
    private Button mapButton;
    private MapView mapView;
    private boolean displayingOffline;
    private Location mLastLocation;
    /**
     * A hashmap which stores references to markers as keys to their corresponding location id.
     * The hashmap is used to retrieve the location id of a pressed marker when launching the
     * location activity.
     */
    private HashMap<Marker, Integer> markerMap;

    public static MapFragment newInstance()
    {
        MapFragment fragment = new MapFragment();
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
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) v.findViewById(R.id.map_view);
        mapButton = (Button) v.findViewById(R.id.map_fragment_button);
        mLastLocation = LocationManager.get(getActivity()).getLocation();
        setupMap();
        testConnectivity();
        return v;
    }

    /**
     * Setup map attributes and touch event handler.
     */
    private void setupMap()
    {
        mapView.setUserLocationEnabled(true);
        mapButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (displayingOffline)
                {
                    setOnlineMap();
                }
                else
                {
                    setOfflineMap();
                }
            }
        });
        markerMap = new HashMap<>();
        addLocationMarkers();
        mapView.setMapViewListener(new MarkerClickListener());
        mapView.invalidate();
    }

    /**
     * Tests whether device is connected and sets map accordingly.
     */
    private void testConnectivity()
    {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();

        if (isConnected)
        {
            setOnlineMap();
        }
        else
        {
            setOfflineMap();
        }
    }

    /**
     * Change mapView's display to the offline tile provider. Set scollable bounding box and
     * maximum zoom levels.
     */
    private void setOfflineMap()
    {
        MBTilesLayer layer = new MBTilesLayer(getActivity().getDatabasePath(MAP_DB_NAME));
        mapView.setTileSource(layer);
        mapView.setMaxZoomLevel(OFFLINE_MAX_ZOOM);
        mapView.setMinZoomLevel(OFFLINE_MIN_ZOOM);
        mapView.setZoom(OFFLINE_MIN_ZOOM);
        mapView.setScrollableAreaLimit(layer.getBoundingBox());
        mapButton.setText(R.string.map_change_online);
        displayingOffline = true;

        if (mLastLocation != null &&
                layer.getBoundingBox().contains(new LatLng(mLastLocation.getLatitude(),
                        mLastLocation.getLongitude())))
        {
            mapView.goToUserLocation(true);
        }
        else
        {
            mapView.setCenter(DUNEDIN_LATLNG);
            showAlert(getResources().getString(R.string.map_offline_alert));
        }
    }

    /**
     * Change mapView's display to the online tile provider.
     */
    private void setOnlineMap()
    {
        mapView.setTileSource(new MapboxTileLayer(getResources().getString(R.string.mapbox_map_id)));
        mapView.setMaxZoomLevel(ONLINE_MAX_ZOOM);
        mapButton.setText(R.string.map_change_offline);
        displayingOffline = false;

        if (mLastLocation == null)
        {
            mapView.setCenter(DUNEDIN_LATLNG);
            showAlert(getResources().getString(R.string.map_no_location_alert));
        }
        else
        {
            mapView.goToUserLocation(true);
        }
    }

    /**
     * Shows a modal alert window displaying message.
     * @param message a string to display
     */
    private void showAlert(String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * Queries the local database for a location cursor. Iterate and initialise location markers
     * in correct location with correct dog status icon. Adds marker to makerMap as a key to the
     * corresponding location id value.
     */
    public void addLocationMarkers()
    {
        Cursor c = getActivity().getContentResolver().query(DogBeachesContract.Locations.CONTENT_URI,
                DogBeachesContract.Locations.PROJECTION_ALL, null, null, null);

        int nameIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_NAME);
        int idIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_ID);
        int latIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_LATITUDE);
        int longIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_LONGITUDE);
        int statusIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_DOG_STATUS);
        int imageIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_IMAGE);

        while (c.moveToNext())
        {
            com.mapbox.mapboxsdk.overlay.Marker m = new com.mapbox.mapboxsdk.overlay.Marker(c.getString(nameIndex), "",
                    new com.mapbox.mapboxsdk.geometry.LatLng(c.getDouble(latIndex),
                            c.getDouble(longIndex)));
            m.setMarker(Helpers.getDogIconDrawable(c.getString(statusIndex), getActivity()));
            m.setAnchor(new PointF(0.5f, 0.5f));

            // Sets a custom info window.

            //m.setToolTip(new LocationInfoWindow(mapView, c.getInt(idIndex), c.getString(imageIndex)));
            mapView.addMarker(m);
            markerMap.put(m, c.getInt(idIndex));
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Location Map");
    }

    /**
     * A listener for map marker touch events.
     */
    private class MarkerClickListener implements MapViewListener
    {
        @Override
        public void onShowMarker(MapView mapView, Marker marker)
        {

        }

        @Override
        public void onHideMarker(MapView mapView, Marker marker)
        {

        }

        /**
         * Creates an intent for the location activity. Adds the location id of the clicked
         * marker to the extras bundle. Starts the activity.
         * @param mapView
         * @param marker
         */
        @Override
        public void onTapMarker(MapView mapView, Marker marker)
        {
            Intent i = new Intent(getActivity(), LocationActivity.class);
            i.putExtra(LocationActivity.KEY_LOCATION_ID, markerMap.get(marker));
            startActivity(i);
        }

        @Override
        public void onLongPressMarker(MapView mapView, Marker marker)
        {

        }

        @Override
        public void onTapMap(MapView mapView, ILatLng iLatLng)
        {

        }

        @Override
        public void onLongPressMap(MapView mapView, ILatLng iLatLng)
        {

        }
    }
}