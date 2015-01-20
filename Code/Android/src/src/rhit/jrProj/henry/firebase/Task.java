package rhit.jrProj.henry.firebase;

import rhit.jrProj.henry.MainActivity;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Task implements Parcelable {
	public static int MAX_POINTS=100;
	public static int MIN_POINTS=0;

	/**
	 * A reference to firebase to keep the data up to date.
	 */
	Firebase firebase;

	/**
	 * The task's name
	 */
	String name = "No name assigned";

	/**
	 * A description of the task
	 */
	String description = "No description assigned";

	/**
	 * A list of the user ids of the users assigned to the task
	 */
	String assignedUserId = "No User ID assigned";

	/**
	 * The name of the user assigned to this task
	 */
	String assignedUserName = Task.getDefaultAssignedUserName();

	public static String getDefaultAssignedUserName() {
		return "default";
	}

	/**
	 * The status of the task.
	 */
	String status = "No Status Assigned";

	/**
	 * The number of hours logged for this task
	 */
	double hoursComplete = 0;

	/**
	 * The total number of hours currently estimated for this task
	 */
	private double hoursEstimatedCurrent = 0;

	/**
	 * The total number of hours originally estimated for this task
	 */
	private double hoursEstimatedOriginal = 0;

	/**
	 * The number of lines of code added to this task
	 */
	int addedLines = 0;

	/**
	 * The number of lines of code removed from this task
	 */
	int removedLines = 0;

	/**
	 * The total number of lines of code for this task
	 */
	int totalLines = 0;

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
	 * The task's parent project ID
	 */
	private Firebase parentProjectFB;
	/**
	 * The task's parent milestone ID
	 */
	private Firebase parentMilestoneFB;
	/**
	 * The task's parent project Name
	 */
	private String parentProjectName;
	/**
	 * The task's parent milestone Name
	 */
	private String parentMilestoneName;
	/**
	 * Gamification points
	 */
	private int points;
	/**
	 * This field is because the name of the field in firebase hasn't been decided yet
	 */
	public static String pointsName= "points";
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
		String firebaseURL=pc.readString();
		this.firebase = new Firebase(firebaseURL);
//		setParentIDs(firebaseURL);
//		setParentNames();
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
//		setParentIDs(firebaseURL);
//		setParentNames();
		this.firebase.addChildEventListener(new ChildrenListener(this));
	}
