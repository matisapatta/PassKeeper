package mobile.tema.passwordkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import database.DBManager;

public class AddItem extends AppCompatActivity  implements View.OnClickListener{

    private DataStruct data;
    private Button saveBtn;
    private Button resetBtn;
    private Button editBtn;
    private Button deleteBtn;
    private EditText pwd;
    private EditText acc;
    private EditText usr;
    private EditText cmt;
    private DBManager db;
    private String mode;


    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.saveBtn:
                Validate val= new Validate();
                if(val.validateData(acc, usr, pwd, cmt, this)){
                    // podría enviarse el texto nada más
                    if(mode.equals("NEW"))
                    db.newEntry(acc.getText().toString(),usr.getText().toString(),pwd.getText().toString(),cmt.getText().toString());
                    else
                    db.updateEntry(data.getId(),acc.getText().toString(),usr.getText().toString(),pwd.getText().toString(),cmt.getText().toString());
                    Toast.makeText(getApplicationContext(), "Guardado!", Toast.LENGTH_LONG).show();
                    db.close();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.resetBtn:
                pwd.setText("");
                acc.setText("");
                usr.setText("");
                cmt.setText("");
                Toast.makeText(getApplicationContext(),"Reestablecido!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteBtn:
                db.deleteEntry(data.getId());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.editBtn:
                acc.setEnabled(true);
                pwd.setEnabled(true);
                usr.setEnabled(true);
                cmt.setEnabled(true);
                editBtn.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        // Tomo el intent
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mode = b.getString("MODE");
        data = (DataStruct)i.getParcelableExtra("information");

        // Inicializo BBDD
        db = new DBManager(this);

        // Relaciono los botones del layout con el código Java
        saveBtn = (Button)findViewById(R.id.saveBtn);
        resetBtn = (Button)findViewById(R.id.resetBtn);
        editBtn = (Button)findViewById(R.id.editBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        pwd = (EditText)findViewById(R.id.pwdInput);
        acc = (EditText)findViewById(R.id.accInput);
        usr = (EditText)findViewById(R.id.usrInput);
        cmt = (EditText)findViewById(R.id.cmtInput);

        if(mode.equals("EDIT")){
            if(data!=null){
                acc.setText(data.getAccount());
                usr.setText(data.getUsername());
                pwd.setText(data.getPassword());
                if(data.getComment().length()>0)
                    cmt.setText(data.getComment());
                else
                    cmt.setHint("");

                // Cambio visibilidad de los botones
                saveBtn.setVisibility(View.INVISIBLE);
                resetBtn.setVisibility(View.INVISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.VISIBLE);
                //Cambio editabilidad de los campos
                acc.setEnabled(false);
                pwd.setEnabled(false);
                usr.setEnabled(false);
                cmt.setEnabled(false);
            }
        } else {
            data = new DataStruct();
            // Visibilidad de los botones
            saveBtn.setVisibility(View.VISIBLE);
            resetBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
        }
        // Asigno los eventos onClick
        saveBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
