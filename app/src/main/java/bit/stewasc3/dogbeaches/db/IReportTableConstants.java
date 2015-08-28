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
    public static final String COLUMN_IMAGE_URI = "image";
    public static final String COLUMN_CREATED_AT = "timestamp";

    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_LOCATION_ID, COLUMN_ANIMAL_ID,
            COLUMN_LOCATION_ID, COLUMN_BLURB, COLUMN_IMAGE_URI, COLUMN_CREATED_AT};
}
