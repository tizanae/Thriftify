package theateam.thriftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAccount extends BaseActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    private TextView mScreenName;

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

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String first_name = dataSnapshot.child("first_name").getValue().toString();
                String last_name = dataSnapshot.child("last_name").getValue().toString();
                String full_name = first_name + " " + last_name;
                mScreenName.setText(full_name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void EditUser(View v) {
        Intent intent = new Intent(MyAccount.this, EditMyProfile.class);
        startActivity(intent);
        finish();
    }
}

