
package com.kdp.quest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kdp.quest.model.Target;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.list.TaskList;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;


public abstract class ARActivity extends AppCompatActivity {

    public static final String TAG = ARActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isSupportES2()) {
            Toast.makeText(this, getString(R.string.openGL_not_support), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        MaxstAR.init(getApplicationContext(), getString(R.string.app_key));
        MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);

        initializeTargets();
        initializeTasks();
        loadTrackerData();
    }

    @Override
    protected void onResume() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TrackerManager.getInstance().destroyTracker();
        MaxstAR.deinit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        MaxstAR.setScreenOrientation(newConfig.orientation);
    }

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
     * Initialize Targets and loading in TargetList
     */
    public void initializeTargets() {
        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target("Robot", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla a tortor at libero placerat mattis. Quisque mi lorem, commodo sit amet commodo sit amet, maximus sed quam. Vivamus eu viverra turpis. Pellentesque tincidunt nibh a placerat cursus. Duis velit sapien, ultricies venenatis semper vel, aliquet eu diam. Quisque egestas pellentesque rhoncus. Praesent sed faucibus magna. Nam non orci eu justo dignissim congue. Mauris nisi ligula, sodales et turpis ut, varius luctus sem. Suspendisse fringilla arcu a enim scelerisque, ac iaculis turpis sodales. Aliquam erat volutpat."));
        targets.add(new Target("ClearCode", "Lorem ipsum dolor."));
        targets.add(new Target("Kish", "Lorem ipsum dolor."));
        TargetList.getInstance().setData(targets);
    }

    /**
     * Initialize Task and loading in TaskList
     */
    public void initializeTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Task("1", "95", 2));
        tasks.add(new Task("2", "4", 1));
        tasks.add(new Task("3", "110", 3));
        TaskList.getInstance().setData(tasks);
    }

    /**
     * Load data for tracker from TargetList
     */
    private void loadTrackerData() {
        List<String> trackingFileName = TargetList.getInstance().getTrackingFileName();
        for (String s : trackingFileName) {
            TrackerManager.getInstance().addTrackerData(s, true);
        }
        TrackerManager.getInstance().loadTrackerData();
    }
}
