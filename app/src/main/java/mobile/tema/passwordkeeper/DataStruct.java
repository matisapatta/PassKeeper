package mobile.tema.passwordkeeper;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * La estructura de datos que necesito
 */
public class DataStruct implements Parcelable {

    private int id;
    private String username;
    private String account;
    private String password;
    private String comment;

    public DataStruct(){
        id = 0;
        account = "";
        password = "";
        username = "";
        comment = "";
    }
    public DataStruct(int i, String a, String u, String p, String c){
        this.id = i;
        this.account = a;
        this.password = p;
        this.username = u;
        this.comment = c;
    }
    public void setId(int i){
        this.id=i;
    }
    public void setAccount(String a){
        this.account = a;
    }
    public void setPass(String p){
        this.password = p;
    }

    public void setUsername(String u) {
        this.username = u;
    }
    public void setComment(String c){
        this.comment = c;
    }

    public String getAccount(){
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getComment(){
        return comment;
    }

    public int getId(){
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(account);
        out.writeString(username);
        out.writeString(password);
        out.writeString(comment);
    }
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<DataStruct> CREATOR = new Parcelable.Creator<DataStruct>() {
        public DataStruct createFromParcel(Parcel in) {
            return new DataStruct (in);
        }

        public DataStruct[] newArray(int size) {
            return new DataStruct[size];
        }
    };
    // example constructor that takes a Parcel and gives you an object populated with it's values
    private DataStruct (Parcel in) {
        id = in.readInt();
        account = in.readString();
        username = in.readString();
        password = in.readString();
        comment = in.readString();

    }


}
