package com.kqsoft.expensetutorken;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Treatment extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_treatment);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

    DBHelper glb_DB;
    ListView lv;
    //declare arrray adapter for data
    TreatmentAdapter adapterLV = null;

    int SkinID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SkinID = getIntent().getIntExtra("ID", 0);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-------------Start Code-----------------------------------------
        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton
        //retrieve catlist
        ArrayList<Treatment_Tbl> treatmentList = new ArrayList<Treatment_Tbl>();


        treatmentList = glb_DB.TreamentConditionList(SkinID);
        //get adapter
        //this is android builtin listview
        //adapterLV = new CatAdapter(this, android.R.layout.simple_list_item_2, l_CatList);
        //this is custom
//        adapterLV = new CatAdapter(this, R.layout.simplelistitem2_image, treatmentList);
        adapterLV = new TreatmentAdapter(this, R.layout.simplelistitem2_image, treatmentList);

        // Assign adapter to ListView
        lv = (ListView) findViewById(R.id.treatmentLV);
        lv.setAdapter(adapterLV);

        final TextView treatmentText = findViewById(R.id.treatmentTextView);
        treatmentText.setMovementMethod(new ScrollingMovementMethod());
        //WHEN ITEM TAB
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get current row tab
                Treatment_Tbl l_row  = (Treatment_Tbl) parent.getItemAtPosition(position);
//                int l_rid = l_row.catid;
//                startActivityItem(l_rid);
                treatmentText.setTextSize(18);
                treatmentText.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
                String des = "Description: " + l_row.TreatmentDescription;
                        treatmentText.setText(des);

            }
        });

    }
    //======================CUSTOM CODE HERE=======================================
    private void startActivityForAdd(){
        Intent intent = new Intent(this, CatAddActivity.class);
        intent.putExtra("rid", 0); // pass id to add page by integer
        startActivity(intent);
    }
    //pass catid
    private void startActivityItem(int l_catid){
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("rid", l_catid); // pass id to add page by integer
        startActivity(intent);
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
