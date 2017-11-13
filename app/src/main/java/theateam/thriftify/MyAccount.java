package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private TextView mScreenName;
    private ImageView mProfilePicture;

    private ProgressDialog mRegistrationProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getToolbar();
        getDrawer();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = mCurrentUser.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        mScreenName = findViewById(R.id.screen_name);
        mProfilePicture = findViewById(R.id.profile_picture);
        mRegistrationProgress = new ProgressDialog(this);
        mRegistrationProgress.setTitle("Loading user profile...");
        mRegistrationProgress.setMessage("Please wait!");
        mRegistrationProgress.setCanceledOnTouchOutside(false);
        mRegistrationProgress.show();
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRegistrationProgress.dismiss();
                User currentUser = dataSnapshot.getValue(User.class);
//                String first_name = dataSnapshot.child("first_name").getValue().toString();
//                String last_name = dataSnapshot.child("last_name").getValue().toString();
//                String profile_pic_uri = dataSnapshot.child("profile_picture_uri").getValue().toString();
                String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
                mScreenName.setText(fullName);
                Picasso.with(MyAccount.this)
                        .load(currentUser.getThumbnail())
                        .placeholder(R.drawable.profile)
                        .into(mProfilePicture);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void EditUser(View v) {
//        Intent intent = new Intent(MyAccount.this, EditMyProfile.class);
//        startActivity(intent);
//        finish();
    }
    public void contactUser(View v){
        Intent intent = new Intent(MyAccount.this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}

