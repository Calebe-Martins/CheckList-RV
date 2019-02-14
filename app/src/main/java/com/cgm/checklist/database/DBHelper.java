package com.cgm.checklist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Tabela com as pastas
 * Colocar tipo da pasta como menu para chamar só os tipos no menu
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

    // Adiciona as pastas no banco de dados
    public boolean addDataFolder(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item); // Coloca só o nome da pasta como item

        long result = db.insert(TABLE_NAME, null, contentValues);

        //se a data inserida for incorreta, retorna -1
        if (result == -1) {
            return  false;
        } else {
            return true;
        }
    }

    // Mostra todos os itens do banco de dados
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE_FOLDER + " LIKE '" + "menu%'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    // WHERE é o meu filtro, no caso usar TYPE_FOLDER se possível
//     + " WHERE " + TYPE_FOLDER + " like '" + "casa%'"
//     + " WHERE " + TYPE_FOLDER + " like '" + test +"%'"

    // Pega os itens da pasta selecionada
    public Cursor getDataItems(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + TYPE_FOLDER + " LIKE '" + type + "%'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

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

    // TESTE DE ADIÇÃO DE COLUNAS NOVAS NA MINHA TABELA
//    public static void updateTabelasBanco(SQLiteDatabase db, String table,
//                                          String column, String typ, String valor) {
//        try {
//            db.execSQL("ALTER TABLE " + table + " ADD " + column + " " + typ);
//            if (valor != ""){
//                db.execSQL("update "+ table +" set "+ column +" = '"+ valor +"'");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
