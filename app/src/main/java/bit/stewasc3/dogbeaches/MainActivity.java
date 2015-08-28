package bit.stewasc3.dogbeaches;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;

import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;
import bit.stewasc3.dogbeaches.map.MapDisplayFragment;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mContentContainer;
    private FragmentManager fm;
    private Account mAccount;
    private android.location.Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    public static final String AUTHORITY = DogBeachesContract.AUTHORITY;
    public static final String ACCOUNT_TYPE = "bit.stewasc3.dogbeaches";
    public static final String ACCOUNT = "dummyaccount";

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

        // Create a dummy account for synchronisation, add a periodic sync for two hour intervals.
        mAccount = CreateSyncAccount(this);
        //ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);

        // Set initial content fragment to home
        setContentFragment(new HomeFragment());

        // Some testing
        File f = Environment.getExternalStorageDirectory();
        File j = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static Account CreateSyncAccount(Context context)
    {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null))
        {
            // Inform the system this account supports sync
            ContentResolver.setIsSyncable(newAccount, DogBeachesContract.AUTHORITY, 1);
            // Inform the system this account is eligible for auto sync when the network is up
        }
        else
        {
            Log.d("Account", "Error adding account");
        }
        return newAccount;
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
                        setContentFragment(MapDisplayFragment.newInstance());
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

    public void onReportAction(MenuItem mi)
    {
        //handle onClick of camera here
        setContentFragment(new ReportFragment());
        getSupportActionBar().setTitle("Wildlife Report");

    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }


    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

}
