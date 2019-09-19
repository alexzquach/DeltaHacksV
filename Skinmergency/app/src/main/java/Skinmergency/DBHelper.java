package com.kqsoft.expensetutorken;

/**
 * Created by KenQ on 2015-12-01.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    //The Android's default system path of your application database.
    //String DB_PATH =null;

    private static String DB_NAME = "Skinmergency.db";
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;

    private final Context myContext;

    private static DBHelper instance;

    //========================COPY DATABASE ROUTINE================================================================

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DBHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }
    //use singleton here
    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
            Log.d("ddddddddd", "====");
        }
        else{
            Log.d("bbbb", "====");

        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //setup database
    public void initDB(){
        //can not use dbfile check here for android need to create data directory first before
        //copy file can be perform
        File dbFile = myContext.getDatabasePath(DB_NAME);

        if (!dbFile.exists()) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            //without this method, it will failed copy
            this.getReadableDatabase();

            try {
                copyDatabase(dbFile);
                Toast.makeText(myContext, "First time run: Setting up database success.", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                String l_msg = "First time run: Setting up database failed.";
                Toast.makeText(myContext, l_msg, Toast.LENGTH_LONG).show();
                //use throw new RunTimeException will stop the app (crash)
                //throw new RuntimeException("Error creating source database", e);
            }
        }
        else{
            //Toast.makeText(myContext, "DB Exists", Toast.LENGTH_LONG).show();


        }
    }

    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = myContext.getAssets().open(DB_NAME);
        OutputStream os = new FileOutputStream(dbFile);
        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            os.write(buffer);
        }
        os.flush();
        os.close();
        is.close();
    }


    //=====================================UTILITIES FUNCTIONS===========================================
    //utilities functions put this on every page
    // Used to convert 24hr format to 12hr format with AM/PM values
    public String convertMilitaryToAMPM(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String l_hour;
        if (hours < 10)
            l_hour = "0" + String.valueOf(hours);
        else
            l_hour = String.valueOf(hours);

        String minutes = "";
        if (mins < 10)
            minutes = "0" + String.valueOf(mins);
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(l_hour).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;

    }
    //get currentdate in yyyy-mm-dd format
    public String currentDate() {
        String l_currentdate;
        final Calendar c = Calendar.getInstance();
        //set currentdate
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = mMonth + 1;

        String l_date;
        String tmp_month = String.valueOf(mMonth);
        String tmp_day = String.valueOf(mDay);
        String tmp_year = String.valueOf(mYear);

        if (mMonth < 10){
            tmp_month = "0" + tmp_month;
        }
        if (mDay < 10){
            tmp_day = "0" + tmp_day;
        }

        l_currentdate = tmp_year + "-" + tmp_month + "-" + tmp_day;
        return l_currentdate;

    }
    //Given sql string, this will execute the query
    public boolean execQuery(String l_sql)
    {   boolean success = true;
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL(l_sql);
        }catch (SQLiteException e){
            //e.printStackTrace();
            String l_error = "SQL Error: " + e.getMessage();
            Toast.makeText(myContext, l_error, Toast.LENGTH_LONG).show();
            success = false;

        }
        db.close();
        return success;
    }
    //return total record
    public int count(String l_sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = l_sql; //"SELECT * FROM students";
        int recordCount = 0;
        try{
            recordCount = db.rawQuery(sql, null).getCount();
        }catch (SQLiteException e){
            //e.printStackTrace();
            String l_error = "SQL Error: " + e.getMessage();
            Toast.makeText(myContext, l_error, Toast.LENGTH_LONG).show();
            recordCount = -1;
        }
        db.close();
        return recordCount;

    }
    //return maxid (pk id)
    public int maxid(String l_sql) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = l_sql; //"SELECT * FROM students";
        int l_maxid = 0;
        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                l_maxid = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return l_maxid;

    }

//=====================================DB CRUD ROUTINE===========================================

//**************************************SkinCondition***********************************************
    ArrayList<SkinCondition_Tbl> SkinConditionList(){
        ArrayList<SkinCondition_Tbl> s_list = new ArrayList<SkinCondition_Tbl>();
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.SkinConditionID, t1.SkinConditionName, t1.Dangerous, t1.VisitLocalClinic, t1.VisitEmergencyRoom, t1.SDescription from SkinCondition t1 order by t1.SkinConditionName";

        Cursor cursor = db.rawQuery(l_sql, null);

        if (cursor.moveToFirst()){
            do{
                SkinCondition_Tbl l_rec = new SkinCondition_Tbl();
                l_rec.SkinConditionID = cursor.getInt(0);
                l_rec.SkinConditionName = cursor.getString(1);
                l_rec.Dangerous = cursor.getInt(2);
                l_rec.VisitLocalClinic = cursor.getInt(3);
                l_rec.VisitEmergencyRoom = cursor.getInt(4);
                l_rec.SDescription = cursor.getString(5);
                s_list.add(l_rec);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s_list;

    }

    public SkinCondition_Tbl SkinGetRecord(int i_id) {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.SkinConditionID, t1.SkinConditionName, t1.Dangerous, t1.VisitLocalClinic, t1.VisitEmergencyRoom, t1.SDescription from SkinCondition t1 where SkinConditionID = " + String.valueOf(i_id);
        SkinCondition_Tbl l_rec = new SkinCondition_Tbl();

        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                l_rec.SkinConditionID = cursor.getInt(0);
                l_rec.SkinConditionName = cursor.getString(1);
                l_rec.Dangerous = cursor.getInt(2);
                l_rec.VisitLocalClinic = cursor.getInt(3);
                l_rec.VisitEmergencyRoom = cursor.getInt(4);
                l_rec.SDescription = cursor.getString(5);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_rec;
    }

//**************************************Treatment***********************************************
    ArrayList<Treatment_Tbl> TreamentConditionList(int skin_id){
        ArrayList<Treatment_Tbl> s_list = new ArrayList<Treatment_Tbl>();
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.TreatmentID, t1.TreatmentName, t1.TreatmentDescription, t1.SkinConditionID, t1.WhereToFind from Treatment t1 where SkinConditionID = " + String.valueOf(skin_id) + " order by t1.TreatmentName";

        Cursor cursor = db.rawQuery(l_sql, null);

        if (cursor.moveToFirst()){
            do{
                Treatment_Tbl l_rec = new Treatment_Tbl();
                l_rec.TreatmentID = cursor.getInt(0);
                l_rec.TreatmentName = cursor.getString(1);
                l_rec.TreatmentDescription = cursor.getString(2);
                l_rec.SkinConditionID = cursor.getInt(3);
                l_rec.WhereToFind = cursor.getString(4);
                s_list.add(l_rec);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s_list;

    }

    public Treatment_Tbl TreatmentGetRecord(int i_id) {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.TreatmentID, t1.TreatmentName, t1.TreatmentDescription, t1.SkinConditionID, t1.WhereToFind from Treatment t1 where TreatmentID = " + String.valueOf(i_id);
        Treatment_Tbl l_rec = new Treatment_Tbl();

        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                l_rec.TreatmentID = cursor.getInt(0);
                l_rec.TreatmentName = cursor.getString(1);
                l_rec.TreatmentDescription = cursor.getString(2);
                l_rec.SkinConditionID = cursor.getInt(3);
                l_rec.WhereToFind = cursor.getString(4);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_rec;
    }

//**************************************Symptom***********************************************

    ArrayList<Symptom_Tbl> SymptomList(int skin_id){
        ArrayList<Symptom_Tbl> s_list = new ArrayList<Symptom_Tbl>();
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.SymptomID, t1.SymptomName, t1.SkinConditionID from Symptom t1 where SkinConditionID = " + String.valueOf(skin_id) + " order by t1.SymptomName";

        Cursor cursor = db.rawQuery(l_sql, null);

        if (cursor.moveToFirst()){
            do{
                Symptom_Tbl l_rec = new Symptom_Tbl();
                l_rec.SymptomID = cursor.getInt(0);
                l_rec.SymptomName = cursor.getString(1);
                l_rec.SkinConditionID = cursor.getInt(2);
                s_list.add(l_rec);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return s_list;

    }

    public Symptom_Tbl SymptomGetRecord(int i_id) {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.SymptomID, t1.SymptomName, t1.SkinConditionID from Symptom t1 where SymptomID = " + String.valueOf(i_id) + " order by t1.SymptomName";
        Symptom_Tbl l_rec = new Symptom_Tbl();

        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                l_rec.SymptomID = cursor.getInt(0);
                l_rec.SymptomName = cursor.getString(1);
                l_rec.SkinConditionID = cursor.getInt(2);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_rec;
    }





//**************************************Categories***********************************************
//------------------------------CatList-----------------------------------------------
    //get Category List
    ArrayList<Cat_Tbl> CatList() {
        ArrayList<Cat_Tbl> l_List = new ArrayList<Cat_Tbl>();
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select t1.CategoryID, t1.CategoryName, ifnull(t1.IconName, '') IconName, count(t2.ItemName) TotalItems from Category t1 left join Item t2 on t1.CategoryID = t2.CategoryID group by t1.categoryID order by t1.CategoryName";


        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Cat_Tbl l_rec = new Cat_Tbl();
                l_rec.catid = cursor.getInt(0);
                l_rec.catname = cursor.getString(1);
                l_rec.iconName = cursor.getString(2);
                l_rec.totalItems = cursor.getInt(3);

                l_List.add(l_rec);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_List;
    }



//------------------------------CatAdd---------------------------------------------------
    //add a record and return id
    public int CatAdd(Cat_Tbl i_rec) {

        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        //store value in the contents
        ContentValues values = new ContentValues();
        values.put("CategoryName", i_rec.catname);
        // Inserting Row
        long rid = db.insert("Category", null, values);
        db.close(); // Closing database connection
        return (int) rid;
    }
    //------------------------------CatUpdate---------------------------------------------------
    //update record
    public boolean CatUpdate(Cat_Tbl i_rec) {

        //Open connection to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CategoryName", i_rec.catname);

        String where = "CategoryId = ?";
        String[] whereArgs = { Integer.toString(i_rec.catid) };

        boolean l_success = db.update("Category", values, where, whereArgs) > 0;
        db.close(); // Closing database connection
        return l_success;
    }
    //------------------------------CatGetRecord-----------------------------------------------
    //get single record
    public Cat_Tbl CatGetRecord(int i_id) {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select categoryid, categoryname, iconname from category where categoryid = " + String.valueOf(i_id);
        Cat_Tbl l_rec = new Cat_Tbl();

        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                l_rec.catid = cursor.getInt(0);
                l_rec.catname = cursor.getString(1);
                l_rec.iconName = cursor.getString(2);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_rec;
    }

    //------------------------------CatDelete---------------------------------------------------
    //return true false after delete a record
    public boolean CatDelete(int i_recid){
        boolean success = true;
        SQLiteDatabase db = this.getWritableDatabase();
        String l_sql;
        l_sql = "delete from category where categoryid = "+ String.valueOf(i_recid);
        try {
            db.execSQL(l_sql);
        } catch (SQLiteException e) {
            String l_error = "SQL Error: " + e.getMessage();
            Toast.makeText(myContext, l_error, Toast.LENGTH_LONG).show();
            success = false;
        }
        db.close();
        return success;
    }

    //**************************************Items***********************************************
    //------------------------------ItemList-----------------------------------------------
    //get Item List
    ArrayList<Item_Tbl> ItemList(int i_catid) {
        ArrayList<Item_Tbl> l_List = new ArrayList<Item_Tbl>();
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        String l_sql = "select itemid, itemname, regularlyused, active, categoryid, categoryname from categoryitemview where categoryid = " + String.valueOf(i_catid);


        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                Item_Tbl l_rec = new Item_Tbl();
                l_rec.itemid = cursor.getInt(0);
                l_rec.itemname = cursor.getString(1);
                l_rec.regularlyuse = cursor.getInt(2);
                l_rec.active = cursor.getInt(3);
                l_rec.catid = cursor.getInt(4);
                l_rec.catname = cursor.getString(5);
                l_List.add(l_rec);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_List;
    }
    //------------------------------ItemDelete---------------------------------------------------
    //return true false after delete a record
    public boolean ItemDelete(int i_recid){
        boolean success = true;
        SQLiteDatabase db = this.getWritableDatabase();
        String l_sql;
        l_sql = "delete from item where itemid = "+ String.valueOf(i_recid);
        try {
            db.execSQL(l_sql);
        } catch (SQLiteException e) {
            String l_error = "SQL Error: " + e.getMessage();
            Toast.makeText(myContext, l_error, Toast.LENGTH_LONG).show();
            success = false;
        }
        db.close();
        return success;
    }

    //========================this array is use for dropdownlist or spinner======================
    public List<SpinnerObject> LookupValue(String i_sql) {
        //Open connection to read only
        SQLiteDatabase db = this.getReadableDatabase();
        //String l_sql = "select id, name, defaultflag from listtype";
        String l_sql = i_sql;

        //Student student = new Student();
        List<SpinnerObject> l_stdList = new ArrayList<SpinnerObject>();

        Cursor cursor = db.rawQuery(l_sql, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                SpinnerObject stdl = new SpinnerObject(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
                l_stdList.add(stdl);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return l_stdList;

    }

}//END CLASS
