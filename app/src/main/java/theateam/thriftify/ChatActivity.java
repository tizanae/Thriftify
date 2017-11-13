package theateam.thriftify;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private String mCurrentUserId;
    private String mFromUserId;

    private EditText mChatMessageView;

    private final List<Message> messagesList = new ArrayList<>();
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        getToolbar();
        setBackArrow();

        mCurrentUserId = getCurrentUser().getUid();
        mFromUserId = getIntent().getStringExtra("FROM_USER");

//        ImageButton chatAddBtn = findViewById(R.id.chat_add_btn);
        ImageButton chatSendBtn = findViewById(R.id.chat_send_btn);
        mChatMessageView = findViewById(R.id.chat_message_view);

        // Set Up Recycler View
        mAdapter = new MessageAdapter(messagesList, mCurrentUserId);
        RecyclerView messageList = findViewById(R.id.messages_list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        messageList.setHasFixedSize(true);
        messageList.setLayoutManager(linearLayout);
        messageList.setAdapter(mAdapter);

        loadMessages();

        chatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {
        Log.i(TAG, "Loading messages");
        getRootDatabase()
                .child("messages")
                .child(mCurrentUserId)
                .child(mFromUserId)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                messagesList.add(message);
                mAdapter.notifyDataSetChanged();
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
                Log.e(TAG, "Load messages cancelled, " + databaseError.getDetails());
            }
        });

    }

    private void sendMessage() {

        Log.i(TAG, "Sending a message");
        String message = mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){


            String currentUserRef = "messages/" + mCurrentUserId + "/" + mFromUserId;
            String fromUserRef = "messages/" + mFromUserId + "/" + mCurrentUserId;

            DatabaseReference messagePush = getRootDatabase().child("messages")
                    .child(mCurrentUserId).child(mFromUserId).push();

            String msgId = messagePush.getKey();
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", message);
            messageMap.put("timestamp", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map<String, Object> messageUserMap = new HashMap<>();
            messageUserMap.put(currentUserRef + "/" + msgId, messageMap);
            messageUserMap.put(fromUserRef + "/" + msgId, messageMap);

            mChatMessageView.setText("");

            getRootDatabase().updateChildren(messageUserMap).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Send message failed, " + e.getMessage());
                }
            });
        }

    }
}
