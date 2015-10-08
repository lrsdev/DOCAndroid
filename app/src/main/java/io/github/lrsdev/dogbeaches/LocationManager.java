package io.github.lrsdev.dogbeaches;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

/**
 * Created by sam on 16/09/15.
 */

public class LocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private static LocationManager lm;
    private static GoogleApiClient mGoogleApiClient;
    private static Bus eventBus;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequestBalanced;
    private LocationRequest mLocationRequestHighAccuracy;

    private LocationManager(Context c)
    {
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        eventBus = BusProvider.get();
        eventBus.register(this);
        mCurrentLocation = null;
        createLocationRequests();
    }

    public static LocationManager get(Context c)
    {
        if (lm == null)
        {
            lm = new LocationManager(c);
        }
        return lm;
    }

    public Location getLocation()
    {
        return mCurrentLocation;
    }

    private void createLocationRequests()
    {
        mLocationRequestBalanced = new LocationRequest();
        mLocationRequestBalanced.setInterval(10000);
        mLocationRequestBalanced.setFastestInterval(5000);
        mLocationRequestBalanced.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setInterval(5000);
        mLocationRequestHighAccuracy.setFastestInterval(1000);
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public void checkServicesEnabled(ResultCallback a)
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequestBalanced);


        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(a);
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequestBalanced, this);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mCurrentLocation = location;
        eventBus.post(produceLocationEvent());
    }

    @Produce
    public LocationChangedEvent produceLocationEvent()
    {
        return new LocationChangedEvent(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude());
    }

    public class LocationChangedEvent
    {
        public final double lat;
        public final double lon;

        public LocationChangedEvent(Double lat, Double lon)
        {
            this.lat = lat;
            this.lon = lon;
        }
    }
}