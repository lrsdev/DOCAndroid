package UserAPI.Sync;

import java.util.ArrayList;

/**
 * Created by sam on 16/08/15.
 */
public class Sync
{
    private Locations locations;
    private Animals animals;

    public Locations getLocations()
    {
        return locations;
    }

    public void setLocations(Locations locations)
    {
        this.locations = locations;
    }

    public Animals getAnimals()
    {
        return animals;
    }

    public void setAnimals(Animals animals)
    {
        this.animals = animals;
    }
}
