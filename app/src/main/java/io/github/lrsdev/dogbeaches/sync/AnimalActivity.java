package io.github.lrsdev.dogbeaches;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.File;
import io.github.lrsdev.dogbeaches.contentprovider.DogBeachesContract;

public class AnimalActivity extends AppCompatActivity
{
    public static final String KEY_ANIMAL_ID = "dogbeaches.animalid";
    Integer mAnimalId;
    String mUrl;
    ImageView imageView;
    TextView nameTextView;
    TextView blurbTextView;
    TextView guidelinesTextView;
    Button websiteButton;
    private int mNameIndex;
    private int mBlurbIndex;
    private int mGuidelinesIndex;
    private int mLocalMediumImageIndex;
    private int mUrlIndex;
    Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        mAnimalId = getIntent().getIntExtra(KEY_ANIMAL_ID, 0);

        Uri uri = Uri.withAppendedPath(DogBeachesContract.Animals.CONTENT_URI, Integer.toString(mAnimalId));
        mCursor = getContentResolver().query(uri, DogBeachesContract.Animals.PROJECTION_ALL, null, null, null);
        mCursor.moveToFirst();

        mNameIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_NAME);
        mBlurbIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_BLURB);
        mGuidelinesIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_GUIDELINES);
        mLocalMediumImageIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_IMAGE);
        mUrlIndex = mCursor.getColumnIndexOrThrow(DogBeachesContract.Animals.COLUMN_EXT_URL);
        mUrl =  mCursor.getString(mUrlIndex);

        imageView = (ImageView) findViewById(R.id.animal_imageview);
        nameTextView = (TextView) findViewById(R.id.animal_name_textview);
        blurbTextView = (TextView) findViewById(R.id.animal_blurb_textview);
        guidelinesTextView = (TextView) findViewById(R.id.animal_guidelines_textview);
        websiteButton = (Button) findViewById(R.id.animal_website_button);

        File f = new File(mCursor.getString(mLocalMediumImageIndex));
        Picasso.with(this).load(f).into(imageView);

        nameTextView.setText(mCursor.getString(mNameIndex));
        guidelinesTextView.setText(mCursor.getString(mGuidelinesIndex));
        blurbTextView.setText(mCursor.getString(mBlurbIndex));

        websiteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri webpage = Uri.parse(mUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(intent);
            }
        });
        mCursor.close();
    }
}
