package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import android.location.Location;
import UserAPI.RestClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MapDisplayFragment extends MapFragment
{
    private GoogleMap mMap;
    private ArrayList<UserAPI.Location> mLocations;

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
        getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                mMap = googleMap;
                getLocations();
            }
        });

        super.onCreate(savedInstanceState);
    }

    // Get all locations from remote DB for now, in future, restrict to user defined radius.
    public void getLocations()
    {
        RestClient.get().getAllLocations(new Callback<ArrayList<UserAPI.Location>>()
        {
            @Override
            public void success(ArrayList<UserAPI.Location> locations, Response response)
            {
                mLocations = locations;
                setupMap();
            }

            @Override
            public void failure(RetrofitError error)
            {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    public void setupMap()
    {
        mMap.setMyLocationEnabled(true);
        // Center map on Dunedin for now, User location later.
        CameraUpdate c = CameraUpdateFactory.newLatLngZoom(new LatLng(
                -45.873629, 170.503692), 10);
        mMap.moveCamera(c);
        for(UserAPI.Location l : mLocations)
        {
            for(UserAPI.ApiGeoLocation g : l.getAccessPoints())
            {
                mMap.addMarker(new MarkerOptions().position(new LatLng(g.getLatitude(),
                        g.getLongitude())).title(l.getName()));
            }
        }
    }
}
