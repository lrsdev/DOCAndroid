package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import java.io.File;

import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesProvider;
import bit.stewasc3.dogbeaches.db.DBHelper;

/**
 * Created by samuel on 8/07/15.
 */
public class LocationRecyclerFragment extends Fragment
{
    private final static String TAG = "LocationRecyclerFrag";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_location_recycler, container, false);

        mCursor = getActivity().getContentResolver().query(DogBeachesContract.Locations.CONTENT_URI,
                DogBeachesContract.Locations.PROJECTION_ALL, null, null, null);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.locationRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LocationRecyclerAdapter(mCursor, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void onDestroyView()
    {
        mCursor.close();
        super.onDestroyView();
    }

    private class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    {
        private Cursor mLocationCursor;
        private Context mContext;
        private Location mLastLocation;
        private GoogleApiClient mGoogleApiClient;
        private int mNameIndex;
        private int mIdIndex;
        private int mDogGuidelinesIndex;
        private int mLocalMediumImageIndex;
        private int mLatitudeIndex;
        private int mLongitudeIndex;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView titleTextView;
            public TextView guidelinesTextView;
            public TextView distanceTextView;
            public ImageView imageView;
            public Button moreInfoButton;
            public Button sightingsButton;
            public Location coordinates;
            public Integer locationId;

            public ViewHolder(View recyclerView)
            {
                super(recyclerView);
                titleTextView = (TextView) recyclerView.findViewById(R.id.locationCardTitleTextView);
                imageView = (ImageView) recyclerView.findViewById(R.id.locationCardImageView);
                guidelinesTextView = (TextView) recyclerView.findViewById(R.id.locationCardDogGuidelines);
                moreInfoButton = (Button) recyclerView.findViewById(R.id.locationCardMoreInfoButton);
                sightingsButton = (Button) recyclerView.findViewById(R.id.locationCardSightingsButton);
                distanceTextView = (TextView) recyclerView.findViewById(R.id.locationCardDistanceTextView);

                sightingsButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v)
                    {
                        //Intent i = new Intent(mContext, SightingActivity.class);
                        //i.putExtra(SightingActivity.KEY_LOCATIONID, mLocations.get(getLayoutPosition()).getId());
                        //mContext.startActivity(i);
                        //mSightingsCallback.onSightingsSelected(mLocations.get(getLayoutPosition()).getId());
                    }
                });

                moreInfoButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent i = new Intent(mContext, LocationActivity.class);
                        i.putExtra(LocationActivity.KEY_LOCATION_ID, locationId);
                        startActivity(i);
                    }
                });


            }
        }

        public LocationRecyclerAdapter(Cursor locationCursor, Context context)
        {
            mLocationCursor = locationCursor;
            mContext = context;
            mIdIndex = locationCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_ID);
            mNameIndex = locationCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_NAME);
            mDogGuidelinesIndex = locationCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_DOG_GUIDELINES);
            mLocalMediumImageIndex = locationCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_IMAGE);
            mLatitudeIndex = locationCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_LATITUDE);
            mLongitudeIndex = locationCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_LONGITUDE);
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            mLocationCursor.moveToPosition(position);
            holder.locationId = mLocationCursor.getInt(mIdIndex);
            holder.titleTextView.setText(mLocationCursor.getString(mNameIndex));
            holder.guidelinesTextView.setText(mLocationCursor.getString(mDogGuidelinesIndex));
            File f = new File(mLocationCursor.getString(mLocalMediumImageIndex));
            Picasso.with(mContext).load(f).into(holder.imageView);

            holder.coordinates = new Location("");
            holder.coordinates.setLatitude(mLocationCursor.getDouble(mLatitudeIndex));
            holder.coordinates.setLongitude(mLocationCursor.getDouble(mLongitudeIndex));

            if (mLastLocation != null)
            {
                Float distance = mLastLocation.distanceTo(holder.coordinates);
                String kms = String.format("%.2f", (distance/1000)) + " Kms away";
                holder.distanceTextView.setText(kms);
            }
            holder.sightingsButton.setVisibility(View.VISIBLE);
        }

        @Override
        public LocationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card,
                    parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public int getItemCount()
        {
            return mLocationCursor.getCount();
        }

        protected synchronized void buildGoogleApiClient()
        {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        @Override
        public void onConnected(Bundle bundle)
        {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mLastLocation != null)
            {
                mAdapter.notifyDataSetChanged();
            }
        }


        @Override
        public void onConnectionSuspended(int i)
        {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult)
        {

        }
    }
}
