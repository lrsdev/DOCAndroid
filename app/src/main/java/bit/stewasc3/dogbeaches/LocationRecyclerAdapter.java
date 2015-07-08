package bit.stewasc3.dogbeaches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import UserAPI.Location;

/**
 * Created by samuel on 8/07/15.
 */
public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>
{
    private ArrayList<Location> mLocations;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder
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
            blurbTextView = (TextView) recyclerView.findViewById(R.id.locationCardBlurbTextView);
            statusTextView = (TextView) recyclerView.findViewById(R.id.locationCardStatusTextView);
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
        holder.blurbTextView.setText(l.getBlurb());
        holder.statusTextView.setText(l.getDogStatus().getStatus());
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
