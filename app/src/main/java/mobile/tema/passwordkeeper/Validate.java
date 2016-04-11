package mobile.tema.passwordkeeper;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import database.DBManager;

/**
 * Created by mati on 4/7/16.
 */
public class Validate {

    private String pwd;

    public Validate() {

    }

    public Validate(String p) {
        pwd = p;
    }

    public boolean ValidatePwd(String p) {
        if (p.equals(pwd))
            return true;
        else
            return false;
    }

    public boolean ValidateEntry(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Función para validar los datos ingresados.
    public boolean validateData(EditText acc, EditText usr, EditText pwd, EditText cmt, Context context) {
        Validate validate = new Validate();
        if (validate.ValidateEntry(acc)) {
            if (validate.ValidateEntry(usr)) {
                if (validate.ValidateEntry(pwd)) {
                    return true;
                } else {
                    Toast.makeText(context, "La contraseña no puede ser vacía!", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(context, "El usuario no puede ser vacío!", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context, "La cuenta no puede ser vacía!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}