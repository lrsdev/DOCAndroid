package io.github.lrsdev.dogbeaches.sync.API;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface UserApi
{
    @GET("/locations/")
    public void getAllLocations(Callback<ArrayList<Location>> db);

    @GET("/reports/")
    public void getReports(@Query("location_id") Integer location_id, Callback<ArrayList<Sighting>> cb);

    @GET("/reports/")
    public void getAllReports(Callback<ArrayList<Sighting>> cb);

    // Sync objects are fetched off UI thread so do do not need a callback.
    @GET("/sync/")
    public Sync getSync(@Query("from") String timestamp);

    @Multipart
    @POST("/reports")
    public Response createReport(
                             @Part("location_id") Integer locationId,
                             @Part("animal_id") Integer animalId,
                             @Part("blurb") String blurb,
                             @Part("image") TypedFile file,
                             @Part("latitude") Double latitude,
                             @Part("longitude") Double longitude,
                             @Part("created_at") String date
                             );
}
