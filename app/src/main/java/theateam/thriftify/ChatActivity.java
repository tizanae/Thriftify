package theateam.thriftify;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends BaseActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private static final int PLACE_PICKER_REQUEST = 35;

    private User mCurrentUser;
    private User mFromUser;
    private String mCurrentUserId;
    private String mFromUserId;

    private ImageButton mChatAddBtn;
    private ImageButton mChatSendBtn;
    private EditText mChatMessageView;

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

        mCurrentUserId = getCurrentUser().getUid();
        mFromUserId = getIntent().getStringExtra("FROM_USER");

        getRootDatabase().child("users").child(mCurrentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "Received current user info.");
                mCurrentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error getting current user info: " + databaseError.getDetails());
            }
        });

        getRootDatabase().child("users").child(mFromUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "Received second user information.");
                mFromUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Error getting second user information: " + databaseError.getDetails());
            }
        });

        mChatAddBtn = findViewById(R.id.chat_add_btn);
        mChatSendBtn = findViewById(R.id.chat_send_btn);
        mChatMessageView = findViewById(R.id.chat_message_view);

        mAdapter = new MessageAdapter(messagesList, mCurrentUserId);

        mMessagesList =findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);

        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);

        mMessagesList.setAdapter(mAdapter);

        mChatAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatAddBtn.setClickable(false);
                startPlacePicker();
            }
        });

        loadMessages();

        mChatSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String name = place.getName().toString();
                String address = place.getAddress().toString();
                sendMessage(name + "\n" + address);

            }
        }
        mChatAddBtn.setClickable(true);
    }

    protected void startPlacePicker() {
        try {
            double lat = (mCurrentUser.getLatitude() + mFromUser.getLatitude()) / 2;
            double lng = (mCurrentUser.getLongitude() + mFromUser.getLongitude()) / 2;
            LatLng center = new LatLng(lat, lng);
            double radius = 150;
            LatLng targetNorthEast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2), 45);
            LatLng targetSouthWest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2), 225);
            LatLngBounds latLng = new LatLngBounds(targetSouthWest, targetNorthEast);
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder().setLatLngBounds(latLng);
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void loadMessages() {

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

            }
        });

    }

    private void sendMessage() {
        String message = mChatMessageView.getText().toString();

        if(!TextUtils.isEmpty(message)){
            mChatMessageView.setText("");
            sendMessage(message);
        }
    }

    private void sendMessage(String message) {
        String current_user_ref = "messages/" + mCurrentUserId+ "/" + mFromUserId;
        String chat_user_ref = "messages/" + mFromUserId + "/" + mCurrentUserId;

        DatabaseReference user_message_push = getRootDatabase().child("messages")
                .child(mCurrentUserId).child(mFromUserId).push();

        String messageId = user_message_push.getKey();

        // Reason
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("timestamp", ServerValue.TIMESTAMP);
        messageMap.put("from", mCurrentUserId);

        Map<String, Object> messageUserMap = new HashMap<>();
        messageUserMap.put(current_user_ref + "/" + messageId, messageMap);
        messageUserMap.put(chat_user_ref + "/" + messageId, messageMap);

        getRootDatabase().updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError != null) {
                    Log.e(TAG, databaseError.getDetails());
                }

            }
        });
    }
}
