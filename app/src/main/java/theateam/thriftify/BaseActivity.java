package theateam.thriftify;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Random;


public abstract class BaseActivity extends AppCompatActivity {
    private Drawer mDrawer;
    private Toolbar mToolbar;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootDatabase;
    private StorageReference mRootStorage;

    public Toolbar getToolbar() {

        if (mToolbar != null) return mToolbar;
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar == null) {
            return null;
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        return mToolbar;
    }

    public Drawer getDrawer() {
        if (mToolbar == null || mDrawer != null) return mDrawer;
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHasStableIds(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_categories).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_post_item).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_messages).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_my_account).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_log_out).withIdentifier(5)
                )
                .withSelectedItem(-1)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            int identifier = (int)drawerItem.getIdentifier();
                            switch(identifier) {
                                case 1:
                                    intent = new Intent(BaseActivity.this, MainActivity.class);
                                    break;
                                case 2:
                                    intent = new Intent(BaseActivity.this, CreatePostActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(BaseActivity.this, ActiveChatsActivity.class);
                                    break;
                                case 4:
                                    intent = new Intent(BaseActivity.this, MyAccount.class);
                                    break;
                                case 5:
                                    FirebaseAuth.getInstance().signOut();
                                    intent = new Intent(BaseActivity.this, LoginActivity.class);
                                    break;
                                default:
                            }
                            if (intent != null) {
                                BaseActivity.this.startActivity(intent);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                            }
                        }
                        return false;
                    }
                })
                .build();
        return mDrawer;
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public DatabaseReference getRootDatabase() {
        if (mRootDatabase == null) {
            mRootDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return mRootDatabase;
    }

    public StorageReference getRootStorage() {
        if (mRootStorage == null) {
            mRootStorage = FirebaseStorage.getInstance().getReference();
        }
        return mRootStorage;
    }

    public void checkAuthenticated() {
        if (getCurrentUser() == null) {
            Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
            BaseActivity.this.startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
    }

    public void setBackArrow() {
        if (mToolbar == null || getSupportActionBar() == null) {
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Pair with set display home as up enabled to set up go back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    // https://stackoverflow.com/questions/12116092/android-random-string-generator
    public static String generateRandomString(int maxLength) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(maxLength);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
