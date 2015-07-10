package UserAPI;

import java.io.Serializable;

/**
 * Created by samuel on 7/07/15.
 */
public class Image implements Serializable
{
    private String thumbnail;
    private String medium;

    public String getThumbnail()
    {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail)
    {
        this.thumbnail = thumbnail;
    }

    public String getMedium()
    {
        return medium;
    }

    public void setMedium(String medium)
    {
        this.medium = medium;
    }
}
