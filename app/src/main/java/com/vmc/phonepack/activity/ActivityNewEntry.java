package com.vmc.phonepack.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.vmc.phonepack.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityNewEntry extends AppCompatActivity {

    // algolia
    private Index index;

    // views
    private EditText mEditText_Name;
    private EditText mEditText_Category;
    private EditText mEditText_Phone;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        // Algolio
        Client client = new Client(MainActivity.APP_ID, MainActivity.API_KEY);
        index = client.getIndex("users");

        // init views
        mEditText_Name = findViewById(R.id.editText_Name);
        mEditText_Category = findViewById(R.id.editText_Category);
        mEditText_Phone = findViewById(R.id.editText_Phone);
        mButton = findViewById(R.id.button);

        // set Button on Click listener
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDataToAlgoliaDB(mEditText_Name.getText().toString(),
                        mEditText_Category.getText().toString(),
                        mEditText_Phone.getText().toString());
                finish();
            }
        });
    }

    private void addDataToAlgoliaDB(String name, String category, String phone){
        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", name);
        user1.put("category", category);
        user1.put("phone", phone);

        List<JSONObject> userList = new ArrayList<>();
        userList.add(new JSONObject(user1));
        index.addObjectsAsync(new JSONArray(userList), null);
    }
}
