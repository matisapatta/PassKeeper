package mobile.tema.passwordkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import database.DBManager;

public class EntryActivity extends AppCompatActivity {

    private EditText pwdField;
    private Button loginBtn;
    private TextView fail;
    private TextView mainT;
    private DBManager db;
    private String aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        // AddMob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        db = new DBManager(this);
        //db.update();
        //db.deleteMasterPwd();
        pwdField = (EditText)findViewById(R.id.pwdField);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        fail = (TextView)findViewById(R.id.fail);
        mainT = (TextView)findViewById(R.id.enterPwd);
        if(db.getMasterPwd()==null) {
            mainT.setText(getResources().getString(R.string.first));
        } else {
            aux = new String(db.getMasterPwd());
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwdField.getText().toString().trim().length() > 0) {
                    Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                    if (aux == null) {
                        // seteo la master password
                        db.setMasterPwd(pwdField.getText().toString());
                        Toast.makeText(getApplicationContext(),"Contraseña guardada!",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Validate validate = new Validate(aux);
                        if (validate.ValidatePwd(pwdField.getText().toString())) {
                            startActivity(intent);
                            finish();
                        } else {
                            fail.setText("Contraseña Incorrecta");
                            pwdField.setText("");
                        }
                    }
                } else {
                    fail.setText("Ingrese una contraseña");
                }
            }
        });
    }
}
