package mobile.tema.passwordkeeper;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import database.DBManager;

public class AddItem extends AppCompatActivity  implements View.OnClickListener{

    private DataStruct data;
    private Button saveBtn;
    private Button resetBtn;
    private Button editBtn;
    private Button deleteBtn;
    private Button genPassBtn;
    private Button copyUser;
    private Button copyPwd;
    private Button copyCmt;
    private EditText pwd;
    private EditText acc;
    private EditText usr;
    private EditText cmt;
    private DBManager db;
    private String mode;
    private ProgressDialog progressDialog;


    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.saveBtn:
                data.setAccount(acc.getText().toString());
                data.setUsername(usr.getText().toString());
                data.setPass(pwd.getText().toString());
                data.setComment(cmt.getText().toString());
                Validate val = new Validate();
                if (val.validateData(acc, usr, pwd, cmt, getApplicationContext()))
                    new AsyncDB().execute();

                break;
            case R.id.resetBtn:
                pwd.setText("");
                usr.setText("");
                cmt.setText("");
                Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.screenClear),Toast.LENGTH_SHORT).show();
                break;
            case R.id.deleteBtn:
                new AsyncDelete().execute();
                break;
            case R.id.editBtn:
                acc.setEnabled(false);
                pwd.setEnabled(true);
                usr.setEnabled(true);
                cmt.setEnabled(true);
                genPassBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                copyCmt.setClickable(false);
                copyPwd.setVisibility(View.INVISIBLE);
                copyUser.setClickable(false);
                //toLeft(pwd,R.id.genPassBtn);
                //toLeft(usr, R.id.copyUser);
                break;
            case R.id.genPassBtn:
                val = new Validate();
                pwd.setText(val.randomPwd());
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.generated),Toast.LENGTH_SHORT).show();
                break;

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

    }

    @Override
    public void onStart(){
        super.onStart();

        // AddMob
        AdView mAdView = (AdView) findViewById(R.id.adViewItem);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Relaciono los botones del layout con el código Java
        saveBtn = (Button)findViewById(R.id.saveBtn);
        resetBtn = (Button)findViewById(R.id.resetBtn);
        editBtn = (Button)findViewById(R.id.editBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        genPassBtn = (Button)findViewById(R.id.genPassBtn);
        copyUser = (Button)findViewById(R.id.copyUser);
        copyPwd = (Button)findViewById(R.id.copyPwd);
        copyCmt = (Button)findViewById(R.id.copyCmt);
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
                genPassBtn.setVisibility(View.INVISIBLE);

                //Cambio editabilidad de los campos
                acc.setEnabled(false);
                pwd.setEnabled(false);
                usr.setEnabled(false);
                cmt.setEnabled(false);
                copyCmt.setClickable(true);
                copyPwd.setClickable(true);
                copyUser.setClickable(true);
            }
        } else {
            if(data!=null){
                acc.setText(data.getAccount());
                acc.setEnabled(false);
            } else {
                data = new DataStruct();
            }
            // Visibilidad de los botones
            saveBtn.setVisibility(View.VISIBLE);
            resetBtn.setVisibility(View.VISIBLE);
            editBtn.setVisibility(View.INVISIBLE);
            deleteBtn.setVisibility(View.INVISIBLE);
            genPassBtn.setVisibility(View.VISIBLE);
            copyCmt.setClickable(false);
            copyPwd.setVisibility(View.INVISIBLE);
            copyUser.setClickable(false);
            //toLeft(pwd,R.id.genPassBtn);
            //toLeft(usr, R.id.copyUser);
        }

        // Asigno los eventos onClick
        saveBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        genPassBtn.setOnClickListener(this);

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
    private class AsyncDB extends AsyncTask<Void, Integer, Void>{

        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(AddItem.this);
            progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.saving));
            progressDialog.show();
            //progressDialog = ProgressDialog.show(AddItem.this,"Loading...",
              //     "Loading application View, please wait...", false, false);
        }

        @Override
        protected Void doInBackground(Void... params){
            synchronized (getApplicationContext()) {


                    if (mode.equals("NEW")) {
                        db.newEntry(data.getAccount(), data.getUsername(), data.getPassword(), data.getComment());
                    } else {
                        db.updateEntry(data.getId(), data.getAccount(), data.getUsername(), data.getPassword(), data.getComment());
                    }

            }
            return null;
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.itemSaved), Toast.LENGTH_LONG).show();
            db.close();
            //setContentView(R.layout.activity_main);
            Intent intent = new Intent(AddItem.this, MainActivity.class);
            startActivity(intent);
            finish();
            progressDialog.dismiss();

        }
    }

    private void toLeft(EditText vText, int vBtn){
        android.widget.RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)vText.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF, vBtn);
        params.addRule(RelativeLayout.START_OF, vBtn);
        vText.setLayoutParams(params);

    }

    private class AsyncDelete extends AsyncTask<Void, Integer, Void>{

        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            progressDialog = new ProgressDialog(AddItem.this);
            progressDialog.setMessage(getApplicationContext().getResources().getString(R.string.deleting));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params){
            synchronized (getApplicationContext()) {
                db.deleteEntry(data.getId());
            }
            return null;
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            //close the progress dialog
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.deletedEntry), Toast.LENGTH_SHORT).show();
            db.close();
            //setContentView(R.layout.activity_main);
            Intent intent = new Intent(AddItem.this, MainActivity.class);
            startActivity(intent);
            finish();
            progressDialog.dismiss();

        }
    }

}



