package UserAPI.Sync;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import UserAPI.Animal;

/**
 * Created by sam on 16/08/15.
 */
public class Animals
{
    @SerializedName("new")
    private ArrayList<Animal> _new;
    private ArrayList<Animal> updated;
    private ArrayList<Animal> updatedImage;
    private ArrayList<Integer> deleted;

    public ArrayList<Animal> getNew()
    {
        return _new;
    }

    public void setNew(ArrayList<Animal> _new)
    {
        this._new = _new;
    }

    public ArrayList<Animal> getUpdated()
    {
        return updated;
    }

    public void setUpdated(ArrayList<Animal> updated)
    {
        this.updated = updated;
    }

    public ArrayList<Animal> getUpdatedImage()
    {
        return updatedImage;
    }

    public void getUpdatedImage(ArrayList<Animal> updatedImage)
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
