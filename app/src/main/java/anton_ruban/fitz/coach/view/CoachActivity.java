package anton_ruban.fitz.coach.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import anton_ruban.fitz.R;
import anton_ruban.fitz.coach.notifyTraining.NotifyTrainingActivity;
import anton_ruban.fitz.coach.view.fragment.RequestSubscribFragment;
import anton_ruban.fitz.coach.view.fragment.SubscriberFragment;
import anton_ruban.fitz.login.view.LoginActivity;
import anton_ruban.fitz.utils.BaseActivity;
import anton_ruban.fitz.utils.PreferenceManager;

public class CoachActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);
        preferenceManager = new PreferenceManager(this);

        viewPager =  findViewById(R.id.container);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.d("position", String.valueOf(position));
                    return SubscriberFragment.newInstance(0, "Page # 1");
                case 1:
                    Log.d("position", String.valueOf(position));
                    return RequestSubscribFragment.newInstance(1, "Page # 2");
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0: return "Subscribers";
                case 1: return "Request";
                default: return  null;
            }
        }
    }

    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CoachActivity.this);
        builder.setTitle("Logout")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.logOut),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                preferenceManager.deleteSharedPref();
                                startActivity(new Intent(CoachActivity.this, LoginActivity.class));
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
