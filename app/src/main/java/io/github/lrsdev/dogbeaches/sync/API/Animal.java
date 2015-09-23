package io.github.lrsdev.dogbeaches.sync.API;

/**
 * Created by samuel on 14/07/15.
 */
public class Animal
{
    private Integer id;
    private String name;
    private String blurb;
    private String guidelines;
    private String extUrl;
    private String imageMedium;

    public String getImageThumbnail()
    {
        return imageThumbnail;
    }

    public void setImageThumbnail(String imageThumbnail)
    {
        this.imageThumbnail = imageThumbnail;
    }

    public String getImageMedium()
    {
        return imageMedium;
    }

    public void setImageMedium(String imageMedium)
    {
        this.imageMedium = imageMedium;
    }

    private String imageThumbnail;

    public String getGuidelines()
    {
        return guidelines;
    }

    public void setGuidelines(String guidelines)
    {
        this.guidelines = guidelines;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
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

    public String getExtUrl()
    {
        return extUrl;
    }

    public void setExtUrl(String extUrl)
    {
        this.extUrl = extUrl;
    }
}
