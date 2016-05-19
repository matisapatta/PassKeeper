package mobile.tema.passwordkeeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AccountSelect extends AppCompatActivity {

    private ListView accList;
    private String acc;
    static final String newEntry = "NEW";
    private DataStruct data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_select);

        accList = (ListView)findViewById(R.id.accOptions);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> accOptAdapter = ArrayAdapter.createFromResource(this,
                R.array.accOptions, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        accOptAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        // Apply the adapter to the spinner
        accList.setAdapter(accOptAdapter);
        data = new DataStruct();
        accList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Tomo el texto de la posición
                acc = (String)(accList.getItemAtPosition(position));
                // Armo el intent y abro la nueva activity
                Intent intent = new Intent(AccountSelect.this, AddItem.class);
                Bundle b = new Bundle();
                b.putString("MODE",newEntry);
                // Guardo el objeto
                if(position==accList.getCount()-1){
                    data = null;
                } else {
                    data.setAccount(acc);
                }
                b.putParcelable("information",data);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });


    }

}
