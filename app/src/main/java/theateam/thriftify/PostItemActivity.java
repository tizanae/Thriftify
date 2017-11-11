package theateam.thriftify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;


public class PostItemActivity extends BaseActivity {

    private TextView mPostTitle;
    private TextView mPostPrice;
    private TextView mPostDescription;

    private String mCategoryKey;
    private double mLatitude;
    private double mLongitude;

    private PostItemPictureAdapter adapter;

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        getToolbar();
        setBackArrow();

        mCategoryKey = getIntent().getStringExtra("CATEGORY_KEY");
        mPostTitle = (TextView) findViewById(R.id.input_title);
        mPostPrice = (TextView) findViewById(R.id.input_price);
        mPostDescription = (TextView) findViewById(R.id.input_description);

        FloatingActionButton uploadPicture = findViewById(R.id.addPictureButton);
        Button saveButton = findViewById(R.id.post_item_btn);
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .start(PostItemActivity.this);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItem();
            }
        });


        ArrayList<Uri> uris = new ArrayList<>();
        adapter = new PostItemPictureAdapter(this, uris);
        adapter.setNotifyOnChange(true);
        ListView listView = (ListView) findViewById(R.id.picturesContainer);
        listView.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                adapter.add(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error in loading picture: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void postItem() {

        String post_title = mPostTitle.getText().toString();
        String post_price = mPostPrice.getText().toString();
        String post_description = mPostDescription.getText().toString();
        View focusView = null;
        if (TextUtils.isEmpty(post_title)) {
            mPostTitle.setError("Post title is required.");
            focusView = mPostTitle;
        } else if (TextUtils.isEmpty(post_price)) {
            mPostPrice.setError("Post price is required.");
            focusView = mPostPrice;
        } else if (TextUtils.isEmpty(post_description)){
            mPostDescription.setError("Post description is required.");
            focusView = mPostDescription;
        }
        if (focusView != null) {
            focusView.requestFocus();
        } else if (adapter.getCount() < 1) {
            Toast.makeText(this, "You must upload a picture.", Toast.LENGTH_LONG).show();
        }
        ArrayList<Uri> picture_uris = new ArrayList<>();
        for (int i = 0; i < adapter.getCount(); i++) {
            Uri uri = adapter.getItem(i);
            picture_uris.add(uri);
        }

    }

}
