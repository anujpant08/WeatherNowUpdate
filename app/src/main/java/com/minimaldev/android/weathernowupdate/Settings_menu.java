package com.minimaldev.android.weathernowupdate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by ANUJ on 29/12/2016.
 */
public class Settings_menu extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    String des,loc,id;
    double temp;

    Toolbar toolbar;
    int color=0xFF212121;
    Settings_menu settings_menu;
    NotificationManager mNotificationManager;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        getSupportActionBar().setTitle("Settings");



        LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.toastview,(ViewGroup)findViewById(R.id.toast_layout));

        /* des=getIntent().getStringExtra("DES");
         loc=getIntent().getStringExtra("LOC");
         temp=getIntent().getDoubleExtra("TEM",0.0);
         id=getIntent().getStringExtra("ID");
         if(des==null||loc==null)
         {

         }
        else {
             checkValues(des, temp, loc, id);
         } */

    }

    public boolean onPreferenceClick(Preference preference)
    {
        if(preference.getKey().equals("about_us")) {
            Intent emailInt = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
            //String emailList[] = {"minimaldev4playstore@gmail.com"};
            emailInt.setData(Uri.parse("minimaldev4playstore@gmail.com"));
            startActivity(emailInt);
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Boolean val=false;
        val=sharedPreferences.getBoolean("checkBox",false);
         mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent= new Intent(this, WeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,0);
        /*Bitmap bm1= BitmapFactory.decodeResource(getResources(),R.drawable.thunder_not);
        Bitmap bm2= BitmapFactory.decodeResource(getResources(),R.drawable.driz_not);
        Bitmap bm3= BitmapFactory.decodeResource(getResources(),R.drawable.rain_not);
        Bitmap bm4= BitmapFactory.decodeResource(getResources(),R.drawable.snow_not);
        Bitmap bm5= BitmapFactory.decodeResource(getResources(),R.drawable.fog_not);
        Bitmap bm6= BitmapFactory.decodeResource(getResources(),R.drawable.sun_not);
        Bitmap bm7= BitmapFactory.decodeResource(getResources(),R.drawable.moon_not);
        Bitmap bm8= BitmapFactory.decodeResource(getResources(),R.drawable.sc_not);
        Bitmap bm9= BitmapFactory.decodeResource(getResources(),R.drawable.ov_not);
        Bitmap bm10= BitmapFactory.decodeResource(getResources(),R.drawable.ex_not);
        Bitmap bm11= BitmapFactory.decodeResource(getResources(),R.drawable.br_not);


        if(val)
        {


            int actualID = Integer.parseInt(id);
            int ID = actualID / 100;
            DecimalFormat df = new DecimalFormat("###.#");
            String formatTemp = df.format(temp);
            formatTemp=formatTemp+"\u2103";
            des=des.toUpperCase();
            NotificationChannel notificationChannel=new NotificationChannel("channel","WeatherNow",NotificationManager.IMPORTANCE_DEFAULT);
            switch (ID) {
                case 2://for thunderstorm

                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);



                    break;
                case 3://for drizzle
                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                    break;
                case 5: //for rain
                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                    break;
                case 6: //for snowy

                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);

                    break;
                case 7: //for foggy
                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                    break;
                case 8: //for sunnyday and clouds

                    if (actualID == 800) {
                        if(hour>=6 && hour<18) {

                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }
                        else
                        {
                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }

                    }
                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds
                        if(hour>=6 && hour<=18) {
                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }
                        else {

                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);

                        }

                    }
                    if (actualID == 804) {
                        //for overcasttype
                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);

                    }
                    break;
                case 9://for extreme weather

                    if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {
                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);
                    }
                    else
                    {
                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);

                    }


                    break;

            }


        }


        else
        {
            mNotificationManager.cancel(1);

        }*/


    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkValues(String d, double t, String l, String iid)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean update = sharedPrefs.getBoolean("checkBox",false);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Intent i=new Intent(this,WeatherActivity.class);
        DecimalFormat df = new DecimalFormat("###.#");
        String formatTemp = df.format(t);
        d=d.toUpperCase();
        formatTemp=formatTemp+"\u2103";
        Intent intent= new Intent(this, WeatherActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,0);
        /*Bitmap bm1= BitmapFactory.decodeResource(getResources(),R.drawable.thunder_not);
        Bitmap bm2= BitmapFactory.decodeResource(getResources(),R.drawable.driz_not);
        Bitmap bm3= BitmapFactory.decodeResource(getResources(),R.drawable.rain_not);
        Bitmap bm4= BitmapFactory.decodeResource(getResources(),R.drawable.snow_not);
        Bitmap bm5= BitmapFactory.decodeResource(getResources(),R.drawable.fog_not);
        Bitmap bm6= BitmapFactory.decodeResource(getResources(),R.drawable.sun_not);
        Bitmap bm7= BitmapFactory.decodeResource(getResources(),R.drawable.moon_not);
        Bitmap bm8= BitmapFactory.decodeResource(getResources(),R.drawable.sc_not);
        Bitmap bm9= BitmapFactory.decodeResource(getResources(),R.drawable.ov_not);
        Bitmap bm10= BitmapFactory.decodeResource(getResources(),R.drawable.ex_not);
        Bitmap bm11= BitmapFactory.decodeResource(getResources(),R.drawable.br_not); */

        if(update)
        {

            int actualID = Integer.parseInt(iid);
            int ID = actualID / 100;
            NotificationChannel notificationChannel=new NotificationChannel("channel2","WeatherNow",NotificationManager.IMPORTANCE_DEFAULT);

            switch (ID) {


                case 2://for thunderstorm

                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);



                    break;
                case 3://for drizzle
                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);

                    break;
                case 5: //for rain
                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);

                    break;
                case 6: //for snowy

                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);


                    break;
                case 7: //for foggy
                    notificationChannel.setDescription(des);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    mNotificationManager.createNotificationChannel(notificationChannel);

                    break;
                case 8: //for sunnyday and clouds

                    if (actualID == 800) {
                        if(hour>=6 && hour<18) {

                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);

                        }
                        else
                        {
                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);

                        }

                    }
                    if (actualID == 801 || actualID == 802 || actualID == 803) {


                        //for scattered clouds
                        if(hour>=6 && hour<=18) {

                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);

                        }
                        else {

                            notificationChannel.setDescription(des);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);

                        }

                    }
                    if (actualID == 804) {
                        //for overcasttype
                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);


                    }
                    break;
                case 9://for extreme weather

                    if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {
                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);
                    }
                    else
                    {
                        notificationChannel.setDescription(des);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        mNotificationManager.createNotificationChannel(notificationChannel);


                    }


                    break;

            }


        }
        else
        {
            mNotificationManager.cancel(1);
        }


    }








}
