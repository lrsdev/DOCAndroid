package io.github.lrsdev.dogbeaches;


import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by sam on 9/27/15.
 */
public class Helpers
{
    public static Drawable getDogIconDrawable(String dogStatus, Context c)
    {
        switch (dogStatus)
        {
            case "no_dogs":
                return c.getResources().getDrawable(R.drawable.nodogs);
            case "on_lead":
                return c.getResources().getDrawable(R.drawable.dogonlead);
            case "off_lead":
                return c.getResources().getDrawable(R.drawable.dogofflead);
            default:
                return null;
        }
    }
}
