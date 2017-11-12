package theateam.thriftify;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by oflyingturtleo on 10/20/17.
 */



public class PostPreviewAdapter extends ArrayAdapter<PostPreview> {

    private static class ViewHolder {
        TextView title;
        ImageView thumbnail;
        CardView cardView;
    }

    public PostPreviewAdapter(Context context, ArrayList<PostPreview> post_previews) {
        super(context, R.layout.posting_template, post_previews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final PostPreview postPreview = getItem(position);
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

//        Button btnDelete = convertView.findViewById(R.id.deleteButton);
////        Button btnDefault = (Button) convertView.findViewById(R.id.defaultButton);
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                remove(uri);
//                notifyDataSetChanged();
//            }
//        });
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostInfo.class);
                intent.putExtra("CATEGORY_KEY", postPreview.getCategory());
                intent.putExtra("POST_KEY", postPreview.getKey());
                getContext().startActivity(intent);
            }
        });
        viewHolder.title.setText(postPreview.getName());
        Picasso.with(getContext()).load(postPreview.getThumbnail()).into(viewHolder.thumbnail);


        return convertView;
    }
}
