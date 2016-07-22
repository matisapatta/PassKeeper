package database;

import android.content.ContentValues;
import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;

import net.sqlcipher.Cursor;
import net.sqlcipher.DatabaseUtils;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Esto es para definir la clase DataBase, que es el tipo de base de datos que vamos a usar
 * Crea y upgradea la BBDD
 *
 */
public class DataBase extends SQLiteOpenHelper{

    public DataBase(Context context) {
        super(context, DBLayout.DBConstants.DB, null, DBLayout.DBConstants.CURRENT_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        createBDBase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + DBLayout.DBConstants.ACCOUNTS_TABLE);
        db.execSQL(DBLayout.DBConstants.CREATE_BACKUP_TABLE);
        db.execSQL(DBLayout.DBConstants.INSERT_FROM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBLayout.DBConstants.PASS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DBLayout.DBConstants.MASTER_PASS_TABLE);
        createBDBase(db);
    }

    private void createBDBase (SQLiteDatabase db) {
        //Crea las tablas
        db.execSQL(DBLayout.DBConstants.CREATE_ACCOUNTS_TABLE);
        db.execSQL(DBLayout.DBConstants.CREATE_PASS_TABLE);

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                new String[] {"table", DBLayout.DBConstants.PASS_BACKUP});
        if (!cursor.moveToFirst()) {
        }
        int count = cursor.getInt(0);
        cursor.close();
        if (count==1){
            db.execSQL(DBLayout.DBConstants.INSERT_FROM_BACKUP);
            db.execSQL("DROP TABLE IF EXISTS " + DBLayout.DBConstants.PASS_BACKUP);
        }

        db.execSQL(DBLayout.DBConstants.CREATE_MASTER_PASS_TABLE);
        ContentValues registry = new ContentValues();
        // Carga con datos de las cuentas
        for (int i=0; i<DBLayout.DBConstants.ACCOUNTS_NAME.length; i++) {
            registry.put(DBLayout.DBConstants.ACCOUNTS_TABLE_ID, i);
            registry.put(DBLayout.DBConstants.ACCOUNTS_TABLE_DESCRIPTION, DBLayout.DBConstants.ACCOUNTS_NAME[1]);
            db.insert(DBLayout.DBConstants.ACCOUNTS_TABLE, null, registry);
            registry.clear();
        }
        //Carga con datos dummy de un ejemplo
        /*for (int i=0; i<1; i++) {
            registry.put(DBLayout.DBConstants.PASS_TABLE_ACC, DBLayout.DBConstants.ACCOUNTS_NAME[1]);
            registry.put(DBLayout.DBConstants.PASS_TABLE_USER,"UsuarioPrueba1");
            registry.put(DBLayout.DBConstants.PASS_TABLE_PASS, "PasswordPrueba1");
            registry.put(DBLayout.DBConstants.PASS_TABLE_COMMENTS, "ComentarioPrueba1");

            db.insert(DBLayout.DBConstants.PASS_TABLE, null, registry);
            registry.clear();
        }*/
    }

}
