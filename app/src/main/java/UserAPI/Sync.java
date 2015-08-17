package UserAPI;

import java.util.ArrayList;

/**
 * Created by sam on 16/08/15.
 */
public class Sync
{
    private ArrayList<Location> locations;
    private ArrayList<Animal> animals;
    private ArrayList<Integer> deletedLocationIds;
    private ArrayList<Integer> deletedAnimalIds;
    private ArrayList<Integer> locationIds;
    private ArrayList<Integer> animalIds;

    public ArrayList<Integer> getDeletedLocationIds()
    {
        return deletedLocationIds;
    }

    public void setDeletedLocationIds(ArrayList<Integer> deletedLocationIds)
    {
        this.deletedLocationIds = deletedLocationIds;
    }

    public ArrayList<Integer> getDeletedAnimalIds()
    {
        return deletedAnimalIds;
    }

    public void setDeletedAnimalIds(ArrayList<Integer> deletedAnimalIds)
    {
        this.deletedAnimalIds = deletedAnimalIds;
    }

    public ArrayList<Integer> getAnimalIds()
    {
        return animalIds;
    }

    public void setAnimalIds(ArrayList<Integer> animalIds)
    {
        this.animalIds = animalIds;
    }

    public ArrayList<Integer> getLocationIds()
    {
        return locationIds;
    }

    public void setLocationIds(ArrayList<Integer> locationIds)
    {
        this.locationIds = locationIds;
    }


    public ArrayList<Location> getLocations()
    {
        return locations;
    }

    public void setLocations(ArrayList<Location> locations)
    {
        this.locations = locations;
    }

    public ArrayList<Animal> getAnimals()
    {
        return animals;
    }

    public void setAnimals(ArrayList<Animal> animals)
    {
        this.animals = animals;
    }
}
