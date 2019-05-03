package com.kdp.quest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.fragment.TargetFragment;
import com.kdp.quest.fragment.TaskFragment;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.list.TaskList;
import com.maxst.ar.TrackerManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ARActivity {

    public BottomNavigationView navigation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(0));
        navigation.getMenu().getItem(2).setEnabled(false);

        initializeTargets();
        initializeTasks();
        loadTrackerData();
    }



    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            menuItem.setChecked(true);
            switch (menuItem.getItemId()) {
                case R.id.navigation_target:
                    fragment = new TargetFragment();
                    break;
                case R.id.navigation_camera:
                    fragment = CameraFragment.getInstance();
                    break;
                case R.id.navigation_info:
                    fragment = new TaskFragment();
                    break;
            }

            if (fragment == null)
                return false;

            loadFragment(fragment);
            return true;
        }
    };

    public void openItemNavigation(final Integer itemIndex) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOnNavigationItemSelectedListener.onNavigationItemSelected(navigation.getMenu().getItem(itemIndex));
                navigation.getMenu().getItem(itemIndex).setEnabled(true);
            }
        });
    }

    public void updateCurrent() {
        TaskList.getInstance(null).nextTask();
        TargetList.getInstance(null).nextTarget();
        openItemNavigation(0);
    }

    @Override
    public void onBackPressed() {
    }

    /**
     * Loading fragment in frame container
     *
     * @param fragment - fragment for loading
     */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(String.valueOf(navigation.getSelectedItemId()));
        transaction.commit();
    }

    /**
     * Initialize Targets and loading in TargetList
     */
    private void initializeTargets() {
        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target("Robot", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla a tortor at libero placerat mattis. Quisque mi lorem, commodo sit amet commodo sit amet, maximus sed quam. Vivamus eu viverra turpis. Pellentesque tincidunt nibh a placerat cursus. Duis velit sapien, ultricies venenatis semper vel, aliquet eu diam. Quisque egestas pellentesque rhoncus. Praesent sed faucibus magna. Nam non orci eu justo dignissim congue. Mauris nisi ligula, sodales et turpis ut, varius luctus sem. Suspendisse fringilla arcu a enim scelerisque, ac iaculis turpis sodales. Aliquam erat volutpat."));
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