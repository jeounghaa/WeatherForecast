package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    Cursor cursor;

    Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync( this);

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);
    }

    public void onMapReady(final GoogleMap googleMap) {
        ArrayList array = new ArrayList();
        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM weather", null);

        String wid = null;
        String area = null;
        String latitude = null;
        String longitude = null;
        mMap = googleMap;

        while (cursor.moveToNext()) {
            wid = cursor.getString(0);
            area = cursor.getString(1);
            latitude = cursor.getString(2);
            longitude = cursor.getString(3);
            Log.e("$$$$$$$$$$$$$", area);
            Log.e("$$$$$$$$$$$$$2", longitude);
            LatLng llng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(llng);
            markerOptions.title(area);
            mMap.addMarker(markerOptions);
        }

        LatLng SEOUL = new LatLng(37.551148, 126.989368);

//        MarkerOptions markerOptions2 = new MarkerOptions();
//        markerOptions2.position(SEOUL2);
//        markerOptions2.title("서울2");
//        markerOptions2.snippet("한국의 수도2");
//        mMap.addMarker(markerOptions2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 12));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnMenu) {
            PopupMenu p = new PopupMenu(this, v);
            getMenuInflater().inflate(R.menu.main_menu, p.getMenu());
            p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu0:
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.menu1:
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

}
