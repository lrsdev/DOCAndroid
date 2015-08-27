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
        public static final String COLUMN_IMAGE_MEDIUM_LOCAL = "image_medium_local";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String LOCATIONS_TABLE = "locations";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LOCATIONS_TABLE);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/locations";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/location";

        public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_NAME, COLUMN_CATEGORY,
            COLUMN_ANIMAL_BLURB, COLUMN_DOG_STATUS, COLUMN_DOG_GUIDELINES, COLUMN_IMAGE_THUMBNAIL,
            COLUMN_IMAGE_MEDIUM, COLUMN_IMAGE_MEDIUM_LOCAL, COLUMN_LATITUDE, COLUMN_LONGITUDE};
    }

    public static final class Animals
    {
        public static final String ANIMALS_TABLE = "animals";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_BLURB = "blurb";
        public static final String COLUMN_GUIDELINES = "guidelines";
        public static final String COLUMN_EXT_URL = "ext_url";
        public static final String COLUMN_IMAGE_THUMBNAIL = "image_thumbnail";
        public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
        public static final String COLUMN_IMAGE_MEDIUM_LOCAL = "image_medium_local";
        public static final String COLUMN_IMAGE_THUMBNAIL_LOCAL = "image_thumbnail_local";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ANIMALS_TABLE);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/animals";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/animal";

        public static final String[] PROJECTION_ALL = {COLUMN_ID, COLUMN_NAME, COLUMN_BLURB,
                COLUMN_GUIDELINES, COLUMN_EXT_URL, COLUMN_IMAGE_THUMBNAIL, COLUMN_IMAGE_MEDIUM,
                COLUMN_IMAGE_MEDIUM_LOCAL, COLUMN_IMAGE_THUMBNAIL_LOCAL};



    }
}
