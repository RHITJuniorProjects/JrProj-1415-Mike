package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;

/**
 * Created by johnsoaa on 3/14/2015.
 */
public class Trophy implements Parcelable, ChildEventListener {

    /**
     * A Creator object that allows this object to be created by a parcel
     */
    public static final Parcelable.Creator<Trophy> CREATOR = new Parcelable.Creator<Trophy>() {

        public Trophy createFromParcel(Parcel pc) {
            return new Trophy(pc);
        }

        public Trophy[] newArray(int size) {
            return new Trophy[size];
        }
    };
    private final Firebase mFirebase;
    protected int mCost;
    private String mDescription;
    private String mImage;
    private String mName;
    private String mKey;
    private ListChangeNotifier<Trophy> listChangeNotifier;

    public Trophy(String firebaseURL) {
        mFirebase = new Firebase(firebaseURL);
        Log.d("RHH", "trophy created at:" + firebaseURL);
        mFirebase.addChildEventListener(this);
        this.mCost = 0;
        this.mDescription = "";
        this.mImage = "";
        this.mName = "";
        this.mKey = firebaseURL.substring(firebaseURL.lastIndexOf('/') + 1);
    }

    public Trophy(Parcel pc) {
        mFirebase = new Firebase(pc.readString());
        this.mCost = pc.readInt();
        this.mDescription = pc.readString();
        this.mImage = pc.readString();
        this.mName = pc.readString();
        this.mKey = mFirebase.toString().substring(mFirebase.toString().lastIndexOf('/') + 1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mFirebase.toString());
        parcel.writeInt(mCost);
        parcel.writeString(mDescription);
        parcel.writeString(mImage);
        parcel.writeString(mName);

    }

    public int getCost() {
        return mCost;
    }

    public void setCost(int mCost) {
        this.mCost = mCost;
        if (this.listChangeNotifier != null){
            this.listChangeNotifier.onChange();
        }
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String mImage) {
        this.mImage = mImage;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getKey() {return this.mKey; }

    public void setListChangeNotifier(ListChangeNotifier<Trophy> listChangeNotifier) {
        this.listChangeNotifier = listChangeNotifier;
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String key = dataSnapshot.getKey();
        if (key.equals("cost")) {
            setCost(dataSnapshot.getValue(Integer.class));
        } else if (key.equals("description")) {
            setDescription(dataSnapshot.getValue(String.class));
        } else if (key.equals("image")) {
            setImage(dataSnapshot.getValue(String.class));
        } else if (key.equals("name")) {
            setName(dataSnapshot.getValue(String.class));
        }
        if (listChangeNotifier != null) {
            listChangeNotifier.onChange();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }

    /**
     * Compares this Trophy with the other given Trophy.
     *
     * @param p
     * @return
     */
    public int compareToIgnoreCase(Trophy p) {
        return GeneralAlgorithms.compareToIgnoreCase(this.getName(),
                p.getName());
    }

    public int compareToByPoints(Trophy p){
        return ((Integer)this.mCost).compareTo((Integer)p.getCost());
    }

    public int convertLimitFromFirebaseForm(Object limitString){
        try{
            return (Integer) limitString;
        }catch(java.lang.ClassCastException e){
            if (limitString instanceof String && limitString.toString().equals("None")) {
                return -1;
            } else{
                return -10;
            }
        }
    }

    public Object convertLimitToFirebaseForm(int limit){
        if (limit==-1){
            return "None";
        } else{
            return limit;
        }
    }
}
