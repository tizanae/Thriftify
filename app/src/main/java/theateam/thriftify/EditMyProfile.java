package theateam.thriftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditMyProfile extends BaseActivity {

    // Constants
    public static final String APP_PREFS = "AppPrefs";
    public static final String SCREEN_NAME_KEY = "username";

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String userID;
    private TextView firstNameView;
    private TextView lastNameView;
    private ImageView thumbView;
    private Boolean initial_load = false;
    private Button saveBtn ;
    private String screenName;
    private Boolean load_data_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getToolbar();
        setBackArrow();

        setContentView(R.layout.activity_my_profile);

        firstNameView = findViewById(R.id.user_edit_first_name);
        lastNameView = findViewById(R.id.user_edit_last_name);
        thumbView = findViewById(R.id.thumbnail);

        saveBtn = (Button) findViewById(R.id.user_edit_save);

        mAuth = FirebaseAuth.getInstance();
        userID = getCurrentUser().getUid();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (getCurrentUser() != null){
                    //user is signed in
                    Log.d("thriftify", "onAuthStateChanged:signed_in:" + getCurrentUser().getUid());
                    toastMessage("Successfully retrieved: " + getCurrentUser().getEmail());
                } else {
                    //user is signed out
                    Log.d("thriftify", "onAuthStateChanged: signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

        getRootDatabase().child("users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    showData(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//                mReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        try {
//                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                d.child
//                                User u = new User();
//                                u.setName(ds.child(userID).getValue(User.class));
//                                load_data_flag = true;
//                            }
//                        } catch (NullPointerException e) {
//                            Log.d("thriftify", "onAuthStateChanged:signed_in:" + e);
//                            load_data_flag = false;
//                        }
//
//                        if (load_data_flag) {
//                            showData(dataSnapshot);
//                        } else {
//                            //shared preferences username
//                            SharedPreferences prefs = getSharedPreferences(RegistrationActivity.APP_PREFS, MODE_PRIVATE);
//                            screenName = prefs.getString(RegistrationActivity.SCREEN_NAME_KEY, null);
//                            TextView screenNameView = findViewById(R.id.user_edit_username);
//                            screenNameView.setText(screenName);
//                            Log.d("thriftify", "onDataChange: added information to database: \n" +
//                                    dataSnapshot.getValue());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("thriftify", "save button pressed");
//                String username = usernameView.getText().toString();
//                String fullname = fullnameView.getText().toString();
//                String email = emailView.getText().toString();
//                String phonenumber = phonenumberView.getText().toString();
//
//                Log.d("triftify","Attempting to save: \n" +
//                "username: " + username + "\n" +
//                "fullname: " + fullname + "\n" +
//                "email: " + email + "\n" +
//                "phonenumber: " + phonenumber + "\n");
//                //make sure data exists
//                if (!username.equals("") && !fullname.equals("") && !email.equals("") && !phonenumber.equals("")){
//                    if (phonenumber.matches("^(\\([0-9]{3}\\)|[0-9]{3}-)[0-9]{3}-[0-9]{4}$")) {
//                        UserInfo u = new UserInfo(username, fullname, email, phonenumber);
//                        mReference.child("users").child(userID).setValue(u);
//                        toastMessage("User information saved!");
//                        saveScreenName(username);
//                        Intent intent = new Intent(EditMyProfile.this, MyAccount.class);
//                        finish();
//                        startActivity(intent);
//                    }else{
//                        toastMessage("Valid Telephone format: (###)-###-#### or ###-###-####.");
//                    }
//                }else{
//                    toastMessage("Please ensure all data fields have been filled in.");
//                }
//            }
//        });
    }

    private void showData(User user) {
        firstNameView.setText(user.getFirstName());
        lastNameView.setText(user.getLastName());
        Picasso.with(this).load(user.getThumbnail()).into(thumbView);
    }
//    //Update user's screen name to Shared Preferences - LOCALLY
//    private void saveScreenName(String username){
//        String screenName = username;
//        SharedPreferences prefs  = getSharedPreferences(APP_PREFS, 0);
//        prefs.edit().putString(SCREEN_NAME_KEY, screenName).apply();
//    }
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
