package theateam.thriftify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

public class PostInfo extends BaseActivity {

    private String mCategoryKey;
    private String mPostKey;

    private DatabaseReference mRootReference;

    private TextView mTitleView;
    private TextView mPriceView;
    private TextView mDescriptionView;
    private Button mChat;

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

        mCategoryKey = getIntent().getStringExtra("CATEGORY_KEY");
        mPostKey = getIntent().getStringExtra("POST_KEY");

        mRootReference = FirebaseDatabase.getInstance().getReference();

        mCarouselView = findViewById(R.id.carouselView);
        mTitleView = findViewById(R.id.post_title);
        mPriceView = findViewById(R.id.post_price);
        mDescriptionView = findViewById(R.id.post_description);
        mChat = findViewById(R.id.contact);

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setTitle("Loading item...");
        mLoadingDialog.setMessage("Please wait!");
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.show();

        mCarouselView.setImageListener(imageListener);

        mRootReference.child("posts").child(mCategoryKey).child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPost = dataSnapshot.getValue(Post.class);
                mLoadingDialog.dismiss();
//                Toast.makeText(PostInfo.this, "" + post.picture_uris.toString(), Toast.LENGTH_LONG);
                mImageUris = new ArrayList<>(mPost.picture_uris.values());
                mCarouselView.setPageCount(mImageUris.size());
                mTitleView.setText(mPost.title);
                mPriceView.setText(mPost.price);
                mDescriptionView.setText(mPost.description);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostInfo.this, ChatActivity.class);
                intent.putExtra("FROM_USER", mPost.user_id);
                startActivity(intent);
            }
        });


    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            String image_uri = mImageUris.get(position);
            Picasso.with(PostInfo.this).load(image_uri).into(imageView);
        }
    };

}
