package io.github.lrsdev.dogbeaches;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import io.github.lrsdev.dogbeaches.AnimalActivity;
import io.github.lrsdev.dogbeaches.R;
import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;

/**
 * Created by samuel on 8/07/15.
 */
public class AnimalRecyclerFragment extends Fragment
{
    private final static String TAG = "AnimalRecyclerFrag";
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
        View v = inflater.inflate(R.layout.fragment_animal_recycler, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.animal_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        queryAnimals();
        mAdapter = new AnimalRecyclerAdapter(mCursor, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    /**
     * Query content provider for a animal cursor.
     */
    public void queryAnimals()
    {
        mCursor = getActivity().getContentResolver().query(DogBeachesContract.Animals.CONTENT_URI,
                DogBeachesContract.Animals.PROJECTION_ALL, null, null, null);
    }

    @Override
    public void onDestroyView()
    {
        mCursor.close();
        super.onDestroyView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().setTitle("Animal List");
    }

    private class AnimalRecyclerAdapter extends RecyclerView.Adapter<AnimalRecyclerAdapter.ViewHolder>

    {
        private Cursor mAnimalCursor;
        private Context mContext;
        private int mNameIndex;
        private int mIdIndex;
        private int mLocalMediumImageIndex;
        private int mBlurbIndex;

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public TextView titleTextView;
            public TextView guidelinesTextView;
            public ImageView imageView;
            public Button moreInfoButton;
            public Integer animalId;

            public ViewHolder(View recyclerView)
            {
                super(recyclerView);
                titleTextView = (TextView) recyclerView.findViewById(R.id.animal_card_title_textview);
                imageView = (ImageView) recyclerView.findViewById(R.id.animal_card_imageview);
                guidelinesTextView = (TextView) recyclerView.findViewById(R.id.animal_card_info);
                moreInfoButton = (Button) recyclerView.findViewById(R.id.animal_card_button);

                moreInfoButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent i = new Intent(mContext, AnimalActivity.class);
                        i.putExtra(AnimalActivity.KEY_ANIMAL_ID, animalId);
                        startActivity(i);
                    }
                });
            }
        }

        public AnimalRecyclerAdapter(Cursor animalCursor, Context context)
        {
            mAnimalCursor = animalCursor;
            mContext = context;
            mIdIndex = animalCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_ID);
            mNameIndex = animalCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_NAME);
            mLocalMediumImageIndex = animalCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_IMAGE);
            mBlurbIndex = animalCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_BLURB);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position)
        {
            mAnimalCursor.moveToPosition(position);
            holder.animalId = mAnimalCursor.getInt(mIdIndex);
            holder.titleTextView.setText(mAnimalCursor.getString(mNameIndex));
            holder.guidelinesTextView.setText(mAnimalCursor.getString(mBlurbIndex));
            File f = new File(mAnimalCursor.getString(mLocalMediumImageIndex));
            Picasso.with(mContext).load(f).into(holder.imageView);
        }

        @Override
        public AnimalRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_card,
                    parent, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public int getItemCount()
        {
            return mAnimalCursor.getCount();
        }
    }
}
