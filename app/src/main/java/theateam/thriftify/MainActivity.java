package theateam.thriftify;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 36;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAuthenticated();
        getToolbar();
        getDrawer();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ValueEventListener categoriesEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: retrieved categories");

                // Holds Key/Value Pairs for categories in db
                final HashMap<String, String> categoryKeys = new HashMap<>();

                // Hold Values from db
                final ArrayList<String> categories = new ArrayList<>();

                // Reverse Key value pairs to search later
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String category = snapshot.getValue(String.class);
                    categoryKeys.put(category, snapshot.getKey());
                    categories.add(category);
                }

                // Set up ListView for categories
                final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        R.layout.category_item,
                        R.id.category_name,
                        categories
                );
                ListView listView = findViewById(R.id.categories);
                listView.setAdapter(categoryAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        String categoryName = (String)parent.getItemAtPosition(position);
                        openCategory(categoryKeys, categoryName);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getDetails());
            }
        };

        getRootDatabase()
                .child("categories")
                .orderByKey()
                .addValueEventListener(categoriesEventListener);
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

    private void openCategory(HashMap<String, String> categoryKeys, String categoryName) {
        // Reverse search with the value to get key
        String categoryKey = categoryKeys.get(categoryName);
        Log.d(TAG, "openCategory: Category key chosen - " + categoryKey);

        // Feed Category ID to next the category view activity
        Intent intent = new Intent(this, theateam.thriftify.CategoryViewActivity.class);
        intent.putExtra("CATEGORY_KEY", categoryKey);
        startActivity(intent);
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
                            Map<String, Object> locationMap = new HashMap<>();
                            locationMap.put("latitude", location.getLatitude());
                            locationMap.put("longitude", location.getLongitude());
                            Log.i(TAG, "Inserting location for user");
                            getRootDatabase()
                                    .child("users")
                                    .child(getCurrentUser().getUid())
                                    .updateChildren(locationMap);
                        } else {
                            Log.e(TAG, "Missing Location");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error trying to get last GPS location: " + e);
                    }
                });
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