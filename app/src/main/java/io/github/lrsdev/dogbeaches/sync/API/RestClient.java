package io.github.lrsdev.dogbeaches.sync.API;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.github.lrsdev.dogbeaches.BuildConfig;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * A singleton configuring a RetroFit REST adapter for consuming remote API.
 *
 * See http://square.github.io/retrofit/
 */
public class RestClient
{
    private static UserApi REST_CLIENT;
    private static String ROOT = BuildConfig.API_END_POINT;

    private RestClient()
    {
        // Custom deserializer to convert UTC time to device local time zone
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        /*
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>()
                {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
                    {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        format.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try
                        {
                            return format.parse(json.getAsString());
                        } catch (ParseException e)
                        {
                            return null;
                        }
                    }
                })
                .create();
                */

        // Increase the timeout time. Occasionally remote API was responding too slow after a report
        // upload and local data was not getting removed despite a successful upload, therefore
        // uploading more than once
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        okHttpClient.setWriteTimeout(30 * 1000, TimeUnit.MILLISECONDS);

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.FULL);

        RestAdapter restAdapter = builder.build();
        REST_CLIENT = restAdapter.create(UserApi.class);
    }

    public static UserApi get()
    {
        if (REST_CLIENT == null)
        {
            if (REST_CLIENT == null)
            {
                new RestClient();
            }
        }
        return REST_CLIENT;
    }
}
