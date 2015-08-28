package bit.stewasc3.dogbeaches.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sam on 14/08/15.
 */
public class AnimalTable implements IAnimalTableConstants
{
    private static final String TABLE_CREATE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " integer primary key, " +
            COLUMN_NAME + " text not null, " +
            COLUMN_BLURB + " text not null, " +
            COLUMN_GUIDELINES + " text not null, " +
            COLUMN_EXT_URL + " text not null, " +
            COLUMN_IMAGE_THUMBNAIL +  " text not null, " +
            COLUMN_IMAGE_MEDIUM + " text not null, " +
            COLUMN_IMAGE_MEDIUM_LOCAL + " text not null);";

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
