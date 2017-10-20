package theateam.thriftify;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


/**
 * Created by oflyingturtleo on 10/20/17.
 */

public class PostPreviewAdapter extends RecyclerView.Adapter<PostPreviewAdapter.ViewHolder> {
    private Context mContext;
    private List<PostPreview> postPreviewList;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);

            // TODO: Figure out why this doesn't do anything on the picture
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PostInfo.class);
                    context.startActivity(intent);
                }
            });
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

        }

    }




    public PostPreviewAdapter(Context mContext, List<PostPreview> postPreviewList) {
        this.mContext = mContext;
        this.postPreviewList = postPreviewList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posting_template, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        PostPreview postPreview = postPreviewList.get(position);
        holder.title.setText(postPreview.getName());

        holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, postPreview.getThumbnail()));
    }

    @Override
    public int getItemCount() {
        return postPreviewList.size();
    }
}

