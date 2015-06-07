package bit.stewasc3.dogbeaches;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by samuel on 4/06/15.
 */
public class LocationReceiver extends BroadcastReceiver
{
    private static final String TAG = "LocationReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Location l = (Location)intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (l != null)
        {
            onLocationReceived(context, l);
            return;
        }

        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED))
        {
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    protected void onLocationReceived(Context context, Location l)
    {
        // Log
    }

    protected void onProviderEnabledChanged(boolean enabled)
    {
        // Log
    }
}
