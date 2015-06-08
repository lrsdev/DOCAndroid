package UserAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by samuel on 8/06/15.
 * Class to receive lat/long co-ordinates from API.
 */
public class ApiGeoLocation implements Serializable
{
    private double latitude;
    private double longitude;

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

}
