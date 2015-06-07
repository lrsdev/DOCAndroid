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
import UserAPI.Report;

public class SightingPagerActivity extends AppCompatActivity
{
    public static final String KEY_REPORT_ARRAY = "dogapp.report_array";
    public static final String KEY_REPORT_INDEX = "dogapp.report_index";

    private ViewPager mViewPager;
    private ArrayList<Report> mReports;
    private CirclePageIndicator mCircleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting_pager);
        mViewPager = (ViewPager)findViewById(R.id.sightingViewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mReports = (ArrayList<Report>)getIntent().getSerializableExtra(KEY_REPORT_ARRAY);

        Integer index = getIntent().getIntExtra(KEY_REPORT_INDEX, 0);
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm)
        {
            @Override
            public Fragment getItem(int position)
            {
                Report r = mReports.get(position);
                return SightingFragment.newInstance(r);
            }

            @Override
            public int getCount()
            {
                return mReports.size();
            }
        });

        mCircleIndicator = (CirclePageIndicator)findViewById(R.id.sightingViewPagerIndicator);
        mCircleIndicator.setViewPager(mViewPager);

        mViewPager.setCurrentItem(index);
        setTitle("Report");

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
                Report r = mReports.get(i);
                setTitle("Report");
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
