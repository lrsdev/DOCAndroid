package bit.stewasc3.dogbeaches.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sam on 10/08/15.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "docdog.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        LocationTable.onCreate(sqLiteDatabase);
        AnimalTable.onCreate(sqLiteDatabase);
        SyncTable.onCreate(sqLiteDatabase);
        ReportTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion)
    {
        //LocationTable.onUpgrade(sqLiteDatabase, newVersion, oldVersion);
        //AnimalTable.onUpgrade(sqLiteDatabase, newVersion, oldVersion);
    }
}
