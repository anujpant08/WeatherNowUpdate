package com.minimaldev.android.weathernowupdate;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

/**
 * Created by ANUJ on 23/12/2016.
 */
public class Places extends AppCompatActivity {

    private EditText atvPlaces;
    private Button search;
    String loc_desc;
    String URL_place;
    PlacesAutocompleteTextView placesAutocompleteTextView;
    //WeatherActivity weatherActivity = new WeatherActivity();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestWindowFeature(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setContentView(R.layout.search_window);



        placesAutocompleteTextView=(PlacesAutocompleteTextView)findViewById(R.id.placesauto);

        Point point=new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        placesAutocompleteTextView.setDropDownWidth(point.x);

        placesAutocompleteTextView.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                Toast.makeText(Places.this,"Loading location...",Toast.LENGTH_LONG).show();

                placesAutocompleteTextView.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        System.out.println(placeDetails.name);
                        String loc=placeDetails.name.trim();
                        URL_place= "http://api.openweathermap.org/data/2.5/weather?q="+loc+"&appid=bcc6f8e44743e316e5120301ff1a5ad4";
                        Intent intent = new Intent(Places.this, Dialog_weather.class);
                        intent.putExtra("URL", URL_place);
                        intent.putExtra("loc_desc",loc);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                        System.out.println("Here at failure");
                        int ind=placesAutocompleteTextView.getText().toString().indexOf(',');
                        String l=placesAutocompleteTextView.getText().toString().substring(0,ind);
                        URL_place= "http://api.openweathermap.org/data/2.5/weather?q="+l+"&appid=bcc6f8e44743e316e5120301ff1a5ad4";
                        Intent intent = new Intent(Places.this, Dialog_weather.class);
                        intent.putExtra("URL", URL_place);
                        intent.putExtra("loc_desc",l);
                        startActivity(intent);
                        finish();


                    }
                });
            }
        });

        //getSupportActionBar().setElevation(0);




    }

    public void goback(View view)
    {
        finish();
    }

                /*public void Click_method(View view) {
                // TODO Auto-generated method stub
                String loc="";
                atvPlaces = (EditText) findViewById(R.id.edit_places);
                loc_desc = atvPlaces.getText().toString();
                int i;
                for(i=0;i<loc_desc.length();i++)
                {
                    if(loc_desc.charAt(i)==' ')
                    {
                        continue;
                    }
                    else
                    {
                        loc=loc+loc_desc.charAt(i);
                    }

                }
                //Toast.makeText(getApplicationContext(),loc_desc, Toast.LENGTH_SHORT).show();
                URL_place= "http://api.openweathermap.org/data/2.5/weather?q="+loc+"&appid=bcc6f8e44743e316e5120301ff1a5ad4";
                Intent intent = new Intent(this, Dialog_weather.class);
                intent.putExtra("URL", URL_place);
                intent.putExtra("loc_desc",loc);
                startActivity(intent);
                finish();


            } */

}






