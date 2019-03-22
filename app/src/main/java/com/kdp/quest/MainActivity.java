package com.kdp.quest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.fragment.InfoFragment;
import com.kdp.quest.fragment.TargetFragment;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.TargetManager;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.TaskManager;
import com.maxst.ar.TrackerManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ARActivity {

    private ActionBar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = getSupportActionBar();
        toolbar.setTitle(getString(R.string.title_camera));
        loadFragment(CameraFragment.getInstance());

        initializeTargets();
        initializeTasks();
        loadTrackerData();
    }

    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {
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

    private void initializeTargets() {
        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target("Robot", "Lorem ipsum dolor."));
        targets.add(new Target("ClearCode", "Lorem ipsum dolor."));
        TargetManager.getInstance(targets);
    }

    private void initializeTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", "95"));
        tasks.add(new Task("2", "4"));
        TaskManager.getInstance(tasks);
    }

    private void loadTrackerData() {
        List<String> trackingFileName = TargetManager.getInstance(null).getTrackingFileName();
        for (String s : trackingFileName) {
            TrackerManager.getInstance().addTrackerData(s, true);
        }
        TrackerManager.getInstance().loadTrackerData();
    }
}