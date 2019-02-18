package com.cgm.checklist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cgm.checklist.database.BancoController;
import com.cgm.checklist.database.DBHelper;

import java.util.ArrayList;
import java.util.List;

/** CheckList 2.0
 * Calebe Martins 04/02/2019
 * Arrumar botão voltar q não recarrega a lista
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaração das variaveis e recursos
    final Context context = this;
    DBHelper dbHelper;
    ListView listView;
    ArrayAdapter adapter, adapterFolder;


    private String type_folder = "";
    private String name_folder;

    // MultiChoiceModeListener = ActionMode
    public static List<String> UserSelection = new ArrayList<>(); // PQ COM STATIC MOSTRA NO ADAPTERITEM E SEM N MOSTRA?
    public static boolean isActionMode = false;
    public static ActionMode actionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicialização das variaveis e construtores
        dbHelper = new DBHelper(this);
        listView = (ListView) findViewById(R.id.lista_folder);
        listView.setMultiChoiceModeListener(modeListener);

        // ********* CARREGAR ITENS DO MENU
        LoadDataFolder();

        // Menu lateral
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Habilita função de voltar no botão do android
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_folder) {

            // Inflate do alert dialog prompt
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptsView = layoutInflater.inflate(R.layout.prompts, null);

            // Pega prompts.xml como view
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // Seta prompts.xml como alertdialog construtor
            alertDialogBuilder.setView(promptsView);

            // Variavel q recebe oq foi digitado no dialogPrompt
            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogPrompt);

            // Pega o nome do item e salva no banco de dados ou cancela
            alertDialogBuilder.setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Salva as pastas no bando de dados
                    String newEntry = userInput.getText().toString();
                    if (userInput.length() != 0) {
                        AddDataFolder(newEntry);
                        userInput.setText("");
                        LoadDataFolder();
                    } else {
                        toastMenssage("Digite um nome.");
                    }

                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Cria o alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // Mostra o dialog
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_folder) {
            // Carrega as pastas
            LoadDataFolder();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Verificação de inserção do item no banco de dados
    public void AddDataFolder(String newEntry) {
        BancoController crud = new BancoController(getBaseContext());
        String resultado;
        String padrao = "menu";

        resultado = crud.AddData(newEntry, padrao);
        Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
    }

    // Carrega as pastas(tipo)
    public void LoadDataFolder() {
        // Obtem os dados e anexar a uma lista
        Cursor data = dbHelper.getData();
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
//        adapter = new ArrayAdapter<String>(this, R.layout.selection_layout, R.id.folder_name, listData);
        adapterFolder = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listData);

        // Criador da lista adaptada e seta a lista adaptada
        listView.setAdapter(adapter);

        // Click da Pasta
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pega o nome da pasta
                name_folder = (String) listView.getItemAtPosition(position);
                // Passa o nome da pasta para AddItems
                type_folder = name_folder;
                // Vai para a activity que mostra os itens da pasta
                Intent intent = new Intent(MainActivity.this, ListItems.class);
                // Envia o nome da pasta para AddItems
                intent.putExtra("name_folder", type_folder);
                startActivity(intent);
            }
        });
    }


    // ######################           TESTE de deletar multiplos itens           ##################
    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            if (UserSelection.contains(listView.getItemAtPosition(position))) {
                UserSelection.remove(listView.getItemAtPosition(position));
            } else {
                UserSelection.add((String) listView.getItemAtPosition(position));
            }
            mode.setTitle(UserSelection.size() + " items adicionados...");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.delete_folder, menu);

            // Seta a lista de pastas com checkbox em cada pasta
            listView.setAdapter(adapterFolder);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // Icone da lixeira na action bar
            switch (item.getItemId()) {
                case R.id.action_delete: {
                    // Deleta a pasta do banco de dados


                    // Inflate do alert dialog prompt
                    LayoutInflater layoutInflater = LayoutInflater.from(context);

                    // Pega prompts.xml como view
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // Coloca titulo no Pop Up
                    alertDialogBuilder.setTitle("Você tem certeza?\nTodos itens serão apagados.");

                    // Coloca icone de lixeira
                    alertDialogBuilder.setIcon(R.drawable.ic_delete_popup);

                    alertDialogBuilder.setCancelable(false).setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // DELETA AS PASTAS E SEUS ITENS DO BANCO DE DADOS
// ########################## NOME DA PASTA AQUI ###########################
                            dbHelper.deleteFolder("pi");

                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Cria o alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // Mostra o dialog
                    alertDialog.show();




                    mode.finish();
                    return true;
                }
                default: return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            listView.setAdapter(adapter);
            UserSelection.clear();
        }
    };

    // Aparece uma menssagem Toast
    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
