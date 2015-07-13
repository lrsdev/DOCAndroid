package bit.stewasc3.dogbeaches.Location;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import UserAPI.Location;
import UserAPI.RestClient;
import bit.stewasc3.dogbeaches.R;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by samuel on 8/07/15.
 */
public class LocationRecyclerFragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<UserAPI.Location> mLocations;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mLocations = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_location_recycler, container, false);
        mAdapter = new LocationRecyclerAdapter(mLocations, getActivity());
        populateLocations();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.locationRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    private void populateLocations()
    {
        RestClient.get().getAllLocations(new Callback<ArrayList<Location>>()
        {
            @Override
            public void success(ArrayList<Location> locations, Response response)
            {
                mLocations.addAll(locations);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error)
            {
                Toast.makeText(getActivity().getApplicationContext(),
                        error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
