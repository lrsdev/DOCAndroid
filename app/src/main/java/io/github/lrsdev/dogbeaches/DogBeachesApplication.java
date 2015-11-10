package io.github.lrsdev.dogbeaches;

import android.app.Application;

import com.squareup.otto.Bus;

/**
 * Dogs on Beaches application class.
 *
 * Overriden to initialise singletons.
 *
 * @author Samuel Stewart
 */
public class DogBeachesApplication extends Application
{
    private static Bus bus;
    private static LocationManager lm;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initSingletons();
    }

    // Create instances of eventbus and locationmanager singletons so they persist while application
    // is running.
    private void initSingletons()
    {
        bus = BusProvider.get();
        lm = LocationManager.get(this);
    }
}
