package io.github.lrsdev.dogbeaches;

import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.otto.Bus;

/**
 * A SquareUp otto event bus. Allows components to easily post, and subscribe to events.
 *
 * Not currently used.
 * See: http://square.github.io/otto/
 *
 * @author Samuel Stewart
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
