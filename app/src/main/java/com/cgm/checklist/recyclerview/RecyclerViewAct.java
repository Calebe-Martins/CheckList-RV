package com.cgm.checklist.recyclerview;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Toast;

import com.cgm.checklist.R;
import com.cgm.checklist.adapter.Item;
import com.cgm.checklist.adapter.ItemAdapter;
import com.cgm.checklist.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAct extends AppCompatActivity {

    DBHelper dbHelper;

    List<Item> items = new ArrayList<>();
    private RecyclerView recyclerView;
    private ItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        dbHelper = new DBHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ItemAdapter(items, this, dbHelper);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        populateItemsName(); // Mostrando meu banco de dados
        mAdapter.notifyDataSetChanged();
    }

    private void populateItemsName() {

        final Cursor data = dbHelper.getData("puc");
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            items.add(new Item(data.getString(1)));
            if (data.getString(3).equals("1")) {
//                Item item = items.get(data.getPosition());

//                if (!IsChecked.contains(data.getPosition())) { // Adiciona a IsChecked para setar como checado
//                    IsChecked.add(String.valueOf(data.getPosition()));
                }
        }

    }
}
