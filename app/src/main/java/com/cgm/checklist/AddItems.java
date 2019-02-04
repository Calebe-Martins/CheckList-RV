package com.cgm.checklist;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddItems extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_items);

        //BotÃ£o voltar na actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botÃ£o
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botÃ£o
        getSupportActionBar().setTitle("");                    //Titulo para ser exibido na sua Action Bar em frente Ã  seta

    }

    public void proximaTela(View view){

        Intent intent = new Intent(this, AddItems.class);
        startActivity(intent);
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
}
