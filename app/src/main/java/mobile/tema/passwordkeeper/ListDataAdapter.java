package mobile.tema.passwordkeeper;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mati on 4/11/16.
 */
public class ListDataAdapter extends ArrayAdapter<DataStruct> {

    static class ViewHolder {
        TextView title;
        TextView subtitle;
    }

    private List<DataStruct> dataList;

     public ListDataAdapter(Context context, List<DataStruct> data) {
         super(context, R.layout.list_layout, data);
         dataList=data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Inflate XML
        View item = convertView;
        ViewHolder holder;
        if(item==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.list_layout, null);
            holder = new ViewHolder();
            holder.title = (TextView)item.findViewById(R.id.titleLabel);
            holder.subtitle = (TextView)item.findViewById(R.id.subtitleLabel);
            item.setTag(holder);
        } else {
            holder = (ViewHolder)item.getTag();
        }
        holder.title.setText(dataList.get(position).getAccount());
        holder.subtitle.setText(dataList.get(position).getUsername());

        return(item);
    }



}
