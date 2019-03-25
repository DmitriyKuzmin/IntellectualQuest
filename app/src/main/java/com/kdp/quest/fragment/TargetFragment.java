package com.kdp.quest.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kdp.quest.R;
import com.kdp.quest.model.Target;
import com.kdp.quest.model.list.TargetList;
import com.maxst.ar.MaxstARUtil;

import java.util.Objects;


public class TargetFragment extends Fragment {
    private Target currentTarget;

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

        TextView textView = view.findViewById(R.id.target_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textView.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }

        textView.setText(currentTarget.getDescription());
        return view;
    }
}
