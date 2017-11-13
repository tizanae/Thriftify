package theateam.thriftify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveChatsActivity extends BaseActivity {

    DatabaseReference mRootDatabase;
    ActiveChatUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_chats);

        getToolbar();
        getDrawer();

        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRootDatabase = FirebaseDatabase.getInstance().getReference();
//        ValueEventListener responseListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
//
//                } else {
//                    // Do stuff
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };

//        mRootDatabase.child("messages").child(user_id).addListenerForSingleValueEvent(responseListener);
        mRootDatabase
                .child("messages")
                .child(user_id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String from_user_id = dataSnapshot.getKey();
                        getUser(from_user_id);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        ListView listView = findViewById(R.id.active_chats);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

        mAdapter = new ActiveChatUserAdapter(this, new ArrayList<ActiveChatUser>());
        listView.setAdapter(mAdapter);
    }

    public void getUser(final String user_id) {
        mRootDatabase.child("users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mAdapter.add(new ActiveChatUser(user_id, user.first_name + " " + user.last_name, user.profile_picture_uri));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
