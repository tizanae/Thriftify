package theateam.thriftify;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
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

    public PostItemPictureAdapter(Context context, ArrayList<Uri> picture_uris) {
        super(context, R.layout.post_item_picture, picture_uris);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
//        Button btnDefault = (Button) convertView.findViewById(R.id.defaultButton);
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

