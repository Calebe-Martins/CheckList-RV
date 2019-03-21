package com.cgm.checklist.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cgm.checklist.R;
import com.cgm.checklist.database.DBHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFrag extends Fragment {

    DBHelper dbHelper;
    private String type_folder;

    public MenuFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());

        View view = inflater.inflate(R.layout.menu_frag, container, false);

        final ListView listViewMenu = (ListView) view.findViewById(R.id.list_view_frag);

        Cursor data = dbHelper.getData("menu");
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, listData);

        listViewMenu.setAdapter(adapter);

        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type_folder = listViewMenu.getItemAtPosition(position).toString();

                RecyclerViewFrag recyclerViewFrag = new RecyclerViewFrag();
                Bundle bundle = new Bundle();
                bundle.putString("NOME_DA_PASTA_PARA_TITULO_ACTIONBAR", type_folder);
                recyclerViewFrag.setArguments(bundle);
                FragmentTransaction manager = getFragmentManager().beginTransaction();
                manager.addToBackStack(null).replace(R.id.frameContainer, recyclerViewFrag).commit();
            }
        });

        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return view;
    }

}
