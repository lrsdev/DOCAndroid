package UserAPI;

/**
 * Created by samuel on 24/05/15.
 */
public class ReportSubmit
{
    private int locationId;
    private int userId;
    private int animalId;
    private String blurb;
    private String geolocation;
    private ImageAttachment image;

    public int getLocationId()
    {
        return locationId;
    }

    public void setLocationId(int locationId)
    {
        this.locationId = locationId;
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

    public String getBlurb()
    {
        return blurb;
    }

    public void setBlurb(String blurb)
    {
        this.blurb = blurb;
    }

    public String getGeolocation()
    {
        return geolocation;
    }

    public void setGeolocation(String geolocation)
    {
        this.geolocation = geolocation;
    }

    public ImageAttachment getImage()
    {
        return image;
    }

    public void setImage(ImageAttachment image)
    {
        this.image = image;
    }
}
