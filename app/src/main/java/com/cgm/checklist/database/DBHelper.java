package com.cgm.checklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.cgm.checklist.MainActivity;

/** Comando do banco de dados sql
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
                + COL2 + " TEXT UNIQUE,"
                + TYPE_FOLDER + " TEXT"
                + ")"; // Coluna de identificação da pasta
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // PQ DISSO AQUI?
    }

    // Adiciona o item no banco de dados
    public String AddData(String item, String type) {
        ContentValues valores = new ContentValues();
        long resultado;

        SQLiteDatabase db = this.getWritableDatabase();
        valores.put(COL2, item);
        valores.put(TYPE_FOLDER, type);

        resultado = db.insert(TABLE_NAME, null, valores);
        db.close();

        if (resultado == -1) {
            return "Nome já existe";
        } else {
            return "Item adicionado em: " + type;
        }
    }

    // Mostra todos os itens do banco de dados
    public Cursor getData(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE_FOLDER + " = '" + type + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // WHERE é o meu filtro, no caso usar TYPE_FOLDER se possível
//     + " WHERE " + TYPE_FOLDER + " like '" + "casa%'"
//     + " WHERE " + TYPE_FOLDER + " like '" + test +"%'"

    // Atualiza o nome do item dentro da pasta
    public void updateItem(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = '" + newName + "' WHERE " + COL1 + " = '" + id + "'" + " AND " + COL2 + " = '" + oldName + "'";
        db.execSQL(query);
    }

    // Deletar os itens JAH dentro da pasta
    public void deleteItems(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'";
        db.execSQL(query);
    }

    // Deleta a(s) pasta(s) seleciona(s) e os itens dentro dela(s)
    public void deleteAll(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL2 + " = '" + name + "'";
        db.execSQL(query);
        String query2 = "DELETE FROM " + TABLE_NAME + " WHERE " + TYPE_FOLDER + " = '" + name + "'";
        db.execSQL(query2);
    }
}
