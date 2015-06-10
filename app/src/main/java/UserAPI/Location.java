package UserAPI;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by samuel on 23/05/15.
 */
public class Location implements Serializable
{
    private int id;
    private String locationType;
    private String name;
    private String blurb;
    private String dogGuidelines;
    private String dogStatus;
    private String imageThumb;
    private String imageMedium;
    private ArrayList<ApiGeoLocation> accessPoints;

    public ArrayList<Sighting> getSightings()
    {
        return sightings;
    }

    public void setSightings(ArrayList<Sighting> sightings)
    {
        this.sightings = sightings;
    }

    private ArrayList<Sighting> sightings;

    public ArrayList<ApiGeoLocation> getAccessPoints()
    {
        return accessPoints;
    }

    public void setAccessPoints(ArrayList<ApiGeoLocation> accessPoints)
    {
        this.accessPoints = accessPoints;
    }

    public String getImageMedium()
    {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium)
    {
        this.imageMedium = imageMedium;
    }

    public String getImageThumb()
    {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb)
    {
        this.imageThumb = imageThumb;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }


    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBlurb()
    {
        return blurb;
    }

    public void setBlurb(String blurb)
    {
        this.blurb = blurb;
    }

    public String getDogGuidelines()
    {
        return dogGuidelines;
    }

    public void setDogGuidelines(String dogGuidelines)
    {
        this.dogGuidelines = dogGuidelines;
    }

    public String getDogStatus()
    {
        return dogStatus;
    }

    public void setDogStatus(String dogStatus)
    {
        this.dogStatus = dogStatus;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
