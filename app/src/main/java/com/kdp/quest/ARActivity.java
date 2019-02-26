
package com.kdp.quest;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.maxst.ar.MaxstAR;
import com.maxst.ar.TrackerManager;


public abstract class ARActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MaxstAR.init(getApplicationContext(), "QeBzgTfJ6dmBlLjeK2rZHUBpM3yslDOBV/qyVgWL8dY=");
		MaxstAR.setScreenOrientation(getResources().getConfiguration().orientation);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		TrackerManager.getInstance().destroyTracker();
		MaxstAR.deinit();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);


		MaxstAR.setScreenOrientation(newConfig.orientation);
	}
}
