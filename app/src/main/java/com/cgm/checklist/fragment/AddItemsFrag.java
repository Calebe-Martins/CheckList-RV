package com.cgm.checklist.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cgm.checklist.R;
import com.cgm.checklist.database.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItemsFrag extends Fragment {

    EditText userInput;
    ImageButton imageButton;

    DBHelper dbHelper;
    private String type_folder;


    public AddItemsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());

        View view = inflater.inflate(R.layout.add_items_frag, container, false);

        type_folder = getArguments().getString("NOME_DA_PASTA");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(type_folder);

        userInput = (EditText) view.findViewById(R.id.editText);
        imageButton = (ImageButton) view.findViewById(R.id.imageButton_Save);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = userInput.getText().toString();
                String resultado;

                if (userInput.length() != 0) {
                    resultado = dbHelper.AddData(newEntry, type_folder);
                    Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
                    userInput.setText("");

                } else {
                    Toast.makeText(getContext(), "Digite um item", Toast.LENGTH_SHORT).show();
                }
            }
        });


        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


}
