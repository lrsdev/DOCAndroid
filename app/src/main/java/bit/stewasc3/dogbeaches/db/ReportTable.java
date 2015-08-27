package bit.stewasc3.dogbeaches.db;

/**
 * Reports are stored in this table until synchronisation.
 */

public class ReportTable
{
    public static final String TABLE_REPORTS = "reports";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_ANIMAL_ID = "animal_id";
    public static final String COLUMN_BLURB = "blurb";
    public static final String COLUMN_IMAGE = "image";
}
