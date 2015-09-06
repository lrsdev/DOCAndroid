package bit.stewasc3.dogbeaches;


import android.app.Activity;
import android.os.Bundle;

/**
 * Created by rickiekewene on 2/09/15.
 */
public class DonateActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
    /**    if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, DonateFragment.newInstance())
                    .commit();
        } */
    }
}
