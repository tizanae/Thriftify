package theateam.thriftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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