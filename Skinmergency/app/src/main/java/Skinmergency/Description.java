package com.kqsoft.expensetutorken;

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

public class Description extends AppCompatActivity {
    DBHelper glb_DB;
    ListView lv;
    //declare arrray adapter for data
    SymptomAdapter adapterLV = null;
    String description;
    int ID;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        description = getIntent().getStringExtra("Des");
        ID = getIntent().getIntExtra("ID", 0);

        desc = (TextView) findViewById(R.id.descriptionTextView);
        desc.setMovementMethod(new ScrollingMovementMethod());
        desc.setText(description);

        //-------------Start Code-----------------------------------------
        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton
        //retrieve catlist
        ArrayList<Symptom_Tbl> symptomsList = new ArrayList<Symptom_Tbl>();
        symptomsList = glb_DB.SymptomList(ID);

        //  treatmentList = glb_DB.CatList();
        //get adapter
        //this is android builtin listview
        //adapterLV = new CatAdapter(this, android.R.layout.simple_list_item_2, l_CatList);
        //this is custom
//        adapterLV = new CatAdapter(this, R.layout.simplelistitem2_image, treatmentList);
        adapterLV = new SymptomAdapter(this, R.layout.simplelistitem2_image, symptomsList);

        // Assign adapter to ListView
        lv = (ListView) findViewById(R.id.symptomsLV);
        lv.setAdapter(adapterLV);

//        //final TextView treatmentText = findViewById(R.id.treatmentTextView);
//        //WHEN ITEM TAB
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                //get current row tab
//                Symptom_Tbl l_row  = (Symptom_Tbl) parent.getItemAtPosition(position);
//                int l_rid = l_row.catid;
//////                startActivityItem(l_rid);
////                treatmentText.setText(l_rid);
//
//            }
//        });
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
