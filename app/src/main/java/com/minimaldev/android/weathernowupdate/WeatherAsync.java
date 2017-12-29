package com.minimaldev.android.weathernowupdate;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by ANUJ on 20/12/2016.
 */
public class WeatherAsync extends AsyncTask <String,Void,String> {

   // private Context context;
     String new_url;
    String enc;

    String key="6024521-aa3caf3e11ed4bf5eead80356",secret="WHjf5GVFChvtUFsxYpBv4XbaYK7Mb5BNpYfN3t3FhQzKc";

    //String api="28f81515b88702b7a13f0debfe474d013c29decba95bc3741ae569dc4dc03673";


    private WeatherActivity WeatherActivity;

    LinearLayout linearLayout;

    public  WeatherAsync(WeatherActivity weatherActivity, String url) {

        this.WeatherActivity = weatherActivity;
        //dialog=new ProgressDialog(weatherActivity);
        String condition=url;
        new_url="https://api.unsplash.com/search/photos?page=1&query="+condition+"&client_id=28f81515b88702b7a13f0debfe474d013c29decba95bc3741ae569dc4dc03673";
        //new_url="https://pixabay.com/api/?key="+key+"&q="+enc+"&image_type=photo&category=travel&order=popular&per_page=200";

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
    protected String doInBackground(String ... urls)  {
        // this weather service method will be called after the service executes.
        // it will run as a seperate process, and will populate the activity in the onPostExecute
        // method below



        String response="";
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(new_url)).openConnection();

           // con.addRequestProperty("Authorization","Bearer "+secret);
            con.setRequestMethod("GET");
            //con.setRequestProperty("Accept-Version","v1");
            //con.setRequestProperty("Authorization","Client-ID "+api);

            //con.setRequestProperty("Accept-Language","json");
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
        return response;
    }
    @Override
    public void onPostExecute(String result)
    {

        System.out.println(result);

        /*if(dialog.isShowing())
        {
            dialog.dismiss();
        } */


        if(result==null)
        {
            //Toast.makeText(this.WeatherActivity,"Error fetching weather!",Toast.LENGTH_SHORT).show();
            LayoutInflater inflater=this.WeatherActivity.getLayoutInflater();
            View layout=inflater.inflate(R.layout.toastview,(ViewGroup)this.WeatherActivity.findViewById(R.id.toast_layout));
            TextView text=(TextView)layout.findViewById(R.id.text_toast);
            text.setText("Error fetching weather!");

            Toast toast=new Toast(this.WeatherActivity);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,80);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();

        }

        try {
// parse the json result returned from the service
            JSONObject jsonResult = new JSONObject(result);
//parse out the co-ordinates of location


// parse out the temperature from the JSON result
            /*double temperature = jsonResult.getJSONObject("main").getDouble("temp");
            temperature = ConvertTemperatureToFarenheit(temperature);

            // parse out the pressure from the JSON Result
            double pressure = jsonResult.getJSONObject("main").getDouble("pressure");

// parse out the humidity from the JSON result
            double humidity = jsonResult.getJSONObject("main").getDouble("humidity");

            double min=jsonResult.getJSONObject("main").getDouble("temp_min");
            double max=jsonResult.getJSONObject("main").getDouble("temp_max");

            min= ConvertTemperatureToFarenheit(min);
            max= ConvertTemperatureToFarenheit(max);

// parse out the description from the JSON result
            String description = jsonResult.getJSONArray("weather").getJSONObject(0).getString("description");
//parse out weather id
            String id=jsonResult.getJSONArray("weather").getJSONObject(0).getString("id");

            //parse out city name
            String locate=jsonResult.getString("name");
            //parse out country
            String cntry=jsonResult.getJSONObject("sys").getString("country");



// set all the fields in the activity from the parsed JSON
            this.WeatherActivity.SetDescription(description);
            this.WeatherActivity.SetTemperature(temperature,min,max);
            //this.WeatherActivity.SetPressure(pressure);
            //
            //
            //
            //
            // this.WeatherActivity.SetHumidity(humidity);

            this.WeatherActivity.sendNotification(description,temperature,locate,id); */

            Random x = new Random();
            String uri;

            //String hits=jsonResult.getString("total");
            //int h=Integer.parseInt(hits);

            JSONArray jsonArray=jsonResult.getJSONArray("results");


            JSONObject j = jsonArray.getJSONObject(x.nextInt(5));
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

            //this.WeatherActivity.SetWeatherIcon(uri);






        }
        catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error: ",e.getMessage());
        }
    }
    private double ConvertTemperatureToFarenheit(double temperature) {
        return (temperature - 273);
    }

}
