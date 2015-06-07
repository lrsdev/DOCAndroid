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
import UserAPI.Sighting;

public class SightingPagerActivity extends AppCompatActivity
{
    public static final String KEY_SIGHTING_ARRAY = "dogapp.report_array";
    public static final String KEY_SIGHTING_INDEX = "dogapp.report_index";

    private ViewPager mViewPager;
    private ArrayList<Sighting> mSightings;
    private CirclePageIndicator mCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting_pager);
        mViewPager = (ViewPager)findViewById(R.id.sightingViewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSightings = (ArrayList<Sighting>)getIntent().getSerializableExtra(KEY_SIGHTING_ARRAY);

        Integer index = getIntent().getIntExtra(KEY_SIGHTING_INDEX, 0);
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int position)
            {
                Sighting s = mSightings.get(position);
                return SightingFragment.newInstance(s);
            }

            @Override
            public int getCount()
            {
                return mSightings.size();
            }
        });

        mCircleIndicator = (CirclePageIndicator)findViewById(R.id.sightingViewPagerIndicator);
        mCircleIndicator.setViewPager(mViewPager);

        mViewPager.setCurrentItem(index);
        setTitle("Sighting");

        // Set title, set on indicator to enable it to cycle.
        mCircleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i2)
            {
            }

            @Override
            public void onPageSelected(int i)
            {
                Sighting r = mSightings.get(i);
                setTitle("Sighting");
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sighting_pager, menu);
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
