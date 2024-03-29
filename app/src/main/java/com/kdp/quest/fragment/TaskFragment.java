package com.kdp.quest.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.model.Task;
import com.kdp.quest.model.list.TargetList;
import com.kdp.quest.model.list.TaskList;
import com.maxst.ar.MaxstARUtil;

import java.util.Objects;

public class TaskFragment extends Fragment {
    private Task current_task;
    private EditText answer;

    private MainActivity activity;

    @SuppressLint("StaticFieldLeak")
    private static TaskFragment instance;

    public static TaskFragment getInstance() {
        if (instance == null)
            instance = new TaskFragment();

        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        current_task = TaskList.getInstance().getCurrentTask();

        View view = inflater.inflate(R.layout.fragment_task, container, false);

        ImageView imageView = view.findViewById(R.id.task_image);
        imageView.setImageBitmap(MaxstARUtil.getBitmapFromAsset(current_task.getPathTaskFile(), Objects.requireNonNull(getActivity()).getAssets()));

        answer = view.findViewById(R.id.task_answer_edit_text);

        Button sendAnswerButton = view.findViewById(R.id.send_answer_btn);
        sendAnswerButton.setOnClickListener(sendAnswerButtonOnClickListener);

        EditText editText = view.findViewById(R.id.task_answer_edit_text);
        editText.setText(current_task.getAnswer());

        return view;
    }

    private View.OnClickListener sendAnswerButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            current_task.setTrueUserAnswer(answer.getText().toString().equals(current_task.getAnswer()));
            current_task.setUserAnswer(answer.getText().toString());

            TaskList.getInstance().nextTask();
            TargetList.getInstance().nextTarget();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                activity.progressBar.setProgress(TaskList.getInstance().getCurrentIterator(), true);

            if (TaskList.getInstance().getCurrentIterator() == 3)
                activity.openNavigationItem(R.id.navigation_finish);
            else {
                activity.openNavigationItem(R.id.navigation_target);
            }

        }
    };


}
