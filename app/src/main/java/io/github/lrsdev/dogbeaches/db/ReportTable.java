package io.github.lrsdev.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * A helper class for creating and upgrading the local SQLite report table.
 *
 * Stores user generated wildlife reports. Reports are removed once synchronisation successfully
 * executes.
 *
 * @author Samuel Stewart
 */
public class ReportTable implements IReportTableConstants
{

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_LOCATION_ID + " integer, " +
            COLUMN_ANIMAL_ID + " integer, " +
            COLUMN_BLURB + " text, " +
            COLUMN_IMAGE + " text, " +
            COLUMN_LATITUDE + " real, " +
            COLUMN_LONGITUDE + " real, " +
            COLUMN_CREATED_AT + " timestamp);";

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
