package com.kdp.quest.util;

import android.app.Activity;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

import com.maxst.ar.CameraDevice;
import com.maxst.ar.ResultCode;

import java.util.Objects;

import androidx.fragment.app.Fragment;

/**
 * Camera utilities
 *
 * @author DmitriyKuzmin <i>dmitriy.kuzmin910@gmail.com</i>
 * @version 1.0
 */
public class CameraUtil {

    /**
     * View Type - Front Camera of Device
     */
    public static final int FRONT_CAMERA = 1;
    /**
     * View Type - Rear Camera of Device
     */
    public static final int REAR_CAMERA = 0;

    /**
     * Hiding keyboard from active fragment.
     *
     * @param activity - active activity
     * @param fragment - active fragment
     */
    public static void hideKeyboardFromFragment(Activity activity, Fragment fragment) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        IBinder windowToken = Objects.requireNonNull(fragment.getView()).getRootView().getWindowToken();
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    /**
     * Camera starting of Device
     *
     * @param viewTypeCamera - view Type Camera {@link #REAR_CAMERA} or {@link #FRONT_CAMERA}
     */
    public static void startCamera(int viewTypeCamera) {
        CameraDevice cameraDevice = CameraDevice.getInstance();
        ResultCode resultCode = cameraDevice.start(viewTypeCamera, 640, 480);

        // TODO Calling exception about not success camera start
        if (resultCode != ResultCode.Success)
            return;

        cameraDevice.flipVideo(CameraDevice.FlipDirection.VERTICAL, viewTypeCamera == FRONT_CAMERA);
    }
}
