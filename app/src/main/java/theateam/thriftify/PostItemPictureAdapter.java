package theateam.thriftify;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.List;


/**
 * Created by oflyingturtleo on 10/20/17.
 */

public class PostItemPictureAdapter extends RecyclerView.Adapter<PostItemPictureAdapter.ViewHolder> {
    private Context mContext;
    private List<Bitmap> pictures;
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
        }
    }




    public PostItemPictureAdapter(Context mContext, List<Bitmap> pictures) {
        this.mContext = mContext;
        this.pictures = pictures;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item_picture, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Bitmap bitmap = pictures.get(position);
        holder.thumbnail.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }
}

