package UserAPI;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
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

    @Multipart
    @POST("/reports")
    public void createReport(@Part("image") TypedFile file,
                             @Part("location_id") Integer id,
                             @Part("created_at") String date,
                             @Part("blurb") String blurb,
                             @Part("latitude") Double latitude,
                             @Part("longitude") Double longitude,
                             @Part("animal_type") String animalType,
                             Callback<Sighting> cb);
}
