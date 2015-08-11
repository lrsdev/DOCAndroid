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
    private String category;
    private String name;
    private String blurb;
    private String imageThumbnail;
    private String imageMedium;
    private double latitude;
    private double longitude;
    private String dogStatus;
    private String dogGuidelines;

    @SerializedName("reports")
    private ArrayList<Sighting> sightings;

    public String getDogStatus() {
        return dogStatus;
    }

    public void setDogStatus(String dogStatus) {
        this.dogStatus = dogStatus;
    }

    public String getDogGuidelines() {
        return dogGuidelines;
    }

    public void setDogGuidelines(String dogGuidelines) {
        this.dogGuidelines = dogGuidelines;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumb) {
        this.imageThumbnail = imageThumb;
    }

    public String getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium) {
        this.imageMedium = imageMedium;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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

    public String getBlurb()
    {
        return blurb;
    }

    public void setBlurb(String blurb)
    {
        this.blurb = blurb;
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
