package bit.stewasc3.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sam on 20/08/15.
 */
public class SyncTable
{
    public static final String TABLE_NAME = "sync_metadata";
    public static final String LAST_SYNC = "last_sync";

    private static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            LAST_SYNC + " text primary key);";

    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
