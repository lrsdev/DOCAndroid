package io.github.lrsdev.dogbeaches.db;

/**
 * Created by sam on 28/08/15.
 */
public interface ILocationTableConstants
{
    public static final String TABLE_NAME = "locations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ANIMAL_BLURB = "animal_blurb";
    public static final String COLUMN_DOG_STATUS = "dog_status";
    public static final String COLUMN_DOG_GUIDELINES = "dog_guidelines";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_NAME, COLUMN_CATEGORY,
            COLUMN_ANIMAL_BLURB, COLUMN_DOG_STATUS, COLUMN_DOG_GUIDELINES,
            COLUMN_IMAGE, COLUMN_IMAGE_URL, COLUMN_LATITUDE, COLUMN_LONGITUDE};
}
