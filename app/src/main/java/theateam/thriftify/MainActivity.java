package theateam.thriftify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    private Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar and drawer setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_categories).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_post_item).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_messages).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_my_account).withIdentifier(4).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_log_out).withIdentifier(5).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            int identifier = (int)drawerItem.getIdentifier();
                            switch(identifier) {
                                case 1:
                                    intent = new Intent(MainActivity.this, MainActivity.class);
                                    break;
                                case 2:
                                    intent = new Intent(MainActivity.this, PostInfo.class);
                                    break;
                                default:
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

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