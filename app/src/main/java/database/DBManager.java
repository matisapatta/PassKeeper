package database;

import android.content.ContentValues;
import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import net.sqlcipher.Cursor;
import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import mobile.tema.passwordkeeper.DataStruct;
import mobile.tema.passwordkeeper.Validate;


/**
 * Created by mati on 4/8/16.
 */
public class DBManager extends Observable{
    private DataBase b;
    private String key;
    private File file;

    public DBManager(Context context){
        SQLiteDatabase.loadLibs(context);
        b = new DataBase(context);
        file = new File(context.getFilesDir(), DBLayout.DBConstants.FILENAME);
        if(file.exists()){
            try {
                FileInputStream fIn = context.openFileInput(DBLayout.DBConstants.FILENAME);
                InputStreamReader isr = new InputStreamReader(fIn);

                // Prepara un array para leer el archivo
                char[] inputBuffer = new char[20];
                // Lleno con el contenido del archivo
                isr.read(inputBuffer);
                // Transform the chars to a String
                key = new String(inputBuffer);
                isr.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            try {
                Validate v = new Validate();
                FileOutputStream outputStream = context.openFileOutput(DBLayout.DBConstants.FILENAME, Context.MODE_PRIVATE);
                String aKey = v.randomString();
                outputStream.write(aKey.getBytes());
                outputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Sólo utilizado para hacer update de la base de datos por algún cambio
    public void update(){
        SQLiteDatabase db = b.getWritableDatabase(key);
        b.onUpgrade(db, DBLayout.DBConstants.CURRENT_VERSION, DBLayout.DBConstants.CURRENT_VERSION);

    }

    // Buscar las cuentas predefinidas en la tabla de Accounts
    public List<String> getAccounts(){
        List<String> acc = new ArrayList<String>();
        String selectQuery = "SELECT " + DBLayout.DBConstants.ACCOUNTS_TABLE_ID + " FROM " + DBLayout.DBConstants.ACCOUNTS_TABLE;

        SQLiteDatabase db = b.getReadableDatabase(key);
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
        SQLiteDatabase db = b.getReadableDatabase(key);
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
        SQLiteDatabase db = b.getReadableDatabase(key);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataStruct obj = new DataStruct(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                        cursor.getString(4));
                list.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Buscar una entrada particular, con número de ID. Usado para editar y eliminar
    public DataStruct getEntrybyID(int id) {
        DataStruct entry = null;
        String selectQuery = "SELECT * FROM " + DBLayout.DBConstants.PASS_TABLE+ " WHERE "
                + DBLayout.DBConstants.PASS_TABLE_ID + " = " + id;
        SQLiteDatabase db = b.getReadableDatabase(key);

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            entry = new DataStruct(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4));
        }
        cursor.close();
        return entry;
    }

    // Eliminar una entrada, es necesario el ID.
    public void deleteEntry(int id){
        SQLiteDatabase db = b.getWritableDatabase(key);
        db.delete(DBLayout.DBConstants.PASS_TABLE, DBLayout.DBConstants.PASS_TABLE_ID + "=?", new String[]{String.valueOf(id)});
        setChanged();
        notifyObservers();
    }

    // Agregar una entrada a la BBDD
    public void newEntry ( String acc, String usr, String pwd, String cmt){
        SQLiteDatabase db = b.getWritableDatabase(key);
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
        SQLiteDatabase db = b.getWritableDatabase(key);
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
        SQLiteDatabase db = b.getWritableDatabase(key);
        db.delete(DBLayout.DBConstants.PASS_TABLE, null, null);
        setChanged();
        notifyObservers();
    }

    // Método para setear la master password. Utilizado la primera vez que se usa la app
    public void setMasterPwd(String masterPwd){
        SQLiteDatabase db = b.getWritableDatabase(key);
        ContentValues registry = new ContentValues();
        registry.put(DBLayout.DBConstants.MASTER_PASS_PASS, masterPwd);
        db.insert(DBLayout.DBConstants.MASTER_PASS_TABLE, null, registry);
    }

    // Método para obtener la master password para el login
    public String getMasterPwd(Context context){
        String entry = null;
        String selectQuery = "SELECT * FROM " + DBLayout.DBConstants.MASTER_PASS_TABLE;
        SQLiteDatabase db = b.getReadableDatabase(key);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            entry = new String(cursor.getString(1));
        }
        cursor.close();
        return entry;
    }

    // Método para eliminar la master password.
    public void deleteMasterPwd(){
        SQLiteDatabase db = b.getWritableDatabase(key);
        db.delete(DBLayout.DBConstants.MASTER_PASS_TABLE, null, null);
        setChanged();
        notifyObservers();
    }


    public void close(){
        b.close();
    }
}
