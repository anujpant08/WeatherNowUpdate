package com.minimaldev.android.weathernowupdate;

import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ANUJ on 24/12/2016.
 */
public class Async_weather extends AsyncTask<String,Void,String> {

    // private Context context;
    String new_url;
    String lat,lon;
    String fullLocation;
    private Dialog_weather DialogWeather;
    private ProgressDialog dialog;
    String api="28f81515b88702b7a13f0debfe474d013c29decba95bc3741ae569dc4dc03673";

    ArrayList<String> arr=new ArrayList<>();
    String text="";

    public  Async_weather(Dialog_weather dialogWeather, String url) {

        this.DialogWeather = dialogWeather;
        //dialog=new ProgressDialog(dialogWeather);
        String condition=url;
        new_url="https://api.unsplash.com/search/photos?page=1&query="+condition+"&client_id=28f81515b88702b7a13f0debfe474d013c29decba95bc3741ae569dc4dc03673";
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
            con.setRequestProperty("Accept-Version","v1");
            con.setRequestProperty("Authorization","Client-ID "+api);
            con.setDoInput(true);

            con.setDoOutput(false);
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
            /*JSONObject coord= jsonResult.getJSONObject("coord");
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
            this.DialogWeather.SetWeatherIcon(id);
            //this.DialogWeather.time(dateTime);
            this.DialogWeather.setcor(lat,lon); */


            Random x = new Random();
            String s,f;
            String uri;
            int c,pos;
            String hits=jsonResult.getString("total");
            //int h=Integer.parseInt(hits);

            JSONArray jsonArray=jsonResult.getJSONArray("results");


                JSONObject j = jsonArray.getJSONObject(x.nextInt(10));
                //uri=j.getString("webformatURL");

            JSONObject jsonObject=j.getJSONObject("urls");
            uri=jsonObject.getString("regular");

            //JSONArray array=j.getJSONArray("display_sizes");
            //JSONObject js=array.getJSONObject(0);



            /*pos=uri.indexOf('_');
            c=uri.lastIndexOf('.');
            s=uri.substring(0,pos);
            f=uri.substring(c,uri.length());
            String u=s+"_960"+f; */

            System.out.println(uri);
            //this.DialogWeather.SetWeatherIcon(uri);



        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private double ConvertTemperatureToFarenheit(double temperature) {
        return (temperature - 273);
    }


}
