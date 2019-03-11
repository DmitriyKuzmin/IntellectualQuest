package com.kdp.quest;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import com.kdp.quest.fragment.CameraFragment;
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


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ImageTrackerRenderer implements Renderer {

    private final Activity activity;
    private int surfaceWidth;
    private int surfaceHeight;
    private BackgroundRenderHelper backgroundRenderHelper;

    private Target currentTarget;
    private Task currentTask;


    private ImageRender imageRender;

    public ImageTrackerRenderer(Activity activity) {
        this.activity = activity;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // TODO Target and Task Manager
        currentTarget = new Target("Robot");
        currentTask = new Task("1", "96");

        imageRender = new ImageRender();
        imageRender.setImage(MaxstARUtil.getBitmapFromAsset(currentTask.getPathTaskFile(), activity.getAssets()));
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
            imageRender.setTransform(trackable.getPoseMatrix());
            imageRender.setTranslate(0.0f, 0.0f, 0.0f);
            imageRender.setScale(trackable.getWidth(), trackable.getHeight(), 1.0f);
            imageRender.draw();

            CameraFragment.getInstance().visibleButton();
        }
    }
}
