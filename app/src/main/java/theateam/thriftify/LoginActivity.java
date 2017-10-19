package theateam.thriftify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.R.attr.password;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth Auth; //firebase authentication object
    // UI reference objects
    private AutoCompleteTextView emailView;
    private EditText passwordView;

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

        Auth = FirebaseAuth.getInstance();      //get firebase instance and assign create object
    }

    // call from sign up link
    public void SignUpNewUser(View v) {
        Intent intent = new Intent(this, theateam.thriftify.SignUpActivity.class);
        finish();
        startActivity(intent);
    }


    // call from sign in button
    public void UserSignIn(View v)   {
        Login();
    }


    private void Login() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        if (email.equals("") || password.equals("")) return;
        Toast.makeText(this, getString(R.string.progress_msg), Toast.LENGTH_SHORT).show();

        Auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("thiftify", "sign in complete: " + task.isSuccessful());

                if (!task.isSuccessful()){
                    Log.d("thriftify", "Sign in issue: " + task.getException());
                    errorAlert(getString(R.string.sign_in_issue));
                }else{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });



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
