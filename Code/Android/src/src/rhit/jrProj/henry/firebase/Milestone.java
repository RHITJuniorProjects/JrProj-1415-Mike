package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Milestone implements Parcelable, ChildEventListener {

	/**
	 * A reference to firebase to keep the data up to date.
	 */
	private Firebase firebase;

	/**
	 * A List of tasks that are contained within the Milestone
	 */
	private ArrayList<Task> tasks = new ArrayList<Task>();

	/**
	 * The name of the milestone
	 */
	private String name;
	
	/**
	 * The due date of a milestone
	 */
	private String dueDate = "10/19/1996";
	
	/**
	 * The percentage of tasks completed for this milestone
	 */
	private int taskPercent;
	

	/**
	 * A description of the work that needs to happen in this milestone.
	 */
	private String description;

	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * Milestone that this object has been updated.
	 */
	private ListChangeNotifier<Milestone> listViewCallback;

	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Milestone> CREATOR = new Parcelable.Creator<Milestone>() {
		/**
		 * Returns a new Milestone with that parcel
		 */
		public Milestone createFromParcel(Parcel pc) {
			return new Milestone(pc);
		}

		/**
		 * returns a new milestone array
		 */
		public Milestone[] newArray(int size) {
			return new Milestone[size];
		}
	};

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
		this.firebase.addChildEventListener(this);
		this.name = in.readString();
		this.dueDate = in.readString();
		this.description = in.readString();
		this.taskPercent = in.readInt();
		in.readTypedList(this.tasks, Task.CREATOR);
	}

	/**
	 * 
	 * Sets a new list changed notifier
	 * 
	 * @param lcn
	 */
	public void setListChangeNotifier(ListChangeNotifier<Milestone> lcn) {
		this.listViewCallback = lcn;
	}

	/**
	 * Returns the name of the milestone
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * If both of the Firebase URLs are the same, then they are referencing the
	 * same project.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Milestone) {
			return ((Milestone) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
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

	/**
	 * Passes to the parcel the Firebase URL, the milestone's name, and the
	 * milestone's description
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.dueDate);
		dest.writeString(this.description);
		dest.writeInt(this.taskPercent);
		dest.writeTypedList(this.tasks);
	}

	/**
	 * Do nothing
	 */
	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.
	}

	/**
	 * Fills in the new milestone's properties including the milestone name,
	 * description and list of tasks for that milestone
	 */
	public void onChildAdded(DataSnapshot arg0, String arg1) {
		if (arg0.getName().equals("name")) {
			this.name = arg0.getValue(String.class);
			if (this.listViewCallback != null) {
				this.listViewCallback.onChange();
			}
		} else if (arg0.getName().equals("description")) {
			this.description = arg0.getValue(String.class);
		} else if (arg0.getName().equals("dueDate")) { 
			this.dueDate = arg0.getValue(String.class);
		} else if (arg0.getName().equals("task_percent")) {
			this.taskPercent = arg0.getValue(Integer.class);
		} else if (arg0.getName().equals("tasks")) {
			for (DataSnapshot child : arg0.getChildren()) {
				Task t = new Task(child.getRef().toString());
				if (!this.tasks.contains(t)) {
					this.tasks.add(t);
				}
			}
		}
	}

	/**
	 * This will be called when the milestone data in Firebased is updated
	 */
	public void onChildChanged(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.

	}

	/**
	 * Might do something here for the tablet
	 */
	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.
	}

	/**
	 * Do nothing
	 */
	public void onChildRemoved(DataSnapshot arg0) {
		// TODO Auto-generated method stub.

	}
	
	/**
	 * Returns the name of the milestone
	 * @return the name of the milestone
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the due date of the milestone
	 * @return the due date of the milestone
	 */
	public String getDueDate() {
		return this.dueDate;
	}

	/**
	 * Gets the description of the milestone
	 * @return String
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the percentage of tasks completed in this milestone
	 * @return the percentage of tasks completed in this milestone
	 */
	public int getTaskPercent() {
		return this.taskPercent;
	}
}
