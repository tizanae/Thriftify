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


///**
// * Created by oflyingturtleo on 10/20/17.
// */
//
//public class PostItemPictureAdapter extends RecyclerView.Adapter<PostItemPictureAdapter.ViewHolder> {
//    private Context mContext;
//    private List<Bitmap> pictures;
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView thumbnail;
//
//        public ViewHolder(View view) {
//            super(view);
//            thumbnail = view.findViewById(R.id.thumbnail);
//        }
//    }
//
//
//
//
//    public PostItemPictureAdapter(Context mContext, List<Bitmap> pictures) {
//        this.mContext = mContext;
//        this.pictures = pictures;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.post_item_picture, parent, false);
//
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        Bitmap bitmap = pictures.get(position);
//        holder.thumbnail.setImageBitmap(bitmap);
//    }
//
//    @Override
//    public int getItemCount() {
//        return pictures.size();
//    }
//}
public class PostItemPictureAdapter extends ArrayAdapter<Bitmap> {
    // View lookup cache
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
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Button btnDelete = (Button) convertView.findViewById(R.id.deleteButton);
        Button btnDefault = (Button) convertView.findViewById(R.id.defaultButton);
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

