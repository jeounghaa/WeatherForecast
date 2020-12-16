package com.example.weatherforecast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlusDialog extends Dialog implements View.OnClickListener {

    private Context context;
    Button btnSearch;
    EditText etSearch;
    ImageButton ibCancel;
    DBHelper dbHelper;
    SQLiteDatabase sqlDB;
    String lon, lat;

    public PlusDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_dialog);
        setTitle("지역 검색");

        btnSearch = findViewById(R.id.btnSearch);
        etSearch = findViewById(R.id.etSearch);
        ibCancel = findViewById(R.id.ibCancel);

        btnSearch.setOnClickListener(this);
        ibCancel.setOnClickListener(this);

        dbHelper = new DBHelper(getContext());

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getContext());
        }
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnSearch:
                String search = etSearch.getText().toString();
                sqlDB = dbHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM weather WHERE area='"+
                        search + "';", null);
                if (!cursor.moveToNext()){
                    cursor.close();
                    sqlDB.close();
                    getRequest(search);
                } else {
                    Toast.makeText(getContext(), "이미 존재합니다..", Toast.LENGTH_SHORT).show();
                }
            case R.id.ibCancel:
                dismiss();
        }
    }

    public void getRequest(final String str) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?q="+str+"&appid=82d6a21bd4c148facc22d2f9302ac891&lang=kr",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject weather = response.getJSONObject("coord");
                    Log.e("★★★★★★★★★★★", response.toString());
                    lon = weather.getString("lon");
                    lat = weather.getString("lat");
                    Log.e("★★★★★★★★★★★2", lon);
                    sqlDB = dbHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO weather VALUES(" +
                            "NULL, '" + str + "', '" +  lat + "', '" + lon + "');" );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);


    }

}
