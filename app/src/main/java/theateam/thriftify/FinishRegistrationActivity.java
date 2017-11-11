package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FinishRegistrationActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private Button mUploadPhotoButton;
    private TextView mFirstName;
    private TextView mLastName;
    private Button mSaveButton;

    private ProgressDialog mRegistrationProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_registration);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mFirstName = (TextView) findViewById(R.id.first_name);
        mLastName = (TextView) findViewById(R.id.last_name);
        mUploadPhotoButton = (Button) findViewById(R.id.upload_photo_button);
        mSaveButton = (Button) findViewById(R.id.save_button);

        mUploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishRegistration();
            }
        });

        mRegistrationProgress = new ProgressDialog(this);

    }

    private void finishRegistration() {
        String first_name = mFirstName.getText().toString();
        String last_name = mLastName.getText().toString();
        View focusView = null;

        if (TextUtils.isEmpty(first_name)) {
            mFirstName.setError("First name is required.");
            focusView = mFirstName;
        } else if (TextUtils.isEmpty(last_name)) {
            mLastName.setError("Last name is required.");
            focusView = mLastName;
        }
        if (focusView != null) {
            focusView.requestFocus();
        } else {

            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = current_user.getUid();

            User user = new User(first_name, last_name);

            mRegistrationProgress.setTitle("Finishing Registration");
            mRegistrationProgress.setMessage("Please wait!");
            mRegistrationProgress.setCanceledOnTouchOutside(false);
            mRegistrationProgress.show();
            mDatabase.child("users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mRegistrationProgress.dismiss();
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(FinishRegistrationActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        finish();
                        startActivity(intent);
                    } else {
                        Toast.makeText(FinishRegistrationActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
