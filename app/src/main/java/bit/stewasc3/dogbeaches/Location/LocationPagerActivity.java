package bit.stewasc3.dogbeaches.Location;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.viewpagerindicator.IconPageIndicator;
import UserAPI.Location;
import bit.stewasc3.dogbeaches.R;

/**
 * Created by samuel on 9/07/15.
 */
public class LocationPagerActivity extends AppCompatActivity
{
    public static String KEY_LOCATION = "dogapp.location";
    private ViewPager mViewPager;
    private IconPageIndicator mIconIndicator;
    private Location mLocation;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_pager);
        mViewPager = (ViewPager) findViewById(R.id.locationViewPager);

        // Provide up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLocation = (Location) getIntent().getSerializableExtra(KEY_LOCATION);
        FragmentManager fm = getSupportFragmentManager();

        mViewPager.setAdapter(new LocationPagerAdapter(fm, mLocation));

        mIconIndicator = (IconPageIndicator)findViewById(R.id.locationViewPagerIndicator);
        mIconIndicator.setViewPager(mViewPager);

        setTitle(mLocation.getName());
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
                // Change behaviour so the back button simulates a hardware back button press
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
