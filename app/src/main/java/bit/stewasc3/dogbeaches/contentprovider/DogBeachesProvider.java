package bit.stewasc3.dogbeaches.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import bit.stewasc3.dogbeaches.db.DBHelper;
import bit.stewasc3.dogbeaches.db.LocationsTable;

/**
 * Created by sam on 11/08/15.
 */
public class DogBeachesProvider extends ContentProvider
{
    private DBHelper mHelper;
    public static final int LOCATIONS = 10;
    public static final int LOCATIONS_ID = 11;
    public static final int ANIMALS = 20;
    public static final int ANIMALS_ID = 21;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
        Initialise the URI Matcher. # is a placeholder for a numeric value.
     */
    static
    {
        sURIMatcher.addURI(DogBeachesContract.AUTHORITY, "locations", LOCATIONS);
        sURIMatcher.addURI(DogBeachesContract.AUTHORITY, "locations/#", LOCATIONS_ID);
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
        queryBuilder.setTables(LocationsTable.TABLE_LOCATIONS);
        int uriType = sURIMatcher.match(uri);
        switch (uriType)
        {
            case LOCATIONS_ID:
                queryBuilder.appendWhere(LocationsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case LOCATIONS:
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
        // ToDo: Check for correct URI, throw exception
        SQLiteDatabase db = mHelper.getWritableDatabase();

        long id = 0;

        if(sURIMatcher.match(uri) == LOCATIONS)
        {
            id = db.insert(LocationsTable.TABLE_LOCATIONS, null, contentValues);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(LocationsTable.TABLE_LOCATIONS + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delCount = 0;
        int uriType = sURIMatcher.match(uri);

        switch(uriType)
        {
            case LOCATIONS:
                delCount = db.delete(LocationsTable.TABLE_LOCATIONS, selection, selectionArgs);
                break;
            case LOCATIONS_ID:
                String idStr = uri.getLastPathSegment();
                String where = LocationsTable.TABLE_LOCATIONS;
                delCount = db.delete(LocationsTable.TABLE_LOCATIONS, where, selectionArgs);
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
        int uriType = sURIMatcher.match(uri);
        switch (sURIMatcher.match(uri))
        {
            case LOCATIONS:
                updateCount = db.update(LocationsTable.TABLE_LOCATIONS, contentValues, selection, selectionArgs);
                break;
            case LOCATIONS_ID:
                String idStr = uri.getLastPathSegment();
                String where = LocationsTable.COLUMN_ID + " " + idStr;
                updateCount = db.update(LocationsTable.TABLE_LOCATIONS, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // Todo: Notify listeners

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
            case LOCATIONS_ID:
                return DogBeachesContract.Locations.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }
}
