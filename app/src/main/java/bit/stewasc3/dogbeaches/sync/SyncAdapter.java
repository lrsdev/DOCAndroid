package bit.stewasc3.dogbeaches.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import bit.stewasc3.dogbeaches.sync.API.Animal;
import bit.stewasc3.dogbeaches.sync.API.Location;
import bit.stewasc3.dogbeaches.sync.API.RestClient;
import bit.stewasc3.dogbeaches.sync.API.Sync;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;
import bit.stewasc3.dogbeaches.db.DBHelper;
import bit.stewasc3.dogbeaches.db.SyncTable;

/**
 * Created by sam on 16/08/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    private static String LOCATION_IMAGE_PATH = "/images/locations";
    private static String ANIMAL_IMAGE_PATH = "/images/animals";
    private static final String TAG = "SyncAdapter";
    private ContentResolver mContentResolver;
    private DBHelper mDbHelper;
    private String mLocationImagePath;
    private String mAnimalImagePath;

    public SyncAdapter(Context context, boolean autoInitialize)
    {
        this(context, autoInitialize, false);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mDbHelper = new DBHelper(context);
        mLocationImagePath = context.getFilesDir().toString() + LOCATION_IMAGE_PATH;
        mAnimalImagePath = context.getFilesDir().toString() + ANIMAL_IMAGE_PATH;
        File f = new File(mLocationImagePath);
        f.mkdirs();
        f = new File(mAnimalImagePath);
        f.mkdirs();
    }

    // Syncing algorithm. Performed on a background thread so no need to worry about calling http
    // asynchronously.
    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult)
    {
        ArrayList<String> filesCreated = new ArrayList<>();
        ArrayList<String> filesToDelete = new ArrayList<>();
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();
        Cursor locationCursor = null;
        Cursor animalCursor = null;
        Cursor reportCursor = null;
        Boolean success = false;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String timestamp = getLastSyncTimestamp(db);

        try
        {
            Sync syncObject = RestClient.get().getSync(timestamp);

            // Get all required cursors
            locationCursor = mContentResolver.query(DogBeachesContract.Locations.CONTENT_URI,
                DogBeachesContract.Locations.PROJECTION_ALL, null, null, null);

            animalCursor = mContentResolver.query(DogBeachesContract.Animals.CONTENT_URI,
                    DogBeachesContract.Animals.PROJECTION_ALL, null, null, null);

            updateLocations(syncObject, batch, locationCursor, filesCreated, filesToDelete);
            updateAnimals(syncObject, batch, animalCursor, filesCreated, filesToDelete);

            // Apply batch operation
            mContentResolver.applyBatch(DogBeachesContract.AUTHORITY, batch);

            // If batch applies without exception, success.
            success = true;

            // Clean up replaced files
            deleteFiles(filesToDelete);

            // Write timestamp to db
            ContentValues cv = new ContentValues();
            cv.put(SyncTable.LAST_SYNC, syncObject.getSyncedAt());
            db.execSQL("DELETE FROM " + SyncTable.TABLE_NAME);
            db.insert(SyncTable.TABLE_NAME, null, cv);
        }
        catch(RemoteException|OperationApplicationException e)
        {
            Log.e(TAG, "Database error: " + e.toString());
            return;
        }
        catch(IOException e)
        {
            Log.e(TAG, "I/O Error: " + e.toString());
            return;
        }
        catch(Exception e)
        {
            Log.e(TAG, "Error: " + e.toString());
            return;
        }
        finally
        {
            // Delete downloaded files if sync not success (database will stiff ref old files)
            if(!success)
                deleteFiles(filesCreated);
            if(locationCursor != null)
                locationCursor.close();
            if(animalCursor != null)
                animalCursor.close();
        }

    }

    private void updateAnimals(Sync syncObject, ArrayList<ContentProviderOperation> batch, Cursor c,
                               ArrayList<String> filesCreated, ArrayList<String> filesToDelete)
            throws RemoteException, OperationApplicationException, IOException
    {
        ArrayList<Animal> animals = syncObject.getAnimals();
        ArrayList<Integer> deletedAnimals = syncObject.getDeletedAnimalIds();

        HashMap<Integer, Animal> entryMap = new HashMap<>();
        for (Animal a : animals)
        {
            entryMap.put(a.getId(), a);
        }

        Integer idIndex = c.getColumnIndex(DogBeachesContract.Animals.COLUMN_ID);
        Integer imageIndex = c.getColumnIndex(DogBeachesContract.Animals.COLUMN_IMAGE);
        Integer imageIndexURL = c.getColumnIndex(DogBeachesContract.Animals.COLUMN_IMAGE_URL);

        while (c.moveToNext())
        {
            Integer currentId = c.getInt(idIndex);

            if(deletedAnimals.contains(c.getInt(idIndex)))
            {
                Uri deleteUri = DogBeachesContract.Animals.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(currentId)).build();
                filesToDelete.add(c.getString(imageIndex));
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
            }

            else if(entryMap.containsKey(currentId))
            {
                Uri existingUri = DogBeachesContract.Animals.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(currentId)).build();
                Animal a = entryMap.get(currentId);
                ContentProviderOperation.Builder op = ContentProviderOperation.newUpdate(existingUri)
                        .withValue(DogBeachesContract.Animals.COLUMN_NAME, a.getName())
                        .withValue(DogBeachesContract.Animals.COLUMN_BLURB, a.getBlurb())
                        .withValue(DogBeachesContract.Animals.COLUMN_GUIDELINES, a.getGuidelines())
                        .withValue(DogBeachesContract.Animals.COLUMN_EXT_URL, a.getExtUrl())
                        .withValue(DogBeachesContract.Animals.COLUMN_IMAGE_URL, a.getImageMedium())
                        .withYieldAllowed(true);

                if(a.getImageMedium().compareTo(c.getString(imageIndexURL)) != 0)
                {
                    File f = createImageFile(mAnimalImagePath, a.getId().toString() + ".jpg");
                    downloadImageToFile(f, a.getImageMedium());
                    filesCreated.add(f.getAbsolutePath());
                    filesToDelete.add(c.getString(imageIndex));
                    op.withValue(DogBeachesContract.Animals.COLUMN_IMAGE, f.getAbsolutePath());
                }
                batch.add(op.build());
                entryMap.remove(currentId);
            }
        }
        for (Animal a : entryMap.values())
        {
            File f = createImageFile(mAnimalImagePath, a.getId().toString() + ".jpg");
            downloadImageToFile(f, a.getImageMedium());
            filesCreated.add(f.getAbsolutePath());
            batch.add(
                ContentProviderOperation.newInsert(DogBeachesContract.Animals.CONTENT_URI)
                    .withValue(DogBeachesContract.Animals.COLUMN_ID, a.getId())
                    .withValue(DogBeachesContract.Animals.COLUMN_NAME, a.getName())
                    .withValue(DogBeachesContract.Animals.COLUMN_BLURB, a.getBlurb())
                    .withValue(DogBeachesContract.Animals.COLUMN_GUIDELINES, a.getGuidelines())
                    .withValue(DogBeachesContract.Animals.COLUMN_EXT_URL, a.getExtUrl())
                        .withValue(DogBeachesContract.Animals.COLUMN_IMAGE, f.getAbsolutePath())
                        .withValue(DogBeachesContract.Animals.COLUMN_IMAGE_URL, a.getImageMedium())
                        .withYieldAllowed(true).build());
        }
    }

    private void updateLocations(Sync syncObject, ArrayList<ContentProviderOperation> batch, Cursor c,
                                 ArrayList<String> filesCreated, ArrayList<String> filesToDelete)
            throws RemoteException, OperationApplicationException, IOException
    {
        // Get references to sync arrays
        ArrayList<Location> locations = syncObject.getLocations();
        ArrayList<Integer> deletedLocations = syncObject.getDeletedLocationIds();

        // Create a hash map of updated locations
        HashMap<Integer, Location> entryMap = new HashMap<>();
        for (Location l : locations)
        {
            entryMap.put(l.getId(), l);
        }

        // Get integer values to index cursor columns
        Integer idIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_ID);
        Integer imageIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_IMAGE);
        Integer imageUrlIndex = c.getColumnIndex(DogBeachesContract.Locations.COLUMN_IMAGE_URL);

        // Iterate cursor containing local containers.
        while (c.moveToNext())
        {
            Integer currentId = c.getInt(idIndex);
            // If current record ID is in deleted_ids, delete it.
            if(deletedLocations.contains(c.getInt(idIndex)))
            {
                Uri deleteUri = DogBeachesContract.Locations.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(currentId)).build();

                filesToDelete.add(c.getString(imageIndex));
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
                        .withValue(DogBeachesContract.Locations.COLUMN_IMAGE_URL, l.getImageMedium())
                        .withValue(DogBeachesContract.Locations.COLUMN_LATITUDE, l.getLatitude())
                        .withValue(DogBeachesContract.Locations.COLUMN_LONGITUDE, l.getLongitude())
                        .withYieldAllowed(true);

                // Get new medium image if URL's are different
                if(l.getImageMedium().compareTo(c.getString(imageUrlIndex)) != 0)
                {
                    File f = createImageFile(mLocationImagePath, Integer.toString(l.getId()) + ".jpg");
                    downloadImageToFile(f, l.getImageMedium());
                    filesCreated.add(f.getAbsolutePath());
                    op.withValue(DogBeachesContract.Locations.COLUMN_IMAGE, f.getAbsolutePath());
                }
                batch.add(op.build());
                entryMap.remove(currentId);
            }
        }
        // Any elements still present in hashmap are new records, insert them
        for (Location l : entryMap.values())
        {
            File f = createImageFile(mLocationImagePath, Integer.toString(l.getId()) + ".jpg");
            downloadImageToFile(f, l.getImageMedium());
            filesCreated.add(f.getAbsolutePath());
            batch.add(ContentProviderOperation.newInsert(DogBeachesContract.Locations.CONTENT_URI).withValue(DogBeachesContract.Locations.COLUMN_ID, l.getId()).withValue(DogBeachesContract.Locations.COLUMN_NAME, l.getName()).withValue(DogBeachesContract.Locations.COLUMN_CATEGORY, l.getCategory()).withValue(DogBeachesContract.Locations.COLUMN_ANIMAL_BLURB, l.getAnimalBlurb()).withValue(DogBeachesContract.Locations.COLUMN_DOG_STATUS, l.getDogStatus()).withValue(DogBeachesContract.Locations.COLUMN_DOG_GUIDELINES, l.getDogGuidelines()).withValue(DogBeachesContract.Locations.COLUMN_IMAGE, f.getAbsolutePath()).withValue(DogBeachesContract.Locations.COLUMN_IMAGE_URL, l.getImageMedium()).withValue(DogBeachesContract.Locations.COLUMN_LATITUDE, l.getLatitude()).withValue(DogBeachesContract.Locations.COLUMN_LONGITUDE, l.getLongitude()).withYieldAllowed(true).build());
        }
    }

    private File createImageFile(String path, String fileName)
    {
        File file = new File(path, fileName);
        return file;
    }

    private void downloadImageToFile(File file, String url) throws IOException
    {
        FileOutputStream out = null;
        Bitmap bm = Picasso.with(getContext()).load(url).get();
        out = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
        if (out != null)
            out.close();
    }

    private String getLastSyncTimestamp(SQLiteDatabase db)
    {
        // Get last sync time
        String[] projection = {SyncTable.LAST_SYNC};
        Cursor c = db.query(SyncTable.TABLE_NAME, projection, null, null, null, null, null);
        String timestamp = "1970-01-01T00:00:00.000Z";
        if(c.getCount() != 0)
        {
            c.moveToFirst();
            timestamp = c.getString(c.getColumnIndex(SyncTable.LAST_SYNC));
        }
        c.close();
        return timestamp;
    }

    private void deleteFiles(ArrayList<String> fileList)
    {
        for (String fileString : fileList)
        {
            getContext().deleteFile(fileString);
        }
    }
}
