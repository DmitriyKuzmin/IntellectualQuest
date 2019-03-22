
package com.kdp.quest;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;


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

    /**
     * Is support system Open ES2
     *
     * @return boolean - true if support, else false
     */
    private boolean isSupportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();

        // TODO checking configurationInfo.reqGlEsVersion
        Log.d(TAG, "reqGLEsVersion(ConfigurationInfo): " + configurationInfo.reqGlEsVersion);

        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}
