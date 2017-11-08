package theateam.thriftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMyProfile extends AppCompatActivity {

    // Constants
    public static final String APP_PREFS = "AppPrefs";
    public static final String SCREEN_NAME_KEY = "username";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mReference;

    private String email;
    private String userID;
    private TextView usernameView;
    private TextView fullnameView;
    private TextView emailView;
    private TextView phonenumberView;
    private Boolean initial_load = false;
    private Button saveBtn ;
    private String screenName;
    private Boolean load_data_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        usernameView = (TextView)findViewById(R.id.user_edit_username);
        fullnameView = (TextView)findViewById(R.id.user_edit_fullname);
        emailView = (TextView)findViewById(R.id.user_edit_email);
        phonenumberView = (TextView)findViewById(R.id.user_edit_phone_number);
        saveBtn = (Button) findViewById(R.id.user_edit_save);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user is signed in
                    Log.d("thriftify", "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully retrieved: " + user.getEmail());
                } else {
                    //user is signed out
                    Log.d("thriftify", "onAuthStateChanged: signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        UserInfo u = new UserInfo();
                        u.setUsername(ds.child(userID).getValue(UserInfo.class).getUsername());
                        load_data_flag = true;
                    }
                }catch (NullPointerException e){
                    Log.d("thriftify", "onAuthStateChanged:signed_in:" + e);
                    load_data_flag = false;
                }

                if (load_data_flag){
                    showData(dataSnapshot);
                } else {
                    //shared preferences username
                    SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
                    screenName = prefs.getString(SignUpActivity.SCREEN_NAME_KEY, null);
                    TextView screenNameView = findViewById(R.id.user_edit_username);
                    screenNameView.setText(screenName);
                    Log.d("thriftify", "onDataChange: added information to database: \n" +
                            dataSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("thriftify", "save button pressed");
                String username = usernameView.getText().toString();
                String fullname = fullnameView.getText().toString();
                String email = emailView.getText().toString();
                String phonenumber = phonenumberView.getText().toString();

                Log.d("triftify","Attempting to save: \n" +
                "username: " + username + "\n" +
                "fullname: " + fullname + "\n" +
                "email: " + email + "\n" +
                "phonenumber: " + phonenumber + "\n");
                //make sure data exists
                if (!username.equals("") && !fullname.equals("") && !email.equals("") && !phonenumber.equals("")){
                    if (phonenumber.matches("^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$")) {
                        UserInfo u = new UserInfo(username, fullname, email, phonenumber);
                        mReference.child("users").child(userID).setValue(u);
                        toastMessage("User information saved!");
                        saveScreenName(username);
                        Intent intent = new Intent(EditMyProfile.this, MyAccount.class);
                        finish();
                        startActivity(intent);
                    }else{
                        toastMessage("Valid Telephone format: (###)-###-#### or ###-###-####.");
                    }
                }else{
                    toastMessage("Please ensure all data fields have been filled in.");
                }
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            UserInfo u = new UserInfo();
            u.setUsername(ds.child(userID).getValue(UserInfo.class).getUsername()); //set username
            u.setEmail(ds.child(userID).getValue(UserInfo.class).getEmail()); //set email
            u.setFullname(ds.child(userID).getValue(UserInfo.class).getFullname()); //set fullname
            u.setPhonenumber(ds.child(userID).getValue(UserInfo.class).getPhonenumber()); //set phonenumber

            Log.d("thriftify", "showdata: username: " + u.getUsername());
            Log.d("thriftify", "showdata: email: " + u.getEmail());
            Log.d("thriftify", "showdata: fullname: " + u.getFullname());
            Log.d("thriftify", "showdata: phonenumber: " + u.getPhonenumber());

            usernameView.setText(u.getUsername());
            fullnameView.setText(u.getFullname());
            emailView.setText(u.getEmail());
            phonenumberView.setText(u.getPhonenumber());
        }
    }
    //Update user's screen name to Shared Preferences - LOCALLY
    private void saveScreenName(String username){
        String screenName = username;
        SharedPreferences prefs  = getSharedPreferences(APP_PREFS, 0);
        prefs.edit().putString(SCREEN_NAME_KEY, screenName).apply();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
