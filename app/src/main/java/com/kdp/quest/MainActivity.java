package com.kdp.quest;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import com.kdp.quest.util.SampleUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;

public class MainActivity extends ARActivity {

    /**
     *
     */
    private GLSurfaceView glSurfaceView;
    /**
     *
     */
    private int preferCameraResolution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isSupportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        setContentView(R.layout.activity_main);


        ImageTrackerRenderer imageTrackerRenderer = new ImageTrackerRenderer(this);
        glSurfaceView = findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(imageTrackerRenderer);

        TrackerManager.getInstance().addTrackerData("ImageTarget/Blocks.2dmap", true);
        TrackerManager.getInstance().addTrackerData("ImageTarget/Glacier.2dmap", true);
        TrackerManager.getInstance().addTrackerData("ImageTarget/Lego.2dmap", true);
        TrackerManager.getInstance().loadTrackerData();

        preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        glSurfaceView.onPause();

        TrackerManager.getInstance().stopTracker();
        CameraDevice.getInstance().stop();
        MaxstAR.onPause();    }

    @Override
    protected void onResume() {
        super.onResume();

        glSurfaceView.onResume();
        TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

        ResultCode resultCode = ResultCode.Success;
        switch (preferCameraResolution) {
            case 0:
                resultCode = CameraDevice.getInstance().start(0, 640, 480);
                break;

            case 1:
                resultCode = CameraDevice.getInstance().start(0, 1280, 720);
                break;

            case 2:
                resultCode = CameraDevice.getInstance().start(0, 1920, 1080);
                break;
        }

        if (resultCode != ResultCode.Success) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            finish();
        }

        MaxstAR.onResume();
    }

    private boolean isSupportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}

