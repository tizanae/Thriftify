package theateam.thriftify;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActiveChatsActivity extends BaseActivity {

    private static final String TAG = ActiveChatsActivity.class.getSimpleName();

    ActiveChatUserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_chats);

        checkAuthenticated();
        getToolbar();
        getDrawer();

        ChildEventListener activeUserChatsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "User added");
                String from_user_id = dataSnapshot.getKey();
                getUser(from_user_id);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        Log.i(TAG, "Returning active chats");

        getRootDatabase()
                .child("messages")
                .child(getCurrentUser().getUid())
                .addChildEventListener(activeUserChatsListener);

        ListView listView = findViewById(R.id.active_chats);
        TextView emptyText = findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);

        mAdapter = new ActiveChatUserAdapter(this, new ArrayList<User>());
        listView.setAdapter(mAdapter);
    }

    public void getUser(final String userId) {
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mAdapter.add(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        getRootDatabase().child("users").child(userId).addValueEventListener(userListener);
    }
}
