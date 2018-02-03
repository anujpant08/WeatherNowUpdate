package com.minimaldev.android.weathernowupdate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ANUJ on 24/12/2016.
 */
public class Dialog_weather extends AppCompatActivity implements View.OnClickListener {

        String url="",loc="";
        TextView textView;
        String fullLocation;
        LinearLayout linearLayout;
        Set<String> set=new HashSet<String>();
        int countfav=0;
        public  static  DisplayFav displayFav=new DisplayFav();
        public   ArrayAdapter<String> adapter;
        ArrayList<String> array=new ArrayList<String>();
        public static ArrayList<String> list =new ArrayList<String>();
        String tim,wid;
        String enc;
        Snackbar snackbar;
        String arr[]=new String[1000];
        String cor_url;
        boolean shown;
        int hour;
        String apikey="6024521-aa3caf3e11ed4bf5eead80356";
        public boolean flag=false;
        double lat,lon,lt,ln;
        TextView tv;
        String weatherid="";
        ActionBar actionBar;
        LottieAnimationView animationView, moonView, cloudy,cloudymoon,fogday,fognight,snowday,snownight,thunder,rainy,overcast;

        public void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);

                hasNavBar();
               // LoadPreference(adapter);
                //tim="";
                url = getIntent().getStringExtra("URL");
                loc=getIntent().getStringExtra("loc_desc");
                shown=false;
                //getCoordinates(loc);
                /*supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                actionBar=getSupportActionBar();
                getSupportActionBar().setTitle(""); */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Window w = getWindow(); // in Activity's onCreate() for instance
                        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                        //w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                }

                if(hasNavBar()) {

                        setContentView(R.layout.dialog_main);
                }
                else
                {
                        setContentView(R.layout.dialog_mainnonavbar);
                }

                animationView= (LottieAnimationView) findViewById(R.id.animation_view);
                moonView = (LottieAnimationView) findViewById(R.id.moon_view);
                cloudy=(LottieAnimationView) findViewById(R.id.cloud_view);
                cloudymoon=(LottieAnimationView) findViewById(R.id.cloudmoon_view);
                fogday=(LottieAnimationView) findViewById(R.id.fogday_view);
                fognight=(LottieAnimationView) findViewById(R.id.fognight_view);
                overcast=(LottieAnimationView) findViewById(R.id.overcast_view);
                rainy=(LottieAnimationView) findViewById(R.id.rainy_view);
                snowday=(LottieAnimationView) findViewById(R.id.snowday_view);
                snownight=(LottieAnimationView) findViewById(R.id.snownight_view);
                thunder=(LottieAnimationView) findViewById(R.id.thunder_view);

                animationView.setImageAssetsFolder("images/");
                animationView.setAnimation("sun.json");
                animationView.loop(false);

                moonView.setImageAssetsFolder("moon/");
                moonView.setAnimation("moon.json");
                moonView.loop(false);

                cloudy.setImageAssetsFolder("cloudy/");
                cloudy.setAnimation("cloudy.json");
                cloudy.loop(false);

                cloudymoon.setImageAssetsFolder("cloudymoon/");
                cloudymoon.setAnimation("cloudymoon.json");
                cloudymoon.loop(false);

                fogday.setImageAssetsFolder("fogday/");
                fogday.setAnimation("fogday.json");
                fogday.loop(false);

                fognight.setImageAssetsFolder("fognight/");
                fognight.setAnimation("fognight.json");
                fognight.loop(false);

                overcast.setImageAssetsFolder("overcast/");
                overcast.setAnimation("overcast.json");
                overcast.loop(false);

                cloudy.setImageAssetsFolder("cloudy/");
                cloudy.setAnimation("cloudy.json");
                cloudy.loop(false);

                cloudymoon.setImageAssetsFolder("cloudymoon/");
                cloudymoon.setAnimation("cloudymoon.json");
                cloudymoon.loop(false);

                rainy.setImageAssetsFolder("rainy/");
                rainy.setAnimation("rainy.json");
                rainy.loop(false);

                snowday.setImageAssetsFolder("snowday/");
                snowday.setAnimation("snowday.json");
                snowday.loop(false);

                snownight.setImageAssetsFolder("snownight/");
                snownight.setAnimation("snownight.json");
                snownight.loop(false);

                thunder.setImageAssetsFolder("thunder/");
                thunder.setAnimation("thunder.json");
                thunder.loop(false);




                linearLayout=(LinearLayout) findViewById(R.id.progressLayout);
                ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressDialog);
                //progressBar.setBackgroundColor(Color.WHITE);
                //progressBar.setProgressTintList(ColorStateList.valueOf(Color.MAGENTA));
                linearLayout.setVisibility(View.VISIBLE);

                RetrieveWeather(url);
                RetrieveForecast(loc);

                final Handler handler=new Handler();

                handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                if(!shown)
                                        Toast.makeText(Dialog_weather.this,"Network Error! PLease go back and search again..",Toast.LENGTH_LONG).show();
                        }
                },20000);
                //LoadPreference();
               // RetrieveTime(lat,lon);
               // Widget_Async();
        }

        public void visible(View view)
        {

                Animation animslide,slidedown;
                ImageView img= (ImageView) this.findViewById(R.id.foree);
                if(flag) {
                        HorizontalScrollView sc = (HorizontalScrollView) findViewById(R.id.scroll);
                        sc.setVisibility(View.GONE);
                        img.setImageResource(R.drawable.ic_action_collapse);
                        slidedown= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.swipedown);
                        sc.startAnimation(slidedown);
                        flag=false;
                }
                else
                {
                        HorizontalScrollView sc = (HorizontalScrollView) findViewById(R.id.scroll);
                        sc.setVisibility(View.VISIBLE);
                        img.setImageResource(R.drawable.ic_action_expand);
                        animslide= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.swipeup);
                        sc.startAnimation(animslide);
                        flag=true;
                }
        }

        public boolean hasNavBar()
        {
                boolean hasSoftwareKeys=true;
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        Display d = ((WindowManager) this.getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();
                        DisplayMetrics realDisplay = new DisplayMetrics();
                        d.getRealMetrics(realDisplay);

                        int realHeight=realDisplay.heightPixels;
                        int realWidth=realDisplay.widthPixels;

                        DisplayMetrics display=new DisplayMetrics();
                        d.getMetrics(display);

                        int Height=display.heightPixels;
                        int Width=display.widthPixels;
                        hasSoftwareKeys=(realWidth-Width)>0 || (realHeight-Height)>0;
                }
                else
                {
                        boolean hasMenuKey= ViewConfiguration.get(this).hasPermanentMenuKey();
                        boolean hasBackKey= KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                        hasSoftwareKeys=!hasMenuKey && !hasBackKey;
                }
                return hasSoftwareKeys;
        }

        public void setcor(String la, String lo, String id)
        {
                lat=Double.parseDouble(la);
                lon=Double.parseDouble(lo);
                String add="http://api.timezonedb.com/v2/get-time-zone?key=WM0USFMFHGKX&format=json&by=position&lat="+la+"&lng="+lo;
                Async_timezone task1=new Async_timezone(this,add);
                task1.execute(add);

        }

        public void time(String t)
        {
               // timet=t;
                String timev;
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latoitalic.ttf");
                tv=(TextView)findViewById(R.id.time_text);
                ImageView sc=(ImageView)this.findViewById(R.id.back);

                //Toast.makeText(this,t,Toast.LENGTH_SHORT).show();
                timev=t.substring(11,16);
                tim=t.substring(11,13);

                hour=Integer.parseInt(tim);
                tv.setText("Time: "+timev);
                tv.setTypeface(face);
                int actualID=Integer.parseInt(wid);
                int ID=actualID/100;

                setWeatherIcon(weatherid);
                setanimation(weatherid, hour);

                linearLayout.setVisibility(View.INVISIBLE);

                //Toast.makeText(this,tim, Toast.LENGTH_SHORT).show();
        }

        //private  void getCoordinates(String lo)
       // {
                // cor_url = "http://api.opencagedata.com/geocode/v1/json?q="+lo+"&key=4952e05e2efb2e4e52f7b0a3810641bf";
        //}


        private void RetrieveWeather(String addrs) {

                Conditionsasync task = new Conditionsasync(this, addrs);
                task.execute(addrs);
        }

       @Override
       public void onBackPressed()
       {
               finish();
       }

        public void RetrieveForecast(String loc)
        {
                String url="";
                System.out.println(loc);
                url="http://api.openweathermap.org/data/2.5/forecast/daily?q="+loc+"&appid=bcc6f8e44743e316e5120301ff1a5ad4";

            ForecastDialogAsync task= new ForecastDialogAsync(this,url);
                task.execute(url);
                shown=true;

        }

        public void SetDescription(String des)
        {
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latomedium.ttf");
                TextView view=(TextView)this.findViewById(R.id.condition);
                char c=des.charAt(0);
                c=Character.toUpperCase(c);
                des=des.substring(1,des.length());
                des=c+des;
                view.setText(des);
                view.setTypeface(face);

                int pos=des.lastIndexOf(' ');
            if(pos!=-1)
                des=des.substring(pos,des.length());
                try {
                        enc = URLEncoder.encode(des, "utf-8");
                for (int i = 0; i < enc.length(); i++) {
                    if (enc.charAt(i) == ' ') {
                        enc = enc.replace(enc.charAt(i), '+');
                    }
                }


                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                }

                //String  url = "https://pixabay.com/api/?key=" + apikey + "&q=" + enc + "&image_type=photo&category=nature&order=popular&per_page=200";
                linearLayout=(LinearLayout) findViewById(R.id.progressLayout);
                Async_weather task = new Async_weather(this, enc);

                linearLayout.setVisibility(View.VISIBLE);
                task.execute(enc);



        }
        public void SetTemperature(double temp,double min,double max)
        {
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");
                TextView view=(TextView)this.findViewById(R.id.temp_text);
                //TextView view1=(TextView)this.findViewById(R.id.hilow_text);
                DecimalFormat df=new DecimalFormat("###.#");
                String formatTemp=df.format(temp);
                String formatTemp1=df.format(min);
                String formatTemp2=df.format(max);
                view.setText(formatTemp+"\u00B0");
               // view1.setText("Hi:"+formatTemp2+" Lo:"+formatTemp1);
                view.setTypeface(face);

                //view1.setTypeface(face);
        }

        public void sharedialog(View view)
        {
                if(Build.VERSION.SDK_INT>=23)
                {
                        if(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                        {
                                takeScreenshot();
                        }
                        else
                        {
                                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                                takeScreenshot();
                        }
                }
                else
                {
                        takeScreenshot();
                }
        }


        public void takeScreenshot()
        {
                String imagePath= Environment.getExternalStorageDirectory().toString()+"/Pictures/Screenshots/"+"screenshotwn.jpg";
                View v1=getWindow().getDecorView().getRootView();
                v1.setDrawingCacheEnabled(true);
                Bitmap bitmap=v1.getDrawingCache();


                File newFile=new File(imagePath);
                FileOutputStream fos;
                try
                {
                        fos=new FileOutputStream(newFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        shareIt(imagePath);

                }
                catch (Throwable e)
                {
                        e.printStackTrace();
                }
        }

        public void setHi(double h)
        {
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");
                double max=h-273;
                DecimalFormat df=new DecimalFormat("###.#");
                String formatTempMin=df.format(max);
                TextView textView=(TextView)findViewById(R.id.hi);
                textView.setText("Day "+formatTempMin+"\u2103"+"  ");
                textView.setTypeface(face);

        }

        public void setLo(double l)
        {
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");
                double min=l-273;
                DecimalFormat df=new DecimalFormat("###.#");
                String formatTempMin=df.format(min);
                TextView textView=(TextView)findViewById(R.id.lo);
                textView.setText("Night "+formatTempMin+"\u2103");
                textView.setTypeface(face);
        }


        public void shareIt(String imagePath)
        {
                Intent shareing=new Intent(Intent.ACTION_SEND);
                shareing.setType("image/*");
                //Uri uri= getUriForFile(getApplicationContext(), "com.minimaldev.android.weathernow",imagePath);
                Uri uri=Uri.parse("file://"+imagePath);
                String text="Check out the current weather at my place! By WeatherNow - https://play.google.com/store/apps/details?id=com.minimaldev.android.weathernow ";
                shareing.putExtra(Intent.EXTRA_SUBJECT,"Check out WeatherNow by MinimalDev");
                shareing.putExtra(Intent.EXTRA_TEXT, text);
                shareing.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(Intent.createChooser(shareing,"Share weather via"));
        }




        public void SetLocation(String name, String country)  {


                //ImageView imageView =(ImageView)findViewById(R.id.favorite);
               //ImageView imageView1 =(ImageView)findViewById(R.id.notfavorite);
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");
                fullLocation=name+", "+country;
                TextView view= (TextView)this.findViewById(R.id.location_text);
                view.setText(fullLocation);
                view.setTypeface(face);



        }


        public void SetTime(String id)
        {

                //int hour=Integer.parseInt(tim);
                wid=id;
                /*int actualID=Integer.parseInt(id);
                int ID=actualID/100;
                String h=tv.getText().toString();
                String a=h.substring(16,18);
                int hr=Integer.parseInt(a); */

        }
        public void setWeatherIcon(String id)
        {


                ImageView sc = (ImageView) this.findViewById(R.id.back);

                int actualID=Integer.parseInt(id);
                int ID=actualID/100;
                switch (ID)
                {
                        case 2://for thunderstorm

                                Glide.with(this)
                                        .load(this.getResources().getIdentifier("thunder","drawable",this.getPackageName()))
                                        //.load("")
                                        //.error(R.drawable.background_)
                                        .centerCrop()
                                        .dontTransform()
                                        .crossFade()
                                        .into(sc);

                                linearLayout.setVisibility(View.INVISIBLE);
                                //view11.setBackgroundResource(R.color.thunder);

                                break;
                        case 3://for drizzle
                                if(hour>=6 && hour <18)
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("rainy","drawable",this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);
                                else
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("rainynight","drawable",this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);

                                linearLayout.setVisibility(View.INVISIBLE);

                                break;
                        case 5: //for rain
                                if(hour>=6 && hour <18)
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("rainy","drawable",this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);
                                else
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("rainynight","drawable",this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);

                                linearLayout.setVisibility(View.INVISIBLE);
                                break;
                        case 6: //for snow

                                Glide.with(this)
                                        .load(this.getResources().getIdentifier("snowy","drawable",this.getPackageName()))
                                        //.load("")
                                        //.error(R.drawable.background_)
                                        .centerCrop()
                                        .dontTransform()
                                        .crossFade()
                                        .into(sc);

                                linearLayout.setVisibility(View.INVISIBLE);

                                break;
                        case 7: //for fog

                                if(hour>=6 && hour <18)
                                {
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("dayfog","drawable",this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);

                                        linearLayout.setVisibility(View.INVISIBLE);
                                }
                                else {
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("nightfog", "drawable", this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);

                                        linearLayout.setVisibility(View.INVISIBLE);
                                }
                                break;
                        case 8: //for clear and clouds

                                if(actualID==800)
                                {
                                        //System.out.println(hour);
                                        //for clear
                                        //  view1.setImageResource(R.drawable.clear);
                                        //view11.setBackgroundResource(R.color.clear);
                                        if(hour>=5 && hour<7) {
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("sunrise", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                                linearLayout.setVisibility(View.INVISIBLE);
                                        }
                                        else if(hour>=7 && hour<17) {
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("sunday", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                                linearLayout.setVisibility(View.INVISIBLE);
                                        }
                                        else if(hour>=17 && hour<19)
                                        {
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("sunset","drawable",this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                                linearLayout.setVisibility(View.INVISIBLE);
                                        }



                                        else {
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("night", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                                linearLayout.setVisibility(View.INVISIBLE);
                                        }
                                        // linear.setBackgroundResource(R.drawable.at_day);

                                }
                                if(actualID==801 || actualID==802 || actualID==803)
                                {
                                        //for scattered clouds
                                        //view1.setImageResource(R.drawable.scattered_clouds);
                                        //view11.setBackgroundResource(R.color.clouds);
                                        if(hour>=6 && hour <18)
                                        {
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("cloudy", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                                linearLayout.setVisibility(View.INVISIBLE);
                                        }
                                        else
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("nightcloud", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                        linearLayout.setVisibility(View.INVISIBLE);
                                        //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                }
                                if(actualID==804)
                                {
                                        //for overcast
                                        //view1.setImageResource(R.drawable.overcast);
                                        //view11.setBackgroundResource(R.color.overcast);
                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("overcasttype", "drawable", this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);

                                        linearLayout.setVisibility(View.INVISIBLE);
                                        //linear.setBackgroundResource(R.drawable.overcastat_day);

                                }
                                break;
                        case 9://for extreme weather
                                if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                        Glide.with(this)
                                                .load(this.getResources().getIdentifier("thunder", "drawable", this.getPackageName()))
                                                //.load("")
                                                //.error(R.drawable.background_)
                                                .centerCrop()
                                                .dontTransform()
                                                .crossFade()
                                                .into(sc);

                                        linearLayout.setVisibility(View.INVISIBLE);
                                        //view1.setImageResource(R.drawable.tornado);
                                }
                                else
                                {
                                        if(hour>=6 && hour <18)
                                        {
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("dayfog", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                                linearLayout.setVisibility(View.INVISIBLE);
                                        }
                                        else
                                                Glide.with(this)
                                                        .load(this.getResources().getIdentifier("nightfog", "drawable", this.getPackageName()))
                                                        //.load("")
                                                        //.error(R.drawable.background_)
                                                        .centerCrop()
                                                        .dontTransform()
                                                        .crossFade()
                                                        .into(sc);

                                        linearLayout.setVisibility(View.INVISIBLE);
                                        // view1.setImageResource(R.drawable.breeze);
                                }
                                // view11.setBackgroundResource(R.color.extreme);

                                //linear.setBackgroundResource(R.drawable.extreme_weather);

                                //linear.setBackgroundResource(R.drawable.);

                                break;

                }



        }


        public void setanimation(String id, int hr)
        {
                int actualID=Integer.parseInt(id);
                int ID=actualID/100;
            System.out.println(hr);
                switch (ID)
                {
                        case 2://for thunderstorm
                                //view1.setImageResource(R.drawable.thunderstorm);
                                this.thunder.playAnimation();
                                //view11.setBackgroundResource(R.color.thunder);

                                break;
                        case 3://for drizzle
                                //view1.setImageResource(R.drawable.drizzle);
                                //view11.setBackgroundResource(R.color.drizzle);
                                this.rainy.playAnimation();
                                //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                break;
                        case 5: //for rain
                                //view1.setImageResource(R.drawable.rain);
                                //view11.setBackgroundResource(R.color.rain);
                                this.rainy.playAnimation();
                                //linear.setBackgroundResource(R.drawable.rainat_night);

                                break;
                        case 6: //for snowy

                                if(hr >=6 && hr <18)
                                {
                                        this.snowday.playAnimation();
                                }
                                else
                                        this.snownight.playAnimation();
                                //view1.setImageResource(R.drawable.snowy);
                                //view11.setBackgroundResource(R.color.snowy);

                                //linear.setBackgroundResource(R.drawable.snowat_night);

                                break;
                        case 7: //for foggy
                                //view1.setImageResource(R.drawable.foggy);
                                //view11.setBackgroundResource(R.color.foggy);
                                if(hr >=6 && hr <18)
                                {
                                        this.fogday.playAnimation();
                                }
                                else
                                        this.fognight.playAnimation();
                                //linear.setBackgroundResource(R.drawable.fogat_day);
                                break;
                        case 8: //for sunnyday and clouds

                                if(actualID==800)
                                {
                                        //for sunnyday
                                        //  view1.setImageResource(R.drawable.sunnyday);
                                        //view11.setBackgroundResource(R.color.sunnyday);
                                        if(hr >=6 && hr <18)
                                                this.animationView.playAnimation();
                                        else
                                                this.moonView.playAnimation();
                                        // linear.setBackgroundResource(R.drawable.clearat_day);

                                }
                                if(actualID==801 || actualID==802 || actualID==803)
                                {
                                        //for scattered clouds
                                        //view1.setImageResource(R.drawable.scattered_clouds);
                                        //view11.setBackgroundResource(R.color.clouds);
                                        if(hr >=6 && hr <18)
                                        {
                                                this.cloudy.playAnimation();
                                        }
                                        else
                                                this.cloudymoon.playAnimation();
                                        //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                }
                                if(actualID==804)
                                {
                                        //for overcasttype
                                        //view1.setImageResource(R.drawable.overcasttype);
                                        //view11.setBackgroundResource(R.color.overcasttype);
                                        this.overcast.playAnimation();
                                        //linear.setBackgroundResource(R.drawable.overcastat_day);

                                }
                                break;
                        case 9://for extreme weather
                                if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                        this.thunder.playAnimation();
                                        //view1.setImageResource(R.drawable.tornado);
                                }
                                else
                                {
                                        if(hr >=6 && hr <18)
                                        {
                                                this.fogday.playAnimation();
                                        }
                                        else
                                                this.fognight.playAnimation();
                                        // view1.setImageResource(R.drawable.breeze);
                                }
                                // view11.setBackgroundResource(R.color.extreme);

                                //linear.setBackgroundResource(R.drawable.extreme_weather);

                                //linear.setBackgroundResource(R.drawable.);

                                break;

                }
        }


        public void SetDay(String day, int dayNo)

        {
                String d=day.substring(0,3);
                d=d.toUpperCase();
                //d=d+",";
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");
                TextView view1 = (TextView) findViewById(R.id.day11);
                TextView view2 = (TextView) findViewById(R.id.day12);
                TextView view3 = (TextView) findViewById(R.id.day13);
                TextView view4 = (TextView) findViewById(R.id.day14);
                TextView view5 = (TextView) findViewById(R.id.day15);
                TextView view6 = (TextView) findViewById(R.id.day16);
                //TextView view7=(TextView)findViewById(R.id.temptext_sun);
                switch (dayNo) {
                        case 1:
                                view1.setText(d);
                                view1.setTypeface(face);
                                //view11.setText(t);
                                break;
                        case 2:
                                view2.setText(d);
                                view2.setTypeface(face);
                                //view22.setText(t);
                                break;
                        case 3:
                                view3.setText(d);
                                view3.setTypeface(face);
                                // view33.setText(t);
                                break;
                        case 4:
                                view4.setText(d);
                                view4.setTypeface(face);
                                // view44.setText(t);
                                break;
                        case 5:
                                view5.setText(d);
                                view5.setTypeface(face);
                                // view55.setText(t);
                                break;
                        case 6:
                                view6.setText(d);
                                view6.setTypeface(face);
                                // view66.setText(t);
                                break;
                        //case 7: view7.setText(day);
                        // break;}
                }
        }


        public void SetDes(String des, int index)
        {
                int actualID=Integer.parseInt(des);
                int ID=actualID/100;

                ImageView view1=(ImageView)findViewById(R.id.icon11);
                ImageView view2=(ImageView)findViewById(R.id.icon12);
                ImageView view3=(ImageView)findViewById(R.id.icon13);
                ImageView view4=(ImageView)findViewById(R.id.icon14);
                ImageView view5=(ImageView)findViewById(R.id.icon15);
                ImageView view6=(ImageView)findViewById(R.id.icon16);
                //ImageView view7=(ImageView)findViewById(R.id.iconweather_sun);
                if(index==0)
                {
                        switch (ID)
                        {
                                case 2://for thunderstorm
                                        view1.setImageResource(R.drawable.thunderstorm);
                                        //view11.setBackgroundResource(R.color.thunder);

                                        break;
                                case 3://for drizzle
                                        view1.setImageResource(R.drawable.drizzle);
                                        //view11.setBackgroundResource(R.color.drizzle);

                                        //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                        break;
                                case 5: //for rain
                                        view1.setImageResource(R.drawable.rain);
                                        //view11.setBackgroundResource(R.color.rain);

                                        //linear.setBackgroundResource(R.drawable.rainat_night);

                                        break;
                                case 6: //for snowy

                                        view1.setImageResource(R.drawable.snow);
                                        //view11.setBackgroundResource(R.color.snowy);

                                        //linear.setBackgroundResource(R.drawable.snowat_night);

                                        break;
                                case 7: //for foggy
                                        view1.setImageResource(R.drawable.fog);
                                        //view11.setBackgroundResource(R.color.foggy);

                                        //linear.setBackgroundResource(R.drawable.fogat_day);
                                        break;
                                case 8: //for sunnyday and clouds

                                        if(actualID==800)
                                        {
                                                //for sunnyday
                                                view1.setImageResource(R.drawable.clear);
                                                //view11.setBackgroundResource(R.color.sunnyday);

                                                // linear.setBackgroundResource(R.drawable.clearat_day);

                                        }
                                        if(actualID==801 || actualID==802 || actualID==803)
                                        {
                                                //for scattered clouds
                                                view1.setImageResource(R.drawable.scattered_clouds);
                                                //view11.setBackgroundResource(R.color.clouds);

                                                //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                        }
                                        if(actualID==804)
                                        {
                                                //for overcasttype
                                                view1.setImageResource(R.drawable.overcast);
                                                //view11.setBackgroundResource(R.color.overcasttype);

                                                //linear.setBackgroundResource(R.drawable.overcastat_day);

                                        }
                                        break;
                                case 9://for extreme weather
                                        if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                                view1.setImageResource(R.drawable.tornado);
                                        }
                                        else
                                        {
                                                view1.setImageResource(R.drawable.breeze);
                                        }                                                           // view11.setBackgroundResource(R.color.extreme);

                                        //linear.setBackgroundResource(R.drawable.extreme_weather);

                                        //linear.setBackgroundResource(R.drawable.);

                                        break;

                        }
                }
                else if(index==1)
                {
                        switch (ID)
                        {
                                case 2://for thunderstorm
                                        view2.setImageResource(R.drawable.thunderstorm);
                                        //view22.setBackgroundResource(R.color.thunder);

                                        //linear.setBackgroundResource(R.drawable.thunder);

                                        break;
                                case 3://for drizzle
                                        view2.setImageResource(R.drawable.drizzle);
                                        //view22.setBackgroundResource(R.color.drizzle);

                                        //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                        break;
                                case 5: //for rain
                                        view2.setImageResource(R.drawable.rain);
                                        //view22.setBackgroundResource(R.color.rain);

                                        //linear.setBackgroundResource(R.drawable.rainat_night);

                                        break;
                                case 6: //for snowy

                                        view2.setImageResource(R.drawable.snow);
                                        //view22.setBackgroundResource(R.color.snowy);

                                        //linear.setBackgroundResource(R.drawable.snowat_night);

                                        break;
                                case 7: //for foggy
                                        view2.setImageResource(R.drawable.fog);
                                        //view22.setBackgroundResource(R.color.foggy);

                                        //linear.setBackgroundResource(R.drawable.fogat_day);
                                        break;
                                case 8: //for sunnyday and clouds

                                        if(actualID==800)
                                        {
                                                //for sunnyday
                                                view2.setImageResource(R.drawable.clear);
                                                //view22.setBackgroundResource(R.color.sunnyday);

                                                // linear.setBackgroundResource(R.drawable.clearat_day);

                                        }
                                        if(actualID==801 || actualID==802 || actualID==803)
                                        {
                                                //for scattered clouds
                                                view2.setImageResource(R.drawable.scattered_clouds);
                                                //view22.setBackgroundResource(R.color.clouds);

                                                //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                        }
                                        if(actualID==804)
                                        {
                                                //for overcasttype
                                                view2.setImageResource(R.drawable.overcast);
                                                //view22.setBackgroundResource(R.color.overcasttype);

                                                //linear.setBackgroundResource(R.drawable.overcastat_day);

                                        }
                                        break;
                                case 9://for extreme weather
                                        if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                                view2.setImageResource(R.drawable.tornado);
                                        }
                                        else
                                        {
                                                view2.setImageResource(R.drawable.breeze);
                                        }                                                           //view22.setBackgroundResource(R.color.extreme);

                                        //linear.setBackgroundResource(R.drawable.extreme_weather);

                                        //linear.setBackgroundResource(R.drawable.);

                                        break;

                        }
                }
                else if(index==2)
                {
                        switch (ID)
                        {
                                case 2://for thunderstorm
                                        view3.setImageResource(R.drawable.thunderstorm);
                                        //view33.setBackgroundResource(R.color.thunder);

                                        //linear.setBackgroundResource(R.drawable.thunder);

                                        break;
                                case 3://for drizzle
                                        view3.setImageResource(R.drawable.drizzle);
                                        //view33.setBackgroundResource(R.color.drizzle);

                                        //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                        break;
                                case 5: //for rain
                                        view3.setImageResource(R.drawable.rain);
                                        //view33.setBackgroundResource(R.color.rain);

                                        //linear.setBackgroundResource(R.drawable.rainat_night);

                                        break;
                                case 6: //for snowy

                                        view3.setImageResource(R.drawable.snow);
                                        // view33.setBackgroundResource(R.color.snowy);

                                        //linear.setBackgroundResource(R.drawable.snowat_night);

                                        break;
                                case 7: //for foggy
                                        view3.setImageResource(R.drawable.fog);
                                        //view33.setBackgroundResource(R.color.foggy);

                                        //linear.setBackgroundResource(R.drawable.fogat_day);
                                        break;
                                case 8: //for sunnyday and clouds

                                        if(actualID==800)
                                        {
                                                //for sunnyday
                                                view3.setImageResource(R.drawable.clear);
                                                // view33.setBackgroundResource(R.color.sunnyday);

                                                // linear.setBackgroundResource(R.drawable.clearat_day);

                                        }
                                        if(actualID==801 || actualID==802 || actualID==803)
                                        {
                                                //for scattered clouds
                                                view3.setImageResource(R.drawable.scattered_clouds);
                                                //view33.setBackgroundResource(R.color.clouds);

                                                //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                        }
                                        if(actualID==804)
                                        {
                                                //for overcasttype
                                                view3.setImageResource(R.drawable.overcast);
                                                //view33.setBackgroundResource(R.color.overcasttype);

                                                //linear.setBackgroundResource(R.drawable.overcastat_day);

                                        }
                                        break;
                                case 9://for extreme weather
                                        if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                                view3.setImageResource(R.drawable.tornado);
                                        }
                                        else
                                        {
                                                view3.setImageResource(R.drawable.breeze);
                                        }                                                           // view33.setBackgroundResource(R.color.extreme);

                                        //linear.setBackgroundResource(R.drawable.extreme_weather);

                                        //linear.setBackgroundResource(R.drawable.);

                                        break;

                        }
                }
                else if(index==3)
                {
                        switch (ID)
                        {
                                case 2://for thunderstorm
                                        view4.setImageResource(R.drawable.thunderstorm);
                                        //view44.setBackgroundResource(R.color.thunder);

                                        //linear.setBackgroundResource(R.drawable.thunder);

                                        break;
                                case 3://for drizzle
                                        view4.setImageResource(R.drawable.drizzle);
                                        //view44.setBackgroundResource(R.color.drizzle);

                                        //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                        break;
                                case 5: //for rain
                                        view4.setImageResource(R.drawable.rain);
                                        //view44.setBackgroundResource(R.color.rain);

                                        //linear.setBackgroundResource(R.drawable.rainat_night);

                                        break;
                                case 6: //for snowy

                                        view4.setImageResource(R.drawable.snow);
                                        //iew44.setBackgroundResource(R.color.snowy);

                                        //linear.setBackgroundResource(R.drawable.snowat_night);

                                        break;
                                case 7: //for foggy
                                        view4.setImageResource(R.drawable.fog);
                                        //view44.setBackgroundResource(R.color.foggy);

                                        //linear.setBackgroundResource(R.drawable.fogat_day);
                                        break;
                                case 8: //for sunnyday and clouds

                                        if(actualID==800)
                                        {
                                                //for sunnyday
                                                view4.setImageResource(R.drawable.clear);
                                                //view44.setBackgroundResource(R.color.sunnyday);

                                                // linear.setBackgroundResource(R.drawable.clearat_day);

                                        }
                                        if(actualID==801 || actualID==802 || actualID==803)
                                        {
                                                //for scattered clouds
                                                view4.setImageResource(R.drawable.scattered_clouds);
                                                //view44.setBackgroundResource(R.color.clouds);

                                                //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                        }
                                        if(actualID==804)
                                        {
                                                //for overcasttype
                                                view4.setImageResource(R.drawable.overcast);
                                                //view44.setBackgroundResource(R.color.overcasttype);

                                                //linear.setBackgroundResource(R.drawable.overcastat_day);

                                        }
                                        break;
                                case 9://for extreme weather
                                        if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                                view4.setImageResource(R.drawable.tornado);
                                        }
                                        else
                                        {
                                                view4.setImageResource(R.drawable.breeze);
                                        }                                                           //view44.setBackgroundResource(R.color.extreme);

                                        //linear.setBackgroundResource(R.drawable.extreme_weather);

                                        //linear.setBackgroundResource(R.drawable.);

                                        break;

                        }
                }
                else if(index==4)
                {
                        switch (ID)
                        {
                                case 2://for thunderstorm
                                        view5.setImageResource(R.drawable.thunderstorm);
                                        //view55.setBackgroundResource(R.color.thunder);

                                        //linear.setBackgroundResource(R.drawable.thunder);

                                        break;
                                case 3://for drizzle
                                        view5.setImageResource(R.drawable.drizzle);
                                        // view55.setBackgroundResource(R.color.drizzle);

                                        //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                        break;
                                case 5: //for rain
                                        view5.setImageResource(R.drawable.rain);
                                        //view55.setBackgroundResource(R.color.rain);

                                        //linear.setBackgroundResource(R.drawable.rainat_night);

                                        break;
                                case 6: //for snowy

                                        view5.setImageResource(R.drawable.snow);
                                        //view55.setBackgroundResource(R.color.snowy);

                                        //linear.setBackgroundResource(R.drawable.snowat_night);

                                        break;
                                case 7: //for foggy
                                        view5.setImageResource(R.drawable.fog);
                                        //view55.setBackgroundResource(R.color.foggy);

                                        //linear.setBackgroundResource(R.drawable.fogat_day);
                                        break;
                                case 8: //for sunnyday and clouds

                                        if(actualID==800)
                                        {
                                                //for sunnyday
                                                view5.setImageResource(R.drawable.clear);
                                                //view55.setBackgroundResource(R.color.sunnyday);

                                                // linear.setBackgroundResource(R.drawable.clearat_day);

                                        }
                                        if(actualID==801 || actualID==802 || actualID==803)
                                        {
                                                //for scattered clouds
                                                view5.setImageResource(R.drawable.scattered_clouds);
                                                //view55.setBackgroundResource(R.color.clouds);

                                                //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                        }
                                        if(actualID==804)
                                        {
                                                //for overcasttype
                                                view5.setImageResource(R.drawable.overcast);
                                                //view55.setBackgroundResource(R.color.overcasttype);

                                                //linear.setBackgroundResource(R.drawable.overcastat_day);

                                        }
                                        break;
                                case 9://for extreme weather
                                        if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                                view5.setImageResource(R.drawable.tornado);
                                        }
                                        else
                                        {
                                                view5.setImageResource(R.drawable.breeze);
                                        }                                                           //view55.setBackgroundResource(R.color.extreme);

                                        //linear.setBackgroundResource(R.drawable.extreme_weather);

                                        //linear.setBackgroundResource(R.drawable.);

                                        break;

                        }
                }
                else
                {
                        switch (ID)
                        {
                                case 2://for thunderstorm
                                        view6.setImageResource(R.drawable.thunderstorm);
                                        //view66.setBackgroundResource(R.color.thunder);

                                        //linear.setBackgroundResource(R.drawable.thunder);

                                        break;
                                case 3://for drizzle
                                        view6.setImageResource(R.drawable.drizzle);
                                        //view66.setBackgroundResource(R.color.drizzle);

                                        //linear.setBackgroundResource(R.drawable.drizzleat_day);

                                        break;
                                case 5: //for rain
                                        view6.setImageResource(R.drawable.rain);
                                        //view66.setBackgroundResource(R.color.rain);

                                        //linear.setBackgroundResource(R.drawable.rainat_night);

                                        break;
                                case 6: //for snowy

                                        view6.setImageResource(R.drawable.snow);
                                        //view66.setBackgroundResource(R.color.snowy);

                                        //linear.setBackgroundResource(R.drawable.snowat_night);

                                        break;
                                case 7: //for foggy
                                        view6.setImageResource(R.drawable.fog);
                                        //view66.setBackgroundResource(R.color.foggy);

                                        //linear.setBackgroundResource(R.drawable.fogat_day);
                                        break;
                                case 8: //for sunnyday and clouds

                                        if(actualID==800)
                                        {
                                                //for sunnyday
                                                view6.setImageResource(R.drawable.clear);
                                                //view66.setBackgroundResource(R.color.sunnyday);

                                                // linear.setBackgroundResource(R.drawable.clearat_day);

                                        }
                                        if(actualID==801 || actualID==802 || actualID==803)
                                        {
                                                //for scattered clouds
                                                view6.setImageResource(R.drawable.scattered_clouds);
                                                //view66.setBackgroundResource(R.color.clouds);

                                                //linear.setBackgroundResource(R.drawable.scatteredat_day);

                                        }
                                        if(actualID==804)
                                        {
                                                //for overcasttype
                                                view6.setImageResource(R.drawable.overcast);
                                                // view66.setBackgroundResource(R.color.overcasttype);

                                                //linear.setBackgroundResource(R.drawable.overcastat_day);

                                        }
                                        break;
                                case 9://for extreme weather
                                        if(actualID == 900 || actualID == 901|| actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {

                                                view6.setImageResource(R.drawable.tornado);
                                        }
                                        else
                                        {
                                                view6.setImageResource(R.drawable.breeze);
                                        }                                                           //view66.setBackgroundResource(R.color.extreme);

                                        //linear.setBackgroundResource(R.drawable.extreme_weather);

                                        //linear.setBackgroundResource(R.drawable.);

                                        break;

                        }
                }

        }

        public void SetTemp(double min,double max, int index)
        {
                min=min-273;
                max=max-273;
                Typeface face= Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

                DecimalFormat df=new DecimalFormat("###.#");
                String formatTempMin=df.format(min);
                String formatTempMax=df.format(max);
                formatTempMin=formatTempMin+"\u2103"+"/";
                formatTempMax=formatTempMax+"\u2103";
                String t=formatTempMin+formatTempMax;
                TextView view1=(TextView)findViewById(R.id.temp11);
                TextView view2=(TextView)findViewById(R.id.temp12);
                TextView view3=(TextView)findViewById(R.id.temp13);
                TextView view4=(TextView)findViewById(R.id.temp14);
                TextView view5=(TextView)findViewById(R.id.temp15);
                TextView view6=(TextView)findViewById(R.id.temp16);
                switch (index)
                {
                        case 0: view1.setText(t);
                                view1.setTypeface(face);
                                // view11.setText(formatTempMax);
                                break;
                        case 1: view2.setText(t);
                                view2.setTypeface(face);
                                //view22.setText(formatTempMax);
                                break;
                        case 2: view3.setText(t);
                                view3.setTypeface(face);
                                // view33.setText(formatTempMax);
                                break;
                        case 3: view4.setText(t);
                                view4.setTypeface(face);
                                // view44.setText(formatTempMax);
                                break;
                        case 4: view5.setText(t);
                                view5.setTypeface(face);
                                // view55.setText(formatTempMax);
                                break;
                        case 5: view6.setText(t);
                                view6.setTypeface(face);
                                //view66.setText(formatTempMax);
                                break;
                        //case 6: view7.setText(formatTemp/);
                        //  break;

                }



        }


        public void favit(View view) {

                //setContentView(R.layout.fav_display);

                //this.addfav();

                //Toast.makeText(this, fullLocation+" added to favorites !",Toast.LENGTH_SHORT).show();
                snackbar=Snackbar.make(findViewById(R.id.coordialog),"Added to Favourites",Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.clear));
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.thunder));
                snackbar.setAction("View", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                                Intent intent = new Intent(Dialog_weather.this, DisplayFav.class);
                                intent.putExtra("LOCATION_NAME", fullLocation);
                                intent.putExtra("COUNTER_VALUE", countfav);

                                startActivity(intent);
                                countfav++;
                                //finish();

                        }
                });


                textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.Magenta));
                textView.setTextSize(14);

                snackbar.show();
        }


        public void onResume(ArrayAdapter adapter)
        {
                super.onResume();
                SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
                String prev=preferences.getString("label","");
                if(!TextUtils.isEmpty(prev))
                 list.add(fullLocation);
                 this.adapter.notifyDataSetChanged();



        }


        @Override
        public void onClick(View v) {
                //null
        }



        public class Conditionsasync extends AsyncTask<String,Void,String> {

                // private Context context;
                String new_url;
                String lat,lon;
                String fullLocation;
                private Dialog_weather DialogWeather;
                private ProgressDialog dialog;

                ArrayList<String> arr=new ArrayList<>();
                String text="";

                public  Conditionsasync(Dialog_weather dialogWeather, String url) {

                        this.DialogWeather = dialogWeather;
                        //dialog=new ProgressDialog(dialogWeather);
                        new_url=url;
                }

                @Override
                protected void onPreExecute()
                {

                }


                @Override
                protected String doInBackground(String ... urls)  {
                        // this weather service method will be called after the service executes.
                        // it will run as a seperate process, and will populate the activity in the onPostExecute
                        // method below



                        String response="";
                        try {
                                HttpURLConnection con = (HttpURLConnection) ( new URL(new_url)).openConnection();
                                con.setRequestMethod("POST");
                                con.setDoInput(true);
                                con.setDoOutput(true);
                                con.connect();
                                //HttpResponse execute = client.execute(httpGet);
                                InputStream content = (InputStream) con.getContent();
                                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                                String s = "";
                                while ((s = buffer.readLine()) != null) {
                                        response = response+s;
                                }
                                con.disconnect();
                        }
                        catch (Exception e) {
                                e.printStackTrace();
                        }
                        return  response;
                }



                @Override
                public void onPostExecute(String result)
                {

                        String test=result;

                        if(test==null)
                        {
                                Toast.makeText(this.DialogWeather,"Error fetching weather",Toast.LENGTH_SHORT).show();
                        }

                        try {
// parse the json result returned from the service
                                JSONObject jsonResult = new JSONObject(test);
//parse out the co-ordinates of location
                                JSONObject coord= jsonResult.getJSONObject("coord");
                                lat= coord.getString("lat");
                                lon= coord.getString("lon");

// parse out the temperature from the JSON result
                                double temperature = jsonResult.getJSONObject("main").getDouble("temp");
                                temperature = ConvertTemperatureToFarenheit(temperature);

                                // parse out the pressure from the JSON Result
                                double pressure = jsonResult.getJSONObject("main").getDouble("pressure");

                                double min=jsonResult.getJSONObject("main").getDouble("temp_min");
                                double max=jsonResult.getJSONObject("main").getDouble("temp_max");

                                min= ConvertTemperatureToFarenheit(min);
                                max= ConvertTemperatureToFarenheit(max);

// parse out the humidity from the JSON result
                                double humidity = jsonResult.getJSONObject("main").getDouble("humidity");

// parse out the description from the JSON result
                                String description = jsonResult.getJSONArray("weather").getJSONObject(0).getString("description");
//parse out weather id
                                String id=jsonResult.getJSONArray("weather").getJSONObject(0).getString("id");

                                //parse out city name
                                String locate=jsonResult.getString("name");
                                //parse out country
                                String cntry=jsonResult.getJSONObject("sys").getString("country");
                                //get date and time
                                //String dateTime=jsonResult.getString("dt");
                                //long dy=Long.parseLong(dateTime);
                                //Date date=new Date(dy*1000L);
                                //SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HHMM");
                                //String fdate=simpleDateFormat.format(date);





// set all the fields in the activity from the parsed JSON
                                this.DialogWeather.SetDescription(description);
                                this.DialogWeather.SetTemperature(temperature,min,max);
                                //this.DialogWeather.SetPressure(pressure);
                                //this.DialogWeather.SetHumidity(humidity);
                                this.DialogWeather.SetLocation(locate,cntry);
                                this.DialogWeather.SetTime(id);
                                this.DialogWeather.setcor(lat,lon, id);
                                //this.DialogWeather.setWeatherIcon(id);
                                //this.DialogWeather.time(dateTime);

                                DialogWeather.weatherid=id;

                                //this.DialogWeather.setanimation(id);



                        }
                        catch (JSONException e) {
                                e.printStackTrace();
                        }

                }
                private double ConvertTemperatureToFarenheit(double temperature) {
                        return (temperature - 273);
                }


        }


}

