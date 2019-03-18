package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kdp.quest.ImageTrackerRenderer;
import com.kdp.quest.R;
import com.kdp.quest.model.TargetManager;
import com.kdp.quest.model.TaskManager;
import com.kdp.quest.util.SampleUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;

import java.util.Objects;


public class CameraFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static CameraFragment instance;

    final String[] listItems = {"640x480", "1280x720", "1920x1080"};
    AlertDialog.Builder builder;

    private GLSurfaceView glSurfaceView;
    private int preferCameraResolution = 0;
    private int preferCameraId = 0;

    private ImageTrackerRenderer trackerRenderer;

    private Button onTargetImage;
    private View message_panel;
    private EditText editMessage;
    private TextView progressBar;
    private int maxCount;

    public static CameraFragment getInstance() {
        if (instance == null)
            instance = new CameraFragment();

        return instance;
    }

    @SuppressLint("ValidFragment")
    private CameraFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Разрешение экрана");
        builder.setItems(listItems, onClickDialogItems);

    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        preferCameraResolution = Objects.requireNonNull(getActivity()).getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
        preferCameraId = Objects.requireNonNull(getActivity()).getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).getInt("cam_id", 0);

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        onTargetImage = view.findViewById(R.id.btn1);
        onTargetImage.setOnClickListener(onClickOnTargetButton);

        message_panel = view.findViewById(R.id.message_panel);
        editMessage = message_panel.findViewById(R.id.edit_message);

        Integer countTasks = TaskManager.getInstance(null).getCountTasks();
        Integer countTargets = TargetManager.getInstance(null).getCountTargets();
        Integer maxCount = (countTargets > countTasks) ? countTasks : countTargets;
        this.maxCount = maxCount;

        Integer iterator = TaskManager.getInstance(null).getCurrentIterator();
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setText(getString(R.string.progress, iterator,maxCount));
        //progressBar.setText("Пройдено " + iterator.toString() + " из " + this.maxCount);

        Button sendMessageButton = message_panel.findViewById(R.id.send_message);
        sendMessageButton.setOnClickListener(onClickSendMessageButton);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(2);
        trackerRenderer = new ImageTrackerRenderer(getActivity());
        glSurfaceView.setRenderer(trackerRenderer);


        Button cameraSizeTuneButton = view.findViewById(R.id.camera_size_tune_button);
        cameraSizeTuneButton.setOnClickListener(onClickTuneButton);

        ToggleButton directionCameraToggleButton = view.findViewById(R.id.toggle_direction_camera);
        directionCameraToggleButton.setChecked(preferCameraId == 1);
        directionCameraToggleButton.setOnCheckedChangeListener(onCheckedChangeDirectionCameraToggleButton);

        ToggleButton flashLightToggleButton = view.findViewById(R.id.toggle_flash_light);
        flashLightToggleButton.setOnCheckedChangeListener(onCheckedChangeFlashLight);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();

        TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

        setCameraSize(preferCameraId);
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

            setCameraSize(preferCameraId);
        }
    };

    private View.OnClickListener onClickTuneButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };


    private void setCameraSize(int cameraId) {
        ResultCode resultCode = ResultCode.Success;
        switch (preferCameraResolution) {
            case 0:
                resultCode = CameraDevice.getInstance().start(cameraId, 640, 480);
                break;
            case 1:
                resultCode = CameraDevice.getInstance().start(cameraId, 1280, 720);
                break;
            case 2:
                resultCode = CameraDevice.getInstance().start(cameraId, 1920, 1080);
                break;
        }

        if (resultCode != ResultCode.Success) {
            Objects.requireNonNull(getActivity()).finish();
        }
        if (cameraId == 1) {
            CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, true);
        } else {
            CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, false);
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
        @SuppressLint({"SetTextI18n", "ResourceType"})
        @Override
        public void onClick(View v) {
            String answer = editMessage.getText().toString();

            if (answer.equals(TaskManager.getInstance(null).getCurrentTask().getAnswer())) {
                TargetManager.getInstance(null).nextTarget();
                TaskManager.getInstance(null).nextTask();

                Integer iterator = TaskManager.getInstance(null).getCurrentIterator();
                Log.d(CameraFragment.class.getSimpleName(), "iterator: " + iterator.toString());

                progressBar.setText(getString(R.string.progress, iterator, maxCount));

                trackerRenderer.updateTargetCurrent();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener onCheckedChangeDirectionCameraToggleButton = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isFront) {
            CameraDevice.getInstance().stop();
            if (isFront) {
                preferCameraId = 1;

                setCameraSize(1);
                CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, true);
            } else {
                preferCameraId = 0;
                setCameraSize(0);
                CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, false);
            }

            SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences(SampleUtil.PREF_NAME, Activity.MODE_PRIVATE).edit();
            editor.putInt("cam_id", preferCameraId);
            editor.apply();
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeFlashLight = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                CameraDevice.getInstance().setFlashLightMode(true);
            }else{
                CameraDevice.getInstance().setFlashLightMode(false);

            }
        }
    };
}
