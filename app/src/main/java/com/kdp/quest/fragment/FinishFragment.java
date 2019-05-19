package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.adapter.SpecialtyAdapter;
import com.kdp.quest.model.Specialty;

import java.util.ArrayList;
import java.util.List;

public class FinishFragment extends Fragment {

    private static final String TAG = FinishFragment.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static FinishFragment instance;

    private List<Specialty> specialtyList = new ArrayList<>();


    private MainActivity activity;

    public static FinishFragment getInstance() {
        if (instance == null)
            instance = new FinishFragment();

        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        if (activity != null)
            activity.navigation.setVisibility(View.INVISIBLE);
        setInitialData();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_finish, container, false);


        Button questAgainButton = view.findViewById(R.id.quest_again_btn);
        questAgainButton.setOnClickListener(questAgainButtonOnClickListener);

        RecyclerView recyclerView = view.findViewById(R.id.specialties);
        SpecialtyAdapter adapter = new SpecialtyAdapter(specialtyList, recyclerView, activity);

        Log.d(TAG, "specialtyList: " + specialtyList);
        Log.d(TAG, "adapter: " + adapter);

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void setInitialData() {
        specialtyList.add(new Specialty("Huawei P10", "Huawei"));
        specialtyList.add(new Specialty("Elite z3", "HP"));
        specialtyList.add(new Specialty("Galaxy S8", "Samsung"));
        specialtyList.add(new Specialty("LG G 5", "LG"));
    }

    private View.OnClickListener questAgainButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.resetQuest();
        }
    };
}
