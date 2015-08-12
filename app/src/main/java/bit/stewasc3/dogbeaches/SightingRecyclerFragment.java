package bit.stewasc3.dogbeaches;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import UserAPI.RestClient;
import UserAPI.Sighting;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SightingRecyclerFragment extends Fragment
{
    public final static String KEY_LOCATIONID = "dogbeaches.sightingrecyclerfragment.locationid";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Sighting> mSightings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mSightings = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_sighting_recycler, container, false);
        mAdapter = new SightingRecyclerAdapter();

        Bundle args = getArguments();

        if(args != null && args.containsKey(KEY_LOCATIONID))
        {
            populateSightingsByLocationId(args.getInt(KEY_LOCATIONID));
        }
        else
            populateSightings();

        mRecyclerView = (RecyclerView) v.findViewById(R.id.sightingRecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    public static Fragment newInstance(int locationId)
    {
        Fragment f = new SightingRecyclerFragment();
        Bundle b = new Bundle();
        b.putInt(KEY_LOCATIONID, locationId);
        f.setArguments(b);
        return f;
    }

    private void populateSightings()
    {
        RestClient.get().getAllReports(new Callback<ArrayList<Sighting>>()
        {
            @Override
            public void success(ArrayList<Sighting> sightings, Response response)
            {
                mSightings.addAll(sightings);
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

    private void populateSightingsByLocationId(int locationId)
    {
        RestClient.get().getReports(locationId, new Callback<ArrayList<Sighting>>()
        {
            @Override
            public void success(ArrayList<Sighting> sightings, Response response)
            {
                mSightings.addAll(sightings);
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

    private class SightingRecyclerAdapter extends RecyclerView.Adapter<SightingRecyclerAdapter.ViewHolder>
    {
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView nameTextView;
            public TextView locationTextView;
            public ImageView imageView;
            public TextView dateTextView;
            public TextView blurbTextView;

            public ViewHolder(View recyclerView)
            {
                super(recyclerView);
                nameTextView = (TextView) recyclerView.findViewById(R.id.sightingCardNameTextView);
                locationTextView = (TextView) recyclerView.findViewById(R.id.sightingCardLocationTextView);
                imageView = (ImageView) recyclerView.findViewById(R.id.sightingCardImageView);
                dateTextView = (TextView) recyclerView.findViewById(R.id.sightingCardDateTextView);
                blurbTextView = (TextView) recyclerView.findViewById(R.id.sightingCardBlurbTextView);
            }
        }

        public SightingRecyclerAdapter()
        {
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            Sighting s = mSightings.get(position);
            holder.nameTextView.setText(s.getAnimal().getName());
            String date = new SimpleDateFormat("EEE, d MMM ''yy HH:mm").format(s.getCreatedAt());
            holder.dateTextView.setText(date);
            holder.locationTextView.setText(s.getLocation().getName());
            Picasso.with(getActivity()).load(s.getImageMedium()).into(holder.imageView);
            holder.blurbTextView.setText(s.getBlurb());
        }

        @Override
        public SightingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sighting_card,
                    parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public int getItemCount()
        {
            return mSightings.size();
        }
    }
}
