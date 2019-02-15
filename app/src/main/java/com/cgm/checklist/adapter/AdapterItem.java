package com.cgm.checklist.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cgm.checklist.R;
import com.cgm.checklist.database.DBHelper;

import java.util.ArrayList;

public class AdapterItem extends ArrayAdapter<String> {

    DBHelper dbHelper;
    private Context context;

    public AdapterItem(Context context) {
        super(context, R.layout.checkable_listview);
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        Cursor data = dbHelper.getData();
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.folder_layout, parent, false);
        TextView folderName = row.findViewById(R.id.folder_name);
        folderName.setText(listData.get(position));
        return row;
    }
}
