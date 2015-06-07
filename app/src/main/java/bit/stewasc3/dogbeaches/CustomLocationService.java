package bit.stewasc3.dogbeaches;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

/**
 * Created by samuel on 4/06/15.
 * A structure for delivering location updates to multiple parts of the UI.
 * Inspired by Android Nerd Ranch - Ch 33.
 */
public class CustomLocationService
{
    private static final String TAG = "CustomLocationService";
    public static final String ACTION_LOCATION = "nz.stewpot.dogbeaches.CUSTOM_LOCATION";

    private static CustomLocationService sCustomLocationService;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private CustomLocationService(Context appContext)
    {
        mAppContext = appContext;
        mLocationManager = (LocationManager)mAppContext
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public static CustomLocationService get(Context c)
    {
        if (sCustomLocationService == null)
            sCustomLocationService = new CustomLocationService(c.getApplicationContext());
        return sCustomLocationService;
    }

    private PendingIntent getLocationPendingIntent(boolean create)
    {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = create ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    // Start requesting updates from location manager. If last known location known when started,
    // broadcast this.
    public void startUpdates()
    {
        String provider = LocationManager.GPS_PROVIDER;

        Location lastKnown = mLocationManager.getLastKnownLocation(provider);

        if(lastKnown != null)
        {
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    public void stopUpdates()
    {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null)
        {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    private void broadcastLocation(Location l)
    {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, l);
        mAppContext.sendBroadcast(broadcast);
    }

    public boolean isTracking()
    {
        return getLocationPendingIntent(false) != null;
    }
}
