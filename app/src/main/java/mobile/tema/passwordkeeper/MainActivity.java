package mobile.tema.passwordkeeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import database.DBManager;


public class MainActivity extends AppCompatActivity implements Observer {

    // Variables
    static final String editEntry = "EDIT";
    private Button addBtn;
    private Button resetBtn;
    private ListView pwdList;
    private List<DataStruct> list;
    private DBManager db;
    private ListDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent i = getIntent();

        // Inicializo Base de datos
        db = new DBManager(this);
        db.addObserver(this);

        // Inicializo el resto de los componentes
        addBtn=(Button)findViewById(R.id.addBtn);
        resetBtn=(Button)findViewById(R.id.resetBtn);
        pwdList=(ListView)findViewById(R.id.listView);

        //Cargo la list view
        addRow();



    }
    @Override
    public void onStart(){
        super.onStart();
        // AddMob
        AdView mAdView = (AdView) findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Evento on click del botón add
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //newActivity(null,newEntry);
                Intent intent = new Intent(MainActivity.this, AccountSelect.class);
                startActivity(intent);
                finish();
            }
        });

        // Evento del click reset
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Put up the Yes/No message box
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder
                        .setTitle(R.string.resetBtn)
                        .setMessage(R.string.areYouSure)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setPositiveButton(R.string.yesString, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Yes button clicked, do something
                                db.deleteAll();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.deletedEntry),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.noString, null)						//Do nothing on no
                        .show();
            }
        });

        pwdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int position, long id) {
                //Busco el ID de lo que seleccioné
                int selected = ((DataStruct)a.getItemAtPosition(position)).getId();
                // Busco la estructura
                DataStruct data = db.getEntrybyID(selected);
                newActivity(data,editEntry);

            }
        });



    }
    @Override
    public void update(Observable o, Object data) {
        // Cuando hay cambio en los datos
        pwdList.setAdapter(null);
        addRow();
    }

    public void addRow(){
        list = db.getPwdList();
        if(list!=null) {
            adapter = new ListDataAdapter(this, list);

            // Ordena la lista antes de cargarla
            Comparator<DataStruct> ALPHABETICAL_ORDER1 = new Comparator <DataStruct> () {
                public int compare(DataStruct object1, DataStruct object2) {
                    int res = String.CASE_INSENSITIVE_ORDER.compare(object1.getAccount().toString(), object2.getAccount().toString());
                    return res;
                }
            };
            Collections.sort(list, ALPHABETICAL_ORDER1);
            // Cargo la lista
            pwdList.setAdapter(adapter);
        }
    }

    public void newActivity(DataStruct data, String mode){
        Intent intent = new Intent(MainActivity.this, AddItem.class);
        Bundle b = new Bundle();
        b.putString("MODE",mode);
        // Guardo el objeto
        b.putParcelable("information",data);
        intent.putExtras(b);
        startActivity(intent);
        this.finish();
    }
    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }


}
