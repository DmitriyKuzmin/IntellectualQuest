package com.kdp.quest;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.kdp.quest.model.Target;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.renderer.BackgroundRenderHelper;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class TrackerRenderer implements Renderer {


    private static final String TAG = TrackerRenderer.class.getSimpleName();
    private int surfaceWidth;
    private int surfaceHeight;
    private MainActivity activity;

    private BackgroundRenderHelper backgroundRenderHelper;

    private Target currentTarget;

    public TrackerRenderer(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        backgroundRenderHelper = new BackgroundRenderHelper();
        currentTarget = TargetList.getInstance(null).getCurrentTarget();
        Log.d(TAG, "onSurfaceCreated: " + currentTarget);
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


        TrackingResult trackingResult = state.getTrackingResult();

        for (int i = 0; i < trackingResult.getCount(); i++) {
            Trackable trackable = trackingResult.getTrackable(i);
            if (!trackable.getName().equals(currentTarget.getName())) {
                continue;
            }

            activity.openItemNavigation(2);
        }
    }


}
