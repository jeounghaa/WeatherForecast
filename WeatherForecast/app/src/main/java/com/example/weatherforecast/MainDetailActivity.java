package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainDetailActivity extends AppCompatActivity {

    Intent intent;

    String str;

    TextView tvArea3, tvTemp3, tvMinTemp, tvMaxTemp, tvHumidity, tvSpeed, tvClouds;

    ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        intent = getIntent();

        str = intent.getStringExtra("area");

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        tvArea3 = findViewById(R.id.tvArea3);
        tvTemp3 = findViewById(R.id.tvTemp3);
        tvMinTemp = findViewById(R.id.tvMinTemp);
        tvMaxTemp = findViewById(R.id.tvMaxTemp);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvClouds = findViewById(R.id.tvClouds);

        tvArea3.setText(str);

        getRequest(str);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getRequest(final String str) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?q="+str+"&appid=82d6a21bd4c148facc22d2f9302ac891&lang=kr",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject weather = response.getJSONObject("main");
                    JSONObject weather2 = response.getJSONObject("wind");
                    JSONObject weather3 = response.getJSONObject("clouds");

                    String temp = weather.getString("temp");
                    String min = weather.getString("temp_min");
                    String max = weather.getString("temp_max");
                    String hum = weather.getString("humidity");
                    String speed = weather2.getString("speed");
                    String clouds = weather3.getString("all");

                    Double temp2 = Double.parseDouble(temp) - 273.15;
                    Double min2 = Double.parseDouble(min) - 273.15;
                    Double max2 = Double.parseDouble(max) - 273.15;

                    tvTemp3.setText(String.format("%.0f", temp2) + " ℃");
                    tvMinTemp.setText("최저 " + String.format("%.0f", min2) + " ℃");
                    tvMaxTemp.setText("최고 " + String.format("%.0f", max2) + " ℃");
                    tvHumidity.setText(hum + " %");
                    tvSpeed.setText(speed + " m/s");
                    tvClouds.setText(clouds + " %");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);

    }
}
