package bit.stewasc3.dogbeaches.db;

/**
 * Created by sam on 28/08/15.
 */
public interface IReportTableConstants
{
    public static final String TABLE_NAME = "reports";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_ANIMAL_ID = "animal_id";
    public static final String COLUMN_BLURB = "blurb";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_CREATED_AT = "timestamp";

    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_LOCATION_ID, COLUMN_ANIMAL_ID,
            COLUMN_LOCATION_ID, COLUMN_BLURB, COLUMN_IMAGE, COLUMN_LATITUDE,
            COLUMN_LONGITUDE, COLUMN_CREATED_AT};
}
