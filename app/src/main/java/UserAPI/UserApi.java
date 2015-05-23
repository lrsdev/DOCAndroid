package UserAPI;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;

public interface UserApi
{
    @GET("/locations/")
    public void getAllLocations(Callback<ArrayList<Location>> db);
}
