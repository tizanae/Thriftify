package theateam.thriftify;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class PostItemActivity extends BaseActivity {

    static final int REQUEST_IMAGE_GET = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    PostItemPictureAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        getToolbar();
        getDrawer();

        FloatingActionButton fab = findViewById(R.id.addPictureButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        adapter = new PostItemPictureAdapter(this, bitmaps);
        adapter.setNotifyOnChange(true);
        ListView listView = (ListView) findViewById(R.id.picturesContainer);
        listView.setAdapter(adapter);

    }

    private void showDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        String[] dialogItems = {
                "Select from gallery",
                "Take a picture"
        };
        dialog.setItems(
                dialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                selectImage();
                                break;
                            case 1:
                                takePicture();
                                break;
                        }
                    }
                }
        );
        dialog.show();
    }

    public void selectImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GET);
    }

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            adapter.add(bitmap);
        }
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                adapter.add(bitmap);
            } catch (IOException e) {

            }

        }
    }

}
