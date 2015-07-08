package UserAPI;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by samuel on 7/07/15.
 */
public class DogStatus implements Serializable
{
    private String status;
    private String guidelines;
    private Date created_at;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getGuidelines()
    {
        return guidelines;
    }

    public void setGuidelines(String guidelines)
    {
        this.guidelines = guidelines;
    }

    public Date getCreated_at()
    {
        return created_at;
    }

    public void setCreated_at(Date created_at)
    {
        this.created_at = created_at;
    }
}
