package io.github.lrsdev.dogbeaches;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationActivity extends AppCompatActivity
{
    public static final String KEY_LOCATION_ID = "dogapp.locationid";
    Integer mLocationId;
    ImageView imageView;
    ImageView iconImageView;
    TextView dogStatusTextView;
    TextView nameTextView;
    TextView animalBlurbTextView;
    TextView dogGuidelinesTextView;
    Button directionsButton;
    private int mNameIndex;
    private int mAnimalBlurbIndex;
    private int mDogGuidelinesIndex;
    private int mLocalMediumImageIndex;
    private int mLatitudeIndex;
    private int mLongitudeIndex;
    private int mDogStatusIndex;

    private String latitude;
    private String longitude;

    Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mLocationId = getIntent().getIntExtra(KEY_LOCATION_ID, 0);

        Uri uri = Uri.withAppendedPath(DogBeachesContract.Locations.CONTENT_URI,
                Integer.toString(mLocationId));
        mCursor = getContentResolver().query(uri, DogBeachesContract.Locations.PROJECTION_ALL,
                null, null, null);
        mCursor.moveToFirst();

        mNameIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_NAME);
        mAnimalBlurbIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_ANIMAL_BLURB);
        mDogGuidelinesIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_DOG_GUIDELINES);
        mLocalMediumImageIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_IMAGE);
        mDogStatusIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_DOG_STATUS);
        mLatitudeIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_LATITUDE);
        mLongitudeIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Locations.COLUMN_LONGITUDE);

        imageView = (ImageView) findViewById(R.id.location_imageview);
        iconImageView = (ImageView) findViewById(R.id.location_icon_imageview);
        dogStatusTextView = (TextView) findViewById(R.id.location_status_textview);
        nameTextView = (TextView) findViewById(R.id.location_name_textview);
        animalBlurbTextView = (TextView) findViewById(R.id.location_animal_blurb_textview);
        dogGuidelinesTextView = (TextView) findViewById(R.id.location_dog_guidelines_textview);
        directionsButton = (Button) findViewById(R.id.location_directions_button);

        File f = new File(mCursor.getString(mLocalMediumImageIndex));
        Picasso.with(this).load(f).into(imageView);

        nameTextView.setText(mCursor.getString(mNameIndex));
        iconImageView.setImageDrawable(getResources().getDrawable(R.drawable.dogonlead));
        dogStatusTextView.setText(getDogStatusString(mCursor.getString(mDogStatusIndex)));
        dogGuidelinesTextView.setText(mCursor.getString(mDogGuidelinesIndex));
        animalBlurbTextView.setText(mCursor.getString(mAnimalBlurbIndex));

        latitude = Double.toString(mCursor.getDouble(mLatitudeIndex));
        longitude = Double.toString(mCursor.getDouble(mLongitudeIndex));

        directionsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        mCursor.close();
    }

    private String getDogStatusString(String s)
    {
        switch(s)
        {
            case "on_lead":
                return "Dogs allowed on lead";
            case "off_lead":
                return "Dogs allowed off lead";
            case "no_dogs":
                return "No dogs allowed";
            default:
                return "";
        }
    }
}
