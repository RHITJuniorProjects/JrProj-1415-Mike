package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.Firebase;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;

/**
 * Created by johnsoaa on 3/14/2015.
 */
public class Trophy implements Parcelable {

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

    public Trophy(String firebaseURL) {
        mFirebase = new Firebase(firebaseURL);
        this.mCost = 0;
        this.mDescription = "";
        this.mImage = "";
        this.mName = "";
    }

    public Trophy(Parcel pc) {
        mFirebase = new Firebase(pc.readString());
        this.mCost = pc.readInt();
        this.mDescription = pc.readString();
        this.mImage = pc.readString();
        this.mName = pc.readString();
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

    public int getCost() {
        return mCost;
    }

    public void setCost(int mCost) {
        this.mCost = mCost;
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
}
