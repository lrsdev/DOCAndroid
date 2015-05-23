package UserAPI;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient
{
    private static UserApi REST_CLIENT;
    private static String ROOT = "http://meat.stewpot.nz:3000/";

    private RestClient()
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(UserApi.class);
    }

    public static UserApi get()
    {
        if(REST_CLIENT == null)
        {
            if(REST_CLIENT == null)
                new RestClient();
        }
        return REST_CLIENT;
    }
}
