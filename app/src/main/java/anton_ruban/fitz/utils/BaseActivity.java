package anton_ruban.fitz.utils;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import anton_ruban.fitz.R;
import anton_ruban.fitz.club.updateClub.view.UpdateClubActivity;
import anton_ruban.fitz.club.view.CreateClubActivity;
import anton_ruban.fitz.club.view.MapsActivity;
import anton_ruban.fitz.login.view.LoginActivity;
import anton_ruban.fitz.main.view.MainActivity;
import anton_ruban.fitz.mytraining.view.MyTrainingActivity;
import anton_ruban.fitz.nutri.NutriActivity;
import anton_ruban.fitz.profile.view.ProfileActivity;
import anton_ruban.fitz.timers.TimersActivity;
import de.hdodenhof.circleimageview.CircleImageView;


public class BaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{

    public static final String TAG = "Nope";
    private FrameLayout view_stub;
    private NavigationView navigation_view;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;
    private PreferenceManager preferenceManager;
    private CircleImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        if(preferenceManager == null){
            preferenceManager = new PreferenceManager(this);
        }
        view_stub =  findViewById(R.id.view_stub);
        navigation_view =  findViewById(R.id.navigation_view);
        mDrawerLayout =  findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BitmapDrawable backgrd = new BitmapDrawable(BitmapFactory.decodeResource(getResources(),R.drawable.bar));
        getSupportActionBar().setBackgroundDrawable(backgrd);
        drawerMenu = navigation_view.getMenu();
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        NavigationView navigationView =  findViewById(R.id.navigation_view);

        View header = navigationView.getHeaderView(0);
        img = header.findViewById(R.id.profile_image);
        TextView userEmailHeader = header.findViewById(R.id.userEmail);
        userEmailHeader.setText(preferenceManager.getUserName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferenceManager.getUserIsOwner() != 0) {
            drawerMenu.getItem(2).setTitle(R.string.manageClub);
        }

        if (preferenceManager.getImageUser() != null) {
            try {
                img.setImageBitmap(preferenceManager.getImageUser());
            } catch (Exception error) {}
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (view_stub != null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, view_stub, false);
            view_stub.addView(stubView, lp);
        }

    }

    @Override
    public void setContentView(View view) {
        if (view_stub != null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            view_stub.addView(view, lp);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (view_stub != null) {
            view_stub.addView(view, params);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if (!this.getClass().getName().equals(MainActivity.class.getName())){
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
                mDrawerLayout.closeDrawers();
                break;

            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.club:
                if (preferenceManager.getUserIsOwner() != 0) {
                    startActivity(new Intent(this, UpdateClubActivity.class));
                    mDrawerLayout.closeDrawers();
                } else {
                    startActivity(new Intent(this, CreateClubActivity.class));
                    mDrawerLayout.closeDrawers();
                }
                break;
            case R.id.findMap:
                Intent intent5 = new Intent(this,MapsActivity.class);
                intent5.putExtra("mapStateFind",true);
                startActivity(intent5);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.workout:
                Intent intent2 = new Intent(this,MyTrainingActivity.class);
                startActivity(intent2);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nutrition:
                Intent intent3 = new Intent(this,NutriActivity.class);
                startActivity(intent3);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.tools:
                startActivity(new Intent(this, TimersActivity.class));
                mDrawerLayout.closeDrawers();
                break;
            case R.id.logout:
                signOut();
        }
        return false;
    }
    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        builder.setTitle("LogOut")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.logOut),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                preferenceManager.deleteSharedPref();
                                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                                finish();
                                dialog.cancel();
                            }
                        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
