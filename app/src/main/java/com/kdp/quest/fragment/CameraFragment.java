package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
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
import android.widget.EditText;

import com.kdp.quest.ImageTrackerRenderer;
import com.kdp.quest.R;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.TargetManager;
import com.kdp.quest.util.SampleUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;

import java.util.ArrayList;
import java.util.Objects;


public class CameraFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static CameraFragment instance;

    final String[] listItems = {"640x480", "1280x720", "1920x1080"};
    AlertDialog.Builder builder;

    private GLSurfaceView glSurfaceView;
    private int preferCameraResolution = 0;

    private ImageTrackerRenderer trackerRenderer;

    private Button onTargetImage;
    private View message_panel;
    private Button sendMessageButton;
    private EditText editMessage;


    public static CameraFragment getInstance() {
        if (instance == null)
            instance = new CameraFragment();

        return instance;
    }

    @SuppressLint("ValidFragment")
    private CameraFragment() {
        ArrayList<Target> targets = new ArrayList<>();
        targets.add(new Target("Robot"));
        targets.add(new Target("ClearCode"));
        targets.add(new Target("Kish"));
        TargetManager.getInstance(targets);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        Button cameraSizeTuneButton = view.findViewById(R.id.camera_size_tune_button);

        onTargetImage = view.findViewById(R.id.btn1);
        onTargetImage.setOnClickListener(onClickOnTargetButton);

        message_panel = view.findViewById(R.id.message_panel);
        sendMessageButton = message_panel.findViewById(R.id.send_message);
        editMessage = message_panel.findViewById(R.id.edit_message);

        sendMessageButton.setOnClickListener(onClickSendMessageButton);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);

        trackerRenderer = new ImageTrackerRenderer(getActivity());
        glSurfaceView.setRenderer(trackerRenderer);

        ArrayList<String> trackingFileName = TargetManager.getInstance(null).getTrackingFileName();

        for (String s : trackingFileName) {
            TrackerManager.getInstance().addTrackerData(s, true);
        }
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

    public void visibleButton() {
        if (onTargetImage.getVisibility() == View.VISIBLE)
            return;

        new Thread() {
            @Override
            public void run() {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTargetImage.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }

    public void invisibleButton() {
        if (onTargetImage.getVisibility() == View.INVISIBLE)
            return;

        new Thread() {
            @Override
            public void run() {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTargetImage.setVisibility(View.INVISIBLE);
                        message_panel.setVisibility(View.INVISIBLE);

                        SampleUtil.hideKeyboard(Objects.requireNonNull(getActivity()), instance);
                    }
                });
            }
        }.start();

    }

    public View.OnClickListener onClickOnTargetButton = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            new Thread() {
                @Override
                public void run() {
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (message_panel.getVisibility() == View.INVISIBLE)
                                message_panel.setVisibility(View.VISIBLE);
                            else message_panel.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }.start();

        }
    };

    public View.OnClickListener onClickSendMessageButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String answer = editMessage.getText().toString();

            if (answer.equals("95")) {
                Target target = TargetManager.getInstance(null).getNextTarget();
                trackerRenderer.updateTargetCurrent();
            }
        }
    };

}
