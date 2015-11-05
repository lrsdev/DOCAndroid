package io.github.lrsdev.dogbeaches.db;

/**
 * An interface defining name and column constants for the local report SQLite table.
 *
 * @author Samuel Stewart
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

    /**
     * A complete projection for querying the report table.
     */
    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_LOCATION_ID, COLUMN_ANIMAL_ID,
            COLUMN_LOCATION_ID, COLUMN_BLURB, COLUMN_IMAGE, COLUMN_LATITUDE,
            COLUMN_LONGITUDE, COLUMN_CREATED_AT};
}
