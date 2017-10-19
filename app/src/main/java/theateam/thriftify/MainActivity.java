package theateam.thriftify;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {
//    private String screenName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setupScreenName();
    }

//    private void setupScreenName(){
//        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
//        screenName = prefs.getString(SignUpActivity.SCREEN_NAME_KEY, null);
//        if (screenName == null) screenName = "User";
//        TextView screenNameView  = findViewById(R.id.screen_name);
//        screenNameView.setText("Hi " + screenName);
//    }
}
