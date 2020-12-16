package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    EditText etArea;
    Button btnConfirm, btnCha;
    TextView tvResult;
    ImageView imageView;

    String str = "";
    String url;

//    class Weather extends AsyncTask<String, Void, String> {// URL is in String, nothing, Return type will be String
//
//        @Override
//        protected String doInBackground(String... address) {
//            //String.. means multiple address can be send. it acts as array
//            try {
//                URL url = new URL(address[0]);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                // retrieve data from url
//                InputStream is = connection.getInputStream();
//                InputStreamReader isr = new InputStreamReader(is);
//
//                int data = isr.read();
//                String content = "";
//                char ch;
//
//                while (data != -1) {
//                    ch = (char) data;
//                    content = content + ch;
//                    data = isr.read();
//                }
//                return content;
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // 이미지 가져오기위함
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MODE_PRIVATE);

        etArea = findViewById(R.id.etArea);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);
        tvResult = findViewById(R.id.tvResult);
        btnCha = findViewById(R.id.btnCha);
        btnCha.setOnClickListener(this);
        imageView = findViewById(R.id.imageView);

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

//        Weather weather = new Weather();
//        try {
//            content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q=" + str +
//                    "&appid=82d6a21bd4c148facc22d2f9302ac891&lang=kr").get();
//
//            Log.i("연결되었니?", content);
//
//            // json
//            JSONObject jsonObject = new JSONObject(content);
//            String weatherData = jsonObject.getString("weather");
//            Log.i("나오세요!!!!!!!!", weatherData);
//            JSONArray array = new JSONArray(weatherData);
//            String main = "";
//            String description = "";
//
//            for (int i=0; i<array.length(); i++) {
//                JSONObject weatherPart = array.getJSONObject(i);
//                main = weatherPart.getString("main");
//                description = weatherPart.getString("description");
//            }
//            Log.i("main!!!!!!!!!!!!!!!", main);
//            Log.i("description!!!!!!!!!!!", description);
//            tvResult.append(main + "\n");
//            tvResult.append(description);
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCha:
                getImage();
            case R.id.btnConfirm:
//                getData(str);
                str = etArea.getText().toString();
                url = "http://api.openweathermap.org/data/2.5/weather?q="+str+"&appid=82d6a21bd4c148facc22d2f9302ac891&lang=kr";
                getRequest(url);
                etArea.setText("");
        }
    }

    // 외부 이미지 가져오기
    public void getImage(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        Uri uri = CallLog.Calls.CONTENT_URI;
        String[] imgSet = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE
//                CallLog.Calls.DATE, CallLog.Calls.NUMBER
        };
        Cursor cursor = getContentResolver().query(uri, imgSet, null, null, null);

        if (cursor == null || !cursor.moveToFirst()){
            Log.e("없어요..", "cursor가 null,,,비어있어요");
            return;
//            return"기록 x";
        }
//        StringBuffer sb = new StringBuffer();
//        sb.append("날짜....\n");
        do {
//            long callDate = cursor.getLong(0);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String date_str = sdf.format(new Date(callDate));
//            sb.append(date_str + ":");
//            sb.append(cursor.getString(1));
            String contenturi = uri.toString() + "/" + cursor.getString(0);
            Log.e("제발..ㅠㅠ", contenturi);
            try {
                InputStream is = getContentResolver().openInputStream(Uri.parse(contenturi));
                if (is != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } while (cursor.moveToNext());
    }

    public void getRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String weatherData = response.getString("weather");
                    JSONObject weather = response.getJSONObject("main");
//                    String weatherData2 = response.getString("main");
                    Log.e("★★★★★★★★★★★", weatherData);
                    Log.e("★★★★★★★★★★★", response.toString());
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                    JSONArray array = new JSONArray(weatherData);
                    String main = "";
                    String description = "";
                    String icon = "";

                    String temp = weather.getString("temp");
                    Double temp2 = Double.parseDouble(temp) - 273.15;
                    Log.e("☆★★★★★★★★★★★",  temp2+ " 룰르");
                    for (int i=0; i<array.length(); i++) {
                        JSONObject weatherPart = array.getJSONObject(i);
//                        JSONObject weatherPart2 = weatherData2.get;

                        main = weatherPart.getString("main");
                        description = weatherPart.getString("description");
                        icon = weatherPart.getString("icon");
//                        temp = weatherPart2.getString("temp");
                        Log.e("☆★★★★★★★★★★★", temp);

                    }
//                    imgGET(icon);
                    tvResult.append("\n"+main+"\n");
                    tvResult.append(description);
                    tvResult.append(imgGET(icon));

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
        Toast.makeText(getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();


    }

    public Spanned imgGET(final String icon){
        Spanned imgText = Html.fromHtml("<img src=\"icon_drag\" width=\"30\" height=\"30\"", new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                if (source.equals("icon_drag")) {
                    Drawable drawable = null;
                    Log.e("●●●●●●●●", icon);
                    if (icon.contains("09")||icon.contains("10")){
                        drawable = getResources().getDrawable(R.drawable.ic_wi_rain);
                    } else if(icon.contains("01")){
                        drawable = getResources().getDrawable(R.drawable.ic_wi_day_sunny);
                    } else if(icon.contains("13")){
                        drawable = getResources().getDrawable(R.drawable.ic_wi_snow);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.ic_wi_cloud);
                    }
                    drawable.setBounds(0, 0, 30, 30);
                    return drawable;
                }
                return null;
            }
        }, null);
        return imgText;
    }

//    public void getData(String str){
//        String content;
//        Weather weather = new Weather();
//        try {
//            content = weather.execute("http://api.openweathermap.org/data/2.5/weather?q=" + str +
//                    "&appid=82d6a21bd4c148facc22d2f9302ac891&lang=kr").get();
//
//            Log.i("연결되었니?", content);
//
//            // json
//            JSONObject jsonObject = new JSONObject(content);
//            String weatherData = jsonObject.getString("weather");
//            Log.i("나오세요!!!!!!!!", weatherData);
//            JSONArray array = new JSONArray(weatherData);
//            String main = "";
//            String description = "";
//
//            for (int i=0; i<array.length(); i++) {
//                JSONObject weatherPart = array.getJSONObject(i);
//                main = weatherPart.getString("main");
//                description = weatherPart.getString("description");
//            }
//            Log.i("main!!!!!!!!!!!!!!!", main);
//            Log.i("description!!!!!!!!!!!", description);
//            tvResult.append(main + "\n");
//            tvResult.append(description);
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}

