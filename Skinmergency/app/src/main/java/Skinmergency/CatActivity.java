package com.kqsoft.expensetutorken;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class CatActivity extends AppCompatActivity {
    DBHelper glb_DB;
    ListView lv;
    //declare arrray adapter for data
    CatAdapter adapterLV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForAdd();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-------------Start Code-----------------------------------------
        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton
        //retrieve catlist
        ArrayList<Cat_Tbl> l_CatList = new ArrayList<Cat_Tbl>();
        l_CatList = glb_DB.CatList();
        //get adapter
        //this is android builtin listview
        //adapterLV = new CatAdapter(this, android.R.layout.simple_list_item_2, l_CatList);
        //this is custom
        adapterLV = new CatAdapter(this, R.layout.simplelistitem2_image, l_CatList);

        // Assign adapter to ListView
        lv = (ListView) findViewById(R.id.catLV);
        lv.setAdapter(adapterLV);

        //WHEN ITEM TAB
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get current row tab
                Cat_Tbl l_row  = (Cat_Tbl) parent.getItemAtPosition(position);
                int l_rid = l_row.catid;
                startActivityItem(l_rid);
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
