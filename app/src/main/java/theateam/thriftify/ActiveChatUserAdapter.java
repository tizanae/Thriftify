package theateam.thriftify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActiveChatUserAdapter extends ArrayAdapter<User> {

    private static class ViewHolder {
        TextView name;
        ImageView thumbnail;
    }

    @SuppressWarnings("WeakerAccess")
    public ActiveChatUserAdapter(Context context, ArrayList<User> activeChatUsers) {
        super(context, R.layout.active_chat_user, activeChatUsers);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final User activeChatUser = getItem(position);
        if (activeChatUser == null) return convertView;
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

                String user_id = activeChatUser.getUserId();
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("FROM_USER", user_id);
                getContext().startActivity(intent);
            }
        });
        String fullName = activeChatUser.getFirstName() + " " + activeChatUser.getLastName();
        viewHolder.name.setText(fullName);
        Picasso.with(getContext()).load(activeChatUser.getThumbnail()).into(viewHolder.thumbnail);


        return convertView;
    }
}
