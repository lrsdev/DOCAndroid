package UserAPI.Sync;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import UserAPI.Location;

/**
 * Created by sam on 16/08/15.
 */
public class Locations
{
    @SerializedName("new")
    private ArrayList<Location> _new;
    private ArrayList<Location> updated;
    private ArrayList<Location> updatedImage;
    private ArrayList<Integer> deleted;

    public ArrayList<Location> getNew()
    {
        return _new;
    }

    public void setNew(ArrayList<Location> _new)
    {
        this._new = _new;
    }

    public ArrayList<Location> getUpdated()
    {
        return updated;
    }

    public void setUpdated(ArrayList<Location> updated)
    {
        this.updated = updated;
    }

    public ArrayList<Location> getUpdatedImage()
    {
        return updatedImage;
    }

    public void setUpdatedImage(ArrayList<Location> updatedImage)
    {
        this.updatedImage = updatedImage;
    }

    public ArrayList<Integer> getDeleted()
    {
        return deleted;
    }

    public void setDeleted(ArrayList<Integer> deleted)
    {
        this.deleted = deleted;
    }
}
