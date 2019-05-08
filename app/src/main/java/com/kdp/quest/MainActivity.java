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
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.fragment.FinishFragment;
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
    ProgressBar progressBar;
    public BottomNavigationView navigation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }


        progressBar = findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        openNavigationItem(R.id.navigation_target);
    }


    public void enableElementsNavigation() {
        for (int i = 0; i < 3; i++) {
            navigation.getMenu().getItem(i).setEnabled(true);
        }
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_target:
                    fragment = TargetFragment.getInstance();
                    if (!TargetList.getInstance(null).getCurrentTarget().isDetected())
                        navigation.getMenu().getItem(2).setEnabled(false);
                    break;
                case R.id.navigation_camera:
                    fragment = CameraFragment.getInstance();
                    if (!TargetList.getInstance(null).getCurrentTarget().isDetected())
                        navigation.getMenu().getItem(2).setEnabled(false);
                    break;
                case R.id.navigation_task:
                    fragment = TaskFragment.getInstance();
                    break;
                case R.id.navigation_finish:
                    for (int i = 0; i < 3; i++) {
                        navigation.getMenu().getItem(i).setEnabled(false);
                    }
                    fragment = FinishFragment.getInstance();
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
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void openNavigationItem(final int itemId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableElementsNavigation();
                navigation.setSelectedItemId(itemId);
            }
        });
    }
}