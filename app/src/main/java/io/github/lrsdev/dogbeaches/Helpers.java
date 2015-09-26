package io.github.lrsdev.dogbeaches;

import android.graphics.drawable.Drawable;

/**
 * Created by sam on 9/27/15.
 */
public class Helpers
{
    public static int getIconResId(String dogStatus)
    {
        switch (dogStatus)
        {
            case "no_dogs":
                return R.drawable.nodogs;
            case "on_lead":
                return R.drawable.dogonlead;
            case "off_lead":
                return R.drawable.dogofflead;
            default:
                return 0;
        }
    }
}
