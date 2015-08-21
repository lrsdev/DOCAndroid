package bit.stewasc3.dogbeaches.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sam on 16/08/15.
 * Reference the following document for information
 * http://developer.android.com/training/sync-adapters/creating-sync-adapter.html
 */
public class SyncService extends Service
{
    private static SyncAdapter sSyncAdapter = null;
    // Object to use as a thread safe lock
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate()
    {
        synchronized (sSyncAdapterLock)
        {
            if (sSyncAdapter == null)
            {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent i)
    {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
