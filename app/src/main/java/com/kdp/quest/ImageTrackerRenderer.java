package com.kdp.quest;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.kdp.quest.renderer.BackgroundRenderHelper;
import com.kdp.quest.renderer.ImageRender;
import com.kdp.quest.util.SampleUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.MaxstARUtil;
import com.maxst.ar.Trackable;
import com.maxst.ar.TrackedImage;
import com.maxst.ar.TrackerManager;
import com.maxst.ar.TrackingResult;
import com.maxst.ar.TrackingState;

import java.util.Objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ImageTrackerRenderer implements Renderer {

    private final Activity activity;
    private final Fragment fragment;
    private int surfaceWidth;
    private int surfaceHeight;
    private BackgroundRenderHelper backgroundRenderHelper;

    private String currentTarget;
    private String currentAnswer;
    private String currentTrackingResult;

    private Button onTargetImage;
    private View message_panel;
    private Button sendMessageButton;
    private EditText editMessage;

    public ImageTrackerRenderer(Activity activity, Fragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        onTargetImage = activity.findViewById(R.id.btn1);
        onTargetImage.setOnClickListener(onClickOnTargetButton);

        message_panel = activity.findViewById(R.id.message_panel);
        sendMessageButton = message_panel.findViewById(R.id.send_message);
        editMessage = message_panel.findViewById(R.id.edit_message);

        sendMessageButton.setOnClickListener(onClickSendMessageButton);

        currentTarget = "Robot";
        currentAnswer = "96";
        currentTrackingResult = "1";

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
            invisibleButton();
        }

        for (int i = 0; i < trackingResult.getCount(); i++) {
            Trackable trackable = trackingResult.getTrackable(i);
            if (!trackable.getName().equals(currentTarget)) {
                invisibleButton();
                continue;
            }

            ImageRender imageRender = new ImageRender();
            imageRender.setImage(MaxstARUtil.getBitmapFromAsset("TrackingResult/" + currentTrackingResult + ".png", activity.getAssets()));

            imageRender.setProjectionMatrix(projectionMatrix);
            imageRender.setTransform(trackable.getPoseMatrix());
            imageRender.setTranslate(0.0f, 0.0f, 0.0f);
            imageRender.setScale(trackable.getWidth(), trackable.getHeight(), 1.0f);
            imageRender.draw();

            visibleButton();
        }
    }


    private void visibleButton() {
        if (onTargetImage.getVisibility() == View.VISIBLE)
            return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onTargetImage.setVisibility(View.VISIBLE);
            }
        });
    }

    private void invisibleButton() {
        if (onTargetImage.getVisibility() == View.INVISIBLE)
            return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onTargetImage.setVisibility(View.INVISIBLE);
                message_panel.setVisibility(View.INVISIBLE);

                SampleUtil.hideKeyboard(activity, fragment);
            }
        });
    }

    private View.OnClickListener onClickOnTargetButton = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (message_panel.getVisibility() == View.INVISIBLE)
                        message_panel.setVisibility(View.VISIBLE);
                    else message_panel.setVisibility(View.INVISIBLE);

                }
            });
        }
    };

    private View.OnClickListener onClickSendMessageButton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String answer = editMessage.getText().toString();

            /*if (answer.equals(currentAnswer)) {

            } else {

            }*/
        }
    };


}
