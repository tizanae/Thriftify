package theateam.thriftify;

import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private String mCurrentUserId;
    private String mFromUserId;
    private String mChatId;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

    private DatabaseReference mRootRef;
    private RecyclerView mMessagesList;

    private final List<Message> messagesList = new ArrayList<>();
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        getToolbar();
        setBackArrow();

        mRootRef = FirebaseDatabase.getInstance().getReference();

        mCurrentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mFromUserId = getIntent().getStringExtra("FROM_USER");
        int compare = mCurrentUserId.compareTo(mFromUserId);

        if (compare <= 0) {
            mChatId = mCurrentUserId + mFromUserId;
        } else {
            mChatId = mFromUserId + mCurrentUserId;
        }

        mChatAddBtn = (ImageButton) findViewById(R.id.chat_add_btn);
        mChatSendBtn = (ImageButton) findViewById(R.id.chat_send_btn);
        mChatMessageView = (EditText) findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesList, mCurrentUserId);

        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        loadMessages();

        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void loadMessages() {

        mRootRef.child("messages").child(mCurrentUserId).child(mFromUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message = dataSnapshot.getValue(Message.class);
                Toast.makeText(ChatActivity.this, "" + message.getFrom(), Toast.LENGTH_LONG);
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

            }
        });

    }

    private void sendMessage() {


        String message = mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){


            String current_user_ref = "messages/" + mCurrentUserId + "/" + mFromUserId;
            String chat_user_ref = "messages/" + mFromUserId + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mFromUserId).push();

            String push_id = user_message_push.getKey();
            Message msg = new Message(message, mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, msg);
            messageUserMap.put(chat_user_ref + "/" + push_id, msg);

            mChatMessageView.setText("");

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){


                    }

                }
            });
        }

    }
}
