package com.kdp.quest.util;

import android.app.Activity;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class SampleUtil {

    public static final int FRONT_CAMERA = 1;
    public static final int REAR_CAMERA = 0;

    public static final int EGLContext_CLIENT_VERSION = 2;

    public static void hideKeyboard(Activity activity, Fragment fragment) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        IBinder windowToken = Objects.requireNonNull(fragment.getView()).getRootView().getWindowToken();
        imm.hideSoftInputFromWindow(windowToken, 0);
    }
}
