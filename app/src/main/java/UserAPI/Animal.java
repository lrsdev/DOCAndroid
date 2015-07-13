package UserAPI;

/**
 * Created by samuel on 14/07/15.
 */
public class Animal
{
    private String name;
    private String blurb;
    private String guideline;
    private String extUrl;

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

    public String getGuideline()
    {
        return guideline;
    }

    public void setGuideline(String guideline)
    {
        this.guideline = guideline;
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
