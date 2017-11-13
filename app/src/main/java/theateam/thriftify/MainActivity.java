package theateam.thriftify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuthenticated();
        getToolbar();
        setDrawer();

        ValueEventListener categoriesEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: retrieved categories");

                // Holds Key/Value Pairs for categories in db
                final HashMap<String, String> categoryKeys = new HashMap<>();

                // Hold Values from db
                final ArrayList<String> categories = new ArrayList<>();

                // Reverse Key value pairs to search later
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.getValue(String.class);
                    categoryKeys.put(category, snapshot.getKey());
                    categories.add(category);
                }

                // Set up ListView for categories
                final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        R.layout.category_item,
                        R.id.category_name,
                        categories
                );
                ListView listView = findViewById(R.id.categories);
                listView.setAdapter(categoryAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        String categoryName = (String)parent.getItemAtPosition(position);
                        openCategory(categoryKeys, categoryName);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        };

        getRootDatabase()
                .child("categories")
                .orderByKey()
                .addValueEventListener(categoriesEventListener);


    }

    private void openCategory(HashMap<String, String> categoryKeys, String categoryName) {
        // Reverse search with the value to get key
        String categoryKey = categoryKeys.get(categoryName);
        Log.d(TAG, "openCategory: Category key chosen - " + categoryKey);

        // Feed Category ID to next the category view activity
        Intent intent = new Intent(this, theateam.thriftify.CategoryViewActivity.class);
        intent.putExtra("CATEGORY_KEY", categoryKey);
        startActivity(intent);
    }

}