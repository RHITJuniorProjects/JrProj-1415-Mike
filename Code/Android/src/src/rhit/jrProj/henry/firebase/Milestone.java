package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Milestone implements Parcelable, ChildEventListener {

	private Firebase firebase;

	/**
	 * A List of tasks that are contained within the Milestone
	 */
	private ArrayList<Task> tasks = new ArrayList<Task>();

	/**
	 * The milestone name within the project
	 */
	private String name;

	/**
	 * A description of the work that needs to happen in this milestone.
	 */
	private String description;

	/**
	 * Creates a Milestone object
	 * 
	 * @param tasks
	 * @param number
	 */
	public Milestone(String firebaseUrl) {
		this.firebase = new Firebase(firebaseUrl);
		this.firebase.addChildEventListener(this);
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 *
	 * @param in
	 */
	public Milestone(Parcel in) {
		this.firebase = new Firebase(in.readString());
		this.name = in.readString();
		this.description = in.readString();
		in.readTypedList(this.tasks, Task.CREATOR);
	}

	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Milestone> CREATOR = new Parcelable.Creator<Milestone>() {

		public Milestone createFromParcel(Parcel pc) {
			return new Milestone(pc);
		}

		public Milestone[] newArray(int size) {
			return new Milestone[size];
		}
	};

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Gets an ArrayList of tasks associated with this milestone.
	 */
	public ArrayList<Task> getTasks() {
		return this.tasks;
	}

	/**
	 * Used to give additional hints on how to process the received parcel.
	 */
	public int describeContents() {
		// Do nothing.
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeTypedList(this.tasks);
	}

	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.

	}

	public void onChildAdded(DataSnapshot arg0, String arg1) {
		if (arg0.getName().equals("name")) {
			this.name = arg0.getValue(String.class);
		} else if (arg0.getName().equals("description")) {
			this.description = arg0.getValue(String.class);
		} else if (arg0.getName().equals("tasks")) {
			// TODO
		}

	}

	public void onChildChanged(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.

	}

	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.

	}

	public void onChildRemoved(DataSnapshot arg0) {
		// TODO Auto-generated method stub.

	}

}
