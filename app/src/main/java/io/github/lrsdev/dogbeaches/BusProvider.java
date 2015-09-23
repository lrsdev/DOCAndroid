package io.github.lrsdev.dogbeaches;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Bus;

/**
 * Created by sam on 16/09/15.
 */
public class BusProvider
{
    private static Bus BUS;
    private GoogleApiClient mGoogleApliClient;

    private BusProvider()
    {
    }

    public static Bus get()
    {
        if (BUS == null)
        {
            BUS = new Bus();
        }
        return BUS;
    }
}
