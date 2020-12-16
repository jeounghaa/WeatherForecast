package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupMenu;

public class StopWatchActivity extends AppCompatActivity implements View.OnClickListener{

    Chronometer chron;
    Button btnStart, btnEnd, btnPause, btnMenu;

    long stopTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        chron = findViewById(R.id.chron);
        btnStart = findViewById(R.id.btnStart);
        btnEnd = findViewById(R.id.btnEnd);
        btnPause = findViewById(R.id.btnPause);
        btnMenu = findViewById(R.id.btnMenu);

        btnStart.setOnClickListener(this);
        btnEnd.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStart) {
            chron.setBase(SystemClock.elapsedRealtime() + stopTime);
            chron.start();
            btnStart.setVisibility(View.GONE);
            btnPause.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btnPause) {
            stopTime = chron.getBase() - SystemClock.elapsedRealtime();
            chron.stop();
            btnStart.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnEnd) {
            chron.setBase(SystemClock.elapsedRealtime());
            stopTime = 0;
            chron.stop();
            btnStart.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnMenu) {
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
                            Intent intent2 = new Intent(getApplicationContext(), MapActivity.class);
                            startActivity(intent2);
                            break;
                        case R.id.menu2:
                            Intent intent3 = new Intent(getApplicationContext(), ContactListActivity.class);
                            startActivity(intent3);
                            break;
                        case R.id.menu3:
                            break;
                    }
                    return false;
                }
            });
            p.show();
        }
    }
}
