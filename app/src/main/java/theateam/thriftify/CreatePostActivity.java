package theateam.thriftify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatePostActivity extends BaseActivity {

    private static final String TAG = CreatePostActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        getToolbar();
        getDrawer();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Query categoriesQuery = databaseReference.child("categories").orderByKey();

        categoriesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, String> categoryKeys = new HashMap<>();
                final ArrayList<String> categories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.getValue(String.class);
                    categoryKeys.put(category, snapshot.getKey());
                    categories.add(category);
                }
                final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                        CreatePostActivity.this,
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
                Toast.makeText(CreatePostActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void openCategory(HashMap<String, String> categoryKeys, String categoryName) {
        Intent intent = new Intent(this, PostItemActivity.class);
        String categoryKey = categoryKeys.get(categoryName);
        intent.putExtra("CATEGORY_KEY", categoryKey);
        startActivity(intent);
    }
}
