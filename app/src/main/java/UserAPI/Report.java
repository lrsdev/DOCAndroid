package UserAPI;

/**
 * Created by samuel on 24/05/15.
 */
public class Report
{
    private int id;
    private int locationId;
    private int userId;
    private int animalId;
    private double latitude;
    private double longitude;
    private String blurb;
    private ImageAttachment image;
    private String submittedAt;

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getSubmittedAt()
    {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt)
    {
        this.submittedAt = submittedAt;
    }

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

    public ImageAttachment getImage()
    {
        return image;
    }

    public void setImage(ImageAttachment image)
    {
        this.image = image;
    }
}
