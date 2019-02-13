package com.cgm.checklist;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cgm.checklist.database.BancoController;
import com.cgm.checklist.database.DBHelper;

public class AddItems extends AppCompatActivity {

    // Declarando Recursos
    final Context context = this;
    DBHelper dbHelper;

    private ImageButton save;
    private EditText userInput;

    private String type_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);

        // Botão voltar NA ACTIONBAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("");                    //Titulo para ser exibido na sua Action Bar em frente a seta

        // Carregando Recursos
        dbHelper = new DBHelper(this);
        userInput = (EditText) findViewById(R.id.editText);
        save = (ImageButton) findViewById(R.id.imageButton_Save);

        // Carrega o nome da pasta da MainActivity
        Intent name_folder = getIntent();
        type_folder = name_folder.getStringExtra("name_folder");
    }

    // Salva o item na pasta seleciona na MainActivity
    public void SaveFolder(View view) {
        BancoController crud = new BancoController(getBaseContext());
        // Salva os itens e as pastas(tipo) no bando de dados
        String newEntry = userInput.getText().toString();
        String resultado;

        if (userInput.length() != 0) {
            resultado = crud.AddDataItems(newEntry, type_folder);
            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
            userInput.setText("");
        } else {
            toastMenssage("Digite um item: ");
        }
    }

    //Ação do botão voltar DA ACTIONBAR
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home: // ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class)); // O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity(); // Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

    // Aparece uma menssagem Toast
    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
