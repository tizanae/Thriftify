package theateam.thriftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    // Constants
    public static final String APP_PREFS = "AppPrefs";
    public static final String SCREEN_NAME_KEY = "username";

    // UI references.
    private AutoCompleteTextView emailView;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private EditText passwordConfirmView;

    // Firebase instance variables
    private FirebaseAuth Auth;  //firebase authentication object
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailView =  findViewById(R.id.sign_up_email);
        usernameView = findViewById(R.id.sign_up_username);
        passwordView =  findViewById(R.id.sign_up_password);
        passwordConfirmView =  findViewById(R.id.sign_up_confirm_password);

        // Keyboard sign in action
        passwordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btn_login || id == EditorInfo.IME_NULL) { //login button or enter pressed from password confirm
                    registerMe();
                    return true;
                }
                return false;
            }
        });

        //instance of firebase authentication object
        Auth = FirebaseAuth.getInstance();
    }


    public void GoToLogin(View v) {
        Intent intent = new Intent(this, theateam.thriftify.LoginActivity.class);
        finish();
        startActivity(intent);
    }

    // call from sign up link
    public void SignUpNewUser(View v) {
        registerMe();
    }

    private void registerMe() {

        // Reset errors displayed in the form.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String username = usernameView.getText().toString();

        //----------------code logic from london app brewery tutorial
        //input validation and error messages
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !validatePassword(password)) {
            passwordView.setError(getString(R.string.invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.required));
            focusView = emailView;
            cancel = true;
        } else if (!validateEmail(email)) {
            emailView.setError(getString(R.string.invalid_email));
            focusView = emailView;
            cancel = true;

//        } else if (TextUtils.isEmpty(username)){
//            usernameView.setError(getString(R.string.required));
//            focusView = usernameView;
//            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            createFBuser();

        }
        //-------------------------------------------------------------
    }

    //confirm email address is formatted correctly
    private boolean validateEmail(String email) {

        return email.contains("@") && email.contains(".");
    }

    //validate password - min 8 chars, containing number, letter
    private boolean validatePassword(String password) {
        String confirmPassword = passwordConfirmView.getText().toString();
        // TODO: improve password validation
        return confirmPassword.equals(password) && password.length() >= 8 && password.matches(".*\\d+.*") ; //&& password.contains("[a-zA-Z]+")
    }

    // creating new firebase user
    private void createFBuser(){
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        //create user then use task object returned to listen for new user created event on fire base
        Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("thriftify", "createUser onComplete: " + task.isSuccessful());

                if (!task.isSuccessful()){
                    Log.d("thriftify", "Failed to create user" );
                    errorAlert(getString(R.string.sign_up_failed));
                }else{
                    saveScreenName();   //save username once user exists in fire base
                    // TODO: popup to show user creation success before switiching activities
//                    successAlert(getString(R.string.sign_up_success));
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }


    //Save the user's screen name to Shared Preferences - LOCALLY
    private void saveScreenName(){
        String screenName = usernameView.getText().toString();
        SharedPreferences prefs  = getSharedPreferences(APP_PREFS, 0);
        prefs.edit().putString(SCREEN_NAME_KEY, screenName).apply();
    }

    //dialog box for successful sign up using real time builder method
    private void successAlert(String msg){
        new AlertDialog.Builder(this)
                .setTitle("Aye!!")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    //dialog box for failed sign up
    private void errorAlert(String msg){
        new AlertDialog.Builder(this)
                .setTitle("Uh oh...")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
