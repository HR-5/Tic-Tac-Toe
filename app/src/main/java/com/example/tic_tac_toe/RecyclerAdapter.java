package com.example.tic_tac_toe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<String> mNames = new ArrayList<>();
    public RecyclerAdapter(ArrayList<String> names,Context context){
        mNames = names;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String n = mNames.get(position);
        n.toUpperCase();
        holder.name.setText(n);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout list;
        @SuppressLint("ResourceType")
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            list = itemView.findViewById(R.layout.list_item);

        }
    }
}
