package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.list.TaskList;

public class FinishFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static FinishFragment instance;

    private MainActivity activity;
    public static FinishFragment getInstance(){
        if (instance == null)
            instance = new FinishFragment();

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish, container, false);


        Button questAgainButton = view.findViewById(R.id.quest_again_btn);
        questAgainButton.setOnClickListener(questAgainButtonOnClickListener);

        return view;
    }

    private View.OnClickListener questAgainButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TaskList.getInstance(null).resetIterator();
            //TargetList.getInstance(null).resetIterator();
            //activity.initNavigation();
            //activity.setItemNavigationEnabled(true);
            //activity.updateCurrent();
        }
    };
}
