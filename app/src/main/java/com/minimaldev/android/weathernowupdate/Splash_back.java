package com.minimaldev.android.weathernowupdate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by ANUJ on 28/12/2016.
 */
public class Splash_back extends AppCompatActivity {

    static int SPLASH_TIME_OUT=3000;
    private Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;

    //boolean gps=false;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //LocationManager lm=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        //gps=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //setContentView(R.layout.activity_main);


        setContentView(R.layout.splash_back);
       // if(!gps)
       // {
        //    Toast.makeText(this,"Enable location in settings",Toast.LENGTH_SHORT).show();
       // }


       /* if(!isNetworkAvailable())
        {
            Toast.makeText(this,"Connection error",Toast.LENGTH_SHORT).show();
        } */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Splash_back.this, WeatherActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }, SPLASH_TIME_OUT);

            }
            else
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(Splash_back.this, WeatherActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }, SPLASH_TIME_OUT );
            }




                //return;
            }

        else
        {
            Intent intent = new Intent(Splash_back.this, WeatherActivity.class);
            startActivity(intent);
            finish();



        }

        }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();


                } else {

                    Toast.makeText(this, "Permission Denied, You cannot access location data. Go to settings and enable location permission for 'WeatherNow'", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo= cm.getActiveNetworkInfo();
        return activeNetworkInfo!=null && activeNetworkInfo.isConnectedOrConnecting();
    }


}
