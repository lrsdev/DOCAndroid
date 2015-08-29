package bit.stewasc3.dogbeaches;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

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
    public static final String ACCOUNT = "Sync Account";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        mContentContainer = (FrameLayout) findViewById(R.id.content_container);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // As we're using a Toolbar, we should retrieve it and set it
        // to be our ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        if (navigationView != null)
        {
            setupDrawerContent(navigationView);
        }

        mAccount = CreateSyncAccount(this);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);

        setContentFragment(new HomeFragment());

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
                        setContentFragment(new HomeFragment());
                        break;
                    case R.id.drawer_map: // Map was clicked
                        setContentFragment(MapDisplayFragment.newInstance());
                        break;
                    case R.id.drawer_locations: // Locations was clicked
                        setContentFragment(new LocationRecyclerFragment());
                        break;
                    case R.id.drawer_wildlife: // Wildlife was clicked
                        notImplemented();
                        break;
                    case R.id.drawer_report: // Report was clicked
                        setContentFragment(new ReportFragment());
                        break;
                    case R.id.drawer_donate: // Donate was clicked
                        notImplemented();
                        break;
                    case R.id.drawer_safety: // Safety was clicked
                        setContentFragment(new SafetyFragment());
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
        Intent i = new Intent(MainActivity.this, ReportFragment.class);
        startActivity(i);
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
