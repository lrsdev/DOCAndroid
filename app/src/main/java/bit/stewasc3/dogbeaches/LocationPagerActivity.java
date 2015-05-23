package bit.stewasc3.dogbeaches;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import UserAPI.Location;


public class LocationPagerActivity extends AppCompatActivity
{
    private ViewPager mViewPager;
    private ArrayList<Location> mLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.locationViewPager);
        setContentView(mViewPager);

        // Get passed array
        mLocations = (ArrayList<Location>)
                getIntent().getSerializableExtra(LocationListFragment.KEY_LOCATION_ARRAY);

        Integer index = getIntent().getIntExtra(LocationListFragment.KEY_LOCATION_ARRAY_INDEX, 0);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int position)
            {
                Location l = mLocations.get(position);
                return LocationFragment.newInstance(l);
            }

            @Override
            public int getCount()
            {
                return mLocations.size();
            }
        });

        mViewPager.setCurrentItem(index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
