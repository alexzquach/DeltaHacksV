package com.kqsoft.expensetutorken;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class Information extends AppCompatActivity {

    DBHelper glb_DB;
//    Button btnTackPic;
    TextView disease;
    TextView dangerous;
    TextView clinic;
    TextView emergencyRoom;
    TextView header;
    int treatmentID;
    String name;
    String description;
    String matched_name;
    Bitmap image;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addTreatmentButtonListener();
        addDescriptionButtonListener();
        addWebmdButtonListener();

//        image = (Bitmap) getIntent().getParcelableExtra("Picture");
        matched_name = getIntent().getStringExtra("Name");
        type = getIntent().getIntExtra("Type", 0);

        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton
        //retrieve catlist
        SkinCondition_Tbl l_skin = new SkinCondition_Tbl();

        //--------------LINE TO CHANGE------------
//        Random rand = new Random();
//        int i = rand.nextInt(10) + 1;
        int i = 1;
        if (matched_name != null){
            if (matched_name.equals("Mosquito Bite")){
                i = 3;
            }else if(matched_name.equals("Melanoma (Skin Cancer)")){
                i = 1;
            }else if(matched_name.equals("Eczema")){
                i = 2;
            }else if(matched_name.equals("Moles (Non Cancerous)")){
                i = 4;
            }else if(matched_name.equals("Bed Bug Bite")){
                i = 12;
            }else if (matched_name.equals("Pimples")){
                i = 11;
            }else{
                i = type;
            }
        }else{
            i = type;
        }


        l_skin = glb_DB.SkinGetRecord(i);
        //--------------LINE TO CHANGE------------

        header = (TextView) findViewById(R.id.textView2);
        disease = (TextView) findViewById(R.id.conditionTextView);
        dangerous = (TextView) findViewById(R.id.dangerousTextView);
        clinic = (TextView) findViewById(R.id.doctorTextView);
        emergencyRoom = (TextView) findViewById(R.id.emergencyTextView);

        if (matched_name == null){
            header.setText("Picture provided is invalid.");
        }
        else if (type != 0){
            header.setText("Disease:");
        }else{
            header.setText("This is most likely...");
        }


        treatmentID = l_skin.SkinConditionID;
        name = l_skin.SkinConditionName;
        description = l_skin.SDescription;

        disease.setText(l_skin.SkinConditionName);
        dangerous.setText((l_skin.Dangerous == 1)? "We think this is dangerous.": "This shouldn't be dangerous.");
        dangerous.setTextColor((l_skin.Dangerous == 1)? Color.RED: Color.GRAY);
        clinic.setText((l_skin.VisitLocalClinic == 1)? "YES" : "NO");
        clinic.setTextColor((l_skin.VisitLocalClinic == 1)? Color.RED: Color.GREEN);
        emergencyRoom.setText((l_skin.VisitEmergencyRoom == 1)? "YES" : "NO");
        emergencyRoom.setTextColor((l_skin.VisitEmergencyRoom == 1)? Color.RED: Color.GREEN);
//        btnTackPic = (Button) findViewById(R.id.treatmentButton);

//        btnTackPic.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
////                Intent cameraIntent = new Intent(this, Treatment.class);
////                startActivityForResult(cameraIntent);
//
//                Intent tmp = new Intent(Treatment.class);
//                //Temporary save
//                startActivityForResult(tmp);
//            }
//
//
//
//
//        });
        addImageComparisonListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void addTreatmentButtonListener() {
        Button treatButton = findViewById(R.id.treatmentButton);
        treatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToTreatment();
            }
        });
    }

    protected void addDescriptionButtonListener() {
        Button descButton = findViewById(R.id.descriptionButton);
        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToDescription();
            }
        });
    }

    protected void addWebmdButtonListener() {
        Button webButton = findViewById(R.id.webmdButton);
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToWebmd();
            }
        });
    }

    protected void addImageComparisonListener() {

        disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToImageComparison();
            }
        });
    }

    /**
     * Switch to the PowersPlusGameActivity view to play the game.
     */
    protected void switchToTreatment() {
        Intent tmp = new Intent(this, Treatment.class);
        tmp.putExtra("ID", treatmentID);
        startActivity(tmp);
    }

    protected void switchToDescription() {
        Intent tmp = new Intent(this, Description.class);
        tmp.putExtra("Des", description);
        tmp.putExtra("ID", treatmentID);
        startActivity(tmp);
    }

    protected void switchToWebmd() {
        String link = "https://www.webmd.com/search/search_results/default.aspx?query=";
        link += name + "%20";
        Uri uri = Uri.parse(link); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    protected void switchToImageComparison() {
        Intent tmp = new Intent(this, ImageComparison.class);
        tmp.putExtra("Disease", disease.getText().toString());
        tmp.putExtra("Picture", image);
        startActivity(tmp);
    }
}
