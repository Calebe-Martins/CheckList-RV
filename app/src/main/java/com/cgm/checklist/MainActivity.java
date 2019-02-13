package com.cgm.checklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cgm.checklist.database.DBHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/** CheckList 2.0
 * Calebe Martins 04/02/2019
 * Criar a pasta com tipo pasta tbm ou só a pasta
 * Quando clicar na pasta mostrar os itens da pasta selecionada
 * Terminar a procura no banco de dados
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaração das variaveis e construtores
    final Context context = this;
    DBHelper dbHelper;
    ListView listView;
    ArrayAdapter adapter;

    private String type_folder = "";
    private String name_folder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicialização das variaveis e construtores
        dbHelper = new DBHelper(this);
        listView = (ListView) findViewById(R.id.lista);

        // TESTE
        LoadDataFolder();

        // Botão flutuante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            // Tela que adiciona itens na lista
            @Override
            public void onClick(View view) {
                // Impede de adicionar um item sem selecionar uma pasta antes
                if (type_folder.equals("")) {
                    toastMenssage("Selecione uma pasta.");
                } else {
                    Intent intent = new Intent(MainActivity.this, AddItems.class);
                    // Envia o nome da pasta para AddItems
                    intent.putExtra("name_folder", type_folder);
                    startActivity(intent);
                }
            }
        });

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
                    } else {
                        toastMenssage("Digite um nome: ");
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
        boolean insertData = dbHelper.addDataFolder(newEntry);

        if (insertData) {
            toastMenssage("Nova Pasta adicionada!");
        } else {
            toastMenssage("Algo deu errado");
        }
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

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);

        // Criador da lista adaptada e seta a lista adaptada
        listView.setAdapter(adapter);

        // Click do item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Pega o nome da pasta
                name_folder = (String) listView.getItemAtPosition(position);
                // Passa o nome da pasta para AddItems
                type_folder = name_folder;
                listView.setAdapter(null);
                Toast.makeText(context, "type_folder: "+type_folder, Toast.LENGTH_SHORT).show();
                LoadDataItems();
                // ******Carrega a lista da pasta selecionada com o adapter
            }
        });
    }

    // Carrega os itens da pasta selecionada
    public void LoadDataItems() {
        Toast.makeText(context, "type_folder = "+type_folder, Toast.LENGTH_SHORT).show();
        Cursor data = dbHelper.getDataItems(type_folder);
//        Cursor data = dbHelper.teste(type_folder);
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);

        // Criador da lista adaptada e seta a lista adaptada
        listView.setAdapter(adapter);
    }

    // Aparece uma menssagem Toast
    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
