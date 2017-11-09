package theateam.thriftify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToolbar();
        getDrawer();

        // Main Stuff
        String[] values = new String[] {
                "Popular Nearby",
                "Electronics",
                "Tools",
                "Video Games",
                "Antiques",
                "Appliances",
                "Arts",
                "Auto Parts"
        };
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                R.layout.category_item,
                R.id.category_name,
                values
        );
        ListView listView = findViewById(R.id.categories);
        listView.setAdapter(categoryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                openCategory();
            }
        });

    }

    private void openCategory() {
        Intent intent = new Intent(this, theateam.thriftify.CategoryViewActivity.class);
        finish();
        startActivity(intent);
    }

}