package com.kdp.quest.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.model.Specialty;

public class SpecialtyFragment extends Fragment {
    private static SpecialtyFragment instance;

    private MainActivity activity;
    private Specialty specialty;

    public static SpecialtyFragment getInstance(Specialty specialty) {
        if (instance == null)
            instance = new SpecialtyFragment(specialty);

        instance.specialty = specialty;
        return instance;
    }

    private SpecialtyFragment(Specialty specialty) {
        this.specialty = specialty;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_specialty, container, false);

        ImageButton button = view.findViewById(R.id.close_specialty);
        button.setOnClickListener(closeButtonOnClickListener);

        TextView name = view.findViewById(R.id.name_specialty);
        name.setText(specialty.getName());

        TextView desc = view.findViewById(R.id.desc_specialty);
        desc.setText(specialty.getDescription());

        return view;
    }


    public View.OnClickListener closeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.loadFragment(FinishFragment.getInstance());
        }
    };
}
