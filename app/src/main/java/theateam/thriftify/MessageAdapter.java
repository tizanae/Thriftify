package theateam.thriftify;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * https://github.com/akshayejh/Lapit---Android-Firebase-Chat-App
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> mMessageList;
    private String mUserId;

    MessageAdapter(List<Message> mMessageList, String mUserId) {
        this.mMessageList = mMessageList;
        this.mUserId = mUserId;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView displayName;
        CircleImageView profileImage;
        TextView timeText;

        MessageViewHolder(View view) {
            super(view);

            messageText = view.findViewById(R.id.message_text_layout);
            displayName = view.findViewById(R.id.name_text_layout);
            profileImage = view.findViewById(R.id.message_profile_layout);
            timeText = view.findViewById(R.id.time_text_layout);

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

        Message message = mMessageList.get(i);

        String from_user = message.getFrom();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(from_user)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user == null) return;
                String fullName = user.getFirstName() + " " + user.getLastName();
                viewHolder.displayName.setText(fullName);

                Picasso.with(viewHolder.profileImage.getContext()).load(user.getThumbnail())
                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        viewHolder.messageText.setText(message.getMessage());
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("d MMM yy, hh:mm aaa", Locale.US);

        viewHolder.timeText.setText(simpleDateFormat.format(message.getTimestamp()));


    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

}