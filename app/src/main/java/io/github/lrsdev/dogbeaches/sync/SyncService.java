package io.github.lrsdev.dogbeaches.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Synchronisation Adapter Service
 *
 * Reference the following document for information
 * http://developer.android.com/training/sync-adapters/creating-sync-adapter.html
 *
 * @author Samuel Stewart
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
