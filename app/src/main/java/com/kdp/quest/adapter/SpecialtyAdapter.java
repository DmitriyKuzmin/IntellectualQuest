package com.kdp.quest.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kdp.quest.MainActivity;
import com.kdp.quest.R;
import com.kdp.quest.fragment.SpecialtyFragment;
import com.kdp.quest.model.Specialty;

import java.util.List;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.ViewHolder> {

    private static final String TAG = SpecialtyAdapter.class.getSimpleName();

    private LayoutInflater inflater;
    private List<Specialty> specialties;
    private RecyclerView recyclerView;
    private MainActivity activity;

    public SpecialtyAdapter(List<Specialty> specialties, RecyclerView recyclerView, MainActivity activity) {
        this.specialties = specialties;
        this.inflater = LayoutInflater.from(activity.getApplicationContext());
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        view.setOnClickListener(specialityOnClickListener);
        Log.d(TAG, "onCreateViewHolder: " + view);
        return new ViewHolder(view);
    }

    private View.OnClickListener specialityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);
            Specialty item = specialties.get(position);
            activity.loadFragment(SpecialtyFragment.getInstance(item));
            Log.d(TAG, "onClick: " + item);
        }
    };

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Specialty specialty = specialties.get(position);
        holder.nameView.setText(specialty.getName());
        Log.d(TAG, "onBindViewHolder: " + specialty);
        //holder.companyView.setText(specialty.getDescription());
    }

    @Override
    public int getItemCount() {
        return specialties.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView imageView;
        TextView nameView, companyView;

        public ViewHolder(@NonNull View view) {
            super(view);
            //imageView = view.findViewById(R.id.image);
            nameView = view.findViewById(R.id.name);
        }
    }
}
