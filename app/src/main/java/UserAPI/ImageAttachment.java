package UserAPI;

/**
 * Created by samuel on 24/05/15.
 */
public class ImageAttachment
{
    private String imageData;
    private String imageContentType;

    public String getImageFilename()
    {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename)
    {
        this.imageFilename = imageFilename;
    }

    public String getImageData()
    {
        return imageData;
    }

    public void setImageData(String imageData)
    {
        this.imageData = imageData;
    }

    public String getImageContentType()
    {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType)
    {
        this.imageContentType = imageContentType;
    }

    private String imageFilename;
}
