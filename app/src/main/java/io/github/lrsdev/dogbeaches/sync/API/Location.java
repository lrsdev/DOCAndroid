package io.github.lrsdev.dogbeaches.sync.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * POJO representing a location. Used by RetroFit, represents a location object deserialised from
 * a JSON object.
 *
 * @author Samuel Stewart
 */
public class Location implements Serializable
{
    private int id;
    private String category;
    private String name;
    private String animalBlurb;
    private String imageThumbnail;
    private String imageMedium;
    private double latitude;
    private double longitude;
    private String dogStatus;

    public String getAnimalBlurb()
    {
        return animalBlurb;
    }

    public void setAnimalBlurb(String animalBlurb)
    {
        this.animalBlurb = animalBlurb;
    }

    private String dogGuidelines;

    @SerializedName("reports")
    private ArrayList<Sighting> sightings;

    public String getDogStatus()
    {
        return dogStatus;
    }

    public void setDogStatus(String dogStatus)
    {
        this.dogStatus = dogStatus;
    }

    public String getDogGuidelines()
    {
        return dogGuidelines;
    }

    public void setDogGuidelines(String dogGuidelines)
    {
        this.dogGuidelines = dogGuidelines;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getImageThumbnail()
    {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumb)
    {
        this.imageThumbnail = imageThumb;
    }

    public String getImageMedium()
    {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium)
    {
        this.imageMedium = imageMedium;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Sighting> getSightings()
    {
        return sightings;
    }

    public void setSightings(ArrayList<Sighting> sightings)
    {
        this.sightings = sightings;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
