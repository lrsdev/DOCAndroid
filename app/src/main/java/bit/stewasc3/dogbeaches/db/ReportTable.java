package bit.stewasc3.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Report storage
 */

public class ReportTable
{
    public static final String TABLE_NAME = "reports";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_ANIMAL_ID = "animal_id";
    public static final String COLUMN_BLURB = "blurb";
    public static final String COLUMN_IMAGE_URI = "image";
    public static final String COLUMN_CREATED_AT = "timestamp";

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_LOCATION_ID + " integer, " +
            COLUMN_ANIMAL_ID + " integer, " +
            COLUMN_BLURB + " text, " +
            COLUMN_IMAGE_URI + " text, " +
            COLUMN_CREATED_AT + " timestamp);";

    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_LOCATION_ID, COLUMN_ANIMAL_ID,
    COLUMN_LOCATION_ID, COLUMN_BLURB, COLUMN_IMAGE_URI, COLUMN_CREATED_AT};

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
