package bit.stewasc3.dogbeaches;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    private String[] mNavTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
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
        mNavTitles = getResources().getStringArray(R.array.navigation_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        setupDrawerList();
        setupDrawerToggle();

        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        // Set initial content fragment to home
        setContentFragment(new HomeFragment());
    }

    // Set array adapter and click listeners for the navigation drawer's listview.
    private void setupDrawerList()
    {
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mNavTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

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
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item))
        {
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
                        case 3: // Wildlife was clicked
                            notImplemented();
                            break;
                        case 4: // Report was clicked
                            setContentFragment(new ReportFragment());
                            getSupportActionBar().setTitle("Wildlife Report");
                            break;
                        case 5: // Donate was clicked
                            notImplemented();
                            break;
                    }

            // Set checked, close the drawer after selection. ToDo: Selected item styling
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
}
