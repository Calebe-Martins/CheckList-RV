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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cgm.checklist.database.DBHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/** CheckList 2.0
 * Calebe Martins 04/02/2019
 * Quando clicar na pasta, limpar a listView
 * Criar a pasta com tipo pasta tbm ou só a pasta
 * Quando adicionar item na pasta, recebe o nome do item e o nome da pasta (TYPE_FOLDER)
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaração das variaveis e construtores
    final Context context = this;
    DBHelper dbHelper;
    ListView listView;
    ArrayAdapter adapter;

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
        LoadData();

        // Botão flutuante
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            // Tela que adiciona itens na lista
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddItems.class);
                startActivity(intent);
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
                    // Verifica se a pasta jah existe, salva se n existe, cancela se existe
                    Cursor data = dbHelper.getData();
                    if (data.moveToNext()) {
                        toastMenssage("Pasta já existente.");
                    } else {
                        // Adiciona a pasta se não existir uma com msm nome
                        if (userInput.length() != 0) {
                            AddData(newEntry);
                            userInput.setText("");
                        } else {
                            toastMenssage("Digite um item: ");
                        }
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

        if (id == R.id.nav_refresh) {
            LoadData();
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
    public void AddData(String newEntry) {
        boolean insertData = dbHelper.addData(newEntry);

        if (insertData) {
            toastMenssage("Novo item adicionado!");
        } else {
            toastMenssage("Algo deu errado");
        }
    }

    public void LoadData() {
        // Obter os dados e anexar a uma lista
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
    }

    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
