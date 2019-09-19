package com.kqsoft.expensetutorken;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static com.kqsoft.expensetutorken.MainActivity.diseasePicture;

public class ImageComparison extends AppCompatActivity {

    TextView conditionTextView;
    String Condition;

    ImageView yourPhotoIV;
    ImageView conditionPhotoIV; //conditionPhotoImageView
    String conditionPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_comparison);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Condition = getIntent().getStringExtra("Disease");
        conditionTextView = findViewById(R.id.yourConditionTextView);
        conditionTextView.setText((Condition.equals("") ? "Unknown Condition" : Condition));

        Bitmap photo = diseasePicture;//(Bitmap) getIntent().getParcelableExtra("Picture");

        yourPhotoIV = findViewById(R.id.yourPhotoImageView);
        yourPhotoIV.setImageBitmap(photo);
        yourPhotoIV.setVisibility(View.VISIBLE);

        conditionPhotoIV = findViewById(R.id.conditionPhotoImageView);

        switch (Condition.toLowerCase()) {
            case "sun burn": conditionPhotoIV.setBackgroundResource(R.drawable.sample_sunburn);
                break;
            case "poison ivy": conditionPhotoIV.setBackgroundResource(R.drawable.sample_poisonivy);
                break;
            case "melanoma (skin cancer)": conditionPhotoIV.setBackgroundResource(R.drawable.sample_melanoma);
                break;
            case "eczema": conditionPhotoIV.setBackgroundResource(R.drawable.sample_eczema);
                break;
            case "mosquito bite": conditionPhotoIV.setBackgroundResource(R.drawable.sample_mosquitobite);
                break;
            case "moles (non cancerous)": conditionPhotoIV.setBackgroundResource(R.drawable.sample_mole);
                break;
            case "first degree burn": conditionPhotoIV.setBackgroundResource(R.drawable.sample_firstburn);
                break;
            case "second degree burn": conditionPhotoIV.setBackgroundResource(R.drawable.sample_secondburn);
                break;
            case "third degree burn": conditionPhotoIV.setBackgroundResource(R.drawable.sample_notavailable);
                break;
            case "psoriasis": conditionPhotoIV.setBackgroundResource(R.drawable.sample_psoriasis);
                break;
            case "pimple": conditionPhotoIV.setBackgroundResource(R.drawable.sample_pimple);
                break;
            default: conditionPhotoIV.setBackgroundResource(R.drawable.sample_notavailable);
                break;
        }

    }
    //when user tap the up/home (back arrow) button on the top left of the screen. the previous screen will
    //run the activity again which is like refresh. To make not refresh we make the button look like the
    //triangle left at navigation at the bottom, since tap this button is not refresh previous screen
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //or can use like below
                //So when user press the Up button it will get the behavior of the back button in the navigation bar.
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
