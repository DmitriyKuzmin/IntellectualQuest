package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.list.TargetList;
import com.maxst.ar.MaxstARUtil;

import java.util.Objects;


public class TargetFragment extends Fragment {

    private Target currentTarget;

    @SuppressLint("StaticFieldLeak")
    private static TargetFragment instance;

    public static TargetFragment getInstance() {
        if (instance == null)
            instance = new TargetFragment();

        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        currentTarget = TargetList.getInstance(null).getCurrentTarget();

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_target, container, false);
        ImageView imageView = view.findViewById(R.id.target_image);
        imageView.setImageBitmap(MaxstARUtil.getBitmapFromAsset(currentTarget.getPathTargetImageFile(), Objects.requireNonNull(getActivity()).getAssets()));

        TextView targetDesc = view.findViewById(R.id.target_text);
        targetDesc.setText(currentTarget.getDescription());

        TextView targetName = view.findViewById(R.id.target_name);
        targetName.setText(currentTarget.getName());

        Button toCameraButton = view.findViewById(R.id.to_camera_btn);
        toCameraButton.setOnClickListener(toCameraButtonOnClickListener);

        return view;
    }


    private View.OnClickListener toCameraButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.mOnNavigationItemSelectedListener.onNavigationItemSelected(mainActivity.navigation.getMenu().getItem(1));
            }
        }
    };
}
