package rhit.jrProj.henry.firebase;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Task implements Parcelable {

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
	 * The name of the user assigned to this task
	 */
	private String assignedUserName = Task.getDefaultAssignedUserName();
	
	public static String getDefaultAssignedUserName() {
		return "default";
	}

	/**
	 * The status of the task.
	 */
	private String status;

	/**
	 * The number of hours logged for this task
	 */
	private double hoursComplete;

	/**
	 * The total number of hours currently estimated for this task
	 */
	private double hoursEstimatedCurrent;

	/**
	 * The total number of hours originally estimated for this task
	 */
	private double hoursEstimatedOriginal;
	
	/**
	 * The number of lines of code added to this task
	 */
	private int addedLines;

	/**
	 * The number of lines of code removed from this task
	 */
	private int removedLines;

	/**
	 * The total number of lines of code for this task
	 */
	private int totalLines;

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
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.name = pc.readString();
		this.description = pc.readString();
		this.assignedUserId = pc.readString();
		this.status = pc.readString();
		this.addedLines = pc.readInt();
		this.removedLines = pc.readInt();
		this.totalLines = pc.readInt();
	}

	public Task(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		this.firebase.addChildEventListener(new ChildrenListener(this));
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
		dest.writeString(this.status);
		dest.writeInt(this.addedLines);
		dest.writeInt(this.removedLines);
		dest.writeInt(this.totalLines);
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

	public String getAssignedUserId() {
		return this.assignedUserId;
	}

	/**
	 * Returns the name of the user assigned to this task
	 * 
	 * @return the name of the user assigned to this task
	 */
	public String getAssignedUserName() {
		return this.assignedUserName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the number of hours logged for this task
	 * 
	 * @return the number of hours logged for this task
	 */
	public double getHoursSpent() {
		return this.hoursComplete;
	}

	/**
	 * Returns the number of hours currently estimated for this task
	 * 
	 * @return the number of hours currently estimated for this task
	 */
	public double getCurrentHoursEstimate() {
		return this.hoursEstimatedCurrent;
	}

	public void setAssignedUserName(String username) {
		this.assignedUserName = username;

	}

	public void setAssignedUserId(String userId) {
		this.assignedUserId = userId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Returns the status of this task
	 * 
	 * @return the status of this task
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * Returns the number of lines of code added to this task
	 * 
	 * @return the number of lines of code added to this task
	 */
	public int getAddedLines() {
		return this.addedLines;
	}

	/**
	 * Returns the number of lines of code removed from this task
	 * 
	 * @return the number of lines of code removed from this task
	 */
	public int getRemovedLines() {
		return this.removedLines;
	}

	/**
	 * Returns the number of lines of code for this task
	 * 
	 * @return the number of lines of code for this task
	 */
	public int getTotalLines() {
		return this.totalLines;
	}

	public ListChangeNotifier<Task> getListChangeNotifier() {
		return this.listViewCallback;
	}

	/**
	 * Returns the number of hours originally estimated for this task
	 * 
	 * @return the number of hours originally estimated for this task
	 */
	public double getOriginalHoursEstimate() {
		return this.hoursEstimatedOriginal;
	}

	public void updateStatus(String taskStatus) {
		this.status = taskStatus;
		this.firebase.child("status").setValue(taskStatus);
		this.firebase.child("is_completed").setValue(Boolean.valueOf(taskStatus.equals("Closed")));
	}

	/**
	 * Task listener
	 */
	class ChildrenListener implements ChildEventListener {
		/**
		 * Task Object
		 */
		private Task task;

		/**
		 * Creates a new ChildrenListener
		 * 
		 * @param t
		 */
		public ChildrenListener(Task t) {
			this.task = t;
		}

		public void onCancelled(FirebaseError arg0) {
			// Do nothing
		}

		/**
		 * Fills in the new milestone's properties including the milestone name,
		 * description and list of tasks for that milestone
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (arg0.getName().equals("name")) {
				this.task.setName(arg0.getValue().toString());
				if (this.task.getListChangeNotifier() != null) {
					this.task.getListChangeNotifier().onChange();
				}
			} else if (arg0.getName().equals("description")) {
				this.task.setDescription(arg0.getValue().toString());
			} else if (arg0.getName().equals("assignedTo")) {
				this.task.setAssignedUserId(arg0.getValue().toString());
				this.getUserNameFromId(this.task.getAssignedUserId());
			} else if (arg0.getName().equals("status")) {
				this.task.setStatus(arg0.getValue().toString());
			} else if (arg0.getName().equals("added_lines_of_code")) {
				this.task.addedLines = arg0.getValue(Integer.class);
			} else if (arg0.getName().equals("removed_lines_of_code")) {
				this.task.removedLines = arg0.getValue(Integer.class);
			} else if (arg0.getName().equals("total_lines_of_code")) {
				this.task.totalLines = arg0.getValue(Integer.class);
			}
		}
			
		private void getUserNameFromId(String id) {
			Firebase userBase = firebase.getRoot().child("users").child(id).child("name");
			userBase.addValueEventListener(new ValueEventListener() {
				
				  public void onDataChange(DataSnapshot snapshot) {
					  task.setAssignedUserName((String) snapshot.getValue());
				  }

				  public void onCancelled(FirebaseError error) { }

			});
		}

		/**
		 * This will be called when the milestone data in Firebased is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getName().equals("added_lines_of_code")) {
				this.task.addedLines = arg0.getValue(Integer.class).intValue();
			} else if (arg0.getName().equals("removed_lines_of_code")) {
				this.task.removedLines = arg0.getValue(Integer.class).intValue();
			} else if (arg0.getName().equals("total_lines_of_code")) {
				this.task.totalLines = arg0.getValue(Integer.class).intValue();
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

}
