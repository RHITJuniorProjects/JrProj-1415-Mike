package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.TaskDetailFragment.Callbacks;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;
import rhit.jrProj.henry.helpers.HorizontalPicker;

public class Bounty {
	private String claimed = "None";
	private String description = "None";
	private String dueDate = "No Due Date";
	private int hourLimit = -1;
	private int lineLimit = -1;
	private String name;
	private int points = -1;
	public static String completionName="completion";
	private boolean isCompletion=false;
	//this field returns if this bounty is a completion bounty
	private boolean canChangePoints=false; 
	//this field is designed to make sure that the HorizontalPicker doesn't wipe out the value of points 
	//by setting it to zero before the value has been found
	Firebase firebase;
	private String id;
	private HorizontalPicker hp;
	
	

	
	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * task that this object has been updated.
	 */
	private ListChangeNotifier<Bounty> listViewCallback;
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
	 * The bounty's parent task
	 */
	private Task parentTask;
	
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
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.name = pc.readString();
		this.description = pc.readString();
		this.hourLimit = pc.readInt();
		this.lineLimit = pc.readInt();
		this.points = pc.readInt();
	}

	public Bounty(String firebaseURL, Task t) {
		this.firebase = new Firebase(firebaseURL);
		this.id=firebaseURL
				.substring(firebaseURL.lastIndexOf('/') + 1);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.parentTask=t;
	}
	/**
	 * Sets the parent names, allows for a title detailing the location
	 * @param projName
	 * @param msName
	 * @param taskName
	 */

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
	public void setListChangeNotifier(ListChangeNotifier<Bounty> lcn) {
		this.listViewCallback = lcn;
	}
	/**
	 * 
	 * gets the list changed notifier
	 * 
	 * @param lcn
	 */
	public ListChangeNotifier<Bounty> getListChangeNotifier() {
		return this.listViewCallback;
	}

	/**
	 * Passes the Firebase URL, the name of the task and the description of the
	 * task to the next view
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeInt(this.hourLimit);
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
	 * returns the user who's claimed the task
	 * @return 
	 */
	public String getClaimed(){
		return this.claimed;
	}
	/**
	 * return the due date field
	 * @return
	 */
	public String getDueDate(){
		return this.dueDate;
	}
	/**
	 * return the hour limit in int form 
	 * "None"=-1
	 * @return
	 */
	public int getHourLimit(){
		return this.hourLimit;
	}
	/**
	 * return the line limit in int form
	 * "None"=-1
	 * @return
	 */
	public int getLineLimit(){
		return this.lineLimit;
	}
	/**
	 * Sets the user who has claimed the bounty
	 * @param s
	 */
	public void setClaimed(String s){
		this.claimed=s;
	}
	/**
	 * sets the due date
	 * @param s
	 */
	public void setDueDate(String s){
		this.dueDate=s;
	}
	/**
	 * sets the hour limit using int form
	 * @param s
	 */
	public void setHourLimit(int s){
		this.hourLimit=s;
	}
	/**
	 * sets the line limit using int form
	 * @param s
	 */
	public void setLineLimit(int s){
		this.lineLimit=s;
	}
	/**
	 * returns the bounty's id
	 * @return
	 */
	public String getID(){
		return this.id;
	}
	/**
	 * sets the parent task of this bounty to the specified task
	 * @param t
	 */
	public void setParentTask(Task t){
		this.parentTask=t;
	}
	public boolean getCanChangePoints(){
		return this.canChangePoints;
	}
	/**
	 * returns the due date as a formatted string
	 * @return
	 */
	public String getDueDateFormatted(){
		return GeneralAlgorithms.getDueDateFormatted(this.getDueDate());
	}
	/**
	 * the parent task has a points field as well, so therefore we need to set that field as well,
	 * but only if we have already found the points value 
	 * and thus won't wipe out the points value
	 */
	public void setParentTaskPoints(){
		if(this.canChangePoints){
		this.parentTask.setPoints(this.points);
		}
	}
	/**
	 * returns true if this is a completion bounty, false otherwise
	 * @return
	 */
	public boolean isCompletion(){
		return this.isCompletion;
	}

	/**
	 * Updates the points assigned to this task
	 */
	public void setPoints(int newPoints) {
		if (this.canChangePoints){
			this.points = newPoints;
			this.firebase.child("points").setValue(this.points);
			if (this.listViewCallback != null) {
				this.listViewCallback.onChange();
			}
		}
		
	}

	/**
	 * retrieves the points value for this task
	 */
	public int getPoints() {
		return this.points;
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
				this.bounty.hourLimit =this.bounty.convertLimitFromFirebaseForm(arg0.getValue());
			} else if (arg0.getKey().equals("line_limit")) {
				this.bounty.lineLimit = this.bounty.convertLimitFromFirebaseForm(arg0.getValue());
			} else if (arg0.getKey().equals("name")) {
				if (arg0.getValue(String.class).equals(Bounty.completionName)){
					this.bounty.parentTask.setCompletionBounty(this.bounty);
						this.bounty.isCompletion=true;
						this.bounty.parentTask.setPoints(this.bounty.points);
				}
				this.bounty.name = arg0.getValue().toString();
			} else if (arg0.getKey().equals("points")) {
				this.bounty.points = (int)arg0.getValue(int.class);
				//because we have now found the points, we can allow the task to change the points,
				//because now we won't wipe them out by setting to zero
				this.bounty.canChangePoints=true;
				this.bounty.parentTask.setPoints(this.bounty.points);
			}
		}

		/**
		 * This will be called when the points data in Firebase is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("claimed")) {
				this.bounty.claimed = arg0.getValue().toString();
			} else if (arg0.getKey().equals("description")) {
				this.bounty.description = arg0.getValue().toString();
			} else if (arg0.getKey().equals("due_date")) {
				this.bounty.dueDate = arg0.getValue().toString();
			} else if (arg0.getKey().equals("hour_limit")) {
				this.bounty.hourLimit =this.bounty.convertLimitFromFirebaseForm(arg0.getValue());
			} else if (arg0.getKey().equals("line_limit")) {
				this.bounty.lineLimit = this.bounty.convertLimitFromFirebaseForm(arg0.getValue());
			} else if (arg0.getKey().equals("name")) {
				this.bounty.name = arg0.getValue().toString();
			} else if (arg0.getKey().equals("points")) {
				this.bounty.points = (int) arg0.getValue(int.class);
				//because we have now found the points, we can allow the task to change the points,
				//because now we won't wipe them out by setting to zero
				this.bounty.canChangePoints=true;
				if (this.bounty.isCompletion()){
					this.bounty.parentTask.setPoints(this.bounty.points);
				}
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
	public int compareToIgnoreCase(Bounty p) {
		return GeneralAlgorithms.compareToIgnoreCase(this.getName(),
				p.getName());
	}
	
	


	public int convertLimitFromFirebaseForm(Object limitString){
		try{
			return (Integer) limitString;
		}catch(java.lang.ClassCastException e){
			if (limitString instanceof String && limitString.toString().equals("None")){
				return -1;
			}
			else{
				return -10;
			}
		}
	}
	public Object convertLimitToFirebaseForm(int limit){
		if (limit==-1){
			return "None";
		}
		else{
			return limit;
		}
	}
	

}
