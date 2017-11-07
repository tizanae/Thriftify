package theateam.thriftify;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MyAccount extends AppCompatActivity {

    private String screenName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        setupScreenName();


    }
        private void setupScreenName(){
        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
        screenName = prefs.getString(SignUpActivity.SCREEN_NAME_KEY, null);
        if (screenName == null) screenName = "User";
            AppCompatTextView screenNameView  = findViewById(R.id.toolbar_title);
            screenNameView.setText(screenName);
            TextView screenNameView2 = findViewById(R.id.screen_name);
            screenNameView2.setText(screenName);
    }
}

