package mobile.tema.passwordkeeper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.Random;

import database.DBManager;

public class EntryActivity extends AppCompatActivity {

    private EditText pwdField;
    private Button loginBtn;
    private TextView fail;
    private TextView mainT;
    private DBManager db;
    private String aux;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entry);
        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHYJKLMNOPQRSTUVWXYZ!$%&/()?*+".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        db = new DBManager(getApplicationContext());
        // AddMob
        AdView mAdView = (AdView) findViewById(R.id.adViewEntry);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onStart(){
        super.onStart();

        pwdField = (EditText)findViewById(R.id.pwdField);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        fail = (TextView)findViewById(R.id.fail);
        mainT = (TextView)findViewById(R.id.enterPwd);
        if(db.getMasterPwd(this)==null) {
            mainT.setText(getResources().getString(R.string.entryMasterPwd));
        } else {
            aux = new String(db.getMasterPwd(this));
        }
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwdField.getText().toString().trim().length() > 0) {
                    Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                    if (aux == null) {
                        // seteo la master password
                        db.setMasterPwd(pwdField.getText().toString());
                        Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.savedPwd),Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Validate validate = new Validate(aux);
                        if (validate.ValidatePwd(pwdField.getText().toString())) {
                            startActivity(intent);
                            finish();
                        } else {
                            fail.setText(getApplicationContext().getResources().getString(R.string.wrongPwd));
                            pwdField.setText("");
                        }
                    }
                } else {
                    fail.setText(getApplicationContext().getResources().getString(R.string.emptyMasterPwd));
                }
            }
        });



    }

    private class AsyncLoad extends AsyncTask<Void, Integer, Void>{

        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(EntryActivity.this);
            progressDialog.setProgressStyle(R.style.Widget_AppCompat_Spinner);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params){
            synchronized (getApplicationContext()){

                //db.update();
                //db.deleteMasterPwd();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            progressDialog.dismiss();

        }
    }

}
