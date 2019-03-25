package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kdp.quest.TrackerRenderer;
import com.kdp.quest.R;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.list.TaskList;
import com.kdp.quest.util.CameraUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CameraFragment extends Fragment {

    private static final String TAG = CameraFragment.class.getSimpleName();
    private static final int EGL_CONTEXT_CLIENT_VERSION = 2;
    private Activity activity;

    @SuppressLint("StaticFieldLeak")
    private static CameraFragment instance;

    private CameraDevice cameraDevice;
    private GLSurfaceView glSurfaceView;

    private TrackerManager trackerManager;
    private TrackerRenderer trackerRenderer;

    private TextView progressBar;
    private Button toggleAnswerPanelButton;
    private View answerPanel;
    private EditText editAnswer;

    private int viewTypeCamera = CameraUtil.REAR_CAMERA;

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

        activity = getActivity();
        cameraDevice = CameraDevice.getInstance();
        trackerManager = TrackerManager.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        toggleAnswerPanelButton = view.findViewById(R.id.toggle_answer_panel_btn);
        toggleAnswerPanelButton.setOnClickListener(onClickToggleAnswerPanel);

        answerPanel = view.findViewById(R.id.answer_panel);
        editAnswer = answerPanel.findViewById(R.id.edit_message);
        Button sendAnswerButton = answerPanel.findViewById(R.id.check_answer_btn);
        sendAnswerButton.setOnClickListener(onClickCheckAnswerButton);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        glSurfaceView.setEGLContextClientVersion(EGL_CONTEXT_CLIENT_VERSION);
        trackerRenderer = new TrackerRenderer(activity);
        glSurfaceView.setRenderer(trackerRenderer);

        ToggleButton viewTypeCameraToggleButton = view.findViewById(R.id.toggle_view_type_camera);
        viewTypeCameraToggleButton.setChecked(false);
        viewTypeCameraToggleButton.setOnCheckedChangeListener(onCheckedChangeViewCameraToggleToggleButton);

        ToggleButton flashLightToggleButton = view.findViewById(R.id.toggle_flash_light);
        flashLightToggleButton.setOnCheckedChangeListener(onCheckedChangeFlashLight);

        progressBar = view.findViewById(R.id.progress_bar);
        updateProgressBar();

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

    /**
     * Set the visibility state of OnTargetImage Button
     *
     * @param visibility - {@link View#VISIBLE}, {@link View#INVISIBLE}
     */
    public void setVisibilityOnTargetImageButton(final int visibility) {
        if (toggleAnswerPanelButton.getVisibility() == visibility)
            return;

        new Thread() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toggleAnswerPanelButton.setVisibility(visibility);
                        if (visibility == View.INVISIBLE) {
                            answerPanel.setVisibility(visibility);
                            CameraUtil.hideKeyboardFromFragment(activity, instance);
                        }
                    }
                });
            }
        }.start();
    }

    private View.OnClickListener onClickToggleAnswerPanel = new View.OnClickListener() {

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

    private View.OnClickListener onClickCheckAnswerButton = new View.OnClickListener() {
        @SuppressLint({"SetTextI18n", "ResourceType"})
        @Override
        public void onClick(View v) {
            String answer = editAnswer.getText().toString();
            TaskList taskList = TaskList.getInstance(null);
            TargetList targetList = TargetList.getInstance(null);

            if (!answer.equals(taskList.getCurrentTask().getAnswer()))
                return;

            if (targetList.getCountTargets() - 1 == targetList.getCurrentIterator()) {
                Log.d(TAG, "End Quest");
                // TODO End Quest
                return;
            }

            targetList.nextTarget();
            taskList.nextTask();
            updateProgressBar();
            trackerRenderer.updateCurrent();

            editAnswer.getText().clear();
        }
    };

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

    /**
     * setting progress based on  count tasks max  and current progress
     */
    private void updateProgressBar() {
        int countTasks = TaskList.getInstance(null).getCountTasks();
        int countTargets = TargetList.getInstance(null).getCountTargets();
        int maxCount = (countTargets > countTasks) ? countTasks : countTargets;

        int iterator = TaskList.getInstance(null).getCurrentIterator();

        progressBar.setText(getString(R.string.progress, iterator, maxCount));
    }
}
