package io.github.lrsdev.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * A helper class for creating and upgrading the local SQLite animal table.
 *
 * The table reflects current location information from the web service.
 * The table is updated by the synchronisation adapter.
 *
 * @author Samuel Stewart
 */
public class AnimalTable implements IAnimalTableConstants
{
    private static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_BLURB + " text not null, " +
            COLUMN_GUIDELINES + " text not null, " +
            COLUMN_EXT_URL + " text not null, " +
            COLUMN_IMAGE + " text not null, " +
            COLUMN_IMAGE_URL + " text not null); ";

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion)
    {
        // MIGRATION CODE NEEDS TO BE WRITTEN IF UPGRADING A LIVE DATASET.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
