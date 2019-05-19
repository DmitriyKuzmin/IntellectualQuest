package com.kdp.quest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.fragment.FinishFragment;
import com.kdp.quest.fragment.TargetFragment;
import com.kdp.quest.fragment.TaskFragment;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.list.TaskList;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends ARActivity {
    TargetList targetList;
    TaskList taskList;

    int max;
    public ProgressBar progressBar;
    public BottomNavigationView navigation;
    public int selectIdNavigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }

        taskList = TaskList.getInstance();
        targetList = TargetList.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

        max = (targetList.getCountTargets() > taskList.getCountTasks()) ? taskList.getCountTasks() : targetList.getCountTargets();
        progressBar.setMax(max);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        openNavigationItem(R.id.navigation_target);


    }


    public void enableElementsNavigation(boolean flag) {
        for (int i = 0; i < navigation.getMenu().size(); i++) {
            navigation.getMenu().getItem(i).setEnabled(flag);
        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;

            boolean isDetectedTarget = targetList.getCurrentTarget().isDetected();

            switch (menuItem.getItemId()) {
                case R.id.navigation_target:
                    fragment = TargetFragment.getInstance();
                    if (!isDetectedTarget)
                        navigation.getMenu().getItem(2).setEnabled(false);
                    break;
                case R.id.navigation_camera:
                    fragment = CameraFragment.getInstance();
                    if (!isDetectedTarget)
                        navigation.getMenu().getItem(2).setEnabled(false);
                    break;
                case R.id.navigation_task:
                    fragment = TaskFragment.getInstance();
                    break;
                case R.id.navigation_finish:
                    enableElementsNavigation(false);
                    fragment = FinishFragment.getInstance();
                    break;
            }

            if (fragment == null)
                return false;

            selectIdNavigation = menuItem.getItemId();
            loadFragment(fragment);
            return true;
        }
    };

    /**
     * Loading fragment in frame container
     *
     * @param fragment - fragment for loading
     */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment, fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    public void openNavigationItem(final int itemId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableElementsNavigation(true);
                navigation.setSelectedItemId(itemId);
            }
        });
    }
    public void resetQuest(){
        initializeTargets();
        initializeTasks();

        targetList = TargetList.getInstance();
        taskList = TaskList.getInstance();

        navigation.setVisibility(View.VISIBLE);
        progressBar.setProgress(taskList.getCurrentIterator());
        openNavigationItem(R.id.navigation_target);
    }

    @Override
    public void onBackPressed() { }
}