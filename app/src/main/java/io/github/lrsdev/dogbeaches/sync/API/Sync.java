package io.github.lrsdev.dogbeaches.sync.API;

import java.util.ArrayList;

/**
 * POJO representing a synchronisation object. Used by RetroFit, represents a sync object deserialised from
 * a JSON object after retrieval from the remote.
 *
 * @author Samuel Stewart
 */
public class Sync
{
    private ArrayList<Location> locations;
    private ArrayList<Animal> animals;
    private ArrayList<Integer> deletedLocationIds;
    private ArrayList<Integer> deletedAnimalIds;
    private ArrayList<Integer> locationIds;
    private ArrayList<Integer> animalIds;
    private String syncedAt;

    public String getSyncedAt()
    {
        return syncedAt;
    }

    public void setSyncedAt(String syncedAt)
    {
        this.syncedAt = syncedAt;
    }

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
