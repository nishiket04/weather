package com.nishiket.weather.Activitis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nishiket.weather.Adapters.HourlyAdapter;
import com.nishiket.weather.Domains.Hourly;
import com.nishiket.weather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    //    provideing global veribale so it can accesed from every where
    RecyclerView.Adapter recycleraapter;
    RecyclerView recyclerView;
    String City;
    ArrayList<Hourly> items;

    TextView txt,datTxt,tmpTxt,humper,windSpd,windDir,locDetail,today,nextDays;
    LinearLayout linr1;
    ImageView deshImg;
    ProgressBar pbr;
    EditText edt;
    ImageView btn,lo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        assing view by its id
             assignView();


//        aasing arraylistobjecct
        items=new ArrayList<>();

        // this will check if there is location permission is granted or not
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
        }
        Location location = locationManager.getLastKnownLocation((LocationManager.NETWORK_PROVIDER));
        //this will get city name by lat,long
        City=getCity(location.getLatitude(),location.getLongitude());




//        set visibility of view gone until api will fetch data
            setVGone();
//        set the recylere view adpater
         initRecily(items);
//        caliing api to get data
        callTheWeather(City);


        Intent toRe=new Intent(MainActivity.this, NextDaysActivity.class);


        nextDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRe.putExtra("city",City);
                startActivity(toRe);
            }
        });

        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                City=getCity(location.getLatitude(),location.getLongitude());
                callTheWeather(City);
            }
        });


//        on button click get the text and search for that city
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                gertting the string
                City=edt.getText().toString();
                edt.setCursorVisible(false);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                to clear input text
                edt.setText("");

//                again set visibilty of view gone
                setVGone();

//                clear the item on array list so it can contain new items for new search
                items.clear();

