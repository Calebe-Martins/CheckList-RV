package com.cgm.checklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

/**
 * Pegar todos os itens com STATUS = 1 e setar eles como checados
 */
public class ListItems extends AppCompatActivity {

    // Declarando recursos
    DBHelper dbHelper;
    ArrayAdapter adapter;
    final Context context = this;
    private ListView listItems;

    private String type_folder;

    SwitchPreference sp;

    // Ação de deletar os itens
    public static List<String> UserSelection = new ArrayList<>();
    public static final List<String> IsChecked = new ArrayList<>();
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

    // Carrega minha lista sempre que entrar nessa tela
    @Override
    protected void onResume() {
        super.onResume();
        LoadDataItems();
    }

    // Carrega os itens da pasta selecionada
    public void LoadDataItems() {
        final Cursor data = dbHelper.getData(type_folder);
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
            if (data.getString(3).equals("1")) { // Salva os itens q tem 1 nos STATUS
                if (!IsChecked.contains(data.getPosition())) { // Adiciona a IsChecked para setar como checado
                    IsChecked.add(String.valueOf(data.getPosition()));
                }
            }
        }

        // Seta a lista para poder selecionar multiplos itens
        listItems.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Adapter para click nas checkbox
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listData);
        // Criador da lista adaptada e seta a lista adaptada
        listItems.setAdapter(adapter);



        /**Quando a preferencia de seleção simples dos itens estiver ativa, após sair do app os
         * itens selecionados serão descelecionados altomaticamente. Caso esteja desativada, faz
         * os itens ficarem marcados mesmo que saia do app
         */


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean simpleSelection = sharedPreferences.getBoolean("simple_selection", true);
        boolean multiSelection  = sharedPreferences.getBoolean("persistence_selection", true);
        boolean deleteSelection = sharedPreferences.getBoolean("delete_selection", true);

        if (simpleSelection) { IsChecked.clear(); }

        // Quando a multiple estiver selecionada, salva os itens marcados
        if (multiSelection) {
            // Verifica se tem 1 nos STATUS e manda ele checado
            for (int i = 0; i < IsChecked.size(); i++) {
                int aux = Integer.parseInt(IsChecked.get(i));
                listItems.setItemChecked(aux, true);
            }
            IsChecked.clear();

            listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = (String) listItems.getItemAtPosition(position);

                    if (listItems.isItemChecked(position)) {
                        dbHelper.updateStatus(1, name);
                    } else {
                        dbHelper.updateStatus(0, name);
                    }
                }
            });
        }

        if (deleteSelection) {
            listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String teste = (String) listItems.getItemAtPosition(position);

                    Toast.makeText(context, teste, Toast.LENGTH_SHORT).show();
                }
            });
            // pegar o iten selecionado
            // dar um tempo para deletar
            // animação de deletar
            // deleta o item
        }
    }

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
                            // .contains serve para saber se a string CONTEM dentro do meu UserSelection
                            if (!UserSelection.contains(listItems.getItemAtPosition(position))) {
                                UserSelection.add((String) listItems.getItemAtPosition(position));
                            }
                        } else {
                            UserSelection.remove(listItems.getItemAtPosition(position));
                        }
                        if (UserSelection.contains(null)) {
                            toastMenssage("Selecione um item");
                        }
                    }
                });

                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Verifica se foi selecionado alguma checkbox
                        if (UserSelection.size() == 0) {
                            toastMenssage("Selecione um item");
                        } else {
                            //percorre e deleta os itens do banco de dados dentro da pasta específica
                            for (int i = 0; i < UserSelection.size(); i++) {
                                dbHelper.deleteItems(UserSelection.get(i));
                            }
                            toastMenssage("Itens deletados");
                            UserSelection.clear();
                            LoadDataItems();
                        }
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
