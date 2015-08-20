package bit.stewasc3.dogbeaches;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        LocationRecyclerFragment.OnSightingsSelectedListener
{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mContentContainer;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();
        mContentContainer = (FrameLayout) findViewById(R.id.content_container);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // As we're using a Toolbar, we should retrieve it and set it
        // to be our ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Set initial content fragment to home
        setContentFragment(new HomeFragment());
    }

    private void setupDrawerContent(NavigationView nv)
    {
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.drawer_home: // Home was clicked
                        getSupportActionBar().setTitle("Home");
                        setContentFragment(new HomeFragment());
                        break;
                    case R.id.drawer_map: // Map was clicked
                        getSupportActionBar().setTitle("Map");
                        setContentFragment(new MapDisplayFragment());
                        break;
                    case R.id.drawer_locations: // Locations was clicked
                        setContentFragment(new LocationRecyclerFragment());
                        getSupportActionBar().setTitle("Location List");
                        break;
                    case R.id.drawer_wildlife: // Wildlife was clicked
                        notImplemented();
                        //setContentFragment(new WildlifeFragment());
                        //getSupportActionBar().setTitle("Wildlife");
                        break;
                    case R.id.drawer_report: // Report was clicked
                        setContentFragment(new ReportFragment());
                        getSupportActionBar().setTitle("Wildlife Report");
                        break;
                    case R.id.drawer_donate: // Donate was clicked
                        notImplemented();
                        //setContentFragment(new DonateFragment());
                        //getSupportActionBar().setTitle("Donate");
                        break;
                    case R.id.drawer_safety: // Safety was clicked
                        setContentFragment(new SafetyFragment());
                        getSupportActionBar().setTitle("Dog Safety");
                        break;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Replace content fragment with fragment argument, add to back stack so we can manually
    // pop them off if required
    private void setContentFragment(Fragment f)
    {
        fm.beginTransaction()
                .replace(R.id.content_container, f)
                .addToBackStack("")
                .commit();
    }

    // Show a non implemented toast when user clicks unimplemented feature
    private void notImplemented()
    {
        Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSightingsSelected(int locationId)
    {
        Fragment f = SightingRecyclerFragment.newInstance(locationId);
        setContentFragment(f);
    }

    public void onReportAction(MenuItem mi)
    {
        //handle onClick of camera here
        setContentFragment(new ReportFragment());
        getSupportActionBar().setTitle("Wildlife Report");

    }


}
