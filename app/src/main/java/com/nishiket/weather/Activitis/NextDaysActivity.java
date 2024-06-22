package com.nishiket.weather.Activitis;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nishiket.weather.Adapters.FutureAdapter;
import com.nishiket.weather.Adapters.HourlyAdapter;
import com.nishiket.weather.Domains.FutureDomain;
import com.nishiket.weather.Domains.Hourly;
import com.nishiket.weather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NextDaysActivity extends AppCompatActivity {

    RecyclerView.Adapter fure;
    RecyclerView rec;
    ArrayList<FutureDomain> items;
    ConstraintLayout bck;
    TextView nextDayTemp,nextDaywethr,nextDayWindir,nextDayWindspd,nextDayhumper;
    ImageView nextDayImg;
    String city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_days);

        rec=findViewById(R.id.nextDaysRecy);
        nextDayTemp=findViewById(R.id.nextDaysTemp);
        nextDayhumper=findViewById(R.id.nextDaysHumper);
        nextDayWindir=findViewById(R.id.nextDaysWindDir);
        nextDayWindspd=findViewById(R.id.nextDaysWindSpd);
        nextDaywethr=findViewById(R.id.nextDaysWether);
        nextDayImg=findViewById(R.id.nextDaysImg);
        bck=findViewById(R.id.bak);

        items=new ArrayList<>();
        Intent fromRe=getIntent();
        city=fromRe.getStringExtra("city");
        callTheWeather(city);
        initReList(items);
        //items.add(0,new FutureDomain("2023-08-31","cloudy","cloudy",25,26));

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
    private void addItem(int maxTemp,int minTemp,String date, String we) {
        we = we.toLowerCase();
        if(we.contains("cloudy")){
            items.add(new FutureDomain(date,"cloudy",we,maxTemp,minTemp));
        }else if (we.contains("mist")){
            items.add(new FutureDomain(date,"windy",we,maxTemp,minTemp));
        } else if (we.contains("cloudy sunny") || we.contains("overcast")) {
            items.add(new FutureDomain(date,"cloudy_sunny",we,maxTemp,minTemp));
        } else if (we.contains("rain")) {
            items.add(new FutureDomain(date,"rainy","rainy",maxTemp,minTemp));
        } else if (we.contains("sunny") || we.contains("clear")) {
            items.add(new FutureDomain(date,"sunny",we,maxTemp,minTemp));
        } else if (we.contains("wind") || we.contains("windy")) {
            items.add(new FutureDomain(date,"windy",we,maxTemp,minTemp));
        } else if (we.contains("strom")) {
            items.add(new FutureDomain(date,"storm",we,maxTemp,minTemp));
        } else if (we.contains("snow") || we.contains("sonwy")) {
            items.add(new FutureDomain(date,"snowy",we,maxTemp,minTemp));
        }
    }//End of the addItem method

    private void initReList(ArrayList<FutureDomain> items) {
        rec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        fure=new FutureAdapter(items);
        rec.setAdapter(fure);
    }

    public void callTheWeather(String City){
        //instilling view

        // giving url with city name
        String url = "https://api.weatherapi.com/v1/forecast.json?key=d72b44a2d311451bba1141823230408&q="+City+"&days=7&aqi=yes&alerts=yes";
        //Volly response code
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Parsing Api response
                try {
//                    set visibilty of views visible so it can show data


//                  geting data and seting it on views
                    String name= response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("day").getJSONObject("condition").getString("text");
                    nextDaywethr.setText(name);
                    String temp=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("day").getString("avgtemp_c");
                    nextDayTemp.setText(temp+" °C");
                    String windDirc=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("day").getString("daily_chance_of_rain");
                    nextDayWindir.setText(windDirc+"%");
                    String windSpde=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("day").getString("maxwind_kph");
                    nextDayWindspd.setText(windSpde+" Km/hr");
                    String humdi=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(1).getJSONObject("day").getString("avghumidity");
                    nextDayhumper.setText(humdi+"%");

//                  tis loop will get the data for every 24hr forcast
                    for (int i=1;i<=2;i++) {
                        int maxTempc = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(i).getJSONObject("day").getInt("maxtemp_c");
                        int minTempc=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(i).getJSONObject("day").getInt("mintemp_c");
                        String date=response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(i).getString("date");
                        String we = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(i).getJSONObject("day").getJSONObject("condition").getString("text");
//                        clling this method so it can add item on list
                        addItem(maxTempc,minTempc,date,we);
                    }
                    fure.notifyDataSetChanged();
                    //calling this method so it can set image according to weathor
                    imgChooser(name);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NextDaysActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });
        //adding apirequest in queue
        requestQueue.add(objectRequest);
    }//End of callTheWeather method.



    //  this method choose which pic to set on deshbord according to weather data
    public void imgChooser(String weather){
        weather = weather.toLowerCase();
        if(weather.contains("cloudy")){
            int id=R.drawable.cloudy;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        }else if (weather.contains("mist")){
            int id=R.drawable.windy;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("cloudy sunny") || weather.contains("overcast")) {
            int id=R.drawable.cloudy_sunny;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("rain")) {
            int id=R.drawable.rainy;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("sunny") || weather.contains("clear")) {
            int id=R.drawable.sunny;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("wind") || weather.contains("windy")) {
            int id=R.drawable.windy;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("strom")) {
            int id=R.drawable.storm;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("snow") || weather.contains("sonwy")) {
            int id=R.drawable.snowy;
            nextDayImg.setImageDrawable(getResources().getDrawable(id));
            nextDayImg.setVisibility(View.VISIBLE);
        }
        return;

    }//End of imgChooser method.
}