package com.minimaldev.android.weathernowupdate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ANUJ on 30/03/2017.
 */

public  class DisplayFav extends AppCompatActivity {


    List<String> array_list = new ArrayList<String>();
    List<String> arr = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> mstring = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String l = "";
    String url;
    String t = "";
    Snackbar snackbar;
    SlideAnimationUtil slideAnimationUtil=new SlideAnimationUtil();
    public static Set<String> set;
    String filename = "WN_FAVPLACES";
   // FileOutputStream fileOutputStream;
    //FileReader fileReader;
    //FileInputStream fileInputStream;
    File path;
    File file;
    String n = "";
    String favlist[] = new String[1000];

    ListView listView;
    String text = "";
    int index = 0;
    int a = 0;

    int count;
    //String [] a= new String[]{"Lucknow","London"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        l = intent.getStringExtra("LOCATION_NAME");
        count = intent.getIntExtra("COUNTER_VALUE", 0);


        l = l + "\n";
        perform();
    }


    public void  perform() {
        setContentView(R.layout.fav_display);

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayFav.this,Places.class);
                startActivity(intent);
                finish();
            }
        });
        //getSupportActionBar().setElevation(0);

        snackbar = Snackbar.make(findViewById(R.id.coor), "Item removed", Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.thunder));
        TextView textView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.Magenta));
        textView.setTextSize(15);
        path = Environment.getExternalStorageDirectory();
        file = new File(path + "/" + filename);
        FileOutputStream fileOutputStream;
        //path.mkdirs();
        try {

            fileOutputStream = openFileOutput(filename, Context.MODE_APPEND);
            fileOutputStream.write(l.getBytes());
            fileOutputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[2048];


        listView = (ListView) findViewById(R.id.list);
        int flag = 0;
        //arr=new String[100];
        try {
            //File file=new File(path,filename);
            FileInputStream fileInputStream;
            fileInputStream = openFileInput(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

            while ((text = br.readLine()) != null) {
                if (!text.equals("null")) {
                    arr.add(a, text);
                    a++;
                }
            }

            set = new HashSet<String>(arr);

            array_list.addAll(set);
            fileInputStream.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }


        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.list_content, array_list);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(slideAnimationUtil);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                t = array_list.get(position);

                TextView textView = (TextView) findViewById(R.id.list_content);

                if (slideAnimationUtil.swipeDetected()) {


                    if (slideAnimationUtil.getAction() == SlideAnimationUtil.Action.RL) {

                        String str = array_list.get(position);


                        adapter.notifyDataSetChanged();

                        if (textView.getVisibility() == View.VISIBLE) {
                            slideInView(view);
                        }
                        snackbar.show();
                        array_list.remove(position);
                        int pos = 0;
                        try {
                            FileOutputStream fileOutputStream;
                            fileOutputStream = openFileOutput(filename, 0);
                            fileOutputStream.write(n.getBytes());
                            fileOutputStream.close();

                            fileOutputStream = openFileOutput(filename, 0);
                            while (pos < array_list.size()) {
                                String newline = array_list.get(pos) + "\n";
                                fileOutputStream.write(newline.getBytes());
                                pos++;

                            }
                            //deleteFile(filename);
                            pos = 0;

                            fileOutputStream.close();
                            //action(str);
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {

                    Intent i = new Intent(DisplayFav.this, Dialog_weather.class);
                    url = "http://api.openweathermap.org/data/2.5/weather?q=" + t + "&appid=bcc6f8e44743e316e5120301ff1a5ad4";
                    i.putExtra("URL", url);
                    i.putExtra("loc_desc", t);
                    startActivity(i);
                    finish();
                }


            }
        });
    }

    public void goback(View view)
    {
        finish();
    }


    private void slideInView(View view) {
        Animation slideIn = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_from_right);
        if (slideIn != null) {
            slideIn.setAnimationListener(new ViewAnimationListener(view) {
                @Override
                protected void onAnimationStart(View view, Animation animation) {
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                protected void onAnimationEnd(View view, Animation animation) {

                }
            });
            view.startAnimation(slideIn);
        }
    }

    private void slideOutView(View view) {
        Animation slideOut = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_to_right);
        if (slideOut != null) {
            slideOut.setAnimationListener(new ViewAnimationListener(view) {
                @Override
                protected void onAnimationStart(View view, Animation animation) {

                }

                @Override
                protected void onAnimationEnd(View view, Animation animation) {
                    view.setVisibility(View.GONE);
                }
            });
            view.startAnimation(slideOut);
        }
    }

    private abstract class ViewAnimationListener implements Animation.AnimationListener {

        private final View view;

        protected ViewAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            onAnimationStart(this.view, animation);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onAnimationEnd(this.view, animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        protected abstract void onAnimationStart(View view, Animation animation);
        protected abstract void onAnimationEnd(View view, Animation animation);
    }

}


