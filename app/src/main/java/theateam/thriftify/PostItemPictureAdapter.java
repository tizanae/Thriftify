package theateam.thriftify;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;


import java.util.ArrayList;

public class PostItemPictureAdapter extends ArrayAdapter<Bitmap> {
    private static class ViewHolder {
        ImageView imageView;
    }

    public PostItemPictureAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        super(context, R.layout.post_item_picture, bitmaps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Bitmap bitmap = getItem(position);
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
                remove(bitmap);
                notifyDataSetChanged();
            }
        });
        viewHolder.imageView.setImageBitmap(bitmap);

        return convertView;
    }
}

