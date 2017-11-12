package theateam.thriftify;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by TC on 11/11/17.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String userName;
    private ArrayList<DataSnapshot> mSnapshots;

    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            //add new messages
            mSnapshots.add(dataSnapshot);
            //update listview
            notifyDataSetChanged();
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
    };
    public ChatListAdapter(Activity activity, DatabaseReference ref, String name) {

        mActivity = activity;
        userName = name;
        mDatabaseReference = ref.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mSnapshots = new ArrayList<>();
    }

    private static class ViewHolder{
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams params;
    }

    @Override
    public int getCount() {
        return mSnapshots.size();
    }

    @Override
    public UserMessage getItem(int i) {
        DataSnapshot snapshot = mSnapshots.get(i);
        return snapshot.getValue(UserMessage.class);    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //dynamically load listview rows with new messages as chat expands
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_chat_item, viewGroup, false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) view.findViewById(R.id.name_text_layout);
            holder.body = (TextView) view.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            view.setTag(holder);

        }


        final UserMessage message = getItem(i);
        final ViewHolder holder = (ViewHolder) view.getTag();

        boolean isMe = message.getSender().equals(userName);
        setChatRowAppearance(isMe, holder);

        String sender = message.getSender();
        holder.authorName.setText(sender);

        String msg = message.getMessage();
        holder.body.setText(msg);


        return view;
    }
    private void setChatRowAppearance(boolean isItMe, ViewHolder holder) {

        if (isItMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);

    }
    void cleanup() {

        mDatabaseReference.removeEventListener(mListener);
    }
}
