package com.kdp.quest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.fragment.InfoFragment;
import com.kdp.quest.fragment.TargetFragment;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.list.TaskList;
import com.maxst.ar.TrackerManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ARActivity {

    private ActionBar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},2);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = getSupportActionBar();
        toolbar.setTitle(getString(R.string.title_camera));
        loadFragment(CameraFragment.getInstance());

        initializeTargets();
        initializeTasks();
        loadTrackerData();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()) {
                case R.id.navigation_camera:
                    toolbar.setTitle(getString(R.string.title_camera));
                    fragment = CameraFragment.getInstance();
                    break;
                case R.id.navigation_info:
                    toolbar.setTitle(getString(R.string.title_info));
                    fragment = new InfoFragment();
                    break;
                case R.id.navigation_target:
                    toolbar.setTitle(getString(R.string.title_target));
                    fragment = new TargetFragment();
                    break;
            }

            if (fragment == null)
                return false;

            loadFragment(fragment);
            return true;
        }
    };

    /**
     * Loading fragment in frame container
     *
     * @param fragment - fragment for loading
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Initialize Targets and loading in TargetList
     */
    private void initializeTargets() {
        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target("Robot", "Lorem ipsum dolor."));
        targets.add(new Target("ClearCode", "Lorem ipsum dolor."));
        targets.add(new Target("Kish", "Lorem ipsum dolor."));
        TargetList.getInstance(targets);
    }

    /**
     * Initialize Task and loading in TaskList
     */
    private void initializeTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", "95"));
        tasks.add(new Task("2", "4"));
        tasks.add(new Task("3", "110"));
        TaskList.getInstance(tasks);
    }

    /**
     * Load data for tracker from TargetList
     */
    private void loadTrackerData() {
        List<String> trackingFileName = TargetList.getInstance(null).getTrackingFileName();
        for (String s : trackingFileName) {
            TrackerManager.getInstance().addTrackerData(s, true);
        }
        TrackerManager.getInstance().loadTrackerData();
    }
}