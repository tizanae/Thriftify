package theateam.thriftify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth; //firebase authentication object
    // UI reference objects
    private TextInputEditText emailView;
    private TextInputEditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailView =  findViewById(R.id.input_email);
        passwordView = findViewById(R.id.input_password);

        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.btn_login || id == EditorInfo.IME_NULL) {    //enter key is pressed in password field or login clicked
                    Login();
                    return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();      //get firebase instance and assign create object
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    // call from sign up link
    public void SignUpNewUser(View v) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }


    // call from sign in button
    public void UserSignIn(View v)   {
        Login();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, theateam.thriftify.MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void Login() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (email.equals("") || password.equals("")) return;
        Toast.makeText(this, getString(R.string.progress_msg), Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:failure" + task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d(TAG, "signInWithEmail:success");
                    FirebaseUser user =  mAuth.getCurrentUser();
                    updateUI(user);
                }
            }
        });
    }
}
