package theateam.thriftify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Chat extends BaseActivity {

    private String userName;
    private ListView msgListView;
    private EditText msgInput;
    private ImageButton sendButton;
    private String userID;

    private FirebaseUser mCurrentUser;
    private DatabaseReference DBref;
    private DatabaseReference mUserDatabaseRef;
    private ChatListAdapter mAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DBref = FirebaseDatabase.getInstance().getReference();
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = mCurrentUser.getUid();
        mUserDatabaseRef = DBref.child("users").child(userID);

        mUserDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("first_name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        msgInput = (EditText) findViewById(R.id.message_capture);
        sendButton = (ImageButton) findViewById(R.id.chat_send_button);
        msgListView = (ListView) findViewById(R.id.chat_list_view);


        //send methods <enter> and button
        msgInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                MsgSend();
                return true;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgSend();
            }
        });
    }


    private void MsgSend() {
        Log.d("thriftify", "something has been sent");

        String input = msgInput.getText().toString();
        if (!input.equals("")) {
            UserMessage chat = new UserMessage(input, userName);
            DBref.child("messages").push().setValue(chat);
            msgInput.setText("");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new ChatListAdapter(this, DBref, userName);
        msgListView.setAdapter(mAdapter);
    }


    @Override
    public void onStop() {
        super.onStop();

        mAdapter.cleanup();

    }
}
