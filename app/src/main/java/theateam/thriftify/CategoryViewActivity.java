package theateam.thriftify;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryViewActivity extends BaseActivity {

    private static final String TAG = CategoryViewActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private String mCategoryKey;
    private GeoFire mGeoFire;

    private FusedLocationProviderClient mFusedLocationClient;

    private Location mLastLocation;
    private PostPreviewAdapter postPreviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        getToolbar();
        setBackArrow();
        mCategoryKey = getIntent().getStringExtra("CATEGORY_KEY");
        mGeoFire = new GeoFire(getRootDatabase().child("geo_fire").child(mCategoryKey));


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ListView itemViewer = findViewById(R.id.item_viewer);
        postPreviewAdapter = new PostPreviewAdapter(this, new ArrayList<Post>());
        itemViewer.setAdapter(postPreviewAdapter);
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

    private void populateView() {
        Log.i(TAG, "Populating the view");

        final DatabaseReference postRef = getRootDatabase().child("posts").child(mCategoryKey);

        // Make geo query 150km away
        GeoQuery geoQuery = mGeoFire.queryAtLocation(
                new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                150
        );

        // Keep track of all keys in the category that are returned.
        final ArrayList<String> catKeys = new ArrayList<>();

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                catKeys.add(key);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                postPreviewAdapter.clear();
                Log.i(TAG, "Geo Query Ready");
                for (String key: catKeys) {
                    postRef.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Post post = dataSnapshot.getValue(Post.class);


                            if (post != null && mCategoryKey.equals(post.getCategoryKey())) {
                                postPreviewAdapter.add(post);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, databaseError.getDetails());
                        }
                    });
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e(TAG, error.getDetails());
            }
        });
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
                            populateView();
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

    private boolean checkPermissions() {
        Log.i(TAG, "Checking permissions");
        int permissionState = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        );
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Log.i(TAG, "Requesting permission");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
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
