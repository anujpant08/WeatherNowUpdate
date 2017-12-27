package com.minimaldev.android.weathernowupdate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by ANUJ on 30/03/2017.
 */

public class FavActivity extends Activity
{

    String loct="",val="";
    int count=0;


    ArrayList<String> list= new ArrayList<String>();


public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


    Intent intent=getIntent();
    Intent in;
    list=intent.getStringArrayListExtra("LOCATION_NAME");
        //val=intent.getStringExtra("COUNTER_VALUE");
        //count=Integer.parseInt(val);

        list.add(loct);  //adding elements to list

         in=new Intent(FavActivity.this,DisplayFav.class);
         in.putExtra("LIST",list);

    }

}
