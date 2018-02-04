package com.minimaldev.android.weathernowupdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
                setContentView(R.layout.splash_back);
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant


            }
            else
            {
                        //setContentView(R.layout.splash_back);
                final Handler handler=new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.splash_back);
                    }
                },3000);
                        Intent intent = new Intent(Splash_back.this, WeatherActivity.class);
                        startActivity(intent);
                        finish();

            }




                //return;
            }

        else
        {
            //setContentView(R.layout.splash_back);
            final Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setContentView(R.layout.splash_back);
                }
            },3000);
            Intent intent = new Intent(Splash_back.this, WeatherActivity.class);
            startActivity(intent);
            finish();



        }

        }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Splash_back.this, WeatherActivity.class);
                    startActivity(intent);
                    finish();


                } else {

                    Toast.makeText(this, "Permission Denied, You cannot access location data. Go to settings and enable location permission for 'WeatherNow'", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }



}
