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
    private ApiGeoLocation geolocation;
    private DogStatus dogStatus;
    private Image image;

    @SerializedName("reports")
    private ArrayList<Sighting> sightings;

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

    public ApiGeoLocation getGeolocation()
    {
        return geolocation;
    }

    public void setGeolocation(ApiGeoLocation geolocation)
    {
        this.geolocation = geolocation;
    }

    public DogStatus getDogStatus()
    {
        return dogStatus;
    }

    public void setDogStatus(DogStatus dogStatus)
    {
        this.dogStatus = dogStatus;
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        this.image = image;
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
