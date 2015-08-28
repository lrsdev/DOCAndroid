package bit.stewasc3.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Report storage
 */

public class ReportTable implements IReportTableConstants
{

    public static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_LOCATION_ID + " integer, " +
            COLUMN_ANIMAL_ID + " integer, " +
            COLUMN_BLURB + " text, " +
            COLUMN_IMAGE_URI + " text, " +
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
