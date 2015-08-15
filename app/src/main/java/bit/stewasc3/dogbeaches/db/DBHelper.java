package bit.stewasc3.dogbeaches.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sam on 10/08/15.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "docdog.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        LocationsTable.onCreate(sqLiteDatabase);
        AnimalsTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion)
    {
        LocationsTable.onUpgrade(sqLiteDatabase, newVersion, oldVersion);
        AnimalsTable.onUpgrade(sqLiteDatabase, newVersion, oldVersion);
    }

    /*
    public void addLocation(UserAPI.Location l)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, l.getName());
        values.put(COLUMN_CATEGORY, l.getCategory());
        values.put(COLUMN_ANIMAL_BLURB, l.getCategory());
        values.put(COLUMN_DOG_STATUS, l.getDogStatus());
        values.put(COLUMN_DOG_GUIDELINES, l.getDogGuidelines());
        values.put(COLUMN_IMAGE_THUMBNAIL, l.getImageThumbnail());
        values.put(COLUMN_IMAGE_MEDIUM, l.getImageMedium());
        values.put(COLUMN_LATITUDE, l.getLatitude());
        values.put(COLUMN_LONGITUDE, l.getLongitude());

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }
    */
}
