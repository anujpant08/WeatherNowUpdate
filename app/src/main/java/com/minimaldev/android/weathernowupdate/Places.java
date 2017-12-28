package com.minimaldev.android.weathernowupdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

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
        //Intent intent = getIntent();
        //search = (Button) findViewById(R.id.button_search);

        /*ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        } */

        setContentView(R.layout.search_window);

        placesAutocompleteTextView=(PlacesAutocompleteTextView)findViewById(R.id.placesauto);

        placesAutocompleteTextView.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

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

                    }
                });
            }
        });

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Search");

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






