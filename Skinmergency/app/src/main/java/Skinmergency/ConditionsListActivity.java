package com.kqsoft.expensetutorken;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ConditionsListActivity extends AppCompatActivity {
    DBHelper glb_DB;
    ListView lv;
    //declare arrray adapter for data
    SkinConditionAdapter adapterLV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //-------------Start Code-----------------------------------------
        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton
        //retrieve catlist
        ArrayList<SkinCondition_Tbl> l_CatList = new ArrayList<SkinCondition_Tbl>();
        l_CatList = glb_DB.SkinConditionList();
        //get adapter
        //this is android builtin listview
        //adapterLV = new CatAdapter(this, android.R.layout.simple_list_item_2, l_CatList);
        //this is custom
        adapterLV = new SkinConditionAdapter(this, R.layout.simplelistitem2_image, l_CatList);

        // Assign adapter to ListView
        lv = (ListView) findViewById(R.id.conditionsLV);
        lv.setAdapter(adapterLV);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //WHEN ITEM TAB
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get current row tab
                SkinCondition_Tbl l_row  = (SkinCondition_Tbl) parent.getItemAtPosition(position);
                int l_rid = l_row.SkinConditionID;
                startActivityInformation(l_rid);
            }
        });

    }

    void startActivityInformation(int i){
        Intent intent = new Intent(this, Information.class);
        intent.putExtra("Type", i); // pass id to add page by integer
        startActivity(intent);
    }

}
