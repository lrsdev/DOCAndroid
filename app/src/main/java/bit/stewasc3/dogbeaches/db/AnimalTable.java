package bit.stewasc3.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sam on 14/08/15.
 */
public class AnimalTable
{
    public static final String TABLE_ANIMALS = "animals";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BLURB = "blurb";
    public static final String COLUMN_GUIDELINES = "guidelines";
    public static final String COLUMN_EXT_URL = "ext_url";
    public static final String COLUMN_IMAGE_THUMBNAIL = "image_thumbnail";
    public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
    public static final String COLUMN_IMAGE_MEDIUM_LOCAL = "image_medium_local";
    public static final String COLUMN_IMAGE_THUMBNAIL_LOCATION = "image_thumbnail_local";

    private static final String TABLE_CREATE = "create table " + TABLE_ANIMALS + "(" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_BLURB + " text not null, " +
            COLUMN_GUIDELINES + " text not null, " +
            COLUMN_EXT_URL + " text not null, " +
            COLUMN_IMAGE_THUMBNAIL +  " text not null, " +
            COLUMN_IMAGE_MEDIUM + " text not null, " +
            COLUMN_IMAGE_MEDIUM_LOCAL + " text not null, " +
            COLUMN_IMAGE_THUMBNAIL_LOCATION + " text not null);";

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIMALS);
        onCreate(db);
    }
}
