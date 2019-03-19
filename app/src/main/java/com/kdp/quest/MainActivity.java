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
import com.maxst.ar.TrackerManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ARActivity {

    private ActionBar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Camera");
        loadFragment(CameraFragment.getInstance());

        ArrayList<Target> targets = new ArrayList<>();

        targets.add(new Target("Robot", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultrices odio sed neque interdum pellentesque. Fusce sed sapien neque. Pellentesque consectetur, elit nec commodo molestie, nisi odio convallis lectus, quis sodales libero nibh non urna. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque accumsan eleifend enim, eget luctus leo tincidunt ac. Donec non metus turpis. Cras at mauris velit. Nam semper dapibus est, at interdum dui maximus ut."));
        targets.add(new Target("ClearCode", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultrices odio sed neque interdum pellentesque. Fusce sed sapien neque. Pellentesque consectetur, elit nec commodo molestie, nisi odio convallis lectus, quis sodales libero nibh non urna. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque accumsan eleifend enim, eget luctus leo tincidunt ac. Donec non metus turpis. Cras at mauris velit. Nam semper dapibus est, at interdum dui maximus ut."));
        targets.add(new Target("Kish", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed ultrices odio sed neque interdum pellentesque. Fusce sed sapien neque. Pellentesque consectetur, elit nec commodo molestie, nisi odio convallis lectus, quis sodales libero nibh non urna. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Quisque accumsan eleifend enim, eget luctus leo tincidunt ac. Donec non metus turpis. Cras at mauris velit. Nam semper dapibus est, at interdum dui maximus ut."));

        TargetManager.getInstance(targets);


        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", "95"));
        tasks.add(new Task("2", "4"));
        tasks.add(new Task("3", "120"));
        TaskManager.getInstance(tasks);


        List<String> trackingFileName = TargetManager.getInstance(null).getTrackingFileName();
        for (String s : trackingFileName) {
            TrackerManager.getInstance().addTrackerData(s, true);
        }
        TrackerManager.getInstance().loadTrackerData();
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