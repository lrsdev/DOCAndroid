package io.github.lrsdev.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * A helper class for creating and upgrading the local SQLite location table.
 *
 * The table reflects current location information from the web service.
 * The table is updated by the synchronisation adapter.
 *
 * @author Samuel Stewart
 */
public class LocationTable implements ILocationTableConstants
{
    private static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_CATEGORY + " text not null, " +
            COLUMN_ANIMAL_BLURB + " text not null, " +
            COLUMN_DOG_STATUS + " text not null, " +
            COLUMN_DOG_GUIDELINES + " text not null, " +
            COLUMN_IMAGE + " text not null, " +
            COLUMN_IMAGE_URL + " text not null," +
            COLUMN_LATITUDE + " real not null, " +
            COLUMN_LONGITUDE + " real not null);";

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
