package UserAPI;

/**
 * Created by samuel on 28/05/15.
 */
public class Report
{
    private int id;
    private int userId;
    private int animalId;
    private String blurb;
    private String imageThumb;
    private String imageMedium;

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
