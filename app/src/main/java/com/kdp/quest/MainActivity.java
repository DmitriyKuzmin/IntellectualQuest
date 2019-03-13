package com.kdp.quest;


import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.fragment.InfoFragment;
import com.kdp.quest.fragment.TargetFragment;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.TargetManager;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.TaskManager;

import java.util.ArrayList;


public class MainActivity extends ARActivity {

    private ActionBar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isSupportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Camera");
        loadFragment(CameraFragment.getInstance());

        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target("Robot"));
        targets.add(new Target("ClearCode"));
        targets.add(new Target("Kish"));
        TargetManager.getInstance(targets);


        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", "95"));
        tasks.add(new Task("2", "4"));
        TaskManager.getInstance(tasks);
    }

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_camera:
                    toolbar.setTitle(getString(R.string.title_camera));
                    fragment = CameraFragment.getInstance();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_info:
                    toolbar.setTitle(getString(R.string.title_info));
                    fragment = new InfoFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_target:
                    toolbar.setTitle(getString(R.string.title_target));
                    fragment = new TargetFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    /**
     * Is support system Open ES2
     *
     * @return boolean - true if support, else false
     */
    private boolean isSupportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

    /**
     * Loading fragment in frame container
     *
     * @param fragment - fragment for loading
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, "CameraFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }


}