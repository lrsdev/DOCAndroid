package bit.stewasc3.dogbeaches.db;

/**
 * Created by sam on 28/08/15.
 */
public interface IAnimalTableConstants
{
    public static final String TABLE_NAME = "animals";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BLURB = "blurb";
    public static final String COLUMN_GUIDELINES = "guidelines";
    public static final String COLUMN_EXT_URL = "ext_url";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_IMAGE_URL = "image_url";

    public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_NAME, COLUMN_BLURB,
            COLUMN_GUIDELINES, COLUMN_EXT_URL, COLUMN_IMAGE, COLUMN_IMAGE_URL};
}
