package theateam.thriftify;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// https://github.com/akshayejh/Lapit---Android-Firebase-Chat-App

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessageList;
    private String mUserId;

    private DatabaseReference mUserDatabase;

    public MessageAdapter(List<Message> mMessageList, String mUserId) {
        this.mMessageList = mMessageList;
        this.mUserId = mUserId;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public TextView displayName;
        public CircleImageView profileImage;


        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return mMessageList.get(position).getFrom().equals(mUserId) ? 1 : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_chat_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_chat_item, parent, false);
        }
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Message c = mMessageList.get(i);

        String from_user = c.getFrom();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(from_user);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                viewHolder.displayName.setText(user.getFirstName() + " " + user.getLastName());

                Picasso.with(viewHolder.profileImage.getContext()).load(user.getThumbnail())
                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

}