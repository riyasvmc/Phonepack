package com.vmc.white.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vmc.white.R;
import com.vmc.white.model.User;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ArrayList<String> phoneList = new ArrayList<>();
        ListView listView = findViewById(R.id.listView);

        User user = null;
        user = (User) getIntent().getSerializableExtra("user");
        if(user != null) {
            String phone[] = user.getPhone().split(":");
            if (phone.length > 0) {
                for (String item : phone) {
                    phoneList.add(item);
                }
            }
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,phoneList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + adapterView.getItemAtPosition(pos)));
                if (ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                }
                else
                {
                    startActivity(intent);
                }
            }
        });
    }
}
