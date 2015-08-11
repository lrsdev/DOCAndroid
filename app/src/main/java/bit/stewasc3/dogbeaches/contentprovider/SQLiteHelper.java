package bit.stewasc3.dogbeaches.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sam on 10/08/15.
 */
public class SQLiteHelper extends SQLiteOpenHelper
{

    public static final String TABLE_LOCATIONS = "locations";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ANIMAL_BLURB = "animal_blurb";
    public static final String COLUMN_DOG_STATUS = "dog_status";
    public static final String COLUMN_DOG_GUIDELINES = "dog_guidelines";
    public static final String COLUMN_IMAGE_THUMBNAIL = "image_thumbnail";
    public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    private static final String DATABASE_NAME = "docdog.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_LOCATIONS + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_CATEGORY + " text not null, " +
            COLUMN_ANIMAL_BLURB + " text not null, " +
            COLUMN_DOG_STATUS + " text not null, " +
            COLUMN_DOG_GUIDELINES + " text not null, " +
            COLUMN_IMAGE_THUMBNAIL +  " text not null, " +
            COLUMN_IMAGE_MEDIUM + " text not null, " +
            COLUMN_LATITUDE + " real not null, " +
            COLUMN_LONGITUDE + " real not null);";

    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
    }

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
}