//	private void setParentIDs(String firebaseURL){
//		String proj="/projects/";
//		String ms= "/milestones/";
//		String task="/tasks/";
//		int indexProj=firebaseURL.indexOf(proj);
//		int indexMS=firebaseURL.indexOf(ms);
//		int indexTask=firebaseURL.indexOf(task);
//		if (indexProj!=-1 && indexMS!=-1 && indexTask!=-1){
//			this.parentProjectFB=new Firebase(firebaseURL.substring(0, indexMS));
//			this.parentMilestoneFB=new Firebase(firebaseURL.substring(0, indexTask));
//		}
//	}
//	
//	private void setParentNames(){
//		
//		this.parentProjectFB.child("name").addListenerForSingleValueEvent(new ProjectNameListener(this));
//		this.parentMilestoneFB.child("name").addListenerForSingleValueEvent(new MilestoneNameListener(this));
//		
//	}
	public void setParentNames(String projName, String msName){
		this.parentProjectName=projName;
		this.parentMilestoneName=msName;
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
	 * Returns the parent milestone id
	 * @return the parent milestone id
	 */
	public String getParentMilestoneID(){
		return this.parentMilestoneFB.getKey();
	}
	
	/**
	 * Returns the parent project id
	 * @return the parent project id
	 */
	public String getParentProjectID(){
		return this.parentProjectFB.getKey();
	}
	/**
	 * Returns the parent milestone Name
	 * @return the parent milestone Name
	 */
	public String getParentMilestoneName(){
		return this.parentMilestoneName;
	}
		
	/**
	 * Returns the parent project Name
	 * @return the parent project Name
	 */
	public String getParentProjectName(){
		return this.parentProjectName;
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
	/**
	 * gets the ListChangeNotifier (aka ListViewCallback) for a task
	 * 
	 * @return
	 */
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

	/**
	 * Updates the status on the task view
	 */
	public void updateStatus(String taskStatus) {
		this.status = taskStatus;
		this.firebase.child("status").setValue(taskStatus);
		this.firebase.child("is_completed").setValue(
				Boolean.valueOf(taskStatus.equals(Enums.CLOSED)));
		if (this.listViewCallback!=null){
			this.listViewCallback.onChange();
		}
	}

	/**
	 * Updates the assigned developer on a task
	 */
	public void updateAssignee(Member member) {
		this.assignedUserId = member.getKey();
		this.assignedUserName = member.toString();
		this.firebase.child("assignedTo").setValue(member.getKey());
		if (this.listViewCallback!=null){
			this.listViewCallback.onChange();
		}
	}
	class ProjectNameListener implements ValueEventListener{
		Task t;
		public ProjectNameListener(Task t){
			this.t=t;
		}
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		        this.t.parentProjectName=snapshot.getValue(String.class);
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		    }
		
	}
	class MilestoneNameListener implements ValueEventListener{
		Task t;
		public MilestoneNameListener(Task t){
			this.t=t;
		}
		    @Override
		    public void onDataChange(DataSnapshot snapshot) {
		        this.t.parentMilestoneName=snapshot.getValue(String.class);
		    }
		    @Override
		    public void onCancelled(FirebaseError firebaseError) {
		    }
		
	}

	/**
	 * Task listener
	 */
	class ChildrenListener implements ChildEventListener {
		/**
		 * Task Object
		 */
		Task task;

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
			if (arg0.getKey().equals("name")) {
				this.task.name = arg0.getValue().toString();
				if (this.task.getListChangeNotifier() != null) {
					this.task.getListChangeNotifier().onChange();
				}
			} else if (arg0.getKey().equals("description")) {
				this.task.description = arg0.getValue().toString();
			} else if (arg0.getKey().equals("assignedTo")) {
				this.task.assignedUserId = arg0.getValue().toString();
				this.getUserNameFromId(this.task.getAssignedUserId());
			} else if (arg0.getKey().equals("status")) {
				this.task.status = arg0.getValue().toString();
			} else if (arg0.getKey().equals("added_lines_of_code")) {
				this.task.addedLines = arg0.getValue(Integer.class).intValue();
			} else if (arg0.getKey().equals("removed_lines_of_code")) {
				this.task.removedLines = arg0.getValue(Integer.class)
						.intValue();
			} else if (arg0.getKey().equals("total_lines_of_code")) {
				this.task.totalLines = arg0.getValue(Integer.class).intValue();
			}else if (arg0.getKey().equals("original_hour_estimate")) {
				this.task.hoursEstimatedOriginal = arg0.getValue(Integer.class).intValue();
			}else if (arg0.getKey().equals("total_hours")) {
				this.task.hoursComplete = arg0.getValue(Integer.class).intValue();
			}else if (arg0.getKey().equals(Task.pointsName)) {
				this.task.points = arg0.getValue(Integer.class).intValue();
			}
			
		}

		public void getUserNameFromId(String id) {
			Firebase userBase = Task.this.firebase.getRoot().child("users")
					.child(id).child("name");
			userBase.addValueEventListener(new ValueEventListener() {

				public void onDataChange(DataSnapshot snapshot) {
					ChildrenListener.this.task.assignedUserName = snapshot
							.getValue(String.class);
				}

				public void onCancelled(FirebaseError error) {
					// Do nothing.
				}

			});
		}

		/**
		 * This will be called when the milestone data in Firebased is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("added_lines_of_code")) {
				this.task.addedLines = arg0.getValue(Integer.class).intValue();
			} else if (arg0.getKey().equals("removed_lines_of_code")) {
				this.task.removedLines = arg0.getValue(Integer.class)
						.intValue();
			} else if (arg0.getKey().equals("total_lines_of_code")) {
				this.task.totalLines = arg0.getValue(Integer.class).intValue();
			}else if (arg0.getKey().equals("original_hour_estimate")) {
				this.task.hoursEstimatedOriginal = arg0.getValue(Integer.class).intValue();
			}else if (arg0.getKey().equals("total_hours")) {
				this.task.hoursComplete = arg0.getValue(Integer.class).intValue();
			}else if (arg0.getKey().equals(Task.pointsName)) {
				this.task.points = arg0.getValue(Integer.class).intValue();
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
	 *  Compares this project with the other given project. This implementation treats lower 
	 *  case letters the same as upper case letters. Also treats numbers differently,
	 *   i.e. puts 10 after 9 instead of after 1
	 * @param p
	 * @return
	 */
	public int compareToIgnoreCase(Task p){
		return GeneralAlgorithms.compareToIgnoreCase(this.getName(), p.getName());
	}
	/**
	 * Updates the points assigned to this task
	 */
	public void setPoints(int newPoints){
		this.points=newPoints;
		this.firebase.child(Task.pointsName).setValue(this.points);
		if (this.listViewCallback!=null){
			this.listViewCallback.onChange();
		}
	}
	/**
	 * retrieves the points value for this task
	 */
	public int getPoints(){
		return this.points;
	}
	
}
