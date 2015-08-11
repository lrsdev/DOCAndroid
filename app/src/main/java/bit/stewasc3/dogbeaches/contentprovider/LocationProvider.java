package bit.stewasc3.dogbeaches.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by sam on 11/08/15.
 */
public class LocationProvider extends ContentProvider
{
    private SQLiteHelper mDb;
    private static final String AUTHORITY = "bit.stewasc3.dogbeaches.contentprovider.LocationProvider";
    private static final String LOCATIONS_TABLE = "locations";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LOCATIONS_TABLE);
    public static final int LOCATIONS = 1;
    public static final int LOCATIONS_ID = 2;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sURIMatcher.addURI(AUTHORITY, LOCATIONS_TABLE, LOCATIONS);
        sURIMatcher.addURI(AUTHORITY, LOCATIONS_TABLE + "/#", LOCATIONS_ID);
    }

    @Override
    public boolean onCreate()
    {
        mDb = new SQLiteHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SQLiteHelper.TABLE_LOCATIONS);
        int uriType = sURIMatcher.match(uri);
        switch (uriType)
        {
            case LOCATIONS_ID:
                queryBuilder.appendWhere(SQLiteHelper.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case LOCATIONS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDb.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings)
    {
        return 0;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }
}
