package com.kqsoft.expensetutorken;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {
    DBHelper glb_DB;   //singleton
    private int glb_id;

    ListView lv;
    //declare arrray adapter for data
    ItemAdapter adapterLV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getParameter();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //-------------Start Code-----------------------------------------
        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton



        //retrieve list
        ArrayList<Item_Tbl> l_ItemList = new ArrayList<Item_Tbl>();
        l_ItemList = glb_DB.ItemList(glb_id);


        //get adapter
        //this is android builtin listview
        //adapterLV = new CatAdapter(this, android.R.layout.simple_list_item_2, l_CatList);
        //this is custom
        adapterLV = new ItemAdapter(this, R.layout.simplelistitem2_checkbox, l_ItemList);

        // Assign adapter to ListView
        lv = (ListView) findViewById(R.id.itemLV);
        lv.setAdapter(adapterLV);
        //WHEN ITEM TAB
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get current row tab
                Item_Tbl l_row  = (Item_Tbl) parent.getItemAtPosition(position);
                int l_rid = l_row.itemid;
                // do something here with item
            }
        });


    }//end onCreate
    //--------------------KQ OPERATION CODE---------------------------------------
    //get parameter passing from previous page
    private void getParameter(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //String value = extras.getString("rid"); //get by string
            glb_id = extras.getInt("rid"); //get by integer
        }
    }

}
