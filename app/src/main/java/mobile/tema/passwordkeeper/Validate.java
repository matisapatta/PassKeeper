package mobile.tema.passwordkeeper;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

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
                    Toast.makeText(context, context.getResources().getString(R.string.emptyPwd), Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(context, context.getResources().getString(R.string.emptyUser), Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context,context.getResources().getString(R.string.emptyAccount), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public String randomString() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHYJKLMNOPQRSTUVWXYZ!$%&/()?*+".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
    public String randomPwd() {
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHYJKLMNOPQRSTUVWXYZ1234567890!*".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        return output;
    }
}