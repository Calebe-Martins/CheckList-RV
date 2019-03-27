package com.cgm.checklist.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
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
            /**Quando a preferencia de seleção simples dos itens estiver ativa, após sair do app os
             * itens selecionados serão descelecionados altomaticamente. Caso esteja desativada, faz
             * os itens ficarem marcados mesmo que saia do app
             */
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean simpleSelection = sharedPreferences.getBoolean("simple_selection", true);
            boolean multiSelection  = sharedPreferences.getBoolean("persistence_selection", true);
            boolean deleteSelection = sharedPreferences.getBoolean("delete_selection", true);

            if (!itemStateArray.get(adapterPosition, false)) {
                checkedTextView.setChecked(true);
                itemStateArray.put(adapterPosition, true);

                // Desmarca itens marcados quando sair da activity
                if (simpleSelection) {
                    itemStateArray.clear();
                }

                // Quando a multiple estiver selecionada, salva os itens marcados
                if (multiSelection) {
                    // Manda o status do item como 1
                    String name = checkedTextView.getText().toString();
                    dbHelper.updateStatus(1, name);

                    Toast.makeText(context, "Status 1", Toast.LENGTH_SHORT).show();

                    // Carrega itens quando selecionados
//                    if (itemStateArray.get(adapterPosition)) {
//                        for (int i = 0; i < itemStateArray.size(); i++) {
//                            checkedTextView.setChecked(true);
//                        }
//                    }


//                    // Verifica se tem 1 nos STATUS e manda ele checado
//                    for (int i = 0; i < IsChecked.size(); i++) {
//                        int aux = Integer.parseInt(IsChecked.get(i));
//                        checkedTextView.setChecked(aux, true);
//                    }
//                    IsChecked.clear();
//
//                    listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            String name = (String) listItems.getItemAtPosition(position);
//
//                            if (listItems.isItemChecked(position)) {
//                                dbHelper.updateStatus(1, name);
//                            } else {
//                                dbHelper.updateStatus(0, name);
//                            }
//                        }
//                    });
                }

//                // Deleta item ao ser clicado
//                if (deleteSelection) {
//                    listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            String deletaItem = (String) listItems.getItemAtPosition(position);
//                            // Deleta o item do banco de dados
//                            dbHelper.deleteItems(deletaItem);
//                            // Deleta o item da listview
//                            Toast.makeText(context, "Item: " + listData.get(position) + " removido com sucesso!", Toast.LENGTH_SHORT).show();
//                            listData.remove(position);
//                            listItems.setItemChecked(position, false);
//                            adapter.notifyDataSetChanged();
//
//                        }
//                    });
//                }
//
//
//
//
//
//                Toast.makeText(context, items.get(adapterPosition).getNome(), Toast.LENGTH_SHORT).show();
//                dbHelper.deleteItems(items.get(adapterPosition).getNome());
//                items.remove(adapterPosition);
//                notifyItemRemoved(adapterPosition);
//                notifyItemRangeChanged(adapterPosition, items.size());
//                // PEGAR O ITEM NA POSIÇÂO E DES SELECIONAR
//                checkedTextView.setChecked(false);
//                itemStateArray.put(adapterPosition, false);

            }
            else  {
                checkedTextView.setChecked(false);
                if (multiSelection) {
                    // Manda o status do item como 0
                    String name = checkedTextView.getText().toString();
                    dbHelper.updateStatus(0, name);
                    Toast.makeText(context, "Status 0", Toast.LENGTH_SHORT).show();
                }
                itemStateArray.put(adapterPosition, false);
            }
        }
    }

    public ItemAdapter(List<Item> items, Context context, DBHelper dbHelper) {
        this.items = items;
        this.context = context;
        this.dbHelper = dbHelper;
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
