package io.github.lrsdev.dogbeaches;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;
import io.github.lrsdev.dogbeaches.map.MapFragment;
import io.github.lrsdev.dogbeaches.sync.SyncAdapter;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FrameLayout mContentContainer;
    private FragmentManager fm;
    private SharedPreferences prefs;
    private ProgressDialog syncProgress;

    private static IntentFilter syncIntentFilter = new IntentFilter(SyncAdapter.FIRST_SYNC_FINISHED);
    private BroadcastReceiver syncFinishedReceiver;
    public static final String AUTHORITY = DogBeachesContract.AUTHORITY;
    public static final String ACCOUNT_TYPE = BuildConfig.SYNC_ACCOUNT_TYPE;
    public static final String ACCOUNT = "Sync Account";

    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE);
        if(prefs.getBoolean("first_run", true))
        {
           firstRun();
        }
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
        setContentFragment(new HomeFragment());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        /*
        if (!prefs.getBoolean("first_sync_completed", false))
        {
            performManualSync();
        }*/
    }

    private void firstRun()
    {
        unzipAssets();
        setupAutoSync();
        performManualSync();
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
        }
        catch (IOException e)
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
        /*
        syncFinishedReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
            }
        };*/
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        //registerReceiver(syncFinishedReceiver, new IntentFilter(SyncAdapter.FIRST_SYNC_FINISHED));
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
                        setContentFragment(MapFragment.newInstance());
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
                        //setContentFragment(new DonateFragment());
                        notImplemented();
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

    public void onFeedbackAction(View v)
    {
        //handle onClick of Feedback Button
        if(v.getId()==R.id.Btnfeedback)
        {
            setContentFragment(new FeedbackFragment());
            //notImplemented();
        }
    }
}
