package io.github.lrsdev.dogbeaches.sync.API;

import java.util.ArrayList;
import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Defines an interface for the RetroFit REST client.
 *
 * See http://square.github.io/retrofit/
 */
public interface UserApi
{
    /**
     * Initialises an asynchronous request for all locations from the remote.
     * @param cb Callback for asynchronous operation
     */
    @GET("/locations/")
    public void getAllLocations(Callback<ArrayList<Location>> cb);

    /**
     * Initialises an asynchronous request for all animals from the remote.
     * @param location_id id of location to retrieve reports for
     * @param cb Callback for asynchronous operation
     */
    @GET("/reports/")
    public void getReports(@Query("location_id") Integer location_id, Callback<ArrayList<Sighting>> cb);

    /**
     * Initialises an asynchronous request for all reports from the remote.
     * @param cb Callback for asynchronous operation
     */
    @GET("/reports/")
    public void getAllReports(Callback<ArrayList<Sighting>> cb);

    // Sync objects are fetched off UI thread so do do not need a callback.
    /**
     * Synchronously requests a synchronisation object from the remote.
     * Sync objects are always requested off the UI thread by sync adapter.
     * @param timestamp Timestamp of the LAST completed synchronisation
     * @return Returns a Sync object.
     */
    @GET("/sync/")
    public Sync getSync(@Query("from") String timestamp);

    // Report with location id and animal id defined
    // Ids are defined as a map so they can be arbitrarily defined by caller. (animal_id, location_id
    // may be missing)
    /**
     * A multipart POST request for synchronising reports to the remote.
     * @param ids A map containing optional location_id and animal_id parameters.
     * @param blurb User written blurb
     * @param file A TypedFile representing the report's image
     * @param latitude Report's latitude
     * @param longitude Report's longitude
     * @param date Report timestamp
     * @return
     */
    @Multipart
    @POST("/reports")
    public Response createReport(
            @PartMap Map<String, Integer> ids,
            @Part("blurb") String blurb,
            @Part("image") TypedFile file,
            @Part("latitude") Double latitude,
            @Part("longitude") Double longitude,
            @Part("created_at") String date
    );
}
