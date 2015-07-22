package bit.stewasc3.dogbeaches;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.support.v7.widget.Toolbar;


public class SightingActivity extends AppCompatActivity
{
    public static final String KEY_LOCATIONID = "dogbeaches.sightingactivity.locationid";

    private FragmentManager fm;
    private FrameLayout mContentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting);

        fm = getFragmentManager();
        mContentContainer = (FrameLayout) findViewById(R.id.sightingActivityContentContainer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.sightingActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment f = null;

        // If activity started with location id request, load fragment with just locations from id.
        if(getIntent().hasExtra(KEY_LOCATIONID))
        {
            f = SightingRecyclerFragment.newInstance(getIntent().getExtras().getInt(KEY_LOCATIONID));
        }
        else
        {
            f = new SightingRecyclerFragment();
        }

        fm.beginTransaction()
                .replace(R.id.sightingActivityContentContainer, f)
                .addToBackStack("")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sighting, menu);
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

        if (id == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
