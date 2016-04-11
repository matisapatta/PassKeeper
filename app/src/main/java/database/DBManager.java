package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import mobile.tema.passwordkeeper.DataStruct;

/**
 * Created by mati on 4/8/16.
 */
public class DBManager extends Observable{
    private DataBase b;

    public DBManager(Context context){
        b = new DataBase(context);
    }

    // Sólo utilizado para hacer update de la base de datos por algún cambio
    public void update(){
        SQLiteDatabase db = b.getWritableDatabase();
        b.onUpgrade(db, 0, 0);
    }

    // Buscar las cuentas predefinidas en la tabla de Accounts
    public List<String> getAccounts(){
        List<String> acc = new ArrayList<String>();
        String selectQuery = "SELECT " + DBLayout.DBConstants.ACCOUNTS_TABLE_ID + " FROM " + DBLayout.DBConstants.ACCOUNTS_TABLE;

        SQLiteDatabase db = b.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                acc.add(DBLayout.DBConstants.ACCOUNTS_NAME[cursor.getInt(0)]);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return acc;
    }

    // Buscar un account name particular una vez seleccionado.
    public String getAccName(int accID){
        String name = "";
        String selectQuery = "SELECT " + DBLayout.DBConstants.ACCOUNTS_TABLE_DESCRIPTION + " FROM " + DBLayout.DBConstants.ACCOUNTS_TABLE +
                " WHERE " + DBLayout.DBConstants.ACCOUNTS_TABLE_ID + " = " + String.valueOf(accID);
        SQLiteDatabase db = b.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();

        return name;
    }

    // Obtener la lista entera de las contraseñas guardadas
    public List<DataStruct> getPwdList() {
        List<DataStruct> list = new ArrayList<DataStruct>();
        String selectQuery = "SELECT  * FROM " + DBLayout.DBConstants.PASS_TABLE;
        SQLiteDatabase db = b.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataStruct obj = new DataStruct(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4));
                list.add(obj);
            } while (cursor.moveToNext());
        }
        return list;
    }

    // Buscar una entrada particular, con número de ID. Usado para editar y eliminar
    public DataStruct getEntrybyID(int id) {
        DataStruct entry = null;
        String selectQuery = "SELECT * FROM " + DBLayout.DBConstants.PASS_TABLE+ " WHERE "
                + DBLayout.DBConstants.PASS_TABLE_ID + " = " + id;
        SQLiteDatabase db = b.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            entry = new DataStruct(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4));
        }
        return entry;
    }

    // Eliminar una entrada, es necesario el ID.
    public void deleteEntry(int id){
        SQLiteDatabase db = b.getWritableDatabase();
        db.delete(DBLayout.DBConstants.PASS_TABLE, DBLayout.DBConstants.PASS_TABLE_ID + "=?", new String[]{String.valueOf(id)});
        setChanged();
        notifyObservers();
    }

    // Agregar una entrada a la BBDD
    public void newEntry ( String acc, String usr, String pwd, String cmt){
        SQLiteDatabase db = b.getWritableDatabase();
        ContentValues registry = new ContentValues();
        //registry.put(DBLayout.DBConstants.PASS_TABLE_ID, id);
        registry.put(DBLayout.DBConstants.PASS_TABLE_ACC, acc);
        registry.put(DBLayout.DBConstants.PASS_TABLE_USER, usr);
        registry.put(DBLayout.DBConstants.PASS_TABLE_PASS, pwd);
        registry.put(DBLayout.DBConstants.PASS_TABLE_COMMENTS, cmt);
        db.insert(DBLayout.DBConstants.PASS_TABLE, null, registry);
        registry.clear();
        setChanged();
        notifyObservers();
    }

    // Actualizar una entrada existente, usada para editar.
    public void updateEntry (int id, String acc, String usr, String pwd, String cmt){
        SQLiteDatabase db = b.getWritableDatabase();
        ContentValues registry = new ContentValues();
        registry.put(DBLayout.DBConstants.PASS_TABLE_ID, id);
        registry.put(DBLayout.DBConstants.PASS_TABLE_ACC, acc);
        registry.put(DBLayout.DBConstants.PASS_TABLE_USER, usr);
        registry.put(DBLayout.DBConstants.PASS_TABLE_PASS, pwd);
        registry.put(DBLayout.DBConstants.PASS_TABLE_COMMENTS, cmt);
        String where = DBLayout.DBConstants.PASS_TABLE_ID + "=?";
        db.update(DBLayout.DBConstants.PASS_TABLE, registry, where, new String[]{String.valueOf(id)});
        registry.clear();
        setChanged();
        notifyObservers();
    }

    // Método utilizado para borrar todas las entradas de la tabla
    public void deleteAll(){
        SQLiteDatabase db = b.getWritableDatabase();
        db.delete(DBLayout.DBConstants.PASS_TABLE, null, null);
        setChanged();
        notifyObservers();
    }

    // Método para setear la master password. Utilizado la primera vez que se usa la app
    public void setMasterPwd(String masterPwd){
        SQLiteDatabase db = b.getWritableDatabase();
        ContentValues registry = new ContentValues();
        registry.put(DBLayout.DBConstants.MASTER_PASS_PASS, masterPwd);
        db.insert(DBLayout.DBConstants.MASTER_PASS_TABLE, null, registry);
    }

    // Método para obtener la master password para el login
    public String getMasterPwd(){
        String entry = null;
        String selectQuery = "SELECT * FROM " + DBLayout.DBConstants.MASTER_PASS_TABLE;
        SQLiteDatabase db = b.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            entry = new String(cursor.getString(1));
        }
        return entry;
    }

    // Método para eliminar la master password.
    public void deleteMasterPwd(){
        SQLiteDatabase db = b.getWritableDatabase();
        db.delete(DBLayout.DBConstants.MASTER_PASS_TABLE, null, null);
        setChanged();
        notifyObservers();
    }


    public void close(){
        b.close();
    }
}
