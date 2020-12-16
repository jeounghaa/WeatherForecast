package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView lvAreaList;
    ArrayList<ItemWeather> itemList;
    ListAdapter listAdapter;
    Button btnAdd, btnMenu;
    TextView tvTime;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    SimpleDateFormat sdf;
    String today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvAreaList = findViewById(R.id.lvAreaList);
        itemList = new ArrayList<ItemWeather>();

        dbHelper = new DBHelper(this);

        listAdapter = new ListAdapter(MainActivity.this, itemList);
        lvAreaList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        lvAreaList.setOnItemLongClickListener(new ListViewItemLongClickListener());

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);
        tvTime = findViewById(R.id.tvTime);

        if (AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM weather", null);

        String wid = null;
        String area = null;
        String latitude = null;
        String longitude = null;

        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();

        NewRunnable2 nr2 = new NewRunnable2();
        Thread t2 = new Thread(nr2);
        t2.start();

        while (cursor.moveToNext()) {
            wid = cursor.getString(0);
            area = cursor.getString(1);
            latitude = cursor.getString(2);
            longitude = cursor.getString(3);

            getRequest(area);
        }

        lvAreaList.setOnItemClickListener(this);
    }

    class NewRunnable2 implements Runnable {
        @Override
        public void run() {
            while(true) {
                try {

                }catch (Exception e) {
                    Log.e("삐삐삐삐삐삐삐삐삐삐", "스레드오류");
                    e.printStackTrace();
                }
            }
        }
    }

    class NewRunnable implements Runnable {
        @Override
        public void run() {
            final TimeZone time = TimeZone.getTimeZone("Asia/Seoul");
            sdf = new SimpleDateFormat("HH:mm:ss");
            while(true) {
                sdf.setTimeZone(time);
                today = sdf.format(new Date());
                try {
                    tvTime.setText(today);
                    Thread.sleep(1000);
                    Log.e("삐삐삐삐삐삐삐삐삐삐", today);
                }catch (Exception e) {
                    Log.e("삐삐삐삐삐삐삐삐삐삐", "스레드오류");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            PlusDialog dialog = new PlusDialog(MainActivity.this);
            dialog.show();
            listAdapter.notifyDataSetChanged();
        } else if(v.getId() == R.id.btnMenu) {
            PopupMenu p = new PopupMenu(this, v);
            getMenuInflater().inflate(R.menu.main_menu, p.getMenu());
            p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu0:
                            break;
                        case R.id.menu1:
                            Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.menu2:
                            Intent intent2 = new Intent(getApplicationContext(), ContactListActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.menu3:
                            Intent intent3 = new Intent(getApplicationContext(), StopWatchActivity.class);
                            startActivity(intent3);
                            break;
                    }
                    return false;
                }
            });
            p.show();
        }
    }

    public void print(String str) {
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }

    public void getRequest(final String str) {

        itemList.clear();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://api.openweathermap.org/data/2.5/weather?q="+str+"&appid=82d6a21bd4c148facc22d2f9302ac891&lang=kr",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String weatherData = response.getString("weather");
                    JSONObject weather = response.getJSONObject("main");
                    Log.e("★★★★★★★★★★★", weatherData);
                    Log.e("★★★★★★★★★★★", response.toString());
                    JSONArray array = new JSONArray(weatherData);
                    String main = "";
                    String description = "";
                    String icon = "";

                    String temp = weather.getString("temp");
                    Double temp2 = Double.parseDouble(temp) - 273.15;
                    Log.e("☆★★★★★★★★★★★",  temp2+ " @@");
                    for (int i=0; i<array.length(); i++) {
                        JSONObject weatherPart = array.getJSONObject(i);

                        main = weatherPart.getString("main");
                        description = weatherPart.getString("description");
                        icon = weatherPart.getString("icon");
                        Log.e("☆★★★★★★★★★★★", temp);

                        itemList.add(
                                new ItemWeather(today, str, String.format("%.0f", temp2)+" ℃")
                        );
                        listAdapter.notifyDataSetChanged();
                    }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String area = ((ItemWeather)listAdapter.getItem(position)).getArea();
        Intent intent = new Intent(getApplicationContext(), MainDetailActivity.class);
        intent.putExtra("area", area);
        startActivity(intent);
    }


    class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("삭제하시겠습니까?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteData(position);
                    itemList.remove(position);
                    listAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return false;
        }
    }

    private void deleteData(int position) {
        try {
            String area = itemList.get(position).getArea();
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL("DELETE FROM weather WHERE area='"
                    + area + "';"
            );
            sqLiteDatabase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
