package theateam.thriftify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PostPreviewAdapter extends ArrayAdapter<Post> {

    private static class ViewHolder {
        TextView title;
        ImageView thumbnail;
        CardView cardView;
    }

    public PostPreviewAdapter(Context context, ArrayList<Post> posts) {
        super(context, R.layout.posting_template, posts);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

        final Post post = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.posting_template, parent, false);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.thumbnail = convertView.findViewById(R.id.thumbnail);
            viewHolder.cardView = convertView.findViewById(R.id.card_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostInfoActivity.class);
                intent.putExtra("CATEGORY_KEY", post.getCategoryKey());
                intent.putExtra("POST_KEY", post.getPostKey());
                getContext().startActivity(intent);
            }
        });
        if (post.getPictures() != null) {
            String defaultThumb = post.getPictures().values().toArray()[0].toString();
            Picasso.with(getContext()).load(defaultThumb).into(viewHolder.thumbnail);
        }

        viewHolder.title.setText(post.getTitle());

        return convertView;
    }
}
