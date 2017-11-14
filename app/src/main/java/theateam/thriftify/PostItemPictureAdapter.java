package theateam.thriftify;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostItemPictureAdapter extends ArrayAdapter<Uri> {
    private static class ViewHolder {
        ImageView imageView;
    }

    PostItemPictureAdapter(Context context, ArrayList<Uri> picture_uris) {
        super(context, R.layout.post_item_picture, picture_uris);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Uri uri = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.post_item_picture, parent, false);
            viewHolder.imageView = convertView.findViewById(R.id.thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Button btnDelete = convertView.findViewById(R.id.deleteButton);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(uri);
                notifyDataSetChanged();
            }
        });
        Picasso.with(getContext()).load(uri).into(viewHolder.imageView);


        return convertView;
    }
}

