package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.kdp.quest.MainActivity;
import com.kdp.quest.TrackerRenderer;
import com.kdp.quest.R;
import com.kdp.quest.util.CameraUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {

    private static final int EGL_CONTEXT_CLIENT_VERSION = 2;
    private static final String TAG = CameraFragment.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static CameraFragment instance;

    private CameraDevice cameraDevice;
    private GLSurfaceView glSurfaceView;
    private TrackerRenderer trackerRenderer;
    private TrackerManager trackerManager;
    private int viewTypeCamera = CameraUtil.REAR_CAMERA;

    public static CameraFragment getInstance() {
        if (instance == null)
            instance = new CameraFragment();

        return instance;
    }

    @SuppressLint("ValidFragment")
    private CameraFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate ");
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        trackerRenderer = new TrackerRenderer((MainActivity) activity);

        cameraDevice = CameraDevice.getInstance();
        trackerManager = TrackerManager.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(EGL_CONTEXT_CLIENT_VERSION);
        glSurfaceView.setRenderer(trackerRenderer);

        ToggleButton viewTypeCameraToggleButton = view.findViewById(R.id.toggle_view_type_camera);
        viewTypeCameraToggleButton.setChecked(false);
        viewTypeCameraToggleButton.setOnCheckedChangeListener(onCheckedChangeViewCameraToggleToggleButton);

        ToggleButton flashLightToggleButton = view.findViewById(R.id.toggle_flash_light);
        flashLightToggleButton.setOnCheckedChangeListener(onCheckedChangeFlashLight);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();

        trackerManager.startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

        CameraUtil.startCamera(viewTypeCamera);
        MaxstAR.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();

        trackerManager.stopTracker();
        cameraDevice.stop();

        MaxstAR.onPause();
    }



    private CompoundButton.OnCheckedChangeListener onCheckedChangeViewCameraToggleToggleButton = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isFront) {
            cameraDevice.stop();
            viewTypeCamera = (isFront) ? CameraUtil.FRONT_CAMERA : CameraUtil.REAR_CAMERA;

            CameraUtil.startCamera(viewTypeCamera);
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeFlashLight = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            cameraDevice.setFlashLightMode(isChecked);
        }
    };

}
