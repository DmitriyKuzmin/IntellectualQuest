package com.kdp.quest.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kdp.quest.ImageTrackerRenderer;
import com.kdp.quest.R;
import com.kdp.quest.util.SampleUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;

import java.util.Objects;


public class CameraFragment extends Fragment {


    final String[] listItems = {"640x480", "1280x720", "1920x1080"};
    AlertDialog.Builder builder;

    private GLSurfaceView glSurfaceView;
    private int preferCameraResolution = 0;
    Button button;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        Button cameraSizeTuneButton = view.findViewById(R.id.camera_size_tune_button);
        button = view.findViewById(R.id.btn1);
        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new ImageTrackerRenderer(getActivity()));

        TrackerManager.getInstance().addTrackerData("ImageTarget/Robot.2dmap", true);
        TrackerManager.getInstance().addTrackerData("ImageTarget/Kish.2dmap", true);
        TrackerManager.getInstance().addTrackerData("ImageTarget/ClearCode.2dmap", true);
        TrackerManager.getInstance().loadTrackerData();

        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Разрешение экрана");
        builder.setItems(listItems, onClickDialogItems);

        cameraSizeTuneButton.setOnClickListener(onClickTuneButton);

        preferCameraResolution = Objects.requireNonNull(getActivity()).getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();

        TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

        setCameraSize();
        MaxstAR.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();

        TrackerManager.getInstance().stopTracker();
        CameraDevice.getInstance().stop();

        MaxstAR.onPause();
    }


    private DialogInterface.OnClickListener onClickDialogItems = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            preferCameraResolution = which;
            SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).edit();
            editor.putInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, which);
            editor.apply();

            setCameraSize();
        }
    };

    private View.OnClickListener onClickTuneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };


    private void setCameraSize() {
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
            Objects.requireNonNull(getActivity()).finish();
        }

    }

}
