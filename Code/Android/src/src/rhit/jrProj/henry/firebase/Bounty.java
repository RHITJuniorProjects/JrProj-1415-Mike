package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;

public class Bounty {
	private String claimed = "None";
	private String description = "None";
	private String dueDate = "No Due Date";
	private double hourLimit = -1;
	private int lineLimit = -1;
	private String name;
	private int points = -1;
	Firebase firebase;
	
	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * task that this object has been updated.
	 */
	private ListChangeNotifier<Task> listViewCallback;
	/**
	 * The bounty's parent project ID
	 */
	private Firebase parentProjectFB;
	/**
	 * The bounty's parent milestone ID
	 */
	private Firebase parentMilestoneFB;
	/**
	 * The bounty's parent project Name
	 */
	private String parentProjectName;
	/**
	 * The bounty's parent milestone Name
	 */
	private String parentMilestoneName;
	/**
	 * The bounty's parent task ID
	 */
	private Firebase parentTaskFB;
	/**
	 * The bounty's parent task Name
	 */
	private String parentTaskName;
	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Bounty> CREATOR = new Parcelable.Creator<Bounty>() {

		public Bounty createFromParcel(Parcel pc) {
			return new Bounty(pc);
		}

		public Bounty[] newArray(int size) {
			return new Bounty[size];
		}
	};

	/**
	 * Creates a new Bounty from a parcel
	 * 
	 * @param pc
	 */
	Bounty(Parcel pc) {
		String firebaseURL = pc.readString();
		this.firebase = new Firebase(firebaseURL);
		// setParentIDs(firebaseURL);
		// setParentNames();
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.name = pc.readString();
		this.description = pc.readString();
		this.hourLimit = pc.readDouble();
		this.lineLimit = pc.readInt();
		this.points = pc.readInt();
	}

	public Bounty(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		// setParentIDs(firebaseURL);
		// setParentNames();
		this.firebase.addChildEventListener(new ChildrenListener(this));
	}

	public void setParentNames(String projName, String msName, String taskName) {
		this.parentProjectName = projName;
		this.parentMilestoneName = msName;
		this.parentTaskName = taskName;
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
		dest.writeDouble(this.hourLimit);
		dest.writeInt(this.lineLimit);
		dest.writeInt(this.points);
	}

	/**
	 * Gets the name of a Task
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Determines if a task is equal to another task
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Task) {
			return ((Task) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	};

	/**
	 * Gets the description of the task
	 * 
	 * @return the description of the task
	 */
	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the parent milestone id
	 * 
	 * @return the parent milestone id
	 */
	public String getParentMilestoneID() {
		return this.parentMilestoneFB.getKey();
	}

	/**
	 * Returns the parent project id
	 * 
	 * @return the parent project id
	 */
	public String getParentProjectID() {
		return this.parentProjectFB.getKey();
	}

	/**
	 * Returns the parent milestone Name
	 * 
	 * @return the parent milestone Name
	 */
	public String getParentMilestoneName() {
		return this.parentMilestoneName;
	}

	/**
	 * Returns the parent project Name
	 * 
	 * @return the parent project Name
	 */
	public String getParentProjectName() {
		return this.parentProjectName;
	}

	/**
	 * Returns the parent task id
	 * 
	 * @return the parent task id
	 */
	public String getParentTaskID() {
		return this.parentTaskFB.getKey();
	}

	/**
	 * Returns the parent task Name
	 * 
	 * @return the parent task Name
	 */
	public String getParentTaskName() {
		return this.parentTaskName;
	}

	/**
	 * Task listener
	 */
	class ChildrenListener implements ChildEventListener {
		/**
		 * Task Object
		 */
		Bounty bounty;

		/**
		 * Creates a new ChildrenListener
		 * 
		 * @param t
		 */
		public ChildrenListener(Bounty b) {
			this.bounty = b;
		}

		public void onCancelled(FirebaseError arg0) {
			// Do nothing
		}

		/**
		 * Fills in the new point's properties.
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("claimed")) {
				this.bounty.claimed = arg0.getValue().toString();
			} else if (arg0.getKey().equals("description")) {
				this.bounty.description = arg0.getValue().toString();
			} else if (arg0.getKey().equals("due_date")) {
				this.bounty.dueDate = arg0.getValue().toString();
			} else if (arg0.getKey().equals("hour_limit")) {
				this.bounty.hourLimit = (double) arg0.getValue();
			} else if (arg0.getKey().equals("line_limit")) {
				this.bounty.lineLimit = (int) arg0.getValue();
			} else if (arg0.getKey().equals("name")) {
				this.bounty.name = arg0.getValue().toString();
			} else if (arg0.getKey().equals("points")) {
				this.bounty.points = (int) arg0.getValue();
			}
		}

		/**
		 * This will be called when the points data in Firebased is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("claimed")) {
				this.bounty.claimed = arg0.getValue().toString();
			} else if (arg0.getKey().equals("points")) {
				this.bounty.points = (int) arg0.getValue();
			}
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
	}

	/**
	 * Compares this bounty with the other given bounty.
	 * 
	 * @param p
	 * @return
	 */
	public int compareToIgnoreCase(Task p) {
		return GeneralAlgorithms.compareToIgnoreCase(this.getName(),
				p.getName());
	}

	/**
	 * Updates the points assigned to this task
	 */
	public void setPoints(int newPoints) {
		this.points = newPoints;
		this.firebase.child(Task.pointsName).setValue(this.points);
		if (this.listViewCallback != null) {
			this.listViewCallback.onChange();
		}
	}

	/**
	 * retrieves the points value for this task
	 */
	public int getPoints() {
		return this.points;
	}

}
