package com.cgm.checklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cgm.checklist.database.DBHelper;

public class EditItems extends AppCompatActivity {

    // Declarando variaveis e construtores
    DBHelper dbHelper;
    private EditText Ed_editText;
    private ImageButton Ed_imageButton;

    private String name_item;
    private String userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_items);

        // Carregando construtores
        dbHelper = new DBHelper(this);
        Ed_editText = (EditText) findViewById(R.id.Ed_editText);
        Ed_imageButton = (ImageButton) findViewById(R.id.Ed_imageButton);

        final Intent name_Item = getIntent();
        name_item = name_Item.getStringExtra("name_item");

        setEd_editText();
    }

    // Renomeia o item
    public void rename() {

    }

    // Pega o nome do item e seta o EditText
    public void setEd_editText() {
        Ed_editText.setText(name_item);
    }

    // Ação do botão salvar quando item for renomeado
    public void RenameItem(View view) {
        Ed_editText.getText();

    }
}
