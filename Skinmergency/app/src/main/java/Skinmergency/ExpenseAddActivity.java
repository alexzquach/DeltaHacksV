package com.kqsoft.expensetutorken;

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
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAddActivity extends AppCompatActivity {
    DBHelper glb_DB;   //singleton
    //global variable
    Spinner ddlAccount;
    Spinner ddlItem;
    Spinner ddlPayee;
    EditText txtAmount;
    private int glb_rid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);
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
        //get fields
        ddlAccount = (Spinner) findViewById(R.id.ddlAccount);
        ddlItem = (Spinner) findViewById(R.id.ddlItem);
        ddlPayee = (Spinner) findViewById(R.id.ddlPayee);

        //get component
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        populateData();

    }
    private void populateData(){
        List<SpinnerObject> lkvalueList = new ArrayList<SpinnerObject>();;
        String l_sql;
        //load listtype
        l_sql = "select accountid, accountname name, defaultvalue defaultflag from account";
        lkvalueList = glb_DB.LookupValue(l_sql);
        ArrayAdapter<SpinnerObject> spinnerAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_item, lkvalueList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ddlAccount.setAdapter(spinnerAdapter);
        //set default value
        setSpinnerDefaultByFlag(ddlAccount);

    }
    //-------------------SPINNER SET DEFAULT CODE--------------------------------
    //set ddl default by value use for edit populate record
    private void setSpinnerDefaultByValue(Spinner spinner, int Id)
    {   SpinnerObject selected;
        for (int i = 0; i < spinner.getCount(); i++) {
            selected = (SpinnerObject) spinner.getItemAtPosition(i);
            if (selected.getId() == Id) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    //this will set selected item to default value in the table
    private void setSpinnerDefaultByFlag(Spinner spinner)
    {   SpinnerObject selected;
        for (int i = 0; i < spinner.getCount(); i++) {
            selected = (SpinnerObject) spinner.getItemAtPosition(i);
            if (selected.getDefaultflag() == 1) {

                spinner.setSelection(i);
                break;
            }
        }
    }
    public void p_add(){
        //this is how you get the accountid in the dropdownlist
        int l_accountid = ((SpinnerObject) ddlAccount.getSelectedItem()).getId();
        Log.d("Account: " + String.valueOf(l_accountid), "");
    }

    //----------------------HANDLE MENU------------------------------------------
    //we attach the menu layout to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.catadd_menu, menu);
        //hide delete button if add
        //if (glb_rid == 0){
            //hide delete action item
            MenuItem item = menu.findItem(R.id.aDelete);
            item.setVisible(false);
        //}
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
            p_add();
            /*
            if (glb_rid == 0){
                p_add();
                //refresh listview from previous page
                //goBackRefreh();

            }
            else{ //update
                //p_update();
                //refresh listview from previous page
                //goBackRefreh();
            }
            */

            return true;
        }
        else if (id == R.id.aDelete){
            //DeleteDiaglog();
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

}
