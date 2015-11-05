package io.github.lrsdev.dogbeaches.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class for creating and upgrading the lcoal SQLite database.
 */
public class DBHelper extends SQLiteOpenHelper
{
    /**
     * Constant defining the local database name.
     */
    private static final String DATABASE_NAME = "docdog.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Calls DB Helper classes to execute table creation code.
     *
     * @param sqLiteDatabase A reference to an SQLite database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        LocationTable.onCreate(sqLiteDatabase);
        AnimalTable.onCreate(sqLiteDatabase);
        SyncTable.onCreate(sqLiteDatabase);
        ReportTable.onCreate(sqLiteDatabase);
    }

    /**
     * Calls DB Helper classes to execute table upgrade code.
     * @param sqLiteDatabase A reference to an SQLite database
     * @param newVersion New database version integer
     * @param oldVersion Old database version integer
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion)
    {
        // Code must be defined in helper classes to migrate data when upgrading the database.
        //LocationTable.onUpgrade(sqLiteDatabase, newVersion, oldVersion);
        //AnimalTable.onUpgrade(sqLiteDatabase, newVersion, oldVersion);
    }
}
