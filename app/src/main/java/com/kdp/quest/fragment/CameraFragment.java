package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
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

    private GLSurfaceView glSurfaceView;
    private ImageTrackerRenderer trackerRenderer;

    private TextView progressBar;

    private Button onTargetImageButton;

    private View answer_panel;
    private EditText editAnswer;

    private int maxCount;

    private int camera_id = SampleUtil.REAR_CAMERA;

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
    }

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        onTargetImageButton = view.findViewById(R.id.on_target_image_btn);
        onTargetImageButton.setOnClickListener(onClickOnTargetButton);

        answer_panel = view.findViewById(R.id.answer_panel);
        editAnswer = answer_panel.findViewById(R.id.edit_message);

        int countTasks = TaskManager.getInstance(null).getCountTasks();
        int countTargets = TargetManager.getInstance(null).getCountTargets();
        this.maxCount = (countTargets > countTasks) ? countTasks : countTargets;
        int iterator = TaskManager.getInstance(null).getCurrentIterator();
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setText(getString(R.string.progress, iterator, maxCount));

        Button sendAnswerButton = answer_panel.findViewById(R.id.sendAnswerButton);
        sendAnswerButton.setOnClickListener(onClickSendMessageButton);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(SampleUtil.EGLContext_CLIENT_VERSION);

        trackerRenderer = new ImageTrackerRenderer(getActivity());
        glSurfaceView.setRenderer(trackerRenderer);


        ToggleButton directionCameraToggleButton = view.findViewById(R.id.toggle_direction_camera);
        directionCameraToggleButton.setChecked(false);
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

        cameraStart(camera_id);
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


    private void cameraStart(int cameraId) {
        ResultCode resultCode = CameraDevice.getInstance().start(cameraId, 640, 480);

        if (resultCode != ResultCode.Success) {
            Log.d(CameraFragment.class.getSimpleName(), "Finish Camera");
            Objects.requireNonNull(getActivity()).finish();
        }

        CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, cameraId == 1);

    }

    public void visibleButton() {
        if (onTargetImageButton.getVisibility() == View.VISIBLE)
            return;

        new Thread() {
            @Override
            public void run() {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTargetImageButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }

    public void invisibleButton() {
        if (onTargetImageButton.getVisibility() == View.INVISIBLE)
            return;

        new Thread() {
            @Override
            public void run() {
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTargetImageButton.setVisibility(View.INVISIBLE);
                        answer_panel.setVisibility(View.INVISIBLE);

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
                            if (answer_panel.getVisibility() == View.INVISIBLE)
                                answer_panel.setVisibility(View.VISIBLE);
                            else answer_panel.setVisibility(View.INVISIBLE);

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
            String answer = editAnswer.getText().toString();

            if (answer.equals(TaskManager.getInstance(null).getCurrentTask().getAnswer())) {
                TargetManager.getInstance(null).nextTarget();
                TaskManager.getInstance(null).nextTask();

                int iterator = TaskManager.getInstance(null).getCurrentIterator();
                Log.d(CameraFragment.class.getSimpleName(), "iterator: " + iterator);

                progressBar.setText(getString(R.string.progress, iterator, maxCount));

                trackerRenderer.updateTargetCurrent();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener onCheckedChangeDirectionCameraToggleButton = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isFront) {
            CameraDevice.getInstance().stop();

            camera_id = (isFront) ? SampleUtil.FRONT_CAMERA : SampleUtil.REAR_CAMERA;
            cameraStart(camera_id);

        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeFlashLight = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CameraDevice.getInstance().setFlashLightMode(isChecked);
        }
    };
}
