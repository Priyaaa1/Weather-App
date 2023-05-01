package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV, temperatureTV, conditionTV;
    private RecyclerView weatherRV;
    private TextInputEditText cityEdt;
    private ImageView backIV, iconIV, searchIV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_main);
        homeRL = findViewById(R.id.idRLHome);
        loadingPB = findViewById(R.id.idPBLoading);
        cityNameTV = findViewById(R.id.idTVCityName);
        temperatureTV = findViewById(R.id.idTVTemperature);
        conditionTV = findViewById(R.id.idTVCondition);
        weatherRV = findViewById(R.id.idRvWeather);
        cityEdt = findViewById(R.id.idEdtCity);
        backIV = findViewById(R.id.idIVBack);
        iconIV = findViewById(R.id.idIVIcon);
        searchIV = findViewById(R.id.idIVSearch);
        weatherRVModalArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName=getCityName(location.getLongitude(),location.getLatitiude());
        getWeatherInfo(cityName);
        searchIV.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void OnClick(View v){
                String city=cityEdt.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(context:MainActivity.this,text:"Please enter city Name",Toast.LENGTH_SHORT).show();
                }else{
                    cityNameTV.setText(cityName);
                    getWeatherInfo(city);
                }
        }
    });
    }

@Override

public void onRequestPermissionResult(int requestCode,@NonNull @org.jetbrains.annotations.NotNull String[] permissions,@NonNull @org.jetbrains.annotations.NotNull int[])
    super.onRequestPermissionResult(requestCode,permissions,grantResults);
if(requestCode==PERMISSION_CODE){
    if(grantResults.length>0 && grantResults[0]==PackagrManager.PERMISSION_GRANTED){
            Toast.makeText(context:this,text:"Permissions granted..",Toast.LENGTH_SHORT).show();
            }else{
        Toast.makeText(context:this,text:"Please provide the permissions",Toast.LENGTH_SHORT").show();
         finish();
            }
            }

    private String getCityName(double longitude,double latitude ){
    String cityName = "Not found ";
        Geocoder gcd=new Geocoder((getBaseContext().Locale.getDefault());
        try {
            List<Address> addresses=gcd.getFromLocation(latitude,longitude,maxResults:10);
             for(Address adr :addresses)
             {
                 if (adr != null) {
                     String city = adr.getLocality();
                     if(city!=null && !city.equals("")){
                         cityName=city;
                     }
                     else{
                         Log.d(tag:"TAG",msg:"CITY NOT FOUND");
                         Toast.makeText(context:this,text:"User City Not Found",Toast.LENGTH_SHORT).show();
                     }
                 }
        }
        }catch(IOException e){
            e.printStackTrace();
        }
        return cityName;

}
    private void getWeatherInfo( String cityName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=0bbffba1f423474d9ab112410232204&q="+cityName +"&days=1&aqi=no&alerts=no"
       cityNameTV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(context:MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,listener:null,new Response.Listener<JSONOBJECT>)
        @Override
public void onResponse(JSONObject response){
            loadingPB.setVisiblility(View.GONE);
            homeRL.setVisibility(View.VISIBLE);
            weatherRVModalArrayList.clear();
try{
            String  temperature =  response.getJSONObject("current").getString(name:"temp_c");
            temperatureTV.setText(temperature+"°c");
            int isDay = response.getJSONObject("current").getInt(name:"is_day");

        }catch(JSONException e){
e.printStackTrace();}
        }


},new Response.ErrorListener(){
    @Override
public void ErrorResponse(VolleyError error){
        Toast.makeText(context:MainActivity,this,text:"Please enter valid city name..",Toast.LENGTH_SHORT).show();
        }
        });

        requestQueue.add(jsonObjectRequest);

