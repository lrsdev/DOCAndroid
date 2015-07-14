package bit.stewasc3.dogbeaches;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    //private String[] mNavTitles;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    //private ListView mDrawerList;

    //private LinearLayout mDrawerView;

    private FrameLayout mContentContainer;
    private FragmentManager fm;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get references to needed views, fragment manager
        fm = getFragmentManager();
        mContentContainer = (FrameLayout) findViewById(R.id.content_container);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        //mNavTitles = getResources().getStringArray(R.array.navigation_array);

        //mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //mDrawerView = (LinearLayout) findViewById(R.id.drawer_view);

        //setupDrawerList();
        //setupDrawerToggle();

        // As we're using a Toolbar, we should retrieve it and set it
        // to be our ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // Now retrieve the DrawerLayout so that we can set the status bar color.
        // This only takes effect on Lollipop, or when using translucentStatusBar
        // on KitKat.
        //mDrawerLayout.setStatusBarBackgroundColor(yourChosenColor);
        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayUseLogoEnabled(true);

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
                    case R.id.drawer_sightings: // Sightings was clicked
                        setContentFragment(new SightingRecyclerFragment());
                        getSupportActionBar().setTitle("Sightings");
                        break;
                    case R.id.drawer_wildlife: // Wildlife was clicked
                        notImplemented();
                        break;
                    case R.id.drawer_report: // Report was clicked
                        setContentFragment(new ReportFragment());
                        getSupportActionBar().setTitle("Wildlife Report");
                        break;
                    case R.id.drawer_donate: // Donate was clicked
                        notImplemented();
                        break;
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    // Set array adapter and click listeners for the navigation drawer's listview.
    /*private void setupDrawerList()
    {
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mNavTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }*/

    // Setup navigation drawer toggle (Clicking hamburger icon), implement onDrawerOpened and
    // onDrawerClosed callbacks. ToDo: Set titles correctly on Open/Close
    private void setupDrawerToggle()
    {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open,
                R.string.drawer_closed)
        {
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        // Drawer indicator (Hamburger Icon)
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    // Synchronise navigation drawer icon (switched between arrow and hamburger icon)
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
 //       mDrawerToggle.onConfigurationChanged(newConfig);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            // Switching on position, relative mNavTitles string array.
            switch (position)
                    {
                        case 0: // Home was clicked
                            getSupportActionBar().setTitle("Home");
                            setContentFragment(new HomeFragment());
                            break;
                        case 1: // Map was clicked
                            getSupportActionBar().setTitle("Map");
                            setContentFragment(new MapDisplayFragment());
                            break;
                        case 2: // Locations was clicked
                            setContentFragment(new LocationRecyclerFragment());
                            getSupportActionBar().setTitle("Location List");
                            break;
                        case 3: // Sightings was clicked
                            setContentFragment(new SightingRecyclerFragment());
                            getSupportActionBar().setTitle("Sightings");
                            break;
                        case 4: // Wildlife was clicked
                            notImplemented();
                            break;
                        case 5: // Report was clicked
                            setContentFragment(new ReportFragment());
                            getSupportActionBar().setTitle("Wildlife Report");
                            break;
                        case 6: // Donate was clicked
                            notImplemented();
                            break;
                    }

            // Set checked, close the drawer after selection. ToDo: Selected item styling
            //mDrawerList.setItemChecked(position, true);
            //mDrawerLayout.closeDrawer(mDrawerView);
        }
    }
}
