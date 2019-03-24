package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class CameraFragment extends Fragment {

    private Activity activity;

    @SuppressLint("StaticFieldLeak")
    private static CameraFragment instance;

    private GLSurfaceView glSurfaceView;
    private ImageTrackerRenderer trackerRenderer;

    private TextView progressBar;

    private Button onTargetImageButton;

    private View answerPanel;
    private EditText editAnswer;


    private int camera = SampleUtil.REAR_CAMERA;

    public static CameraFragment getInstance() {
        if (instance == null)
            instance = new CameraFragment();

        return instance;
    }

    @SuppressLint("ValidFragment")
    private CameraFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        onTargetImageButton = view.findViewById(R.id.on_target_image_btn);
        onTargetImageButton.setOnClickListener(onClickOnTargetButton);

        answerPanel = view.findViewById(R.id.answer_panel);
        editAnswer = answerPanel.findViewById(R.id.edit_message);

        progressBar = view.findViewById(R.id.progress_bar);
        updateProgressBar();

        Button sendAnswerButton = answerPanel.findViewById(R.id.sendAnswerButton);
        sendAnswerButton.setOnClickListener(onClickSendMessageButton);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(SampleUtil.EGLContext_CLIENT_VERSION);

        trackerRenderer = new ImageTrackerRenderer(activity);
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

        cameraStart(camera);
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

    /**
     * Start Camera of Device
     *
     * @param cameraId - {@link SampleUtil#FRONT_CAMERA} or {@link SampleUtil#REAR_CAMERA}
     */
    private void cameraStart(int cameraId) {
        ResultCode resultCode = CameraDevice.getInstance().start(cameraId, 640, 480);

        if (resultCode != ResultCode.Success) {
            //TODO Message of error
            activity.finish();
        }

        CameraDevice.getInstance().flipVideo(CameraDevice.FlipDirection.VERTICAL, cameraId == 1);
    }

    /**
     * Set the visibility state of OnTargetImage Button
     *
     * @param visibility - {@link View#VISIBLE}, {@link View#INVISIBLE}
     */
    public void setVisibilityOnTargetImageButton(final int visibility) {
        if (onTargetImageButton.getVisibility() == visibility)
            return;

        new Thread() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onTargetImageButton.setVisibility(visibility);
                        if (visibility == View.INVISIBLE) {
                            answerPanel.setVisibility(visibility);
                            SampleUtil.hideKeyboard(activity, instance);
                        }
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (answerPanel.getVisibility() == View.INVISIBLE)
                                answerPanel.setVisibility(View.VISIBLE);
                            else answerPanel.setVisibility(View.INVISIBLE);

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
            TaskManager taskManager = TaskManager.getInstance(null);
            TargetManager targetManager = TargetManager.getInstance(null);

            if (answer.equals(taskManager.getCurrentTask().getAnswer())) {
                targetManager.nextTarget();
                taskManager.nextTask();

                updateProgressBar();
                trackerRenderer.updateCurrent();
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener onCheckedChangeDirectionCameraToggleButton = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isFront) {
            CameraDevice.getInstance().stop();
            camera = (isFront) ? SampleUtil.FRONT_CAMERA : SampleUtil.REAR_CAMERA;
            cameraStart(camera);

        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangeFlashLight = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CameraDevice.getInstance().setFlashLightMode(isChecked);
        }
    };

    /**
     *
     */
    public void updateProgressBar(){
        int countTasks = TaskManager.getInstance(null).getCountTasks();
        int countTargets = TargetManager.getInstance(null).getCountTargets();
        int maxCount = (countTargets > countTasks) ? countTasks : countTargets;

        int iterator = TaskManager.getInstance(null).getCurrentIterator();

        progressBar.setText(getString(R.string.progress, iterator, maxCount));
    }
}
