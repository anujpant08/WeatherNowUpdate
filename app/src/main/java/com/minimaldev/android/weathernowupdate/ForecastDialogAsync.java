package com.minimaldev.android.weathernowupdate;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ANUJ on 27/12/2016.
 */
public class ForecastDialogAsync extends AsyncTask<String,Void,String> {

    String new_url;

    private Dialog_weather Dialog_weather;


    public  ForecastDialogAsync(Dialog_weather dialogWeather, String url) {

        this.Dialog_weather=dialogWeather;

        new_url=url;
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
        return response;
    }
    @Override
    public void onPostExecute(String result)
    {
        String test=result;
        if(test==null)
        {
            Toast.makeText(this.Dialog_weather,"Error fetching forecast",Toast.LENGTH_SHORT).show();
        }
        try {
            int i,j=1;
// parse the json result returned from the service
            JSONObject jsonResult = new JSONObject(test);
//parse out the co-ordinates of location


// parse out the forecast list from the JSON result
            JSONArray weather= jsonResult.getJSONArray("list");
            int l=weather.length();
            for(i=0;i<6;i++)
            {
                JSONObject obj=weather.getJSONObject(i);
                JSONObject tobj=obj.getJSONObject("temp");
                String tempmin=tobj.getString("min");
                String tempmax=tobj.getString("max");
                double min=Double.parseDouble(tempmin);
                double max=Double.parseDouble(tempmax);

                this.Dialog_weather.SetTemp(min,max,i);
                JSONArray desc=obj.getJSONArray("weather");
                JSONObject o=desc.getJSONObject(0);
                String description=o.getString("id");
                this.Dialog_weather.SetDes(description,i);

                Calendar calendar = Calendar.getInstance();
                Date today = calendar.getTime();

                calendar.add(Calendar.DAY_OF_YEAR, j);
                Date tomorrow = calendar.getTime();
                // SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
                SimpleDateFormat sdf1 = new SimpleDateFormat("EEEE");
                Date d = new Date();
                //  String t=sdf.format(tomorrow);
                String dayOfTheWeek = sdf1.format(tomorrow);
                this.Dialog_weather.SetDay(dayOfTheWeek,j);
                j++;
            }

            JSONObject hl=weather.getJSONObject(0);
            JSONObject tobj=hl.getJSONObject("temp");
            String mhl=tobj.getString("min");
            String hhl=tobj.getString("max");
            double low=Double.parseDouble(mhl);
            double high=Double.parseDouble(hhl);
            this.Dialog_weather.setHi(high);
            this.Dialog_weather.setLo(low);
            Dialog_weather.shown=true;


// set all the fields in the activity from the parsed JSON






        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private double ConvertTemperatureToFarenheit(double temperature) {
        return (temperature - 273);
    }
}


