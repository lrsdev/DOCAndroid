package bit.stewasc3.dogbeaches.OldLocationClasses;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import UserAPI.Location;
import bit.stewasc3.dogbeaches.R;

/**
 * Created by samuel on 9/07/15.
 */
public class LocationPagerAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter
{
    private static final int[] icons = new int[] {
            R.drawable.infoicon_group,
            R.drawable.sightingicon_group,
            R.drawable.wildlifeicon_group,
            R.drawable.mapicon_group,
    };

    private static int LENGTH = 4;
    private Location mLocation;

    public LocationPagerAdapter(FragmentManager fm, Location l)
    {
        super(fm);
        mLocation = l;
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0: return LocationInfoFragment.newInstance(mLocation);
            case 1: return new LocationSightingFragment();
            case 2: return new LocationWildlifeFragment();
            case 3: return new LocationMapFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        return LENGTH;
    }

    @Override
    public int getIconResId(int index)
    {
        return icons[index];
    }
}
