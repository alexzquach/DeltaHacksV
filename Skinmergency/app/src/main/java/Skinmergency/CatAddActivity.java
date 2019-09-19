package com.kqsoft.expensetutorken;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class CatAddActivity extends AppCompatActivity {
    DBHelper glb_DB;
    EditText txtCatName;
    //this glb_rid is passing from the previous screen
    private int glb_rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getParameter(); //get parameter from previous page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_add);
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
        //Change the activity title base on the recid from previous page
        if (glb_rid > 0){
            getSupportActionBar().setTitle("Category Edit");
        }
        else{
            getSupportActionBar().setTitle("Category Add");
        }

        txtCatName = (EditText) findViewById(R.id.txtCatName);
        populateData();

    }

    //*************************************OPERATION CODE HERE*********************************

    //--------------getparameter-----------------
    //get parameter passing from previous page
    private void getParameter(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //String value = extras.getString("rid"); //get by string
            glb_rid = extras.getInt("rid"); //get by integer
        }
    }
    //p_Add
    public void p_add(){

        String l_CatName;
        int l_recid = 0;

        Cat_Tbl l_rec = new Cat_Tbl();

        //get user input
        l_CatName = txtCatName.getText().toString().trim();

        //put input in the record object
        l_rec.catname = l_CatName;
        if (l_CatName.length() > 0){
            l_recid = glb_DB.CatAdd(l_rec);
        }
    }
    public void populateData(){

        //if update
        if (glb_rid > 0){
            Cat_Tbl l_record = glb_DB.CatGetRecord(glb_rid);
            txtCatName.setText(l_record.catname);
        }
    }
    public void p_update(){
        String l_CatName;
        Cat_Tbl l_rec = new Cat_Tbl();

        //get user input
        l_CatName = txtCatName.getText().toString().trim();
        //put input in the record object
        l_rec.catname = l_CatName;
        l_rec.catid = glb_rid;


        if (l_CatName.length() > 0){
            glb_DB.CatUpdate(l_rec);
        }

    }
    public void DeleteDiaglog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Delete?");
        builder.setMessage("Delete item?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do do my action here
                p_delete();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // I do not need any action here you might
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    public void p_delete(){
        glb_DB.CatDelete(glb_rid);
        //refresh listview from previous page
        goBackRefreh();

    }
    //----------------------HANDLE MENU------------------------------------------
    //we attach the menu layout to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.catadd_menu, menu);
        //hide delete button if add
        if (glb_rid == 0){
            //hide delete action item
            MenuItem item = menu.findItem(R.id.aDelete);
            item.setVisible(false);
        }
        return true;
    }
    //get menu action on the item tap from the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.aSave) {
            if (glb_rid == 0){
                p_add();
                //refresh listview from previous page
                goBackRefreh();

            }
            else{ //update
                p_update();
                //refresh listview from previous page
                goBackRefreh();
            }


            return true;
        }
        else if (id == R.id.aDelete){
            DeleteDiaglog();
        }
        else if (id == android.R.id.home){
            // Respond to the action bar's Up/Home button
            //Back no refresh previous screen
            //So when user press the Up button it will get the behavior of the back button in the navigation bar.
            onBackPressed();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void goBackRefreh(){

        //refresh listview from previous page
        Intent a = new Intent(this, CatActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous screen and refreshing
        startActivity(a);
        finish();

    }

}
