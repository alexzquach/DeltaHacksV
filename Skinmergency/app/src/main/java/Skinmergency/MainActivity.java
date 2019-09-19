package com.kqsoft.expensetutorken;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBHelper glb_DB;
    private MSCognitiveServicesClassifier classifier;

    //Picture stuff
    static int TAKE_PICTURE = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String IMAGE_DIRECTORY = "/KenCam";
    final private int PERMISSION_ALL = 124;
    private boolean selector = true;
    public static Bitmap diseasePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView aboutText = findViewById(R.id.aboutTextView);
        TextView sourcesText = findViewById(R.id.sourcesTextView);
        TextView disclaimerText = findViewById(R.id.disclaimerTextView);
        aboutText.setMovementMethod(new ScrollingMovementMethod());
        sourcesText.setMovementMethod(new ScrollingMovementMethod());
        disclaimerText.setMovementMethod(new ScrollingMovementMethod());

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selector = !selector;
                Toast.makeText(getApplicationContext(), (selector) ? "General Skin Diagnosis" : "Melanoma Vs Mole Detection", Toast.LENGTH_SHORT).show();
                fab.setRippleColor((selector) ? Color.RED : Color.GREEN);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //-------------Start Code-----------------------------------------
        //init db
        glb_DB = DBHelper.getInstance(this);   //singleton
        glb_DB.initDB();


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //startActivityCamera();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, TAKE_PICTURE);

        } else if (id == R.id.nav_gallery) {
            //startActivityCamera();
            switchToConditions();
        }
//        } else if (id == R.id.nav_slideshow) {
//            startActivityCat();
//        } else if (id == R.id.nav_manage) {

//        }
        else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void switchToConditions() {
        Intent tmp = new Intent(this, ConditionsListActivity.class);
        startActivity(tmp);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap)intent.getExtras().get("data");
            diseasePicture = photo;
            //Displays the image on the view
//            ivThumbnailPhoto.setImageBitmap(photo);
//            ivThumbnailPhoto.setVisibility(View.VISIBLE);
            saveImage(photo);
            String path;
            String labelPath;
            if (selector){
                path = "file:///android_asset/model.pb";
                labelPath = "labels.txt";
            }else{
                path = "file:///android_asset/melanoma_model.pb";
                labelPath = "melanoma_labels.txt";
            }
            classifier = new MSCognitiveServicesClassifier(MainActivity.this, path, labelPath);

            Classifier.Recognition r = classifier.classifyImage(photo, 0);
            System.out.println(r.getId());
            System.out.println(r.getTitle());

            //This takes you to a specific activity after taking a picture
            Intent test = new Intent(this, Information.class);
            test.putExtra("Name", r.getTitle());
            //intent.putExtra("rid", 0); // pass id to add page by integer
            startActivity(test);
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());
//            txtFileSaveLocation.setText("File Saved: " + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
            Log.d("TAG Ken Error", "File Saved::---&gt;");
        }
        return "";
    }

    //======================CUSTOM CODE HERE=======================================
    private void startActivityCat(){
        Intent intent = new Intent(this, CatActivity.class);
        //intent.putExtra("rid", 0); // pass id to add page by integer
        startActivity(intent);
    }
//    private void startActivityCamera(){
//        Intent intent = new Intent(this, CameraDemoActivity.class);
//        //intent.putExtra("rid", 0); // pass id to add page by integer
//        startActivity(intent);
//    }
    private void startActivityCamera(){
        Intent intent = new Intent(this, CameraActivity.class);
        //intent.putExtra("rid", 0); // pass id to add page by integer
        startActivity(intent);
    }
    private void startActivityExpenseAdd(){
        Intent intent = new Intent(this, ExpenseAddActivity.class);
        //intent.putExtra("rid", 0); // pass id to add page by integer
        startActivity(intent);
    }

}
