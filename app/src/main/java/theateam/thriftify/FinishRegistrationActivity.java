package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;


public class FinishRegistrationActivity extends AppCompatActivity {

    private static final String TAG = FinishRegistrationActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;
    private StorageReference mImageStorage;

    private ImageView mProfilePicture;
    private TextView mFirstName;
    private TextView mLastName;

    private ProgressDialog mRegistrationProgress;

    private Uri mProfilePictureUri;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_registration);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mProfilePicture = findViewById(R.id.thumbnail);
        mFirstName = findViewById(R.id.first_name);
        mLastName = findViewById(R.id.last_name);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    /**
     * Get last known location from the app.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        Log.i(TAG, "Getting Last Location");
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            Log.i(TAG, "Received Location");
                            mLastLocation = location;
                        } else {
                            Log.e(TAG, "Missing Location");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error trying to get last GPS location: " + e);
                        e.printStackTrace();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProfilePictureUri = result.getUri();
                Picasso.with(FinishRegistrationActivity.this).load(mProfilePictureUri).into(mProfilePicture);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e(TAG, "uploadPicture: Error while loading thumbnail, " + error);
                Toast.makeText(this, "Error in loading picture", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void finishRegistration() {
        final String firstName = mFirstName.getText().toString();
        final String lastName = mLastName.getText().toString();
        final String userId = mCurrentUser.getUid();
        View focusView = null;

        if (TextUtils.isEmpty(firstName)) {
            mFirstName.setError("First name is required.");
            focusView = mFirstName;
        } else if (TextUtils.isEmpty(lastName)) {
            mLastName.setError("Last name is required.");
            focusView = mLastName;
        }
        if (mLastLocation == null) {
            Toast.makeText(this, "Can't get location", Toast.LENGTH_LONG).show();
        } else if (focusView != null) {
            focusView.requestFocus();
        } else if (mProfilePictureUri == null) {
            Toast.makeText(this, "You must upload a picture.", Toast.LENGTH_LONG).show();
        } else {

            mRegistrationProgress.setTitle("Finishing Registration");
            mRegistrationProgress.setMessage("Please wait!");
            mRegistrationProgress.setCanceledOnTouchOutside(false);
            mRegistrationProgress.show();

            StorageReference imagePath = mImageStorage.child("profile_images").child(userId + ".jpg");


            imagePath.putFile(mProfilePictureUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    mRegistrationProgress.dismiss();
                    if (task.isSuccessful()) {
                        String downloadUri = task.getResult().getDownloadUrl().toString();
                        User user = new User(
                                userId,
                                firstName,
                                lastName,
                                downloadUri,
                                mLastLocation.getLatitude(),
                                mLastLocation.getLongitude()
                        );
                        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        Log.e(TAG, "putFile: Failed, " + task.getException());
                        Toast.makeText(
                                FinishRegistrationActivity.this,
                                "Upload of picture failed...",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private boolean checkPermissions() {
        Log.i(TAG, "Checking permissions");
        int permissionState = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        );
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String permissions[],
            @NonNull int[] grantResults
    ) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "Permission Cancelled");
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission Granted.");
                getLastLocation();
            } else {
                Log.i(TAG, "Permission Denied");
            }
        }
    }
}
