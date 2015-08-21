package bit.stewasc3.dogbeaches;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
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

import com.squareup.picasso.Picasso;

import java.io.File;

import bit.stewasc3.dogbeaches.contentprovider.DogBeachesContract;
import bit.stewasc3.dogbeaches.contentprovider.DogBeachesProvider;
import bit.stewasc3.dogbeaches.db.DBHelper;

/**
 * Created by samuel on 8/07/15.
 */

// TODO: Find a way to close the cursor.
public class LocationRecyclerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private final static String TAG = "LocationRecyclerFrag";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnSightingsSelectedListener mSightingsCallback;
    private static int ID_INDEX = 0;
    private static int NAME_INDEX = 1;
    private static int GUIDELINE_INDEX = 2;
    private static int STATUS_INDEX = 3;
    private static int IMAGE_INDEX = 4;

    private static int LOCATION_RECYCLER_LOADER = 1;

    public interface OnSightingsSelectedListener
    {
        public void onSightingsSelected(int locationId);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        try
        {
            mSightingsCallback = (OnSightingsSelectedListener) getActivity();
        }
        catch (ClassCastException e)
        {
           throw new ClassCastException(getActivity().toString() + " must implement OnSightingsSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(LOCATION_RECYCLER_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_location_recycler, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.locationRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle)
    {
        CursorLoader cursorLoader = new CursorLoader(getActivity(), DogBeachesContract.Locations.CONTENT_URI,
                DogBeachesContract.Locations.PROJECTION_ALL, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
    {
        mAdapter = new LocationRecyclerAdapter(cursor, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        // Do nothing
    }

    private class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>
    {
        private Cursor mLocationCursor;
        private Context mContext;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView titleTextView;
            public TextView blurbTextView;
            public TextView guidelinesTextView;
            public ImageView imageView;
            public TextView statusTextView;
            public Button mapButton;
            public Button sightingsButton;

            public ViewHolder(View recyclerView)
            {
                super(recyclerView);
                titleTextView = (TextView) recyclerView.findViewById(R.id.locationCardTitleTextView);
                imageView = (ImageView) recyclerView.findViewById(R.id.locationCardImageView);
                statusTextView = (TextView) recyclerView.findViewById(R.id.locationCardStatusTextView);
                guidelinesTextView = (TextView) recyclerView.findViewById(R.id.locationCardDogGuidelines);
                mapButton = (Button) recyclerView.findViewById(R.id.locationCardMapButton);
                sightingsButton = (Button) recyclerView.findViewById(R.id.locationCardSightingsButton);

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
            }
        }

        public LocationRecyclerAdapter(Cursor locationCursor, Context context)
        {
            mLocationCursor = locationCursor;
            mContext = context;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            mLocationCursor.moveToPosition(position);
            holder.titleTextView.setText(mLocationCursor.getString(NAME_INDEX));
            holder.guidelinesTextView.setText(mLocationCursor.getString(GUIDELINE_INDEX));

            String statusString = "";

            switch(mLocationCursor.getString(STATUS_INDEX))
            {
                case "on_lead": statusString = "Dogs allowed on lead";
                    break;
                case "off_lead": statusString = "Dogs allowed off lead";
                    break;
                case "no_dogs": statusString = "No dogs allowed";
                    break;
            }

            holder.statusTextView.setText(statusString);
            int columnIndex = mLocationCursor.getColumnIndex(DogBeachesContract.Locations
                    .COLUMN_IMAGE_MEDIUM_LOCAL);
            File f = new File(mContext.getFilesDir(), mLocationCursor.getString(columnIndex));
            Picasso.with(mContext).load(f).into(holder.imageView);

            // Show sightings button only if location has sightings
            //if (!(l.getSightings().isEmpty()))
            //{
            holder.sightingsButton.setVisibility(View.VISIBLE);
            //}

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
    }
}
