package theateam.thriftify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);
        RecyclerView itemViewer = (RecyclerView)findViewById(R.id.item_viewer);
        itemViewer.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        itemViewer.setLayoutManager(layoutManager);
        itemViewer.setItemAnimator(new DefaultItemAnimator());

        ArrayList<PostPreview> postings = new ArrayList<>();
        postings.add(new PostPreview("Test", R.drawable.post_image_1));
        postings.add(new PostPreview("Test", R.drawable.post_image_2));
        postings.add(new PostPreview("Test", R.drawable.post_image_3));

        PostPreviewAdapter adapter = new PostPreviewAdapter(this, postings);
        itemViewer.setAdapter(adapter);
    }
}
