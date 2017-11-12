package theateam.thriftify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public abstract class BaseActivity extends AppCompatActivity {
    private Drawer drawer;
    private Toolbar toolbar;

    public Toolbar getToolbar() {
        if (toolbar != null) return toolbar;
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            return null;
        }
        setSupportActionBar(toolbar);
        return toolbar;
    }

    public Drawer getDrawer() {
        if (toolbar == null || drawer != null) return drawer;
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_categories).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_post_item).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_messages).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_my_account).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_log_out).withIdentifier(5),
                        new PrimaryDrawerItem().withName("Maps Test").withIdentifier(6)
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
                                    intent = new Intent(BaseActivity.this, CreatePost.class);
                                    break;
                                case 4:
                                    intent = new Intent(BaseActivity.this, MyAccount.class);
                                    break;
                                case 5:
                                    FirebaseAuth.getInstance().signOut();
                                    intent = new Intent(BaseActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    finish();
                                    break;
                                case 6:
                                    intent = new Intent(BaseActivity.this, MapsActivity.class);
                                    break;
                                default:
                            }
                            if (intent != null) {
                                BaseActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .build();
        return drawer;
    }

    public void setBackArrow() {
        if (toolbar == null) {
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
}
