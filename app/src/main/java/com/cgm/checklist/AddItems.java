package com.cgm.checklist;

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
import android.widget.ImageButton;
import android.widget.Toast;

public class AddItems extends AppCompatActivity {

    // Declarando Recursos
    private ImageButton save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);

        // Botão voltar NA ACTIONBAR
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("");                    //Titulo para ser exibido na sua Action Bar em frente a seta

        // Carregando Recursos
        save = (ImageButton) findViewById(R.id.imageButton_Save);

    }

    public void SaveFolder(View view) {
        Toast.makeText(AddItems.this, "Funcionou", Toast.LENGTH_SHORT).show();
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
}
