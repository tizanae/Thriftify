package theateam.thriftify;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CategoryViewActivity extends BaseActivity {

    private static final String TAG = CategoryViewActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private String mCategoryKey;
    private DatabaseReference mRootDatabase;
    private GeoFire mGeoFire;

    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;
    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;


    private Location mLastLocation;
    private PostPreviewAdapter postPreviewAdapter;

    // TODO: FIXME: PERMISSIONS!
    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        getToolbar();
        setBackArrow();
        mCategoryKey = getIntent().getStringExtra("CATEGORY_KEY");
        mRootDatabase = FirebaseDatabase.getInstance().getReference();
        mGeoFire = new GeoFire(mRootDatabase.child("geo_fire").child(mCategoryKey));


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
//        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // GPS location can be null if GPS is switched off
//                        if (location != null) {
//                            mLastLocation = location;
//                            populateView();
//                        } else {
//                            Log.d(TAG, "MISSING location");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "Error trying to get last GPS location");
//                        e.printStackTrace();
//                    }
//                });


        ListView itemViewer = findViewById(R.id.item_viewer);

        postPreviewAdapter = new PostPreviewAdapter(this, new ArrayList<PostPreview>());
        itemViewer.setAdapter(postPreviewAdapter);
        createLocationCallback();
        createLocationRequest();
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    private void populateView() {
        final DatabaseReference postRef = mRootDatabase.child("posts").child(mCategoryKey);
        GeoQuery geoQuery = mGeoFire.queryAtLocation(
                new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                100
        );
        // TODO: FIXME: UPDATE UI AFTER QUERY READY, SO YOU CAN SORT AND STUFF
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {
                postRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        if (mCategoryKey.equals(post.category_key) && post.picture_uris != null) {
                            String default_uri = post.picture_uris.values().toArray()[0].toString();
                            postPreviewAdapter.add(new PostPreview(post.title, default_uri, key, mCategoryKey));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        checkPermissions();
    }
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                stopLocationUpdates();


                if (mLastLocation == null) {
                    mLastLocation = locationResult.getLastLocation();
                    populateView();
                }

            }
        };
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}
