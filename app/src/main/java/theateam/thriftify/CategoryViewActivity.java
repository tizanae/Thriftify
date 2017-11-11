package theateam.thriftify;

import android.location.Location;
import android.support.annotation.NonNull;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
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

    private String mCategoryKey;
    private DatabaseReference mRootDatabase;
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
        mRootDatabase = FirebaseDatabase.getInstance().getReference();
        mGeoFire = new GeoFire(mRootDatabase.child("geo_fire"));
        mCategoryKey = getIntent().getStringExtra("CATEGORY_KEY");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {

                            mLastLocation = task.getResult();
                            populateView();

                        } else {
                            Toast.makeText(CategoryViewActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        ListView itemViewer = findViewById(R.id.item_viewer);

        postPreviewAdapter = new PostPreviewAdapter(this, new ArrayList<PostPreview>());
        itemViewer.setAdapter(postPreviewAdapter);
    }

    private void populateView() {
        final DatabaseReference postRef = mRootDatabase.child("posts");
        GeoQuery geoQuery = mGeoFire.queryAtLocation(
                new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()),
                5
        );
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                postRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Post post = dataSnapshot.getValue(Post.class);
                        String default_uri = post.picture_uris.values().toArray()[0].toString();
                        postPreviewAdapter.add(new PostPreview(post.title, default_uri));
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
}
