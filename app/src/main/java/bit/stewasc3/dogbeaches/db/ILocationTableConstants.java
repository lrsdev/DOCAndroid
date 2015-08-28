package bit.stewasc3.dogbeaches.db;

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
    public static final String COLUMN_IMAGE_THUMBNAIL = "image_thumbnail";
    public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
    public static final String COLUMN_IMAGE_MEDIUM_LOCAL = "image_medium_local";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_NAME, COLUMN_CATEGORY,
            COLUMN_ANIMAL_BLURB, COLUMN_DOG_STATUS, COLUMN_DOG_GUIDELINES, COLUMN_IMAGE_THUMBNAIL,
            COLUMN_IMAGE_MEDIUM, COLUMN_IMAGE_MEDIUM_LOCAL, COLUMN_LATITUDE, COLUMN_LONGITUDE};
}