//                caling the api to get data
                callTheWeather(City);
            }
        });


    }

    // this method set evry view visibility gone
    private void setVGone() {
        pbr.setVisibility(View.VISIBLE);
        linr1.setVisibility(View.GONE);
        tmpTxt.setVisibility(View.GONE);
        deshImg.setVisibility(View.GONE);
        datTxt.setVisibility(View.GONE);
        txt.setVisibility(View.GONE);
        locDetail.setVisibility(View.GONE);
        today.setVisibility(View.GONE);
        nextDays.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        humper=findViewById(R.id.humper);
        deshImg.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
    //    this method assigine view to its id
    private void assignView() {
        txt=findViewById(R.id.txt);
        today=findViewById(R.id.today);
        nextDays=findViewById(R.id.nextDays);
        locDetail=findViewById(R.id.locDetail);
        datTxt=findViewById(R.id.datTxt);
        deshImg=findViewById(R.id.deshImg);
        tmpTxt=findViewById(R.id.tmpTxt);
        pbr=findViewById(R.id.pbr);
        recyclerView=findViewById(R.id.next24hr);
        linr1=findViewById(R.id.linr1);
        btn=findViewById(R.id.btn);
        edt=findViewById(R.id.edt);
        windDir=findViewById(R.id.windDir);
        windSpd=findViewById(R.id.windSpd);
        lo=findViewById(R.id.lo);
    }

    //mthod for getting city name
    String getCity(double lat,double log){
        String cityNmae=null;
        Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses=gcd.getFromLocation(lat,log,10);
            for (Address adr: addresses){
                if(adr!=null){
                    String city=adr.getLocality();
                    if (city!=null && !city.equals("")){
                        cityNmae=city;
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return  cityNmae;
    }

//    this method set adapter on recyler view
    private void initRecily(ArrayList<Hourly> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recycleraapter=new HourlyAdapter(items);
        recyclerView.setAdapter(recycleraapter);
    }// End of the initRecily method


//  this method hit the api and get the data
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
                    setVVis();

//                  geting data and seting it on views
                    String name= response.getJSONObject("current").getJSONObject("condition").getString("text");
                    txt.setText(name);
                    String dateAndTime= response.getJSONObject("location").getString("localtime");
                    SimpleDateFormat im=new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    SimpleDateFormat ou=new SimpleDateFormat("dd/MM/yyyy | hh:mm aa");
                    Date d= im.parse(dateAndTime);
                    datTxt.setText(ou.format(d));
                    String temp=response.getJSONObject("current").getString("temp_c");
                    tmpTxt.setText(temp+" °C");
                    String windDirc=response.getJSONObject("current").getString("wind_dir");
                    windDir.setText(windDirc);
                    String windSpde=response.getJSONObject("current").getString("wind_kph");
                    windSpd.setText(windSpde+" Km/hr");
                    String humdi=response.getJSONObject("current").getString("humidity");
                    humper.setText(humdi+"%");
                    String city= response.getJSONObject("location").getString("name");
                    String region=response.getJSONObject("location").getString("region");
                    locDetail.setText(city+", "+region);

//                  tis loop will get the data for every 24hr forcast
                    for (int i=0;i<=23;i++) {
                        int tempc = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i).getInt("temp_c");
                        String we = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONArray("hour").getJSONObject(i).getJSONObject("condition").getString("text");
//                        clling this method so it can add item on list
                        addItem(i,tempc,we);
                    }
                    //calling this method so it can set image according to weathor
                    imgChooser(name);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name", Toast.LENGTH_SHORT).show();
            }
        });
        //adding apirequest in queue
        requestQueue.add(objectRequest);
    }//End of callTheWeather method.

    //    this method set Visibilty of evry view visible
    private void setVVis() {
        pbr.setVisibility(View.GONE);
        linr1.setVisibility(View.VISIBLE);
        tmpTxt.setVisibility(View.VISIBLE);
        deshImg.setVisibility(View.VISIBLE);
        datTxt.setVisibility(View.VISIBLE);
        txt.setVisibility(View.VISIBLE);
        locDetail.setVisibility(View.VISIBLE);
        today.setVisibility(View.VISIBLE);
        nextDays.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    //    this method add items on arraylist
    private void addItem(int i, int tempc, String we) {
        we = we.toLowerCase();
        String num = String.valueOf(i+1);
        if(i<11) {
            if (we.contains("cloudy")) {
                items.add(new Hourly(num + "Am", tempc, "cloudy"));
            } else if (we.contains("mist")) {
                items.add(new Hourly(num + "Am", tempc, "windy"));
            } else if (we.contains("cloudy sunny") || we.contains("overcast")) {
                items.add(new Hourly(num + "Am", tempc, "cloudy_sunny"));
            } else if (we.contains("rain")) {
                items.add(new Hourly(num + "Am", tempc, "rainy"));
            } else if (we.contains("sunny") || we.contains("clear")) {
                items.add(new Hourly(num + "Am", tempc, "sunny"));
            } else if (we.contains("wind") || we.contains("windy")) {
                items.add(new Hourly(num + "Am", tempc, "windy"));
            } else if (we.contains("strom")) {
                items.add(new Hourly(num + "Am", tempc, "storm"));
            } else if (we.contains("snow") || we.contains("sonwy")) {
                items.add(new Hourly(num + "Am", tempc, "snowy"));
            }
        } else if (i==11) {
            if (we.contains("cloudy")) {
                items.add(new Hourly(num + "pm", tempc, "cloudy"));
            } else if (we.contains("mist")) {
                items.add(new Hourly(num + "pm", tempc, "windy"));
            } else if (we.contains("cloudy sunny") || we.contains("overcast")) {
                items.add(new Hourly(num + "pm", tempc, "cloudy_sunny"));
            } else if (we.contains("rain")) {
                items.add(new Hourly(num + "pm", tempc, "rainy"));
            } else if (we.contains("sunny") || we.contains("clear")) {
                items.add(new Hourly(num + "pm", tempc, "sunny"));
            } else if (we.contains("wind") || we.contains("windy")) {
                items.add(new Hourly(num + "pm", tempc, "windy"));
            } else if (we.contains("strom")) {
                items.add(new Hourly(num + "pm", tempc, "storm"));
            } else if (we.contains("snow") || we.contains("sonwy")) {
                items.add(new Hourly(num + "pm", tempc, "snowy"));
            }

        }else if (i==23) {
            if (we.contains("cloudy")) {
                items.add(new Hourly("12Am", tempc, "cloudy"));
            } else if (we.contains("mist")) {
                items.add(new Hourly("12Am", tempc, "windy"));
            } else if (we.contains("cloudy sunny") || we.contains("overcast")) {
                items.add(new Hourly("12Am", tempc, "cloudy_sunny"));
            } else if (we.contains("rain")) {
                items.add(new Hourly("12Am", tempc, "rainy"));
            } else if (we.contains("sunny") || we.contains("clear")) {
                items.add(new Hourly("12Am", tempc, "sunny"));
            } else if (we.contains("wind") || we.contains("windy")) {
                items.add(new Hourly("12Am", tempc, "windy"));
            } else if (we.contains("strom")) {
                items.add(new Hourly("12Am", tempc, "storm"));
            } else if (we.contains("snow") || we.contains("sonwy")) {
                items.add(new Hourly("12Am", tempc, "snowy"));
            }

        }
        else {
            int d=i-12+1;
            String dt=String.valueOf(d);
                if (we.contains("cloudy")) {
                    items.add(new Hourly(dt + "pm", tempc, "cloudy"));
                } else if (we.contains("mist")) {
                    items.add(new Hourly(dt + "pm", tempc, "windy"));
                } else if (we.contains("cloudy sunny") || we.contains("overcast")) {
                    items.add(new Hourly(dt + "pm", tempc, "cloudy_sunny"));
                } else if (we.contains("rain")) {
                    items.add(new Hourly(dt + "pm", tempc, "rainy"));
                } else if (we.contains("sunny") || we.contains("clear")) {
                    items.add(new Hourly(dt + "pm", tempc, "sunny"));
                } else if (we.contains("wind") || we.contains("windy")) {
                    items.add(new Hourly(dt + "pm", tempc, "windy"));
                } else if (we.contains("strom")) {
                    items.add(new Hourly(dt + "pm", tempc, "storm"));
                } else if (we.contains("snow") || we.contains("sonwy")) {
                    items.add(new Hourly(dt + "pm", tempc, "snowy"));
                }
            }
    }//End of the addItem method

//  this method choose which pic to set on deshbord according to weather data
    public void imgChooser(String weather){
       weather = weather.toLowerCase();
        if(weather.contains("cloudy")){
            int id=R.drawable.cloudy;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        }else if (weather.contains("mist")){
            int id=R.drawable.windy;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("cloudy sunny") || weather.contains("overcast")) {
            int id=R.drawable.cloudy_sunny;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("rain")) {
            int id=R.drawable.rainy;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("sunny") || weather.contains("clear")) {
            int id=R.drawable.sunny;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("wind") || weather.contains("windy")) {
            int id=R.drawable.windy;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("strom")) {
            int id=R.drawable.storm;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        } else if (weather.contains("snow") || weather.contains("sonwy")) {
            int id=R.drawable.snowy;
            deshImg.setImageDrawable(getResources().getDrawable(id));
            deshImg.setVisibility(View.VISIBLE);
        }
        return;

    }//End of imgChooser method.

    // this will requ. permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Provide Permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}