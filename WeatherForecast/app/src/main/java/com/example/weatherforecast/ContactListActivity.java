package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class ContactListActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnMenu;
    TextView tvReco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactlist);

        btnMenu = findViewById(R.id.btnMenu);
        tvReco = findViewById(R.id.tvReco);

        btnMenu.setOnClickListener(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MODE_PRIVATE);

        Cursor c = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null
        );
        String str = "";
        c.moveToFirst();
        do {
            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            str += name + "\t" + phoneNumber + "\n";
        } while (c.moveToNext()); // 데이터 없을 때까지 반복
        tvReco.setText(str);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMenu) {
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
}
