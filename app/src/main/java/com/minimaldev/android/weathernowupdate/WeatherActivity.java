package com.minimaldev.android.weathernowupdate;
/*

********WeatherNow 12-16*********

DEVELOPED WITH LOVE BY MINIMALDEV

 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;

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
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GestureDetectorCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.preference.PreferenceManager.setDefaultValues;





public class WeatherActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation, mCurrentLocation;

    private GoogleApiClient mGoogleApiClient;

    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    private Context context;


    RelativeLayout llayout;

    final String channelid = "com.minimaldev.android.weathernowupdate";

    Snackbar snackbar, snackbarnetwork, snackbarboth, snackrefresh;

    String la, lo, enc;

    boolean shown;

    String apikey = "6024521-aa3caf3e11ed4bf5eead80356";

    //String secret="WHjf5GVFChvtUFsxYpBv4XbaYK7Mb5BNpYfN3t3FhQzKc";

    static String description;

    //String filename = "WN_FAVPLACES";

    static final int MIN_DISTANCE = 100;

    private float downX, downY, upX, upY;

    private Activity activity;

    private ActionBar actionBar;

    String notiloc = "";

    String tempformat = "";

    String notidesc = "";

    int locationflag = 0;

    ArrayList<String> arrayList = new ArrayList<String>();

    private GestureDetectorCompat gestureDetectorCompat;

    public boolean flag = false;

    SwipeRefreshLayout swipeRefreshLayout;

    BufferedReader bufferedReader;

    HorizontalScrollView horizontalScrollView;

    private static final String TAG = WeatherActivity.class.getSimpleName();

    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    private static final int RESULT_SETTINGS = 1;

    private FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    LocationCallback locationCallback;

    NotificationManager notificationManager;

    int count = 0;

    int indexed = 0;

    SharedPreferences sharedPreferences;

    // Location updates intervals in sec

    private static int UPDATE_INTERVAL = 10000; // 10 sec

    private static int FATEST_INTERVAL = 5000; // 5 sec

    private static int DISPLACEMENT = 10; // 10 meters

    private static int SWIPE_MIN_DISTANCE = 120;

    private static int SWIPE_MAX_OFF_PATH = 120;

    private static int SWIPE_THRESHOLD_VELOCITY = 120;

    private double latitude, longitude;

    PlacesAutocompleteTextView placesAutocompleteTextView;

    private static final int PERMISSION_REQUEST_CODE = 1;

    String DESCRIPTION, LOC, W_ID;

    LocationManager lm;


    Bitmap bitmap;

    DisplayFav displayFav = new DisplayFav();

    //GestureDetectorCompat mDetector;


    double TEM;

    LottieAnimationView animationView, moonView, cloudy, cloudymoon, fogday, fognight, snowday, snownight, thunder, rainy, overcast;


    @Override

    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        hasNavBar();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window w = getWindow(); // in Activity's onCreate() for instance


            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        }

        if (hasNavBar()) {

            setContentView(R.layout.activity_main);


        } else {

            setContentView(R.layout.activity_mainnonavbar);


        }


        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.scroll);

        horizontalScrollView.setVisibility(View.INVISIBLE);

        work();


    }


    @SuppressLint("MissingPermission")
    public void work() {

        boolean enabled = locationEnabled();


        lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);





        sharedPreferences = WeatherActivity.this.getSharedPreferences("location", Context.MODE_PRIVATE);


        System.out.println((sharedPreferences.contains("lat") && sharedPreferences.contains("lon")));


        shown = false;


        snackbar = Snackbar.make(findViewById(R.id.coormain), "Location not enabled", Snackbar.LENGTH_LONG);

        snackbarnetwork = Snackbar.make(findViewById(R.id.coormain), "Network not available", Snackbar.LENGTH_LONG);

        snackbarnetwork = Snackbar.make(findViewById(R.id.coormain), "Network not available", Snackbar.LENGTH_LONG);

        snackbarboth = Snackbar.make(findViewById(R.id.coormain), "Location and Network not available", Snackbar.LENGTH_LONG);

        snackrefresh = Snackbar.make(findViewById(R.id.coormain), "Refreshing", Snackbar.LENGTH_LONG);


        //snackrefresh.setDuration((int) TimeUnit.MINUTES.toMillis(1/4));


        snackbar.setActionTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.Magenta));

        snackbarnetwork.setActionTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.Magenta));

        snackbarboth.setActionTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.Magenta));


        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thunder));

        snackbarnetwork.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thunder));

        snackbarboth.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thunder));

        snackrefresh.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.thunder));


        TextView textView;

        textView = (TextView) snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

        textView.setTextSize(14);


        textView = (TextView) snackbarnetwork.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

        textView.setTextSize(14);


        textView = (TextView) snackbarboth.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

        textView.setTextSize(14);


        textView = (TextView) snackrefresh.getView().findViewById(com.google.android.material.R.id.snackbar_text);

        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

        textView.setTextSize(14);


        snackbar.setAction("Enable", new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                startActivity(intent);


            }

        });


        snackbarnetwork.setAction("Check", new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);

                startActivity(intent);

            }

        });


        snackbarboth.setAction("Check", new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);

                Intent intentnew = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                startActivity(intent);

                startActivity(intentnew);

            }

        });


        animationView = (LottieAnimationView) findViewById(R.id.animation_view);

        moonView = (LottieAnimationView) findViewById(R.id.moon_view);

        cloudy = (LottieAnimationView) findViewById(R.id.cloud_view);

        cloudymoon = (LottieAnimationView) findViewById(R.id.cloudmoon_view);

        fogday = (LottieAnimationView) findViewById(R.id.fogday_view);

        fognight = (LottieAnimationView) findViewById(R.id.fognight_view);

        overcast = (LottieAnimationView) findViewById(R.id.overcast_view);

        rainy = (LottieAnimationView) findViewById(R.id.rainy_view);

        snowday = (LottieAnimationView) findViewById(R.id.snowday_view);

        snownight = (LottieAnimationView) findViewById(R.id.snownight_view);

        thunder = (LottieAnimationView) findViewById(R.id.thunder_view);


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

        thunder.setAnimation("thundering.json");

        thunder.loop(false);


        llayout = (RelativeLayout) findViewById(R.id.progresslayout);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

        llayout.setVisibility(View.VISIBLE);


        setDefaultValues(WeatherActivity.this, R.xml.settings, false);


        if (!sharedPreferences.contains("lat") || !sharedPreferences.contains("lon")) {

            Toast.makeText(WeatherActivity.this, "Fetching location for the first time might take some time. Be patient :)", Toast.LENGTH_LONG).show();


            if (!enabled) {


                snackbar.show();

            } else if (!isNetworkAvailable()) {

                snackbarnetwork.show();

            } else {

                System.out.println("At else part of work()");
                showweather();
            }


            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshswipe);

            swipeRefreshLayout.setColorSchemeResources(R.color.Magenta);

            swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

            swipeRefreshLayout.setProgressViewOffset(false, 100, 200);


            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override

                public void onRefresh() {

                    //buildGoogleApiClient();

                    //createLocationRequest();

                    if (!isNetworkAvailable() && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                        snackbar.show();

                        swipeRefreshLayout.setRefreshing(false);

                    } else {

                        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isNetworkAvailable()) {


                            snackbarboth.show();

                            swipeRefreshLayout.setRefreshing(false);


                        } else if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                            snackbar.show();

                            swipeRefreshLayout.setRefreshing(false);

                        } else if (!isNetworkAvailable()) {

                            snackbarnetwork.show();

                            swipeRefreshLayout.setRefreshing(false);

                        } else {

                            //snackrefresh.show();

                            if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                // TODO: Consider calling

                                //    ActivityCompat#requestPermissions

                                // here to request the missing permissions, and then overriding

                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

                                //                                          int[] grantResults)

                                // to handle the case where the user grants the permission. See the documentation

                                // for ActivityCompat#requestPermissions for more details.

                                return;

                            }

                            //resetanim();

                            showweather();

                            final Handler handler = new Handler();


                            handler.postDelayed(new Runnable() {

                                @Override

                                public void run() {

                                    if (!shown) {

                                        Toast.makeText(WeatherActivity.this, "Network Error! Please swipe down to Refresh.", Toast.LENGTH_LONG).show();

                                        swipeRefreshLayout.setRefreshing(false);

                                    }

                                }

                            }, 10000);


                        }


                    }


                    //


                }


            });


            //hasNavBar();


            //this.dialog.setTitle("Please wait");

            //this.dialog.setMessage("Fetching beautiful weather...");

            //this.dialog.show();

            //this.dialog.setCanceledOnTouchOutside(false);


            //RetrieveWeather(LAT, LON);


        } else {


            SharedPreferences sp = WeatherActivity.this.getSharedPreferences("location", Context.MODE_PRIVATE);

            final String ll = sp.getString("lat", "");

            final String lon = sp.getString("lon", "");


            System.out.println("At elseif part " + ll + " " + lon);

            if (ll.length() == 0 || lon.length() == 0)

            {

                System.out.println("At if part");


                if (!enabled) {


                    snackbar.show();

                } else if (!isNetworkAvailable()) {

                    snackbarnetwork.show();

                } else {

                   // resetanim();

                    showweather();

                }


                swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshswipe);

                swipeRefreshLayout.setColorSchemeResources(R.color.Magenta);

                swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

                swipeRefreshLayout.setProgressViewOffset(false, 100, 200);


                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override

                    public void onRefresh() {

                        //buildGoogleApiClient();

                        //createLocationRequest();

                        if (!isNetworkAvailable() && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            snackbar.show();

                            swipeRefreshLayout.setRefreshing(false);

                        } else {

                            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isNetworkAvailable()) {


                                snackbarboth.show();

                                swipeRefreshLayout.setRefreshing(false);


                            } else if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                                //snackbar.show();

                                //resetanim();

                                RetrieveWeather(ll, lon, "a");

                                RetrieveForecast(ll, lon);


                            } else if (!isNetworkAvailable()) {

                                snackbarnetwork.show();

                                swipeRefreshLayout.setRefreshing(false);

                            } else {

                                //snackrefresh.show();

                                if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    // TODO: Consider calling

                                    //    ActivityCompat#requestPermissions

                                    // here to request the missing permissions, and then overriding

                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

                                    //                                          int[] grantResults)

                                    // to handle the case where the user grants the permission. See the documentation

                                    // for ActivityCompat#requestPermissions for more details.

                                    return;

                                }

                                //resetanim();

                                showweather();

                                //RetrieveWeather(ll, lon, "a");

                                //RetrieveForecast(ll, lon);

                                final Handler handler = new Handler();


                                handler.postDelayed(new Runnable() {

                                    @Override

                                    public void run() {

                                        if (!shown) {

                                            Toast.makeText(WeatherActivity.this, "Network Error! Please swipe down to Refresh.", Toast.LENGTH_LONG).show();

                                            swipeRefreshLayout.setRefreshing(false);

                                        }

                                    }

                                }, 10000);


                            }


                        }


                        //


                    }


                });

            } else


            {


                SharedPreferences spp = this.getSharedPreferences("location", Context.MODE_PRIVATE);

                final String lll = spp.getString("lat", "");

                final String lonn = spp.getString("lon", "");

                swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshswipe);

                swipeRefreshLayout.setColorSchemeResources(R.color.Magenta);

                swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

                swipeRefreshLayout.setProgressViewOffset(false, 100, 200);


                //resetanim();

                RetrieveWeather(lll, lonn, "a");

                RetrieveForecast(lll, lonn);


                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                    @Override

                    public void onRefresh() {

                        //buildGoogleApiClient();

                        //createLocationRequest();

                        if (!isNetworkAvailable() && !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            snackbarboth.show();

                            swipeRefreshLayout.setRefreshing(false);

                        } else {

                            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                                //snackbar.show();

                                //resetanim();

                                RetrieveWeather(lll, lonn, "a");

                                RetrieveForecast(lll, lonn);

                                //swipeRefreshLayout.setRefreshing(false);

                            } else if (!isNetworkAvailable()) {

                                snackbarnetwork.show();

                                swipeRefreshLayout.setRefreshing(false);

                            } else {

                                //snackrefresh.show();

                                if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    // TODO: Consider calling

                                    //    ActivityCompat#requestPermissions

                                    // here to request the missing permissions, and then overriding

                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

                                    //                                          int[] grantResults)

                                    // to handle the case where the user grants the permission. See the documentation

                                    // for ActivityCompat#requestPermissions for more details.

                                    return;

                                }

                                //resetanim();

                                showweather();

                                //RetrieveWeather(lll, lonn, "a");

                                //RetrieveForecast(lll, lonn);

                                final Handler handler = new Handler();


                                handler.postDelayed(new Runnable() {

                                    @Override

                                    public void run() {

                                        if (!shown) {

                                            Toast.makeText(WeatherActivity.this, "Network Error! Please swipe down to Refresh.", Toast.LENGTH_LONG).show();

                                            swipeRefreshLayout.setRefreshing(false);

                                        }

                                    }

                                }, 10000);


                            }


                        }


                        //


                    }


                });


                System.out.println("At else part" + lll + " " + lonn);


                //RetrieveWeather(lll, lonn, "a");

                //RetrieveForecast(lll, lonn);

                //shown=true;


            }

        }


    }


    @SuppressLint("MissingPermission")
    public void showweather() {


        locationRequest = new LocationRequest();

        locationRequest.setNumUpdates(1);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationRequest.setInterval(120000);

        //locationRequest.setFastestInterval(2000);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);


        LocationSettingsRequest locationSettingsRequest = builder.build();


        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        settingsClient.checkLocationSettings(locationSettingsRequest);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling

            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

            //                                          int[] grantResults)

            // to handle the case where the user grants the permission. See the documentation

            // for ActivityCompat#requestPermissions for more details.

            return;

        }



        final SharedPreferences.Editor editor = sharedPreferences.edit();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling

            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

            //                                          int[] grantResults)

            // to handle the case where the user grants the permission. See the documentation

            // for ActivityCompat#requestPermissions for more details.

            return;

        }


        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            @Override

            public void onSuccess(Location location) {


                if (location != null) {

                    latitude = location.getLatitude();

                    longitude = location.getLongitude();

                    la = Double.toString(latitude);

                    lo = Double.toString(longitude);


                    editor.putString("lat", la);

                    editor.putString("lon", lo);



                    editor.apply();


                    RetrieveWeather(la, lo, "a");

                    RetrieveForecast(la, lo);


                    locationflag = 1;

                    shown = true;

                } else

                {

                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback()
                    {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            onLocationChanged(locationResult.getLastLocation());
                            locationflag = 1;
                        }
                    }, Looper.myLooper());




                }


            }

        }).addOnFailureListener(new OnFailureListener() {

            @Override

            public void onFailure(@NonNull Exception e) {

                Toast.makeText(context, "Error getting last location", Toast.LENGTH_SHORT).show();

                Log.d("MainActivity", "Error trying to get last GPS location");

                editor.remove("lat");

                editor.remove("lon");

                editor.apply();

                e.printStackTrace();

            }

        });


        if (locationflag != 1) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback()
            {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    onLocationChanged(locationResult.getLastLocation());
                }
            }, Looper.myLooper());


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling

                //    ActivityCompat#requestPermissions

                // here to request the missing permissions, and then overriding

                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

                //                                          int[] grantResults)

                // to handle the case where the user grants the permission. See the documentation

                // for ActivityCompat#requestPermissions for more details.

                return;

            }

        }


        //lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);


        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        final Handler handler = new Handler();


        handler.postDelayed(new Runnable() {

            @Override

            public void run() {

                if (!shown)

                    Toast.makeText(WeatherActivity.this, "Network Error! Please swipe down to Refresh.", Toast.LENGTH_LONG).show();

            }

        }, 15000);


    }


    private boolean locationEnabled() {

        lm = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);

        boolean gps = false;

        gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);


        return gps;

    }


    public boolean hasNavBar() {

        boolean hasSoftwareKeys = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            Display d = ((WindowManager) this.getSystemService(this.WINDOW_SERVICE)).getDefaultDisplay();

            DisplayMetrics realDisplay = new DisplayMetrics();

            d.getRealMetrics(realDisplay);


            int realHeight = realDisplay.heightPixels;

            int realWidth = realDisplay.widthPixels;


            DisplayMetrics display = new DisplayMetrics();

            d.getMetrics(display);


            int Height = display.heightPixels;

            int Width = display.widthPixels;

            hasSoftwareKeys = (realWidth - Width) > 0 || (realHeight - Height) > 0;

        } else {

            boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();

            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

            hasSoftwareKeys = !hasMenuKey && !hasBackKey;

        }

        return hasSoftwareKeys;

    }


    private boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

    }



    /*@Override

    public boolean onTouchEvent(MotionEvent event) {

        this.gestureDetectorCompat.onTouchEvent(event);

        return super.onTouchEvent(event);

    } */


    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();

        //inflater.inflate(R.menu.mainmenu, menu);

        getMenuInflater().inflate(R.menu.mainmenu, menu);

        return true;

    }


    @Override

    public void onBackPressed() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WeatherActivity.this);

        alertDialog.setTitle("Exit");

        alertDialog.setMessage("Are you sure you want to exit?");

        alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialogInterface, int i) {

                finish();

            }

        });


        alertDialog.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialogInterface, int i) {


            }

        });


        alertDialog.show();

    }


    /**
     * Creating google api client object
     */

    protected synchronized void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)

                    .addConnectionCallbacks(this)

                    .addOnConnectionFailedListener(this)

                    .addApi(LocationServices.API).build();

        }

    }


    @Override

    protected void onStart() {

        super.onStart();

        if (locationEnabled()) {

            buildGoogleApiClient();

            createLocationRequest();

            mGoogleApiClient.connect();


        }


    }


    @Override

    protected void onResume() {

        super.onResume();

        if (locationEnabled()) {

            buildGoogleApiClient();

            createLocationRequest();

            mGoogleApiClient.connect();


        }


    }


    protected void onStop() {

        super.onStop();

        if (fusedLocationProviderClient!=null && locationCallback!=null)

            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        //lm.removeUpdates(locationListener);

        //swipeRefreshLayout.setEnabled(false);

        if (swipeRefreshLayout.isRefreshing())

            swipeRefreshLayout.setRefreshing(false);


    }


    @Override

    protected void onPause() {

        super.onPause();

        if (fusedLocationProviderClient!=null && locationCallback!=null)

            fusedLocationProviderClient.removeLocationUpdates(locationCallback);

        if (swipeRefreshLayout.isRefreshing())

            swipeRefreshLayout.setRefreshing(false);


    }


    protected void createLocationRequest() {


        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        mLocationRequest.setFastestInterval(FATEST_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }


    @Override

    public void onConnectionFailed(ConnectionResult result) {


        //Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();

        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toastview, (ViewGroup) findViewById(R.id.toast_layout));

        TextView text = (TextView) findViewById(R.id.text_toast);

        text.setText("Connection error!");


        Toast toast = new Toast(getApplicationContext());

        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 80);

        toast.setDuration(Toast.LENGTH_LONG);

        toast.setView(layout);

        toast.show();

    }


    public void resetanim()

    {


        animationView.setProgress(0);


        moonView.setProgress(0);


        cloudy.setProgress(0);


        cloudymoon.setProgress(0);


        fogday.setProgress(0);


        fognight.setProgress(0);


        overcast.setProgress(0);


        cloudy.setProgress(0);


        cloudymoon.setProgress(0);


        rainy.setProgress(0);


        snowday.setProgress(0);


        snownight.setProgress(0);


        thunder.setProgress(0);


    }


    public void onConnected(Bundle arg0) {


        // Once connected with google api, get the location

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling

            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

            //                                          int[] grantResults)

            // to handle the case where the user grants the permission. See the documentation

            // for ActivityCompat#requestPermissions for more details.

            return;

        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {

            la = String.valueOf(mLastLocation.getLatitude());

            lo = String.valueOf(mLastLocation.getLongitude());

        }


    }


    @Override

    public void onConnectionSuspended(int arg0) {

        mGoogleApiClient.connect();

    }



    /*private void displayLocation() {



        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling

            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding

            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

            //                                          int[] grantResults)

            // to handle the case where the user grants the permission. See the documentation

            // for ActivityCompat#requestPermissions for more details.

            return;

        }



        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            @Override

            public void onSuccess(Location location) {

                if (location != null) {





                } else

                    Toast.makeText(WeatherActivity.this, "Error fetching location. Please swipe down to refresh.", Toast.LENGTH_LONG).show();

            }

        });





    } */


    public void RetrieveWeather(String lat, String lon, String addrs) {

        //Toast.makeText(getApplicationContext(),lat+" "+lon,Toast.LENGTH_SHORT).show();

        String url = "";

        if (addrs.equals("a")) {

            url = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=bcc6f8e44743e316e5120301ff1a5ad4";


            ConditionAsync task = new ConditionAsync(this, url);

            task.execute(url);

            // url="https://api.gettyimages.com/v3/images?phrase=Chennai "+"beautiful";

            // url="http://api.openweathermap.org/data/2.5/weather?q=NewYork,us&appid=bcc6f8e44743e316e5120301ff1a5ad4";

        } else {

            url = addrs;

        }


    }


    public void RetrieveForecast(String lat, String lon) {

        String url = "";

        url = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + lat + "&lon=" + lon + "&appid=bcc6f8e44743e316e5120301ff1a5ad4";

        ForecastAsync task = new ForecastAsync(this, url);

        task.execute(url);


        if (fusedLocationProviderClient != null && locationCallback!=null)

            fusedLocationProviderClient.removeLocationUpdates(locationCallback);


        //String tkn = FirebaseInstanceId.getInstance().getToken();

        //Toast.makeText(WeatherActivity.this, "Current token ["+tkn+"]",Toast.LENGTH_LONG).show();

        //PendingIntent pendingIntent=PendingIntent.getActivity(context,0,new Intent(context,WeatherActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);

        //save=intent.getStringExtra("quote");


    }


    public void SetDescription(String des) {



        /*//animationView.setImageAssetsFolder("images/");

        //animationView.setAnimation("sun.json");

        //animationView.loop(true);

        this.animationView.playAnimation();







        //moonView.setImageAssetsFolder("moon/");

        //moonView.setAnimation("moon.json");

        //moonView.loop(true);

        this.moonView.playAnimation();



       // LottieAnimationView cloudy = (LottieAnimationView) findViewById(R.id.cloud_view);



        //cloudy.setImageAssetsFolder("cloudy/");

        //cloudy.setAnimation("cloudy.json");

        //cloudy.loop(true);

        this.cloudy.playAnimation();



//        LottieAnimationView cloudymoon = (LottieAnimationView) findViewById(R.id.cloudmoon_view);



        //cloudymoon.setImageAssetsFolder("cloudymoon/");

        //cloudymoon.setAnimation("cloudymoon.json");

        //cloudymoon.loop(true);

        this.cloudymoon.playAnimation();



//        LottieAnimationView fogday = (LottieAnimationView) findViewById(R.id.fogday_view);



        //fogday.setImageAssetsFolder("fogday/");

        //fogday.setAnimation("fogday.json");

        //fogday.loop(true);

        this.fogday.playAnimation();



//        LottieAnimationView fognight = (LottieAnimationView) findViewById(R.id.fognight_view);



        //fognight.setImageAssetsFolder("fognight/");

        //fognight.setAnimation("fognight.json");

        //fognight.loop(true);

        this.fognight.playAnimation();



//        LottieAnimationView overcast = (LottieAnimationView) findViewById(R.id.overcast_view);



        //overcast.setImageAssetsFolder("overcast/");

        //overcast.setAnimation("overcast.json");

        //overcast.loop(true);

        this.overcast.playAnimation();



//        LottieAnimationView rainy = (LottieAnimationView) findViewById(R.id.rainy_view);



        //rainy.setImageAssetsFolder("rainy/");

        //rainy.setAnimation("rainy.json");

        //rainy.loop(true);

        this.rainy.playAnimation();



//        LottieAnimationView snowday = (LottieAnimationView) findViewById(R.id.snowday_view);



        //snowday.setImageAssetsFolder("snowday/");

        //snowday.setAnimation("snowday.json");

        //snowday.loop(true);

        this.snowday.playAnimation();



//        LottieAnimationView snownight = (LottieAnimationView) findViewById(R.id.snownight_view);



        //snownight.setImageAssetsFolder("snownight/");

        //snownight.setAnimation("snownight.json");

        //snownight.loop(true);

        this.snownight.playAnimation();



//        LottieAnimationView thunder = (LottieAnimationView) findViewById(R.id.thunder_view);



        //thunder.setImageAssetsFolder("thunder/");

        //thunder.setAnimation("thunder.json");

        //thunder.loop(true);

        this.thunder.playAnimation(); */


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

        TextView view = (TextView) this.findViewById(R.id.des_text);

        char c = des.charAt(0);

        c = Character.toUpperCase(c);

        des = des.substring(1, des.length());

        des = c + des;

        des = " " + des;

        notidesc = des;

        view.setText(des);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("des",des.substring(1,des.length())).apply();

        //view.setTypeface(face);

        // settings_des(des);

        //System.out.println(des);

        int pos = des.lastIndexOf(' ');

        des = des.substring(pos, des.length());

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

        // WeatherAsync task = new WeatherAsync(this, enc);


        llayout = (RelativeLayout) findViewById(R.id.progresslayout);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

        llayout.setVisibility(View.VISIBLE);

        //task.execute(enc);


    }


    public void SetTemperature(double temp, double min, double max) {


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        boolean t = s.getBoolean("checkBox", false);


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

        TextView view = (TextView) this.findViewById(R.id.temperature_text);

        //TextView view1 = (TextView) this.findViewById(R.id.hilowmain_text);

        DecimalFormat df = new DecimalFormat("###");
        String deg_symbol="\u2103";

        if (!t) {
            temp = (temp * 9 / 5) + 32;

            deg_symbol="\u2109";
        }

        String formatTemp = df.format(temp);

        //String formatTemp1 = df.format(min);

        //String formatTemp2 = df.format(max);

        tempformat = formatTemp + "\u00B0";

        view.setText(tempformat);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("temp",formatTemp+deg_symbol).apply();

        //view1.setText("Hi:"+formatTemp2+" Lo:"+formatTemp1);

        //view.setTypeface(face);

        //view1.setTypeface(face);

        //settings_menu.setTemp(formatTemp);


    }


    public void SetPressure(double pres) {

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");

        TextView view = (TextView) this.findViewById(R.id.humidity_text2);

        DecimalFormat df = new DecimalFormat("###.##");

        String formatPres = df.format(pres);

        view.setText(formatPres + " hPa");

        //view.setTypeface(face);

    }


    public void SetWind(double wind) {

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");

        TextView view = (TextView) this.findViewById(R.id.wind_text);

        DecimalFormat df = new DecimalFormat("###.#");

        String formatPres = df.format(wind);

        view.setText(formatPres + " Kmph");

       // view.setTypeface(face);

    }

    public void SetCloud(String  c) {

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");

        TextView view = (TextView) this.findViewById(R.id.wind_text2);


        view.setText(c + "%");

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rain",c+'%').apply();

        // view.setTypeface(face);

    }


    public void SetHumidity(double hum) {

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");

        TextView view = (TextView) this.findViewById(R.id.humidity_text);

        DecimalFormat df = new DecimalFormat("###.#");

        String formatHum = df.format(hum);

        view.setText(formatHum + "%");

        //view.setTypeface(face);


    }


    public void SetLocation(String name, String country) {

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latoregular.ttf");

        String fullLocation = name;

        notiloc = name;

        TextView view = (TextView) this.findViewById(R.id.city_text);

        view.setText(fullLocation);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("locate",name).apply();

        //view.setTypeface(face);

        //settings_menu.setLoc(name);

    }


    public void setWeatherIcon(String id) {


        //settings_menu.setId(actualID);

        //ImageView view = (ImageView) this.findViewById(R.id.icon);

        //ImageView linear = (ImageView) this.findViewById(R.id.back);

        ImageView sc = (ImageView) this.findViewById(R.id.backgroundWeather);


        int actualID = Integer.parseInt(id);

        int ID = actualID / 100;

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id",ID).apply();

        switch (ID)

        {

            case 2://for thunderstorm


                Glide.with(this)

                        .load(this.getResources().getIdentifier("thnd", "drawable", this.getPackageName()))

                        //.load("")

                        //.error(R.drawable.background_)

                        //.centerCrop()

                        //.dontTransform()

                        //.crossFade()

                        .into(sc);


                llayout.setVisibility(View.INVISIBLE);

                //view11.setBackgroundResource(R.color.thunder);


                break;

            case 3://for drizzle

                if (hour >= 6 && hour < 18)

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("rainysss", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);

                else

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("rainynight", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);


                llayout.setVisibility(View.INVISIBLE);


                break;

            case 5: //for rain

                if (hour >= 6 && hour < 18)

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("rainysss", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);

                else

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("rainynight", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);


                llayout.setVisibility(View.INVISIBLE);

                break;

            case 6: //for snow


                Glide.with(this)

                        .load(this.getResources().getIdentifier("snowy", "drawable", this.getPackageName()))

                        //.load("")

                        //.error(R.drawable.background_)


                        .into(sc);


                llayout.setVisibility(View.INVISIBLE);


                break;

            case 7: //for fog


                if (hour >= 6 && hour < 18) {

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("foggyss", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);


                    llayout.setVisibility(View.INVISIBLE);

                } else {

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("foggyss", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);


                    llayout.setVisibility(View.INVISIBLE);

                }

                break;

            case 8: //for clear and clouds


                if (actualID == 800) {

                    //System.out.println(hour);

                    //for clear

                    //  view1.setImageResource(R.drawable.clear);

                    //view11.setBackgroundResource(R.color.clear);

                    if (hour >= 5 && hour < 7) {

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("sunrise", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                        llayout.setVisibility(View.INVISIBLE);

                    } else if (hour >= 7 && hour < 17) {

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("clearday", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                        llayout.setVisibility(View.INVISIBLE);

                    } else if (hour >= 17 && hour < 19) {

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("sunset", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                        llayout.setVisibility(View.INVISIBLE);

                    } else {

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("nightclr", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                        llayout.setVisibility(View.INVISIBLE);

                    }

                    // linear.setBackgroundResource(R.drawable.at_day);


                }

                if (actualID == 801 || actualID == 802 || actualID == 803) {

                    //for scattered clouds

                    //view1.setImageResource(R.drawable.scattered_clouds);

                    //view11.setBackgroundResource(R.color.clouds);

                    if (hour >= 6 && hour < 18) {

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("clds", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                        llayout.setVisibility(View.INVISIBLE);

                    } else

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("nightclds", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                    llayout.setVisibility(View.INVISIBLE);

                    //linear.setBackgroundResource(R.drawable.scatteredat_day);


                }

                if (actualID == 804) {

                    //for overcast

                    //view1.setImageResource(R.drawable.overcast);

                    //view11.setBackgroundResource(R.color.overcast);

                    Glide.with(this)

                            .load(this.getResources().getIdentifier("overcasttpp", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);


                    llayout.setVisibility(View.INVISIBLE);

                    //linear.setBackgroundResource(R.drawable.overcastat_day);


                }

                break;

            case 9://for extreme weather

                if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                    Glide.with(this)

                            .load(this.getResources().getIdentifier("thnd", "drawable", this.getPackageName()))

                            //.load("")

                            //.error(R.drawable.background_)


                            .into(sc);


                    llayout.setVisibility(View.INVISIBLE);

                    //view1.setImageResource(R.drawable.tornado);

                } else {

                    if (hour >= 6 && hour < 18) {

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("foggyss", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                        llayout.setVisibility(View.INVISIBLE);

                    } else

                        Glide.with(this)

                                .load(this.getResources().getIdentifier("foggyss", "drawable", this.getPackageName()))

                                //.load("")

                                //.error(R.drawable.background_)


                                .into(sc);


                    llayout.setVisibility(View.INVISIBLE);

                    // view1.setImageResource(R.drawable.breeze);

                }


                break;


        }


        //sc.setBackgroundColor(Color.parseColor("#1e88e5"));


    }


    public void setanimation(String id) {

        resetanim();
        int actualID = Integer.parseInt(id);

        int ID = actualID / 100;

        switch (ID) {

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

            case 6: //for snow


                this.snowday.playAnimation();


                break;

            case 7: //for fog


                if (hour >= 6 && hour < 18) {

                    this.fogday.playAnimation();

                } else

                    this.fognight.playAnimation();

                //linear.setBackgroundResource(R.drawable.fogat_day);

                break;

            case 8: //for clear and clouds


                if (actualID == 800) {

                    //System.out.println(hour);

                    //for clear

                    //  view1.setImageResource(R.drawable.clear);

                    //view11.setBackgroundResource(R.color.clear);

                    if (hour >= 6 && hour < 18)


                        this.animationView.playAnimation();


                    else

                        this.moonView.playAnimation();

                    // linear.setBackgroundResource(R.drawable.at_day);


                }

                if (actualID == 801 || actualID == 802 || actualID == 803) {

                    //for scattered clouds

                    //view1.setImageResource(R.drawable.scattered_clouds);

                    //view11.setBackgroundResource(R.color.clouds);

                    if (hour >= 6 && hour < 18) {

                        this.cloudy.playAnimation();

                    } else

                        this.cloudymoon.playAnimation();

                    //linear.setBackgroundResource(R.drawable.scatteredat_day);


                }

                if (actualID == 804) {

                    //for overcast

                    //view1.setImageResource(R.drawable.overcast);

                    //view11.setBackgroundResource(R.color.overcast);

                    this.overcast.playAnimation();

                    //linear.setBackgroundResource(R.drawable.overcastat_day);


                }

                break;

            case 9://for extreme weather

                if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                    this.thunder.playAnimation();

                    //view1.setImageResource(R.drawable.tornado);

                } else //for breeze

                {

                    if (hour >= 6 && hour < 18) {

                        this.fogday.playAnimation();

                    } else

                        this.fognight.playAnimation();


                }


                break;


        }


        horizontalScrollView.setVisibility(View.VISIBLE);
        LinearLayout l1=(LinearLayout)findViewById(R.id.locicontext);
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.cardhumid);
        rl.setVisibility(View.VISIBLE);
        l1.setVisibility(View.VISIBLE);

        TranslateAnimation a = new TranslateAnimation(0, 0, l1.getHeight() - 1000, 0);

        a.setDuration(500);

        a.setFillAfter(true);

        l1.startAnimation(a);

        TranslateAnimation an = new TranslateAnimation(0, 0, rl.getHeight() + 1000, 0);

        an.setDuration(500);

        an.setFillAfter(true);

        rl.startAnimation(an);

        TranslateAnimation animation = new TranslateAnimation(0, 0, horizontalScrollView.getHeight() + 200, 0);

        animation.setDuration(500);

        animation.setFillAfter(true);

        horizontalScrollView.startAnimation(animation);


        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        if (v != null) {

            v.vibrate(20);

        }


    }


    public void backsearch(View view)

    {

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.srch);

        relativeLayout.setVisibility(View.GONE);

        ImageView im1 = (ImageView) findViewById(R.id.fav);

        ImageView im2 = (ImageView) findViewById(R.id.share);

        ImageView im3 = (ImageView) findViewById(R.id.acsearch);

        ImageView im4 = (ImageView) findViewById(R.id.menuicon);

        TextView textView = (TextView) findViewById(R.id.wnlogo);


        im1.setVisibility(View.VISIBLE);

        im2.setVisibility(View.VISIBLE);

        im3.setVisibility(View.VISIBLE);

        im4.setVisibility(View.VISIBLE);

        textView.setVisibility(View.VISIBLE);

    }


    public void actionSearch(View view) {

        Intent in = new Intent(this, Places.class);

        startActivity(in);

        /*final RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.srch);

        relativeLayout.setVisibility(View.VISIBLE);



        ImageView im1=(ImageView) findViewById(R.id.fav);

        ImageView im2=(ImageView) findViewById(R.id.share);

        ImageView im3=(ImageView) findViewById(R.id.acsearch);

        ImageView im4=(ImageView) findViewById(R.id.menuicon);

        TextView textView=(TextView) findViewById(R.id.wnlogo);





        placesAutocompleteTextView=(PlacesAutocompleteTextView)findViewById(R.id.placesauto);



        placesAutocompleteTextView.setVisibility(View.VISIBLE);

        im1.setVisibility(View.GONE);

        im2.setVisibility(View.GONE);

        im3.setVisibility(View.GONE);

        im4.setVisibility(View.GONE);

        textView.setVisibility(View.GONE);

        placesAutocompleteTextView.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {

            @Override

            public void onPlaceSelected(@NonNull Place place) {



                placesAutocompleteTextView.getDetailsFor(place, new DetailsCallback() {

                    @Override

                    public void onSuccess(PlaceDetails placeDetails) {

                        System.out.println(placeDetails.name);

                        String loc=placeDetails.name.trim();

                        String URL_place= "http://api.openweathermap.org/data/2.5/weather?q="+loc+"&appid=bcc6f8e44743e316e5120301ff1a5ad4";

                        Intent intent = new Intent(WeatherActivity.this, Dialog_weather.class);

                        intent.putExtra("URL", URL_place);

                        intent.putExtra("loc_desc",loc);

                        startActivity(intent);



                        relativeLayout.setVisibility(View.GONE);

                        //finish();



                    }



                    @Override

                    public void onFailure(Throwable throwable) {



                    }

                });

            }





        }); */


    }


    public void actionMenu(View view) {

        Intent set = new Intent(this, Settings_menu.class);

        /*set.putExtra("DES", DESCRIPTION);

        set.putExtra("TEM", TEM);

        set.putExtra("LOC", LOC);

        set.putExtra("ID", W_ID); */

        startActivity(set);

    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_share:


                break;

            case R.id.action_search:


                Intent in = new Intent(this, Places.class);

                RelativeLayout rel = (RelativeLayout) findViewById(R.id.relativeBack);

                rel.getBackground().setAlpha(250);

                startActivity(in);

                break;


            case R.id.action_menu:


                Intent set = new Intent(this, Settings_menu.class);

                set.putExtra("DES", DESCRIPTION);

                set.putExtra("TEM", TEM);

                set.putExtra("LOC", LOC);

                set.putExtra("ID", W_ID);

                startActivity(set);


                break;

        }

        return true;

    }


    public void share(View view) {

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                takeScreenshot();

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                takeScreenshot();

            }

        } else {

            takeScreenshot();

        }

        //takeScreenshot();

        //saveBitmap(bitmap);

        //shareIt();

    }


    public void takeScreenshot() {


        //View rootview=this.findViewById(android.R.id.content);


        String imagePath = Environment.getExternalStorageDirectory().toString() + "/Pictures/Screenshots/" + "screenshotwn.jpg";

        View v1 = getWindow().getDecorView().getRootView();

        v1.setDrawingCacheEnabled(true);

        Bitmap bitmap = v1.getDrawingCache();


        File newFile = new File(imagePath);

        FileOutputStream fos;

        try {

            fos = new FileOutputStream(newFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();

            fos.close();

            shareIt(newFile);


        } catch (Throwable e) {

            e.printStackTrace();

        }


    }



    /*public void saveBitmap(Bitmap bitmap)

    {



        File imagePath=new File(Environment.getExternalStorageDirectory().toString()+"/screenshotWN.jpg");

        FileOutputStream fos;

        try

        {

            fos=new FileOutputStream(imagePath);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();

            fos.close();

            shareIt(imagePath);

        }

        catch (FileNotFoundException e)

        {



        }

        catch (IOException e)

        {



        }

    } */


    public void shareIt(File file) {


        Intent shareing = new Intent(Intent.ACTION_SEND);

        shareing.setType("image/*");

        Uri uri = FileProvider.getUriForFile(getApplicationContext(), this.getApplicationContext().getPackageName() + ".provider", file);

        //Uri uri = Uri.parse("file://" + imagePath);

        String text = "Check out the current weather at my place! By WeatherNow - https://play.google.com/store/apps/details?id=com.minimaldev.android.weathernowupdate ";

        shareing.putExtra(Intent.EXTRA_SUBJECT, "Check out WeatherNow by MinimalDev");

        shareing.putExtra(Intent.EXTRA_TEXT, text);

        shareing.putExtra(Intent.EXTRA_STREAM, uri);


        startActivity(Intent.createChooser(shareing, "Share via"));


    }


    public void SetDay(String day, int dayNo)


    {

        String d = day.substring(0, 3);

        d = d.toUpperCase();

        //d=d+",";


        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

        //TextView view1 = (TextView) this.findViewById(R.id.hilowmain_text);


        //String formatTemp1 = df.format(min);

        //String formatTemp2 = df.format(max);


        //view1.setText("Hi:"+formatTemp2+" Lo:"+formatTemp1);


        TextView view1 = (TextView) findViewById(R.id.day11);

        TextView view2 = (TextView) findViewById(R.id.day12);

        TextView view3 = (TextView) findViewById(R.id.day13);

        TextView view4 = (TextView) findViewById(R.id.day14);

        TextView view5 = (TextView) findViewById(R.id.day15);

        TextView view6 = (TextView) findViewById(R.id.day16);

        TextView view7 = (TextView) findViewById(R.id.day17);

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

            case 7:

                view7.setText(d);

                view7.setTypeface(face);

                break;

            //case 7: view7.setText(day);

            // break;}

        }

    }


    public void SetDes(String des, int index) {

        int actualID = Integer.parseInt(des);

        int ID = actualID / 100;


        ImageView view1 = (ImageView) findViewById(R.id.icon11);

        ImageView view2 = (ImageView) findViewById(R.id.icon12);

        ImageView view3 = (ImageView) findViewById(R.id.icon13);

        ImageView view4 = (ImageView) findViewById(R.id.icon14);

        ImageView view5 = (ImageView) findViewById(R.id.icon15);

        ImageView view6 = (ImageView) findViewById(R.id.icon16);

        ImageView view7 = (ImageView) findViewById(R.id.icon17);

        //ImageView view7=(ImageView)findViewById(R.id.iconweather_sun);

        if (index == 0) {

            switch (ID) {

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

                case 6: //for snow


                    view1.setImageResource(R.drawable.snow);

                    //view11.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view1.setImageResource(R.drawable.fog);

                    //view11.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view1.setImageResource(R.drawable.clear);

                        //view11.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view1.setImageResource(R.drawable.scattered_clouds);

                        //view11.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view1.setImageResource(R.drawable.overcast);

                        //view11.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view1.setImageResource(R.drawable.tornado);

                    } else {

                        view1.setImageResource(R.drawable.breeze);

                    }

                    // view11.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        } else if (index == 1) {

            switch (ID) {

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

                case 6: //for snow


                    view2.setImageResource(R.drawable.snow);

                    //view22.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view2.setImageResource(R.drawable.fog);

                    //view22.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view2.setImageResource(R.drawable.clear);

                        //view22.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view2.setImageResource(R.drawable.scattered_clouds);

                        //view22.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view2.setImageResource(R.drawable.overcast);

                        //view22.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view2.setImageResource(R.drawable.tornado);

                    } else {

                        view2.setImageResource(R.drawable.breeze);

                    }                    //view22.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        } else if (index == 2) {

            switch (ID) {

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

                case 6: //for snow


                    view3.setImageResource(R.drawable.snow);

                    // view33.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view3.setImageResource(R.drawable.fog);

                    //view33.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view3.setImageResource(R.drawable.clear);

                        // view33.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view3.setImageResource(R.drawable.scattered_clouds);

                        //view33.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view3.setImageResource(R.drawable.overcast);

                        //view33.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view3.setImageResource(R.drawable.tornado);

                    } else {

                        view3.setImageResource(R.drawable.breeze);

                    }                   // view33.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        } else if (index == 3) {

            switch (ID) {

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

                case 6: //for snow


                    view4.setImageResource(R.drawable.snow);

                    //iew44.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view4.setImageResource(R.drawable.fog);

                    //view44.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view4.setImageResource(R.drawable.clear);

                        //view44.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view4.setImageResource(R.drawable.scattered_clouds);

                        //view44.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view4.setImageResource(R.drawable.overcast);

                        //view44.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view4.setImageResource(R.drawable.tornado);

                    } else {

                        view4.setImageResource(R.drawable.breeze);

                    }                    //view44.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        } else if (index == 4) {

            switch (ID) {

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

                case 6: //for snow


                    view5.setImageResource(R.drawable.snow);

                    //view55.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view5.setImageResource(R.drawable.fog);

                    //view55.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view5.setImageResource(R.drawable.clear);

                        //view55.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view5.setImageResource(R.drawable.scattered_clouds);

                        //view55.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view5.setImageResource(R.drawable.overcast);

                        //view55.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view5.setImageResource(R.drawable.tornado);

                    } else {

                        view5.setImageResource(R.drawable.breeze);

                    }                    //view55.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        } else if (index == 5) {

            switch (ID) {

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

                case 6: //for snow


                    view6.setImageResource(R.drawable.snow);

                    //view66.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view6.setImageResource(R.drawable.fog);

                    //view66.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view6.setImageResource(R.drawable.clear);

                        //view66.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view6.setImageResource(R.drawable.scattered_clouds);

                        //view66.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view6.setImageResource(R.drawable.overcast);

                        // view66.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view6.setImageResource(R.drawable.tornado);

                    } else {

                        view6.setImageResource(R.drawable.breeze);

                    }                    //view66.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        } else


        {

            switch (ID) {

                case 2://for thunderstorm

                    view7.setImageResource(R.drawable.thunderstorm);

                    //view66.setBackgroundResource(R.color.thunder);


                    //linear.setBackgroundResource(R.drawable.thunder);


                    break;

                case 3://for drizzle

                    view7.setImageResource(R.drawable.drizzle);

                    //view66.setBackgroundResource(R.color.drizzle);


                    //linear.setBackgroundResource(R.drawable.drizzleat_day);


                    break;

                case 5: //for rain

                    view7.setImageResource(R.drawable.rain);

                    //view66.setBackgroundResource(R.color.rain);


                    //linear.setBackgroundResource(R.drawable.rainat_night);


                    break;

                case 6: //for snow


                    view7.setImageResource(R.drawable.snow);

                    //view66.setBackgroundResource(R.color.snow);


                    //linear.setBackgroundResource(R.drawable.snowat_night);


                    break;

                case 7: //for fog

                    view7.setImageResource(R.drawable.fog);

                    //view66.setBackgroundResource(R.color.fog);


                    //linear.setBackgroundResource(R.drawable.fogat_day);

                    break;

                case 8: //for clear and clouds


                    if (actualID == 800) {

                        //for clear

                        view7.setImageResource(R.drawable.clear);

                        //view66.setBackgroundResource(R.color.clear);


                        // linear.setBackgroundResource(R.drawable.at_day);


                    }

                    if (actualID == 801 || actualID == 802 || actualID == 803) {

                        //for scattered clouds

                        view7.setImageResource(R.drawable.scattered_clouds);

                        //view66.setBackgroundResource(R.color.clouds);


                        //linear.setBackgroundResource(R.drawable.scatteredat_day);


                    }

                    if (actualID == 804) {

                        //for overcast

                        view7.setImageResource(R.drawable.overcast);

                        // view66.setBackgroundResource(R.color.overcast);


                        //linear.setBackgroundResource(R.drawable.overcastat_day);


                    }

                    break;

                case 9://for extreme weather

                    if (actualID == 900 || actualID == 901 || actualID == 902 || actualID == 958 || actualID == 959 || actualID == 960 || actualID == 961 || actualID == 962) {


                        view7.setImageResource(R.drawable.tornado);

                    } else {

                        view7.setImageResource(R.drawable.breeze);

                    }                    //view66.setBackgroundResource(R.color.extreme);


                    //linear.setBackgroundResource(R.drawable.extreme_weather);


                    //linear.setBackgroundResource(R.drawable.);


                    break;


            }

        }


    }


    public void SetTemp(double min, double max, int index) {

        min = min - 273;

        max = max - 273;

        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        boolean tmp = s.getBoolean("checkBox", false);


        String deg = "\u2103";


        if (!tmp)

        {

            min = (min * 9 / 5 + 32);

            max = (max * 9 / 5 + 32);

            deg = "\u2109";

        }

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latomedium.ttf");

        DecimalFormat df = new DecimalFormat("###");

        double val = (min + max) / 2;

        String t = df.format(val) + " " + deg;


        TextView view1 = (TextView) findViewById(R.id.temp11);

        TextView view2 = (TextView) findViewById(R.id.temp12);

        TextView view3 = (TextView) findViewById(R.id.temp13);

        TextView view4 = (TextView) findViewById(R.id.temp14);

        TextView view5 = (TextView) findViewById(R.id.temp15);

        TextView view6 = (TextView) findViewById(R.id.temp16);

        TextView view7 = (TextView) findViewById(R.id.temp17);


        switch (index) {

            case 0:

                view1.setText(t);

                //view1.setTypeface(face);

                // view11.setText(formatTempMax);

                break;

            case 1:

                view2.setText(t);

                //view2.setTypeface(face);

                //view22.setText(formatTempMax);

                break;

            case 2:

                view3.setText(t);

                //view3.setTypeface(face);

                // view33.setText(formatTempMax);

                break;

            case 3:

                view4.setText(t);

                //view4.setTypeface(face);

                // view44.setText(formatTempMax);

                break;

            case 4:

                view5.setText(t);

                //view5.setTypeface(face);

                // view55.setText(formatTempMax);

                break;

            case 5:

                view6.setText(t);

                //view6.setTypeface(face);

                //view66.setText(formatTempMax);

                break;


            case 6:

                view7.setText(t);

                //view7.setTypeface(face);

                //view66.setText(formatTempMax);

                break;

            //case 6: view7.setText(formatTemp/);

            //  break;


        }


    }


    public void setHi(double high) {


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        boolean t = s.getBoolean("checkBox", false);

        String deg = "\u2103";//default is degree celcius

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

        double max = high - 273;

        if (!t)

        {

            max = (max * 9 / 5) + 32;

            deg = "\u2109";//cahnge to fahrenheit

        }


        DecimalFormat df = new DecimalFormat("###");

        String formatTempMin = df.format(max);

        TextView textView = (TextView) findViewById(R.id.hi);

        textView.setText(formatTempMin + "\u00B0");

        //textView.setTypeface(face);


    }


    public void setLo(double low) {


        SharedPreferences s = PreferenceManager.getDefaultSharedPreferences(this);

        boolean t = s.getBoolean("checkBox", false);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/latobold.ttf");

        double min = low - 273;

        String deg = "\u2103";//default is degree celcius

        if (!t) {

            min = (min * 9 / 5) + 32;

            deg = "\u2109";// cahnge to fahrenheit

        }

        DecimalFormat df = new DecimalFormat("###");

        String formatTempMin = df.format(min);

        //nighttemp=formatTempMin;

        TextView textView = (TextView) findViewById(R.id.lo);

        textView.setText(formatTempMin + "\u00B0");

        //textView.setTypeface(face);


        if (swipeRefreshLayout.isEnabled()) {

            //snackrefresh.dismiss();

            //swipeRefreshLayout.setRefreshing(false);

            //swipeRefreshLayout.setEnabled(false);

            swipeRefreshLayout.setRefreshing(false);

        }


    }


    public void sendNotification(String des, double t, String l, String w_id) {

        DESCRIPTION = des;

        TEM = t;

        LOC = l;

        W_ID = w_id;

    }


    public void favorites(View view) {

        startActivity(new Intent(WeatherActivity.this, DisplayFav.class));


    }


    @Override

    public void onLocationChanged(Location location) {


        if (location != null) {

            latitude = location.getLatitude();

            longitude = location.getLongitude();

            la = Double.toString(latitude);

            lo = Double.toString(longitude);

            System.out.println("*************location changed**************");

            sharedPreferences = this.getSharedPreferences("location", MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("lat", la);

            editor.putString("lon", lo);

            editor.apply();


        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // TODO: Consider calling

                //    ActivityCompat#requestPermissions

                // here to request the missing permissions, and then overriding

                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,

                //                                          int[] grantResults)

                // to handle the case where the user grants the permission. See the documentation

                // for ActivityCompat#requestPermissions for more details.

                return;

            }

            this.fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {

                @Override

                public void onLocationResult(LocationResult locationResult) {

                    onLocationChanged(locationResult.getLastLocation());

                }

            }, Looper.myLooper());

        }


        //this.fusedLocationProviderClient.removeLocationUpdates(locationCallback);


    }


    @Override

    public void onStatusChanged(String s, int i, Bundle bundle) {


    }


    @Override

    public void onProviderEnabled(String s) {


    }


    @Override

    public void onProviderDisabled(String s) {


    }


    public class ConditionAsync extends AsyncTask<String, Void, String> {


        // private Context context;

        String new_url;

        String enc;


        String key = "6024521-aa3caf3e11ed4bf5eead80356", secret = "WHjf5GVFChvtUFsxYpBv4XbaYK7Mb5BNpYfN3t3FhQzKc";


        private WeatherActivity WeatherActivity;

        ProgressDialog dialog;

        LinearLayout linearLayout;


        public ConditionAsync(WeatherActivity weatherActivity, String url) {


            this.WeatherActivity = weatherActivity;

            //dialog=new ProgressDialog(weatherActivity);

            new_url = url;


        }


        @Override

        protected void onPreExecute()

        {

        /*this.dialog.setTitle("Fetching beautiful weather...");

        this.dialog.setMessage("Hmmm...slow isn't?");

        this.dialog.show();

        this.dialog.setCanceledOnTouchOutside(false); */

        }


        @Override

        protected String doInBackground(String... urls) {

            // this weather service method will be called after the service executes.

            // it will run as a seperate process, and will populate the activity in the onPostExecute

            // method below


            String response = "";

            try {

                HttpURLConnection con = (HttpURLConnection) (new URL(new_url)).openConnection();


                // con.addRequestProperty("Authorization","Bearer "+secret);

                con.setRequestMethod("POST");


                //con.setRequestProperty("Accept-Language","json");

                con.setDoInput(true);

                con.setDoOutput(true);

                con.connect();

                //HttpResponse execute = client.execute(httpGet);

                InputStream content = (InputStream) con.getContent();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));

                String s = "";

                while ((s = buffer.readLine()) != null) {

                    response = response + s;

                }

                con.disconnect();


            } catch (Exception e) {

                e.printStackTrace();

            }

            return response;

        }

        @Override

        public void onPostExecute(String result)

        {


            //System.out.println(result);



        /*if(dialog.isShowing())

        {

            dialog.dismiss();

        } */


            if (result == null)

            {

                //Toast.makeText(this.WeatherActivity,"Error fetching weather!",Toast.LENGTH_SHORT).show();

                LayoutInflater inflater = this.WeatherActivity.getLayoutInflater();

                View layout = inflater.inflate(R.layout.toastview, (ViewGroup) this.WeatherActivity.findViewById(R.id.toast_layout));

                TextView text = (TextView) layout.findViewById(R.id.text_toast);

                text.setText("Error fetching weather!");


                Toast toast = new Toast(this.WeatherActivity);

                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 80);

                toast.setDuration(Toast.LENGTH_LONG);

                toast.setView(layout);

                toast.show();


            }


            try {

// parse the json result returned from the service

                JSONObject jsonResult = new JSONObject(result);

//parse out the co-ordinates of location


// parse out the temperature from the JSON result

                double temperature = jsonResult.getJSONObject("main").getDouble("temp");

                temperature = ConvertTemperatureToFarenheit(temperature);


                // parse out the pressure from the JSON Result

                double pressure = jsonResult.getJSONObject("main").getDouble("pressure");


// parse out the humidity from the JSON result

                double humidity = jsonResult.getJSONObject("main").getDouble("humidity");


                double min = jsonResult.getJSONObject("main").getDouble("temp_min");

                double max = jsonResult.getJSONObject("main").getDouble("temp_max");


                double wind = jsonResult.getJSONObject("wind").getDouble("speed");

                wind = (wind / 1000) * 3600;

                min = ConvertTemperatureToFarenheit(min);

                max = ConvertTemperatureToFarenheit(max);

                DecimalFormat df = new DecimalFormat("###");

                String t = df.format(max);

// parse out the description from the JSON result

                String description = jsonResult.getJSONArray("weather").getJSONObject(0).getString("description");

//parse out weather id

                String id = jsonResult.getJSONArray("weather").getJSONObject(0).getString("id");


                //parse out city name

                String locate = jsonResult.getString("name");

                //parse out country

                String cntry = jsonResult.getJSONObject("sys").getString("country");

                String rain=jsonResult.getJSONObject("clouds").getString("all");


                //double r=Double.parseDouble(rain);


// set all the fields in the activity from the parsed JSON

                this.WeatherActivity.SetDescription(description);

                this.WeatherActivity.SetTemperature(temperature, min, max);

                this.WeatherActivity.SetLocation(locate, cntry);

                this.WeatherActivity.setWeatherIcon(id);

                this.WeatherActivity.llayout.setVisibility(View.INVISIBLE);

                this.WeatherActivity.setanimation(id);

                this.WeatherActivity.SetHumidity(humidity);

                this.WeatherActivity.SetPressure(pressure);

                this.WeatherActivity.SetWind(wind);

                this.WeatherActivity.SetCloud(rain);

                this.WeatherActivity.sendNotification(description, temperature, locate, id);

                //WeatherActivity.this.lm.removeUpdates(locationListener);


                Intent intent = new Intent(WeatherActivity.this, WeatherActivity.class);


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(WeatherActivity.this, 0, intent, 0);


                PendingIntent pi = PendingIntent.getActivity(WeatherActivity.this, 0, new Intent(WeatherActivity.this, DisplayFav.class), 0);


                NotificationCompat.Builder builder1 = new NotificationCompat.Builder(WeatherActivity.this, WeatherActivity.this.channelid)

                        .setSmallIcon(R.drawable.sunny)

                        .setContentTitle(notiloc)

                        .setContentText(notiloc)

                        .setAutoCancel(true)

                        .setShowWhen(false)

                        .addAction(0, "GO TO APP", pendingIntent)

                        .addAction(0, "FAVORITES", pi)

                        .setContentIntent(pendingIntent)

                        .setSound(null)

                        .setContentText(tempformat + " " + notidesc)

                        .setColor(Color.parseColor("#e24357"))

                        .setPriority(NotificationCompat.PRIORITY_LOW);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

                {

                    NotificationChannel notificationChannel = new NotificationChannel(WeatherActivity.this.channelid, "Current Weather", NotificationManager.IMPORTANCE_LOW);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    manager.createNotificationChannel(notificationChannel);

                    notificationChannel.setDescription("Current weather");

                    notificationChannel.setShowBadge(false);

                    notificationChannel.setSound(null, null);

                    //NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);


                    manager.notify(0, builder1.build());

                    // System.out.println("Here");

                } else

                {

                    //NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(WeatherActivity.this);

                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    if (manager != null) {

                        manager.notify(0, builder1.build());

                    }

                    //notificationManagerCompat.notify(0,builder1.build());

                    // System.out.println("Not Here");

                }


            } catch (JSONException e) {

                e.printStackTrace();

                Log.e("Error: ", e.getMessage());

            }

        }

        private double ConvertTemperatureToFarenheit(double temperature) {

            return (temperature - 273);

        }


    }
}