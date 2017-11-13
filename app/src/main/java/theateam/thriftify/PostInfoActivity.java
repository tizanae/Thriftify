package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class PostInfoActivity extends BaseActivity {

    private static final String TAG = PostInfoActivity.class.getSimpleName();

    private TextView mTitleView;
    private TextView mPriceView;
    private TextView mDescriptionView;

    private CarouselView mCarouselView;

    private ArrayList<String> mImageUris;

    private ProgressDialog mLoadingDialog;

    private Post mPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_info);

        getToolbar();
        setBackArrow();

        String categoryKey = getIntent().getStringExtra("CATEGORY_KEY");
        String postKey = getIntent().getStringExtra("POST_KEY");

        mCarouselView = findViewById(R.id.carouselView);
        mTitleView = findViewById(R.id.post_title);
        mPriceView = findViewById(R.id.post_price);
        mDescriptionView = findViewById(R.id.post_description);
        Button mChat = findViewById(R.id.contact);

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setTitle("Loading item...");
        mLoadingDialog.setMessage("Please wait!");
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();

        mCarouselView.setImageListener(imageListener);

        Log.i(TAG, "Loading Post");

        getRootDatabase().child("posts").child(categoryKey).child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPost = dataSnapshot.getValue(Post.class);
                mLoadingDialog.dismiss();
                mImageUris = new ArrayList<>(mPost.getPictures().values());
                mCarouselView.setPageCount(mImageUris.size());
                mTitleView.setText(mPost.getTitle());
                mPriceView.setText(mPost.getPrice());
                mDescriptionView.setText(mPost.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Loading post cancelled, " + databaseError.getDetails());
            }
        });
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostInfoActivity.this, ChatActivity.class);
                intent.putExtra("FROM_USER", mPost.getUserId());
                startActivity(intent);
            }
        });


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            String image_uri = mImageUris.get(position);
            Picasso.with(PostInfoActivity.this).load(image_uri).into(imageView);
        }
    };

}
