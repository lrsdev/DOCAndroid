package UserAPI;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface UserApi
{
    @GET("/locations/")
    public void getAllLocations(Callback<ArrayList<Location>> db);

    @GET("/reports/")
    public void getReports(@Query("location_id") Integer location_id, Callback<ArrayList<Sighting>> cb);

    @POST("/reports")
    public void createReport(@Body Report report, Callback<Report> cb);
}
