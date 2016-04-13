package mobile.tema.passwordkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import database.DBManager;


public class MainActivity extends AppCompatActivity implements Observer {

    // Variables
    static final String editEntry = "EDIT";
    static final String newEntry = "NEW";
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
        Intent i = getIntent();
        // Inicializo Base de datos
        db = new DBManager(this);
        db.addObserver(this);

        // Inicializo el resto de los componentes
        addBtn=(Button)findViewById(R.id.addBtn);
        resetBtn=(Button)findViewById(R.id.resetBtn);
        pwdList=(ListView)findViewById(R.id.listView);

        //Cargo la list view
        addRow();

        // Evento on click del botón add
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newActivity(null,newEntry);
            }
        });

        // Evento del click reset
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteAll();
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
                finish();
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();

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
        finish();
    }

}
