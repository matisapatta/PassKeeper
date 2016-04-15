package database;

import android.provider.BaseColumns;

/**
 * Created by mati on 4/8/16.
 * Se define el layout de la BBDD
 */
public class DBLayout {
    public DBLayout() {
    }

    public static abstract class DBConstants implements BaseColumns {
        static final String DB = "PassKeeper";

        static final String ACCOUNTS_TABLE = "Cuentas";

        static final String ACCOUNTS_TABLE_ID = "_id";
        static final String ACCOUNTS_TABLE_DESCRIPTION = "Descripción";
        static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE IF NOT EXISTS " + ACCOUNTS_TABLE
                + " (" + ACCOUNTS_TABLE_ID + "  INTEGER PRIMARY KEY, "
                + ACCOUNTS_TABLE_DESCRIPTION + "  VARCHAR(40)" + ")";

        static final String[] ACCOUNTS_NAME = new String[]{
                "Facebook",
                "Gmail",
                "Homebanking",
                "Otros"
        };

        static final String PASS_TABLE = "Lista";
        static final String PASS_TABLE_ID = "_id";
        static final String PASS_TABLE_ACC = "Cuenta";
        static final String PASS_TABLE_USER = "Usuario";
        static final String PASS_TABLE_PASS = "Password";
        static final String PASS_TABLE_COMMENTS = "Comentarios";

        static final String CREATE_PASS_TABLE = "CREATE TABLE IF NOT EXISTS " + PASS_TABLE
                + " (" + PASS_TABLE_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PASS_TABLE_ACC + " VARCHAR(40), "
                + PASS_TABLE_USER + "  VARCHAR(40), "
                + PASS_TABLE_PASS + "  VARCHAR(40), "
                + PASS_TABLE_COMMENTS + " VARCHAR(100)" + ")";

        static final String MASTER_PASS_TABLE = "MasterPass";
        static final String MASTER_PASS_ID = "_id";
        static final String MASTER_PASS_PASS = "Password";

        static final String CREATE_MASTER_PASS_TABLE = "CREATE TABLE IF NOT EXISTS " + MASTER_PASS_TABLE
                + " (" + MASTER_PASS_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MASTER_PASS_PASS + " VARCHAR(40)" + ")";
        static final String FILENAME = "keyfile";


    }
}
