package com.vmc.white.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.vmc.white.R;
import com.vmc.white.adapter.UsersAdapter;
import com.vmc.white.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActivityCustom implements SearchView.OnQueryTextListener {
    private static final String TAG = "Riyas.Vmc";
    private static final int MIN_SEARCH_CHAR = 3;

    // algolia
    public static final String APP_ID = "4EV80R6YS3";
    public static final String API_KEY = "83c49f53c988e179069d7d45b8cba247";

    private static SearchView searchView = null;
    private static UsersAdapter mUserAdapter;
    private static List<User> mUserList;
    private static ListView mListView;
    private static Index index;

    // Todo collect Manjeri, Malappuram Data.

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_new_entry:
                    goToActivity();
                    return true;
            }
            return false;
        }
    };

    private void goToActivity() {
        Intent i = new Intent(this, ActivityNewEntry.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mUserList = new ArrayList<>();

        mListView = findViewById(R.id.listView);
        mUserAdapter = new UsersAdapter(getApplicationContext(), mUserList);
        mListView.setAdapter(mUserAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                User user = (User) adapterView.getItemAtPosition(pos);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        // Algolio
        Client client = new Client(APP_ID, API_KEY);
        index = client.getIndex("users");

        // addDataToAlgoliaDB( , ,);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String string) {
        if(string.length() < MIN_SEARCH_CHAR) {
            mUserList.clear();
            mUserAdapter.notifyDataSetChanged();
            return false;
        }
        Query query = new Query(string)
            .setAttributesToRetrieve(new String[]{"name", "category", "phone"})
            .setHitsPerPage(50);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
            try {
                JSONArray hits = jsonObject.getJSONArray("hits");
                mUserList.clear();
                for (int i = 0; i < hits.length(); i++) {
                    JSONObject jsonUser = hits.getJSONObject(i);
                    User user = new User(jsonUser.getString("name"), jsonUser.getString("category"), jsonUser.getString("phone"));
                    mUserList.add(user);
                }
                mUserAdapter.notifyDataSetChanged();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            }
        });
        return true;
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