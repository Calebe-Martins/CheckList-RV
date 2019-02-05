package com.cgm.checklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Tabela com as pastas
 * Cada pasta leva ah uma lista diferente
 * No Banco de Dados a tabela recebe o msm nome da pasta
 *
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TABLE_FOLDER = "people_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";

    public DBHelper(Context context) {
        super(context, TABLE_FOLDER, null, 1);
    }

    // Cria a coluna do banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "
                + TABLE_FOLDER + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
        onCreate(db);
    }

    // Adiciona itens no banco de dados
    public boolean addData(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        long result = db.insert(TABLE_FOLDER, null, contentValues);

        //se a data inserida for incorreta, retorna -1
        if (result == -1) {
            return  false;
        } else {
            return true;
        }
    }

    // Pega a lista selecionada
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FOLDER;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Pega os itens da lista
    public Cursor getItemId(String name) {
        SQLiteDatabase db= this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_FOLDER + " WHERE " + COL2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // Atualiza o nome dos itens selecionados
    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_FOLDER + " SET " + COL2 + " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" + oldName + "'";
        db.execSQL(query);
    }

    // Deleta os itens do banco de dados
    public void deleteName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_FOLDER + " WHERE " + COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" + name + "'";
        db.execSQL(query);
    }
}
