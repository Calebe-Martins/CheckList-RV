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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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

    // MultiChoiceModeListener = ActionMode
    public static List<String> UserSelection = new ArrayList<>();
    private ActionMode mActionMode;

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
//        listItems.setMultiChoiceModeListener(modeListener);

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

        listItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                mActionMode = ListItems.this.startActionMode(new ActionBarCallBack());
                return false;
            }
        });
    }

    // DELETAR MULTIPLOS ITENS DE UMA VEZ
//    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
//        @Override
//        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//            if (UserSelection.contains(listItems.getItemAtPosition(position))) {
//                UserSelection.remove(listItems.getItemAtPosition(position));
//            } else {
//                UserSelection.add((String) listItems.getItemAtPosition(position));
//            }
//            mode.setTitle(UserSelection.size() + " items adicionados...");
//        }
//
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.delete_folder, menu);
//
//            // Seta a lista de itens com checkbox para deletar
//            listItems.setAdapter(adapterFolder);
//            return true;
//        }
//
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false;
//        }
//
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_delete: {
//                    // Pega prompts.xml como view
//                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//
//                    // Coloca titulo no Pop Up
//                    alertDialogBuilder.setTitle("Você tem certeza disso?");
//                    alertDialogBuilder.setMessage("Todos os itens serão apagados.");
//
//                    // Coloca icone de lixeira no Pop-Up
//                    alertDialogBuilder.setIcon(R.drawable.ic_delete_popup);
//
//                    // DELETA AS PASTAS E SEUS ITENS DO BANCO DE DADOS
//                    alertDialogBuilder.setCancelable(false).setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Deletando a(s) pasta(s) e os itens dentro dela(s)
//                            for (int i = 0; i < UserSelection.size(); i++) {
//                                dbHelper.deleteItems(UserSelection.get(i));
//                            }
//
//                            // Limpa a seleção das pastas
//                            UserSelection.clear();
//                            toastMenssage("Deletado com sucesso!");
//                            // Recarrega o menu, para o usuário n ter q selecionar opção pastas novamente
//                            LoadDataItems();
//
//                        }
//                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//
//                    // Cria o alert dialog
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//
//                    // Mostra o dialog
//                    alertDialog.show();
//
//                    mode.finish();
//                    return true;
//                }
//                default: return false;
//            }
//        }
//
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            listItems.setAdapter(adapter);
//        }
//    };


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

    // Classe para chamar o ActionMode quando item for pressionado por longo tempo
    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete: {
                    // Pega prompts.xml como view
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                    // Coloca titulo no Pop Up
                    alertDialogBuilder.setTitle("Você tem certeza disso?");
                    alertDialogBuilder.setMessage("Todos os itens selecionados serão apagados.");

                    // Coloca icone de lixeira no Pop-Up
                    alertDialogBuilder.setIcon(R.drawable.ic_delete_popup);

                    // DELETA AS PASTAS E SEUS ITENS DO BANCO DE DADOS
                    alertDialogBuilder.setCancelable(false).setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Deletando a(s) pasta(s) e os itens dentro dela(s)
                            for (int i = 0; i < UserSelection.size(); i++) {
                                dbHelper.deleteItems(UserSelection.get(i));
                            }

                            // Limpa a seleção das pastas
                            UserSelection.clear();
                            toastMenssage("Deletado com sucesso!");
                            // Recarrega o menu, para o usuário n ter q selecionar opção pastas novamente
                            LoadDataItems();

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
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.delete_folder, menu);
//            mode.getMenuInflater().inflate(R.menu.delete_folder, menu);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

    }
}
