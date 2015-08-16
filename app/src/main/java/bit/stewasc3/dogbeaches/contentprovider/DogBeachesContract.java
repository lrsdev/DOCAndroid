package bit.stewasc3.dogbeaches.contentprovider;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by sam on 15/08/15.
 */
public class DogBeachesContract
{
    public static final String AUTHORITY = "bit.stewasc3.dogbeaches.contentprovider.DogBeachesProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Items table constants
     */
    public static final class Locations
    {
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_ANIMAL_BLURB = "animal_blurb";
        public static final String COLUMN_DOG_STATUS = "dog_status";
        public static final String COLUMN_DOG_GUIDELINES = "dog_guidelines";
        public static final String COLUMN_IMAGE_THUMBNAIL = "image_thumbnail";
        public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        private static final String LOCATIONS_TABLE = "locations";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LOCATIONS_TABLE);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/bit.stewasc3.dogbeaches.locations";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/bit.stewasc3.dodbeaches.locations";

        public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_NAME, COLUMN_CATEGORY,
            COLUMN_ANIMAL_BLURB, COLUMN_DOG_STATUS, COLUMN_DOG_GUIDELINES, COLUMN_IMAGE_THUMBNAIL,
            COLUMN_IMAGE_MEDIUM, COLUMN_LATITUDE, COLUMN_LONGITUDE};

    }

    public static final class Animals
    {

    }
}
