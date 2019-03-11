package com.cgm.checklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.cgm.checklist.settingsApp.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

/** CheckList 2.0
 * Calebe Martins 04/02/2019
 * Colocar item com adpter imageButton contendo um lapis para editar
 * Unificar comando de deletar itens
 * Teste de utilizar fragmente em vez de Activity para a ListItems
 * String.xml LINHA 9
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaração das variaveis e recursos
    final Context context = this;
    DBHelper dbHelper;
    ListView listView;
    ArrayAdapter adapter;

    private String type_folder = "";
    private String name_folder;

    //
    public static List<String> UserSelection = new ArrayList<>();
    boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicialização das variaveis e construtores
        dbHelper = new DBHelper(this);
        listView = (ListView) findViewById(R.id.lista_folder);

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

        // Avisa qual opção de seleção de itens estah ativa
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean simpleSelection = sharedPreferences.getBoolean("simple_selection", true);
        boolean multiSelection  = sharedPreferences.getBoolean("persistence_selection", true);
        boolean deleteSelection = sharedPreferences.getBoolean("delete_selection", true);

        View v = findViewById(android.R.id.content);
        if (simpleSelection) {
            Snackbar.make(v, "Marcações simples selecionada", Snackbar.LENGTH_SHORT).setAction("ok", null).show();
        } else if (multiSelection) {
            Snackbar.make(v, "Marcações multiplas selecionada", Snackbar.LENGTH_SHORT).setAction("ok", null).show();
        } else if (deleteSelection) {
            Snackbar.make(v, "Deletar marcações selecionada", Snackbar.LENGTH_SHORT).setAction("ok", null).show();
        }
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
                        // Pega a entrada newEntry e verifica se jah existe;
                        AddDataFolder(newEntry);
                        userInput.setText("");
                        LoadDataFolder();
                    } else {
                        toastMenssage("Digite um nome");
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

        if (id == R.id.action_delete_folder) {
            String padrao = "menu";
            // Obtem os dados e anexar a uma lista
            Cursor data = dbHelper.getData(padrao);
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
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Deletar itens: ");
            alertDialogBuilder.setMultiChoiceItems(mList, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                    if (isChecked) {
                        if (!UserSelection.contains(listView.getItemAtPosition(position))) {
                            UserSelection.add((String) listView.getItemAtPosition(position));
                        }
                    } else {
                        UserSelection.remove(listView.getItemAtPosition(position));
                    }
                }
            });

            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Verifica se foi selecionado alguma checkbox
                    if (UserSelection.size() == 0) {
                        toastMenssage("Selecione uma pasta");
                    } else {
                        //percorre e deleta as pastas e os itens do banco de dados
                        for (int i = 0; i < UserSelection.size(); i++) {
                            dbHelper.deleteAll(UserSelection.get(i));
                        }
                        toastMenssage("Itens deletados");
                        UserSelection.clear();
                        LoadDataFolder();
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

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Verificação de inserção do item no banco de dados
    public void AddDataFolder(String newEntry) {
        String resultado;
        String padrao = "menu";

        resultado = dbHelper.AddData(newEntry, padrao);
        Toast.makeText(context, resultado, Toast.LENGTH_SHORT).show();
    }

    // Carrega as pastas(tipo)
    public void LoadDataFolder() {
        String padrao = "menu";
        // Obtem os dados e anexar a uma lista
        Cursor data = dbHelper.getData(padrao);
        final ArrayList<String> listData = new ArrayList<>();
        while (data.moveToNext()) {
            // Obtenha o valor do banco de dados na coluna -1
            // Em seguida adiciona a lista
            listData.add(data.getString(1));
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Adapter para click simples nas pastas acessando proxima tela
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);

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

    // Aparece uma menssagem Toast
    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
