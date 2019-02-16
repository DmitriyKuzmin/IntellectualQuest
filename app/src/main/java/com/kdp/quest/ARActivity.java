package com.kdp.quest;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;

public abstract class ARActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MaxstAR.init(getApplicationContext(),getResources().getString(R.string.app_key));
        MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        TrackerManager.getInstance().destroyTracker();
        MaxstAR.deinit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        MaxstAR.setScreenOrientation(newConfig.orientation);
    }
}
