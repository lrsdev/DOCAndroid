package bit.stewasc3.dogbeaches.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import UserAPI.Location;
import UserAPI.RestClient;
import UserAPI.Sync;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;

/**
 * Created by sam on 16/08/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    private static final String TAG = "SyncAdapter";
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
            Sync syncObject = RestClient.get().getSync("2015-08-17T12:46:04.964Z");

            // Create a batch operation for transactions
            ArrayList<ContentProviderOperation> batch = new ArrayList<>();

            // Get references to sync arrays
            ArrayList<Location> locations = syncObject.getLocations();
            ArrayList<Integer> deletedLocations = syncObject.getDeletedLocationIds();
            ArrayList<String> filesToDelete = new ArrayList<>();

            // Create a hash map of updated locations
            HashMap<Integer, Location> entryMap = new HashMap<>();
            for (Location l : locations)
            {
                entryMap.put(l.getId(), l);
            }

            // Query content provider for all location records currently stored
            Cursor c = mContentResolver.query(DogBeachesContract.Locations.CONTENT_URI,
                    DogBeachesContract.Locations.PROJECTION_ALL, null, null, null);
            Integer idIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_ID);
            Integer localImageMediumIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM_LOCAL);
            Integer urlImageMediumIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM);

            // Iterate cursor containing local containers.
            while (c.moveToNext())
            {
                Integer currentId = c.getInt(idIndex);
                // If current record ID is in deleted_ids, delete it.
                if(deletedLocations.contains(c.getInt(idIndex)))
                {
                    Uri deleteUri = DogBeachesContract.Locations.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(currentId)).build();

                    filesToDelete.add(c.getString(localImageMediumIndex));
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                }

                // If entry map contains key for location, update record, and remove from entry map.
                else if(entryMap.containsKey(currentId))
                {
                    Uri existingUri = DogBeachesContract.Locations.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(currentId)).build();

                    // Use id from current location in cursor to retrieve object from map
                    Location l = entryMap.get(currentId);
                    ContentProviderOperation.Builder op = ContentProviderOperation.newUpdate(existingUri)
                        .withValue(DogBeachesContract.Locations.COLUMN_NAME, l.getName())
                            .withValue(DogBeachesContract.Locations.COLUMN_CATEGORY, l.getCategory())
                            .withValue(DogBeachesContract.Locations.COLUMN_ANIMAL_BLURB, l.getAnimalBlurb())
                            .withValue(DogBeachesContract.Locations.COLUMN_DOG_STATUS, l.getDogStatus())
                            .withValue(DogBeachesContract.Locations.COLUMN_DOG_GUIDELINES, l.getDogGuidelines())
                            .withValue(DogBeachesContract.Locations.COLUMN_IMAGE_THUMBNAIL, l.getImageThumbnail())
                            .withValue(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM, l.getImageMedium())
                            .withValue(DogBeachesContract.Locations.COLUMN_LATITUDE, l.getLatitude())
                            .withValue(DogBeachesContract.Locations.COLUMN_LONGITUDE, l.getLongitude())
                            .withYieldAllowed(true);

                    // Get new medium image if URL's are different
                    if(l.getImageMedium().compareTo(c.getString(urlImageMediumIndex)) != 0)
                    {
                        String filename = storeImage(l.getImageMedium(), "location", l.getId(), "medium");
                        filesToDelete.add(c.getString(localImageMediumIndex));
                        op.withValue(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM_LOCAL, filename);
                    }

                    batch.add(op.build());
                    entryMap.remove(currentId);
                }
            }

            // Any elements still present in hashmap are new records, insert them
            for (Location l : entryMap.values())
            {
                // Download image, save to file, get file uri, save to database
                String filename = storeImage(l.getImageMedium(), "location", l.getId(), "medium");

                batch.add(
                    ContentProviderOperation.newInsert(DogBeachesContract.Locations.CONTENT_URI)
                    .withValue(DogBeachesContract.Locations.COLUMN_ID, l.getId())
                    .withValue(DogBeachesContract.Locations.COLUMN_NAME, l.getName())
                    .withValue(DogBeachesContract.Locations.COLUMN_CATEGORY, l.getCategory())
                    .withValue(DogBeachesContract.Locations.COLUMN_ANIMAL_BLURB, l.getAnimalBlurb())
                    .withValue(DogBeachesContract.Locations.COLUMN_DOG_STATUS, l.getDogStatus())
                    .withValue(DogBeachesContract.Locations.COLUMN_DOG_GUIDELINES, l.getDogGuidelines())
                    .withValue(DogBeachesContract.Locations.COLUMN_IMAGE_THUMBNAIL, l.getImageThumbnail())
                    .withValue(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM, l.getImageMedium())
                    .withValue(DogBeachesContract.Locations.COLUMN_IMAGE_MEDIUM_LOCAL, filename)
                    .withValue(DogBeachesContract.Locations.COLUMN_LATITUDE, l.getLatitude())
                    .withValue(DogBeachesContract.Locations.COLUMN_LONGITUDE, l.getLongitude())
                    .withYieldAllowed(true)
                    .build());
            }
            mContentResolver.applyBatch(DogBeachesContract.AUTHORITY, batch);
            for(String fileString : filesToDelete)
            {
                getContext().deleteFile(fileString);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Downloads image, stores and returns path
    private String storeImage(String url, String filePrefix, int resourceId, String filePostfix)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = filePrefix + resourceId + "_" + timeStamp + filePostfix + ".jpg";
        File file = new File(getContext().getFilesDir(), filename);
        FileOutputStream out = null;
        try
        {
            Bitmap bm = Picasso.with(getContext()).load(url).get();
            out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (out != null) out.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return filename;
    }
}
