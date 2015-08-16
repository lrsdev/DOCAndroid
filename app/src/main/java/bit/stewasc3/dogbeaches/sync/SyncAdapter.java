package bit.stewasc3.dogbeaches.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import java.util.ArrayList;

import UserAPI.Location;
import UserAPI.RestClient;
import UserAPI.Sync.Sync;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesProvider;

/**
 * Created by sam on 16/08/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    private ContentResolver mContentResolver;

    public SyncAdapter(Context context, boolean autoInitialize)
    {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }
    // Syncing algorithm. Performed on a background thread so no need to worry about calling http
    // asynchronously.
    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult)
    {
        try
        {
            // Arbitrary timestamp for testing
            Sync syncObject = RestClient.get().getSync("2014-08-12T02:45:28.908Z");

            // Get the new location records and insert them
            ArrayList<Location> locations = syncObject.getLocations().getNew();

            for(Location l : locations)
            {
                ContentValues values = new ContentValues();
                values.put(DogBeachesContract.Locations.COLUMN_ID, l.getId());
                values.put(DogBeachesContract.Locations.COLUMN_NAME, l.getName());
                values.put(DogBeachesContract.Locations.COLUMN_CATEGORY, l.getCategory());
                values.put(DogBeachesContract.Locations.COLUMN_ANIMAL_BLURB, l.getCategory());
                values.put(DogBeachesContract.Locations.COLUMN_DOG_STATUS, l.getDogStatus());
                values.put(DogBeachesContract.Locations.COLUMN_DOG_GUIDELINES, l.getDogGuidelines());
                values.put(DogBeachesContract.Locations.COLUMN_IMAGE_THUMBNAIL, l.getImageThumbnail());
                values.put(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM, l.getImageMedium());
                values.put(DogBeachesContract.Locations.COLUMN_LATITUDE, l.getLatitude());
                values.put(DogBeachesContract.Locations.COLUMN_LONGITUDE, l.getLongitude());
                contentProviderClient.insert(DogBeachesContract.Locations.CONTENT_URI, values);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
