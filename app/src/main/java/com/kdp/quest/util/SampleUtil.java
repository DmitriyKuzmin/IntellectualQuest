package com.kdp.quest.util;

import android.app.Activity;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

public class SampleUtil {

	public static final String PREF_NAME = "pref";
	public static final String PREF_KEY_CAM_RESOLUTION = "cam_resolution";

	public static void hideKeyboard(Activity activity, Fragment fragment) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		IBinder windowToken = Objects.requireNonNull(fragment.getView()).getRootView().getWindowToken();
		imm.hideSoftInputFromWindow(windowToken, 0);
	}
}
