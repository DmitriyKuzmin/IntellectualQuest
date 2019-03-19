package com.kdp.quest;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.kdp.quest.fragment.CameraFragment;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.TargetManager;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.TaskManager;
import com.kdp.quest.renderer.BackgroundRenderHelper;
import com.kdp.quest.renderer.ImageRender;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;


import java.util.Arrays;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ImageTrackerRenderer implements Renderer {

    private final Activity activity;

    private int surfaceWidth;
    private int surfaceHeight;

    private BackgroundRenderHelper backgroundRenderHelper;

    private Target currentTarget;
    private Task currentTask;

    private Boolean changeImage = true;

    private ImageRender imageRender;

    public ImageTrackerRenderer(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        imageRender = new ImageRender();
        updateTargetCurrent();

        backgroundRenderHelper = new BackgroundRenderHelper();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        surfaceHeight = height;
        surfaceWidth = width;

        MaxstAR.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight);

        TrackingState state = TrackerManager.getInstance().updateTrackingState();
        TrackedImage image = state.getImage();

        float[] backgroundPlaneProjectionMatrix = CameraDevice.getInstance().getBackgroundPlaneProjectionMatrix();
        backgroundRenderHelper.drawBackground(image, backgroundPlaneProjectionMatrix);


        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        float[] projectionMatrix = CameraDevice.getInstance().getProjectionMatrix();
        TrackingResult trackingResult = state.getTrackingResult();

        if (changeImage){
            imageRender.setImage(MaxstARUtil.getBitmapFromAsset(currentTask.getPathTaskFile(), activity.getAssets()));
            changeImage = false;
        }

        if (trackingResult.getCount() <= 0) {
            CameraFragment.getInstance().invisibleButton();
        }

        for (int i = 0; i < trackingResult.getCount(); i++) {
            Trackable trackable = trackingResult.getTrackable(i);
            if (!trackable.getName().equals(currentTarget.getName())) {
                CameraFragment.getInstance().invisibleButton();
                continue;
            }

            imageRender.setProjectionMatrix(projectionMatrix);

            Log.d(ImageTrackerRenderer.class.getSimpleName(), "----------------------------------------------");
            Log.d(ImageTrackerRenderer.class.getSimpleName(), "Projection matrix: " + Arrays.toString(projectionMatrix));
            Log.d(ImageTrackerRenderer.class.getSimpleName(), "Trackable pose Matrix: " + Arrays.toString(trackable.getPoseMatrix()));
            Log.d(ImageTrackerRenderer.class.getSimpleName(), "Width: " + trackable.getWidth());
            Log.d(ImageTrackerRenderer.class.getSimpleName(), "Height: " + trackable.getHeight());
            Log.d(ImageTrackerRenderer.class.getSimpleName(), "----------------------------------------------");

            imageRender.setTransform(trackable.getPoseMatrix());
            imageRender.setTranslate(0.0f, 0.0f, 0.0f);
            imageRender.setScale(trackable.getWidth(), trackable.getHeight(), 1.0f);
            imageRender.draw();

            CameraFragment.getInstance().visibleButton();
        }
    }

    public void updateTargetCurrent() {
        currentTarget = TargetManager.getInstance(null).getCurrentTarget();
        currentTask = TaskManager.getInstance(null).getCurrentTask();

        Log.d(ImageTrackerRenderer.class.getSimpleName(), "------------------------");
        Log.d(ImageTrackerRenderer.class.getSimpleName(), "Update Current: ");
        Log.d(ImageTrackerRenderer.class.getSimpleName(), "Target: " + currentTarget);
        Log.d(ImageTrackerRenderer.class.getSimpleName(), "Task: " + currentTask);
        Log.d(ImageTrackerRenderer.class.getSimpleName(), "------------------------");

        changeImage = true;
    }

}
