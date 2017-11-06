package theateam.thriftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) { // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_user_profile:
                GoToProfile();
                return true;
//            case R.id.action_chat: GoToChat();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GoToProfile() {
        Intent intent = new Intent(this, MyAccount.class);
        finish();
        startActivity(intent);
    }

//    private  void GoToChat(){
//        Intent intent = new Intent(this, ChatActivity.class);
//        finish();
//        startActivity(intent);
//
//    }

}