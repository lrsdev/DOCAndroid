package UserAPI;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by samuel on 28/05/15.
 */
public class Sighting implements Serializable
{
    private int id;
    private int userId;
    private int animalId;
    private String animalType;
    private String blurb;
    private Date createdAt;
    private Location location;
    private Animal animal;
    private String imageThumb;
    private String imageMedium;
    private double latitude;
    private double longitide;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitide() {
        return longitide;
    }

    public void setLongitide(double longitide) {
        this.longitide = longitide;
    }

    public String getAnimalType()
    {
        return animalType;
    }

    public void setAnimalType(String animalType)
    {
        this.animalType = animalType;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Animal getAnimal()
    {
        return animal;
    }

    public void setAnimal(Animal animal)
    {
        this.animal = animal;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getBlurb()
    {
        return blurb;
    }

    public void setBlurb(String blurb)
    {
        this.blurb = blurb;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getAnimalId()
    {
        return animalId;
    }

    public void setAnimalId(int animalId)
    {
        this.animalId = animalId;
    }

    public String getImageThumb()
    {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb)
    {
        this.imageThumb = imageThumb;
    }

    public String getImageMedium()
    {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium)
    {
        this.imageMedium = imageMedium;
    }
}
