package theateam.thriftify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActiveChatUserAdapter extends ArrayAdapter<ActiveChatUser> {

    private static class ViewHolder {
        TextView name;
        ImageView thumbnail;
    }

    public ActiveChatUserAdapter(Context context, ArrayList<ActiveChatUser> activeChatUsers) {
        super(context, R.layout.active_chat_user, activeChatUsers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ActiveChatUser activeChatUser = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.active_chat_user, parent, false);
            viewHolder.name = convertView.findViewById(R.id.user_name);
            viewHolder.thumbnail = convertView.findViewById(R.id.user_thumb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = activeChatUser.getUser_id();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("FROM_USER", user_id);
                getContext().startActivity(intent);
            }
        });
        viewHolder.name.setText(activeChatUser.getName());
        Picasso.with(getContext()).load(activeChatUser.getThumb()).into(viewHolder.thumbnail);


        return convertView;
    }
}
