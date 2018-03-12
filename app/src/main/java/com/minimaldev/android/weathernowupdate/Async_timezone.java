package com.minimaldev.android.weathernowupdate;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ANUJ on 30/12/2016.
 */
public class Async_timezone extends AsyncTask<String,Void,String> {

    String new_url;

    private Dialog_weather DialogWeather;

    private ProgressDialog dialog;
    public  Async_timezone(Dialog_weather dialogWeather, String url) {

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
            con.setRequestMethod("GET");
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
        return response;
    }
    @Override
    public void onPostExecute(String result)
    {
        String test=result;

        try {
// parse the json result returned from the service
            JSONObject jsonResult = new JSONObject(test);
//parse out the co-ordinates of location
            //JSONObject jsonObject=jsonResult.getJSONObject("formatted");
            String time=jsonResult.getString("formatted");



// set all the fields in the activity from the parsed JSON
            this.DialogWeather.time(time);




        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
