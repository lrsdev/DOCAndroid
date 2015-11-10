package io.github.lrsdev.dogbeaches;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;
import io.github.lrsdev.dogbeaches.map.MapFragment;

public class MainActivity extends AppCompatActivity implements ResultCallback
{
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private FragmentManager fm;
    private SharedPreferences prefs;
    public static final String AUTHORITY = DogBeachesContract.AUTHORITY;
    public static final String ACCOUNT_TYPE = BuildConfig.SYNC_ACCOUNT_TYPE;
    public static final String ACCOUNT = "Sync Account";
    private boolean mLocationServicesEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        if (prefs.getBoolean("first_run", true))
        {
            firstRun();
        }
        performManualSync();
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
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
        setContentFragment(new HomeFragment());

        LocationManager.get(MainActivity.this).checkServicesEnabled(MainActivity.this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private void firstRun()
    {
        unzipAssets();
        setupAutoSync();
        prefs.edit().putBoolean("first_run", false).apply();
    }

    private void unzipAssets()
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = getAssets().open("assets.zip");
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                if (ze.isDirectory())
                {
                    File d = new File(getApplicationInfo().dataDir + "/" + filename);
                    d.mkdirs();
                    continue;
                }
                FileOutputStream fos = new FileOutputStream(getApplicationInfo().dataDir + "/" + filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                zis.closeEntry();
            }
        } catch (IOException e)
        {
            Log.e(TAG, e.toString());
        }
    }

    private void setupAutoSync()
    {
        Account account = CreateSyncAccount(this);
        ContentResolver resolver = getContentResolver();
        resolver.setSyncAutomatically(account, AUTHORITY, true);
    }

    private void performManualSync()
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(null, AUTHORITY, bundle);
    }

    public static Account CreateSyncAccount(Context context)
    {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null))
        {
            ContentResolver.setIsSyncable(newAccount, DogBeachesContract.AUTHORITY, 1);
        }
        else
        {
            Log.e("Account", "Error adding account");
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
                        setContentFragment(MapFragment.newInstance());
                        break;
                    case R.id.drawer_locations: // Locations was clicked
                        setContentFragment(new LocationRecyclerFragment());
                        break;
                    case R.id.drawer_wildlife: // Wildlife was clicked
                        setContentFragment(new AnimalRecyclerFragment());
                        break;
                    case R.id.drawer_report: // Report was clicked
                        startReportFragment();
                        break;
                    case R.id.drawer_donate: // Donate was clicked
                        setContentFragment(new DonateFragment());
                        //notImplemented();
                        break;
                    case R.id.drawer_safety: // Safety was clicked
                        setContentFragment(new SafetyFragment());
                        break;
                    case R.id.drawer_feedback: // Feedback was clicked
                        setContentFragment(new FeedbackFragment());
                        //notImplemented();
                        break;

                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void startReportFragment()
    {
        if (LocationManager.get(MainActivity.this).getLocation() != null)
        {
            setContentFragment(new ReportFragment());
        }
        else
        {
            Toast.makeText(MainActivity.this,
                    "Cannot determine location for reporting. Please check settings.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResult(Result locationSettingsResult)
    {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode())
        {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                mLocationServicesEnabled = true;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");

                try
                {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e)
                {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode)
                {
                    case RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        mLocationServicesEnabled = true;
                        break;
                    case RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
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
        startReportFragment();
    }

    public void onFeedbackAction(View v)
    {
        //handle onClick of Feedback Button
        if (v.getId() == R.id.Btnfeedback)
        {
            setContentFragment(new FeedbackFragment());
            //notImplemented();
        }
    }
}
