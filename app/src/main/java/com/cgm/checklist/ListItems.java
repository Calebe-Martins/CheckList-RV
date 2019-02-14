package com.cgm.checklist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cgm.checklist.database.DBHelper;

import java.util.ArrayList;

/** Carregar os itens da pasta selecionada na tela inicial
 * Deletar pasta com todos os itens
 */
public class ListItems extends AppCompatActivity {

    // Declarando recursos
    DBHelper dbHelper;
    ArrayAdapter adapter;
    final Context context = this;
    private ListView listItems;
    private String type_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items);

        // Botão voltar NA ACTIONBAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("");                    //Titulo para ser exibido na sua Action Bar em frente a seta

        // Carregando recursos
        dbHelper = new DBHelper(this);
        listItems = (ListView) findViewById(R.id.list_items);

        // Carrega o nome da pasta da MainActivity
        Intent name_folder = getIntent();
        type_folder = name_folder.getStringExtra("name_folder");

        // Mostra os itens da pasta selecionada na tela
        LoadDataItems();

        // Botão flutuante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            // Tela que adiciona itens na lista
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListItems.this, AddItems.class);
                // Envia o nome da pasta para AddItems
                intent.putExtra("name_folder", type_folder);
                startActivity(intent);
            }
        });
    }

    // Carrega os itens da pasta selecionada
    public void LoadDataItems() {
        Cursor data = dbHelper.getDataItems(type_folder);
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }

        listItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<String>(this, R.layout.checkable_listview, R.id.txt_item, listData);

        // Criador da lista adaptada e seta a lista adaptada
        listItems.setAdapter(adapter);
    }

    // DELETAR MULTIPLOS ITENS DE UMA VEZ

    //Ação do botão voltar DA ACTIONBAR
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home: {// ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                startActivity(new Intent(this, MainActivity.class)); // O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity(); // Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            }
            case R.id.action_refresh: {
                LoadDataItems();
                toastMenssage("Lista recarregada com sucesso!");
                break;
            }
            default:break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu: adiciona botão de recarregar na actionbar
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    // Aparece uma menssagem Toast
    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
