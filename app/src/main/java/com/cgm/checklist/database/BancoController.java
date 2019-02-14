package com.cgm.checklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.cgm.checklist.MainActivity;

public class BancoController {

    private SQLiteDatabase db;
    private DBHelper banco;

    public BancoController(Context context) {
        banco = new DBHelper(context);
    }

    // Responsavel por adicionar os itens com: tipo = (nome da pasta) e as pastas com: tipo = menu
    public String AddData(String item, String tipo) {
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(DBHelper.COL2, item);
        valores.put(DBHelper.TYPE_FOLDER, tipo);

        resultado = db.insert(DBHelper.TABLE_NAME, null, valores);
        db.close();

        if (resultado == -1) {
            return "Erro!";
        } else {
            return "Item adicionado em: " + tipo;
        }
    }
}
