package com.cgm.checklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.cgm.checklist.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

/** Carregar os itens da pasta selecionada na tela inicial
 * Deletar pasta com todos os itens
 */
public class ListItems extends AppCompatActivity {

    // Declarando recursos
    DBHelper dbHelper;
    ArrayAdapter adapter, adapterFolder;
    final Context context = this;
    private ListView listItems;

    private String type_folder;
    private String name_item;
    private int id_item;

    // Ação de deletar os itens
    public static List<String> UserSelection = new ArrayList<>();
    boolean[] checkedItems;

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
        final Intent name_folder = getIntent();
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

        listItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                name_item = (String) listItems.getItemAtPosition(position);
                Intent intent = new Intent(ListItems.this, EditItems.class);
                intent.putExtra("name_item", name_item);
                startActivity(intent);
                return false;
            }
        });
    }



    // Carrega os itens da pasta selecionada
    public void LoadDataItems() {
        Cursor data = dbHelper.getData(type_folder);
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }

        listItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Adapter para click nas checkbox
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listData);
        // Adapter click longo para deletar itens pela checkbox
        adapterFolder = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listData);
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
            // Botão delete da action bar para excluir os itens do banco de dados
            case R.id.action_delete_actb: {
                Cursor data = dbHelper.getData(type_folder);
                final ArrayList<String> listData = new ArrayList<>();
                while (data.moveToNext()) {
                    // Obtenha o valor do banco de dados na coluna -1
                    // Em seguida adiciona a lista
                    listData.add(data.getString(1));
                }

                String mList[] = new String[listData.size()];

                for (int i = 0; i < listData.size(); i++) {
                    mList[i] = listData.get(i);
                }

                // Carrega o boolean com tamanho da lista
                checkedItems = new boolean[mList.length];
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListItems.this);
                alertDialogBuilder.setTitle("Deletar itens: ");
                alertDialogBuilder.setMultiChoiceItems(mList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if (isChecked) {
                            if (!UserSelection.contains(listItems.getItemAtPosition(position))) {
                                UserSelection.add((String) listItems.getItemAtPosition(position));
                            } else {
                                UserSelection.remove(listItems.getItemAtPosition(position));
                            }
                        }
                    }
                });

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //percorre e deleta os itens do banco de dados dentro da pasta específica
                        for (int i = 0; i < UserSelection.size(); i++) {
                            dbHelper.deleteItems(UserSelection.get(i));
                        }
                        toastMenssage("Itens deletados.");
                        UserSelection.clear();
                        LoadDataItems();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserSelection.clear();
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            default:break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu: adiciona botão de recarregar na actionbar
        getMenuInflater().inflate(R.menu.listview_activity, menu);
        return true;
    }

    // Aparece uma menssagem Toast
    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
