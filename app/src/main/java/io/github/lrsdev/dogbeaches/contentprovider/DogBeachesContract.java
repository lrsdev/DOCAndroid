package io.github.lrsdev.dogbeaches.contentprovider;

import android.content.ContentResolver;
import android.net.Uri;

import io.github.lrsdev.dogbeaches.BuildConfig;
import io.github.lrsdev.dogbeaches.db.IAnimalTableConstants;
import io.github.lrsdev.dogbeaches.db.ILocationTableConstants;

/**
 * Created by sam on 15/08/15.
 */
public class DogBeachesContract
{
    public static final String AUTHORITY = BuildConfig.AUTHORITY;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Items table constants
     */
    public static final class Locations implements ILocationTableConstants
    {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/locations";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/location";
    }

    public static final class Animals implements IAnimalTableConstants
    {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/animals";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/animal";
    }
}
