package com.example.safer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safer.models.Danger;

import java.util.ArrayList;

public class DangerAdapter extends RecyclerView.Adapter<DangerAdapter.ViewHolder> {

    private ArrayList<Danger> dangers;

    public DangerAdapter(ArrayList<Danger> dangers) {
        this.dangers = dangers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_danger, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Danger danger = dangers.get(position);

        holder.tvTitle.setText(danger.getTitle());
        holder.tvDescription.setText(danger.getDescription());
    }

    @Override
    public int getItemCount() {
        return dangers.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvTitle;
        public TextView tvDescription;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.danger_title);
            tvDescription = (TextView) itemView.findViewById(R.id.danger_description);
        }
    }
}