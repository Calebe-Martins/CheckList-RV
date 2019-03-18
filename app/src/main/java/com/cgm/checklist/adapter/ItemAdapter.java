package com.cgm.checklist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.cgm.checklist.R;
import com.cgm.checklist.database.DBHelper;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> implements AdapterView.OnItemClickListener {

    private List<Item> items;
    Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView checkedTextView;

        public CustomViewHolder(View view) {
            super(view);
            checkedTextView = (CheckedTextView) view.findViewById(R.id.checked_text_view);
        }
    }

    public ItemAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Item item = items.get(position);
        holder.checkedTextView.setText(item.getNome());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
