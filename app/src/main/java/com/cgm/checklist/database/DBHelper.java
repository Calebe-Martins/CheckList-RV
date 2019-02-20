package com.cgm.checklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Tabela com as pastas
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "people_table";
    private static final String COL1 = "ID";
    public static final String COL2 = "name";
    public static final String TYPE_FOLDER = "type_folder";

    // Construtor
    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    // Cria a coluna do banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT,"
                + TYPE_FOLDER + " TEXT"
                + ")"; // Coluna de identificação da pasta
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Mostra todos os itens do banco de dados
    public Cursor getData(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE_FOLDER + " LIKE '" + type + "%'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // WHERE é o meu filtro, no caso usar TYPE_FOLDER se possível
//     + " WHERE " + TYPE_FOLDER + " like '" + "casa%'"
//     + " WHERE " + TYPE_FOLDER + " like '" + test +"%'"

    // Atualiza o nome dos itens selecionados
    public void updateName(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" + oldName + "'";
        db.execSQL(query);
    }

    // Deleta os itens do banco de dados
    public void deleteName(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" + name + "'";
        db.execSQL(query);
    }

    // Deleta as pastas
    public void deleteFolder(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " LIKE '" + name + "%'";
        db.execSQL(query);
    }

    // Deleta os itens de dentro da pasta
    public void deleteItems(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + TYPE_FOLDER + " LIKE '" + name + "%'";
        db.execSQL(query);
    }
}
