package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;


public class FinishRegistrationActivity extends AppCompatActivity {
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private StorageReference mImageStorage;

    private ImageView mProfilePicture;
    private TextView mFirstName;
    private TextView mLastName;

    private ProgressDialog mRegistrationProgress;

    private Uri mProfilePictureUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_registration);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mProfilePicture = findViewById(R.id.profile_image);
        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);
        Button mUploadPhotoButton = findViewById(R.id.upload_photo_button);
        Button mSaveButton = findViewById(R.id.save_button);

        mUploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(FinishRegistrationActivity.this);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {
                    Uri resultUri = result.getUri();
                    mProfilePictureUri = resultUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    mProfilePicture.setImageBitmap(bitmap);
                } catch(IOException e) {
                    Toast.makeText(this, "Error in loading picture: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error in loading picture: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void finishRegistration() {
        final String first_name = mFirstName.getText().toString();
        final String last_name = mLastName.getText().toString();
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
        } else if (mProfilePictureUri == null) {
            Toast.makeText(this, "You must upload a picture.", Toast.LENGTH_LONG).show();
        } else {

            mRegistrationProgress.setTitle("Finishing Registration");
            mRegistrationProgress.setMessage("Please wait!");
            mRegistrationProgress.setCanceledOnTouchOutside(false);
            mRegistrationProgress.show();


            final String uid = mCurrentUser.getUid();

            StorageReference imagePath = mImageStorage.child("profile_images").child(uid + ".jpg");

            // Sorry for these nested loops...

            imagePath.putFile(mProfilePictureUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mRegistrationProgress.dismiss();
                    if (task.isSuccessful()) {
                        String download_uri = task.getResult().getDownloadUrl().toString();
                        User user = new User(first_name, last_name, download_uri);
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

                    } else {
                        Toast.makeText(
                                FinishRegistrationActivity.this,
                                "Upload of picture failed...",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    // https://stackoverflow.com/questions/12116092/android-random-string-generator
//    public static String random() {
//        Random generator = new Random();
//        StringBuilder randomStringBuilder = new StringBuilder();
//        int randomLength = generator.nextInt(10);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }
//        return randomStringBuilder.toString();
//    }

}
