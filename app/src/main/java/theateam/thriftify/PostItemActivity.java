package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class PostItemActivity extends BaseActivity {

    private static final String TAG = PostItemActivity.class.getSimpleName();

    private GeoFire mGeoFire;

    private TextView mPostTitle;
    private TextView mPostPrice;
    private TextView mPostDescription;

    private String mCategoryKey;
    private Location mLastLocation;

    private PostItemPictureAdapter adapter;

    protected FusedLocationProviderClient mFusedLocationClient;

    protected ProgressDialog mProgressDialog;

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        getToolbar();
        setBackArrow();
        mCategoryKey = getIntent().getStringExtra("CATEGORY_KEY");

        DatabaseReference mGeoFireReference = getRootDatabase().child("geo_fire").child(mCategoryKey);
        mGeoFire = new GeoFire(mGeoFireReference);

        mPostTitle = findViewById(R.id.input_title);
        mPostPrice = findViewById(R.id.input_price);
        mPostDescription = findViewById(R.id.input_description);

        mProgressDialog = new ProgressDialog(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            mLastLocation = task.getResult();

                        } else {
                            Toast.makeText(PostItemActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        FloatingActionButton uploadPicture = findViewById(R.id.addPictureButton);
        Button saveButton = findViewById(R.id.post_item_btn);
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .start(PostItemActivity.this);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItem();
            }
        });


        ArrayList<Uri> uris = new ArrayList<>();
        adapter = new PostItemPictureAdapter(this, uris);
        adapter.setNotifyOnChange(true);
        ListView listView = findViewById(R.id.picturesContainer);
        listView.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                adapter.add(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error in loading picture: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postItem() {
        String current_user_id = getCurrentUser().getUid();
        String post_title = mPostTitle.getText().toString();
        String post_price = mPostPrice.getText().toString();
        String post_description = mPostDescription.getText().toString();
        View focusView = null;
        if (TextUtils.isEmpty(post_title)) {
            mPostTitle.setError("Post title is required.");
            focusView = mPostTitle;
        } else if (TextUtils.isEmpty(post_price)) {
            mPostPrice.setError("Post price is required.");
            focusView = mPostPrice;
        } else if (TextUtils.isEmpty(post_description)){
            mPostDescription.setError("Post description is required.");
            focusView = mPostDescription;
        }
        if (focusView != null) {
            focusView.requestFocus();
        } else if (mLastLocation == null) {
            Toast.makeText(this, "Don't have a location to use", Toast.LENGTH_SHORT).show();
        } else if (adapter.getCount() < 1) {
            Toast.makeText(this, "You must upload a picture.", Toast.LENGTH_LONG).show();
        }
        final ArrayList<Uri> picture_uris = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Uri uri = adapter.getItem(i);
            picture_uris.add(uri);
        }

        final double lat = mLastLocation.getLatitude();
        final double lng = mLastLocation.getLongitude();



        final String newPostKey = getRootDatabase().child("posts").child(mCategoryKey).push().getKey();

        final Post newPost = new Post(
                newPostKey,
                current_user_id,
                mCategoryKey,
                post_title,
                post_price,
                post_description,
                lat,
                lng,
                new HashMap<String, String>()
        );

        mProgressDialog.setTitle("Finishing Registration");
        mProgressDialog.setMessage("Please wait!");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        getRootDatabase()
                .child("posts")
                .child(mCategoryKey)
                .child(newPostKey)
                .setValue(newPost)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    for (Uri pic_uri: picture_uris) {

                        StorageReference imagePath = getRootStorage()
                                .child("post_images")
                                .child(newPostKey)
                                .child(generateRandomString(10) + ".jpg");

                        // Sorry for these nested loops...

                        UploadTask uploadTask= imagePath.putFile(pic_uri);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    String download_uri = task.getResult().getDownloadUrl().toString();
                                    getRootDatabase().child("posts").child(mCategoryKey).child(newPostKey).child("pictures").push().setValue(download_uri);
                                }
                            }
                        });

                    }
                    mGeoFire.setLocation(newPostKey, new GeoLocation(lat, lng), new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {

                            mProgressDialog.dismiss();
                            if (error != null) {
                                Toast.makeText(PostItemActivity.this, "Something went wrong: " + error.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(PostItemActivity.this, "LIT", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PostItemActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                            }
                        }
                    });
                } else {
                    Toast.makeText(PostItemActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
