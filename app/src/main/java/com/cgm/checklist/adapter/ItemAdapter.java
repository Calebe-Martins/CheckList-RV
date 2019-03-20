package com.cgm.checklist.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.cgm.checklist.R;
import com.cgm.checklist.database.DBHelper;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.CustomViewHolder> {

    private List<Item> items;
    Context context;
    DBHelper dbHelper; //

    SparseBooleanArray itemStateArray= new SparseBooleanArray();

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CheckedTextView checkedTextView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            checkedTextView = (CheckedTextView) itemView.findViewById(R.id.checked_text_view);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            if (!itemStateArray.get(position, false)) {
                checkedTextView.setChecked(false);
            } else {
                checkedTextView.setChecked(true);
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if (!itemStateArray.get(adapterPosition, false)) {
                checkedTextView.setChecked(true);
                itemStateArray.put(adapterPosition, true);
                // ######### COLOCAR AÇÂO DO CLICK AQUI

                Toast.makeText(context, items.get(adapterPosition).getNome(), Toast.LENGTH_SHORT).show();
                dbHelper.deleteItems(items.get(adapterPosition).getNome());
                items.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, items.size());
                // PEGAR O ITEM NA POSIÇÂO E DES SELECIONAR
                checkedTextView.setChecked(false);
                itemStateArray.put(adapterPosition, false);

            }
            else  {
                checkedTextView.setChecked(false);
                itemStateArray.put(adapterPosition, false);
            }
        }
    }

    public ItemAdapter(List<Item> items, Context context, DBHelper dbHelper) {
        this.items = items;
        this.context = context;
        this.dbHelper = dbHelper; //
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        Item item = items.get(position);
        holder.bind(position);
        holder.checkedTextView.setText(item.getNome());
        //setAnimation(holder.itemView, position); // TESTE ANIMAÇÂO FADE IN MUDAR ANIMAÇÂO
    }

    private int lastPosition = -1; // TESTE ANIMAÇÂO FADE IN MUDAR ANIMAÇÂO

    private void setAnimation(View viewToAnimate, int position) { // TESTE ANIMAÇÂO FADE IN MUDAR ANIMAÇÂO
        if (position > lastPosition) {
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(2000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
