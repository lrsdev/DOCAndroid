package UserAPI;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface UserApi
{
    @GET("/locations/")
    public void getAllLocations(Callback<ArrayList<Location>> db);

    @POST("/reports")
    public void createReport(@Body ReportSubmit reportSubmit, Callback<ReportSubmit> cb);
}
