package com.cgm.checklist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.cgm.checklist.database.DBHelper;

/** CheckList 2.0
 * Calebe Martins 04/02/2019
 * Salvar as pastas e mostrar na list view -> Falta testar
 * Iniciar o app com as pastas das listas
 * Mostrar as pastas na listview
 * Mandar nome da pasta criada (linha 112) para DBHelper e criar coluna com nome da pasta
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declaração das variaveis e construtores
    final Context context = this;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inicialização das variaveis e construtores
        dbHelper = new DBHelper(this);

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
                    if (userInput.length() != 0) {
                        AddData(newEntry);
                        userInput.setText("");
                    } else {
                        toastMenssage("Digite um item: ");
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
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

    public void toastMenssage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
