package io.github.lrsdev.dogbeaches.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Authenticator requires a service in order for the sync adapter framework to access it.
 * Allows sync adapter framework to call the authenticator's methods.
 */
public class AuthenticatorService extends Service
{
    private Authenticator mAuthenticator;

    @Override
    public void onCreate()
    {
        mAuthenticator = new Authenticator(this);
    }

    public IBinder onBind(Intent intent)
    {
        return mAuthenticator.getIBinder();
    }
}
