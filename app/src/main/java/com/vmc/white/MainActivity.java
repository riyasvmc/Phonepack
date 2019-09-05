package com.vmc.white;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.vmc.white.adapter.UsersAdapter;
import com.vmc.white.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "Riyas.Vmc";
    private static SearchView searchView = null;
    private static UsersAdapter mUserAdapter;
    private static List<User> mUserList;
    private static ListView mListView;
    private static Index index;
    FirebaseFirestore db;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // init Firebase Firestore database
        db = FirebaseFirestore.getInstance();

        // Add Data to db
        // addDataToDatabase();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mUserList = new ArrayList<>();
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        mUserList.add(user);
                    }
                    mListView = findViewById(R.id.listView);
                    mUserAdapter = new UsersAdapter(getApplicationContext(), mUserList);
                    mListView.setAdapter(mUserAdapter);
                } else {
                    Log.d("MissionActivity", "Error getting documents: ", task.getException());
                }
            }
        });

        // Algolio
        Client client = new Client("4EV80R6YS3", "83c49f53c988e179069d7d45b8cba247");
        index = client.getIndex("users");


        /*Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Riyas Valiyadan");
        user1.put("job", "Mechanical Engineer");
        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Fidha Rensi Kadengal");
        user2.put("job", "Microbiologist");

        List<JSONObject> userList = new ArrayList<>();
        userList.add(new JSONObject(user1));
        userList.add(new JSONObject(user2));
        index.addObjectsAsync(new JSONArray(userList), null);*/


        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String s) {

                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            List<User> mUserList = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++) {
                                //*JSONObject jsonObject = hits.getJSONObject(i);
                                User user = jsonObject.getString("productName");
                                mUserList.add(user);
                            }
                             ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
                             listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return true;
            }
        });*/

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

    private void addDataToDatabase() {
        // Add data
        Map<String, Object> user = new HashMap<>();
        user.put("name", "Fidha Rensi Kadengal");
        user.put("job", "Microbiology Student");

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document " + e);
                    }
                });
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Query query = new Query(s)
                .setAttributesToRetrieve(new String[]{"name", "job"})
                .setHitsPerPage(50);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(@Nullable JSONObject jsonObject, @Nullable AlgoliaException e) {
                try {
                    JSONArray hits = jsonObject.getJSONArray("hits");
                    mUserList.clear();
                    for (int i = 0; i < hits.length(); i++) {
                        JSONObject jsonUser = hits.getJSONObject(i);
                        Log.d("Riyas.Vmc", jsonUser.toString());
                        Toast.makeText(MainActivity.this, jsonUser.getString("name"), Toast.LENGTH_SHORT).show();
                        User user = new User(jsonUser.getString("name"), jsonUser.getString("job"));
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
}