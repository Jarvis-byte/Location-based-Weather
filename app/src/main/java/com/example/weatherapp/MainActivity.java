package com.example.weatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.AsynchronousChannelGroup;

public class MainActivity extends AppCompatActivity {
    EditText editText;
TextView resultTextView;

public class DownloadTask extends AsyncTask<String,Void,String>
{

    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;

        HttpURLConnection urlConnectiom = null;

        try {

            url=new URL(urls[0]);
            urlConnectiom=(HttpURLConnection)url.openConnection();
            InputStream in=urlConnectiom.getInputStream();
            InputStreamReader reader=new InputStreamReader(in);
            int data= reader.read();
            while(data!=-1)
            {
                char current = (char)data;

                result+=current;

                data=reader.read();
            }
            return result;



        } catch (Exception e)
        {
            e.printStackTrace();
          //  Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            return null;
        }
   }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);


            String weatherInfo = jsonObject.getString("weather");



            Log.i("Weather Content", weatherInfo);


            JSONArray arr = new JSONArray(weatherInfo);

          String message="";

            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject jsonPart=arr.getJSONObject(i);

         String main=jsonPart.getString("main");

           String description=jsonPart.getString("description");

         if(!main.equals(null) && !description.equals(null))
              {
    message+= main + ": " + description + "\r\n";
               }

            }
if(!message.equals(null))
{
    resultTextView.setText(message);
}
else
{
    //Toast.makeText(getApplicationContext(), "Could not find Weather:(", Toast.LENGTH_SHORT).show();
}

        }
        catch (Exception e)
        {
            //Toast.makeText(getApplicationContext(), "Could not find Weather:(", Toast.LENGTH_SHORT).show();
e.printStackTrace();
        }

    }
}

public  void getWeather(View x)
{

        try {
            DownloadTask task = new DownloadTask();
            String endodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q=" + endodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02").get();

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        }
        catch (Exception e) {
          //  Toast.makeText(getApplicationContext(), "Could not find Weather:(", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }





}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                    editText =findViewById(R.id.editTextTextPersonName);
                    resultTextView=(TextView) findViewById(R.id.resultTextView);
    }
}