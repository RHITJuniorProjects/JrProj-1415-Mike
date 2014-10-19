package rhit.jrProj.henry.firebase;

import java.util.List;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Task implements Parcelable, ChildEventListener {

	/**
	 * A reference to firebase to keep the data up to date.
	 */
	private Firebase firebase;

	/**
	 * The task's name
	 */
	private String name;

	/**
	 * A description of the task
	 */
	private String description;
	
	/**
	 * A list of the user ids of the users assigned to the task
	 */
	private String assignedUserId;

	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * task that this object has been updated.
	 */
	private ListChangeNotifier<Task> listViewCallback;

	/**
	 * The task's assignee(s)
	 */
	// private User assignedUser;

	/**
	 * the task's category
	 */
	// private Category category;

	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

		public Task createFromParcel(Parcel pc) {
			return new Task(pc);
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};

	/**
	 * Creates a new task from a parcel
	 * 
	 * @param pc
	 */
	Task(Parcel pc) {
		this.firebase = new Firebase(pc.readString());
		this.name = pc.readString();
		this.description = pc.readString();
		this.assignedUserId = pc.readString();
	}

	public Task(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		this.firebase.addChildEventListener(this);
	}

	public int describeContents() {
		return 0;
	}

	/**
	 * 
	 * Sets a new list changed notifier
	 * 
	 * @param lcn
	 */
	public void setListChangeNotifier(ListChangeNotifier<Task> lcn) {
		this.listViewCallback = lcn;
	}

	/**
	 * Passes the Firebase URL, the name of the task and the description of the
	 * task to the next view
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeString(this.assignedUserId);
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Task) {
			return ((Task) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	};

	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.

	}

	public void onChildAdded(DataSnapshot arg0, String arg1) {
		if (arg0.getName().equals("name")) {
			this.name = arg0.getValue().toString();
			if (this.listViewCallback != null) {
				this.listViewCallback.onChange();
			}
		} else if (arg0.getName().equals("description")) {
			this.description = arg0.getValue().toString();
		} else if (arg0.getName().equals("assignedTo")) {
			this.assignedUserId = arg0.getValue().toString();
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

	/**
	 * 
	 * Gets the description of the task
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}
	
	public String getAssignedUserId() {
		return this.assignedUserId;
	}
}
