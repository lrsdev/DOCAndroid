package bit.stewasc3.dogbeaches.Location;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import UserAPI.Location;
import bit.stewasc3.dogbeaches.R;

/**
 * Created by samuel on 8/07/15.
 */
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>
{
    private ArrayList<Location> mLocations;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView titleTextView;
        public TextView blurbTextView;
        public ImageView imageView;
        public TextView statusTextView;

        public ViewHolder(View recyclerView)
        {
            super(recyclerView);
            titleTextView = (TextView) recyclerView.findViewById(R.id.locationCardTitleTextView);
            imageView = (ImageView) recyclerView.findViewById(R.id.locationCardImageView);
            //blurbTextView = (TextView) recyclerView.findViewById(R.id.locationCardBlurbTextView);
            statusTextView = (TextView) recyclerView.findViewById(R.id.locationCardStatusTextView);

            recyclerView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(mContext, LocationPagerActivity.class);
                    i.putExtra(LocationPagerActivity.KEY_LOCATION, mLocations.get(getLayoutPosition()));
                    mContext.startActivity(i);
                }
            });
        }
    }

    public LocationRecyclerAdapter(ArrayList<UserAPI.Location> locations, Context context)
    {
        mLocations = locations;
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Location l = mLocations.get(position);
        holder.titleTextView.setText(l.getName());
        //holder.blurbTextView.setText(l.getBlurb());

        String statusString = "";

        switch(l.getDogStatus().getStatus())
        {
            case "on_lead": statusString = "Dogs allowed on lead";
                break;
            case "off_lead": statusString = "Dogs allowed off lead";
                break;
            case "no_dogs": statusString = "No dogs allowed";
                break;
        }

        holder.statusTextView.setText(statusString);
        Picasso.with(mContext).load(l.getImage().getMedium()).into(holder.imageView);

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
        return mLocations.size();
    }
}
