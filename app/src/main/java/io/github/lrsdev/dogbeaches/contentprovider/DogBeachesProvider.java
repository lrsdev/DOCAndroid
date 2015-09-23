package io.github.lrsdev.dogbeaches.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import io.github.lrsdev.dogbeaches.db.DBHelper;

/**
 * Created by sam on 11/08/15.
 */
public class DogBeachesProvider extends ContentProvider
{
    private DBHelper mHelper;
    public static final int LOCATIONS = 10;
    public static final int LOCATION_ID = 11;
    public static final int ANIMALS = 20;
    public static final int ANIMAL_ID = 21;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
        Initialise the URI Matcher. # is a placeholder for a numeric value.
     */
    static
    {
        sURIMatcher.addURI(DogBeachesContract.AUTHORITY, "locations", LOCATIONS);
        sURIMatcher.addURI(DogBeachesContract.AUTHORITY, "locations/#", LOCATION_ID);
        sURIMatcher.addURI(DogBeachesContract.AUTHORITY, "animals", ANIMALS);
        sURIMatcher.addURI(DogBeachesContract.AUTHORITY, "animals/#", ANIMAL_ID);
    }

    /**
     * Instantiate and store a reference to the DB Helper.
     * @return
     */
    @Override
    public boolean onCreate()
    {
        mHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        switch (uriType)
        {
            case LOCATION_ID:
                queryBuilder.setTables(DogBeachesContract.Locations.TABLE_NAME);
                queryBuilder.appendWhere(DogBeachesContract.Locations.COLUMN_ID +
                        " = " + uri.getLastPathSegment());
                break;
            case LOCATIONS:
                queryBuilder.setTables(DogBeachesContract.Locations.TABLE_NAME);
                break;
            case ANIMAL_ID:
                queryBuilder.setTables(DogBeachesContract.Animals.TABLE_NAME);
                queryBuilder.appendWhere(DogBeachesContract.Animals.COLUMN_ID +
                        " = " + uri.getLastPathSegment());
                break;
            case ANIMALS:
                queryBuilder.setTables(DogBeachesContract.Animals.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(mHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        long id = 0;
        int uriType = sURIMatcher.match(uri);
        String uriString;
        switch (uriType)
        {
            case LOCATIONS:
                id = db.insertOrThrow(DogBeachesContract.Locations.TABLE_NAME, null, contentValues);
                uriString = DogBeachesContract.Locations.TABLE_NAME + "/" + id;
                break;
            case ANIMALS:
                id = db.insertOrThrow(DogBeachesContract.Animals.TABLE_NAME, null, contentValues);
                uriString = DogBeachesContract.Animals.TABLE_NAME + "/" + id;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(uriString);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delCount = 0;
        String id;
        String where;
        int uriType = sURIMatcher.match(uri);

        switch(uriType)
        {
            case LOCATIONS:
                delCount = db.delete(DogBeachesContract.Locations.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION_ID:
                id = uri.getLastPathSegment();
                where = DogBeachesContract.Locations.COLUMN_ID + " = " + id;
                delCount = db.delete(DogBeachesContract.Locations.TABLE_NAME, where, selectionArgs);
                break;
            case ANIMALS:
                delCount = db.delete(DogBeachesContract.Animals.TABLE_NAME, selection, selectionArgs);
                break;
            case ANIMAL_ID:
                id = uri.getLastPathSegment();
                where = DogBeachesContract.Animals.COLUMN_ID + " = " + id;
                delCount = db.delete(DogBeachesContract.Animals.TABLE_NAME, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        // ToDo: implement listener notifications
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updateCount = 0;
        String id;
        String where;
        switch (sURIMatcher.match(uri))
        {
            case LOCATIONS:
                updateCount = db.update(DogBeachesContract.Locations.TABLE_NAME,
                        contentValues, selection, selectionArgs);
                break;
            case LOCATION_ID:
                id = uri.getLastPathSegment();
                where = DogBeachesContract.Locations.COLUMN_ID + " = " + id;
                updateCount = db.update(DogBeachesContract.Locations.TABLE_NAME,
                        contentValues, where, selectionArgs);
                break;
            case ANIMALS:
                updateCount = db.update(DogBeachesContract.Animals.TABLE_NAME, contentValues,
                        selection, selectionArgs);
                break;
            case ANIMAL_ID:
                id = uri.getLastPathSegment();
                where = DogBeachesContract.Animals.COLUMN_ID + " = " + id;
                updateCount = db.update(DogBeachesContract.Animals.TABLE_NAME,
                        contentValues, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // Todo: Notify listeners if necessary

        return updateCount;
    }

    @Override
    /* Returns mime type for URI */
    public String getType(Uri uri)
    {
        switch (sURIMatcher.match(uri))
        {
            case LOCATIONS:
                return DogBeachesContract.Locations.CONTENT_TYPE;
            case LOCATION_ID:
                return DogBeachesContract.Locations.CONTENT_ITEM_TYPE;
            case ANIMALS:
                return DogBeachesContract.Animals.CONTENT_TYPE;
            case ANIMAL_ID:
                return DogBeachesContract.Animals.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }
}
