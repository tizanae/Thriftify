package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyAccount extends BaseActivity {

    private static final String TAG = MyAccount.class.getSimpleName();

    private TextView mScreenName;
    private ImageView mProfilePicture;

    private ProgressDialog mRegistrationProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getToolbar();
        getDrawer();

        String uid = getCurrentUser().getUid();

        mScreenName = findViewById(R.id.screen_name);
        mProfilePicture = findViewById(R.id.profile_picture);
        mRegistrationProgress = new ProgressDialog(this);
        mRegistrationProgress.setTitle("Loading user profile...");
        mRegistrationProgress.setMessage("Please wait!");
        mRegistrationProgress.setCanceledOnTouchOutside(false);
        mRegistrationProgress.show();

        Log.i(TAG, "Getting user profile");
        getRootDatabase().child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRegistrationProgress.dismiss();
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
                    mScreenName.setText(fullName);
                    Picasso.with(MyAccount.this)
                            .load(currentUser.getThumbnail())
                            .placeholder(R.drawable.profile)
                            .into(mProfilePicture);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error while retrieving user profile: " + databaseError.getDetails());
            }
        });

    }

    public void EditUser(View v) {
        Intent intent = new Intent(MyAccount.this, EditMyProfile.class);
        startActivity(intent);
    }
    public void contactUser(View v){
        Intent intent = new Intent(MyAccount.this, ChatActivity.class);
        startActivity(intent);
    }
}

