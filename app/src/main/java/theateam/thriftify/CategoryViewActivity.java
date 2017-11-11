package theateam.thriftify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CategoryViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        getToolbar();
        setBackArrow();


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
