package bit.stewasc3.dogbeaches;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.viewpagerindicator.CirclePageIndicator;
import java.util.ArrayList;
import UserAPI.Location;


public class LocationPagerActivity extends AppCompatActivity
{
    private ViewPager mViewPager;
    private ArrayList<Location> mLocations;
    private CirclePageIndicator mCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationpager);
        mViewPager = (ViewPager) findViewById(R.id.locationViewPager);

        // Provide up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get passed array, Location type implements SERIALIZABLE. Or use singleton?
        mLocations = (ArrayList<Location>)
                getIntent().getSerializableExtra(LocationListFragment.KEY_LOCATION_ARRAY);

        // Index which was selected from location list ( Used to start ViewPager on correct fragment )
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

        mCircleIndicator = (CirclePageIndicator)findViewById(R.id.locationViewPagerIndicator);
        mCircleIndicator.setViewPager(mViewPager);

        mViewPager.setCurrentItem(index);
        setTitle(mLocations.get(index).getName());

        // Set title, set on indicator to enable it to cycle.
        mCircleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i2) { }

            @Override
            public void onPageSelected(int i)
            {
                Location l = mLocations.get(i);
                setTitle(l.getName());
            }

            @Override
            public void onPageScrollStateChanged(int i) { }
        });
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
        // Handle action bar item clicks here.
        switch(item.getItemId())
        {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
