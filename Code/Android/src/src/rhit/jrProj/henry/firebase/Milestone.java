package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.List;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.User.ChildrenListener;
import rhit.jrProj.henry.firebase.User.GrandChildrenListener;
import rhit.jrProj.henry.helpers.GraphHelper;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Milestone implements Parcelable {

	/**
	 * A reference to Firebase to keep the data up to date.
	 */
	private Firebase firebase;

	/**
	 * A List of tasks that are contained within the Milestone
	 */
	private ArrayList<Task> tasks = new ArrayList<Task>();

	/**
	 * The name of the milestone
	 */
	private String name = "No Name assigned";
	
	/**
	 * The due date of a milestone
	 */
	private String dueDate = "No due date assigned";
	
	/**
	 * The percentage of tasks completed for this milestone
	 */
	private int taskPercent = 0;
	

	/**
	 * A description of the work that needs to happen in this milestone.
	 */
	private String description = "No description assigned";

	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * Milestone that this object has been updated.
	 */
	private ListChangeNotifier<Milestone> listViewCallback;
	
	/**
	 * A string of the milestone's firebase id.
	 */
	private String milestoneId = "No ID assigned";

	private ListChangeNotifier<Task> taskListViewCallback;
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
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.firebase.child("tasks").addChildEventListener(
				new GrandChildrenListener(this));
		this.milestoneId = firebaseUrl
				.substring(firebaseUrl.lastIndexOf('/') + 1);
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 * 
	 * @param in
	 */
	public Milestone(Parcel in) {
		this.firebase = new Firebase(in.readString());
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.firebase.child("tasks").addChildEventListener(
				new GrandChildrenListener(this));
		this.setName(in.readString());
		this.setDueDate(in.readString());
		this.setDescription(in.readString());
		this.setTaskPercent(in.readInt());
		in.readTypedList(this.tasks, Task.CREATOR);
	}

	/**
	 * 
	 * Sets a new list changed notifier
	 * 
	 * @param lcn
	 */
	public void setListChangeNotifier(ListChangeNotifier<Milestone> lcn) {
		this.setListViewCallback(lcn);
	}

	/**
	 * Returns the name of the milestone
	 */
	@Override
	public String toString() {
		return this.getName();
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
		dest.writeString(this.getName());
		dest.writeString(this.getDueDate());
		dest.writeString(this.getDescription());
		dest.writeInt(this.getTaskPercent());
		dest.writeTypedList(this.tasks);
	}
	
	public void replaceName(String name) {
		this.name = name;
	}

	/**
	 * Replaces the description of the milestone
	 * 
	 * @param description
	 */
	public void replaceDescription(String description) {
		this.description = description;

	}
	
	/**
	 * Returns the due date of the milestone
	 * @return the due date of the milestone
	 */
	public String getDueDate() {
		return this.dueDate;
	}
	
	/**
	 * Sets the due date of the milestone
	 * @param dueDate the due date of the milestone
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Gets the description of the milestone
	 * 
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
	
	/**
	 * Sets the percentage of tasks completed in this milestone
	 * @param taskPercent the percentage of tasks completed in this milestone
	 */
	public void setTaskPercent(int taskPercent) {
		this.taskPercent = taskPercent;
	}
	
	/**
	 * Returns the milestone's firebase ID.
	 */
	public String getMilestoneId() {
		return this.milestoneId;
	}
	
	/**
	 * sets a milestone description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * gets the name of a milestone
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the name of the milestone
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * gets the ListViewCallback for a milestone
	 * 
	 * @return
	 */
	public ListChangeNotifier<Milestone> getListViewCallback() {
		return this.listViewCallback;
	}
	public ListChangeNotifier<Milestone> getListChangeNotifier(){
		return this.listViewCallback;
	}

	/**
	 * Gets the TaskListViewCallback for a task
	 * 
	 * @return
	 */
	public ListChangeNotifier<Task> getTaskListViewCallback() {
		return this.taskListViewCallback;
	}
	
	/**
	 * Sets the ListViewCallback on a milestone
	 * 
	 * @param listViewCallback
	 */
	public void setListViewCallback(
			ListChangeNotifier<Milestone> listViewCallback) {
		this.listViewCallback = listViewCallback;
	}
	
	public GraphHelper.PieChartInfo getLocAddedInfo() {
		GraphHelper.PieChartInfo chartInfo = new GraphHelper.PieChartInfo();

		for (Task task : this.getTasks()) {
			String userName = task.getAssignedUserName();
			if (!userName.equals(Task.getDefaultAssignedUserName())) {

				if (chartInfo.getKeys().contains(userName)) {
					chartInfo.addValueToKey(userName, task.getAddedLines());
				} else {
					chartInfo.addValueKey(task.getAddedLines(),
							task.getAssignedUserName());
				}

			}
		}

		return chartInfo;
	}
	
	public GraphHelper.StackedBarChartInfo getLocTotalInfo() {
		GraphHelper.StackedBarChartInfo chartInfo = new GraphHelper.StackedBarChartInfo();
		chartInfo.addKey("Added");
		chartInfo.addKey("Removed");
		chartInfo.addKey("Net");

		for (Task task : this.getTasks()) {
			String userName = task.getAssignedUserName();
			if (!userName.equals(Task.getDefaultAssignedUserName())) {

				if (chartInfo.getBarLabels().contains(userName)) {
					chartInfo.addValueToKeyBarLabel(chartInfo.getKeys().get(0), userName, task.getAddedLines());
					chartInfo.addValueToKeyBarLabel(chartInfo.getKeys().get(1), userName, -1 * task.getRemovedLines());
					chartInfo.addValueToKeyBarLabel(chartInfo.getKeys().get(2), userName, task.getAddedLines() - task.getRemovedLines());
				} else {
					List<Double> valueSeries = new ArrayList<Double>();
					valueSeries.add(0.0 + task.getAddedLines());
					valueSeries.add(0.0 - task.getRemovedLines());
					valueSeries.add(0.0 + task.getAddedLines() - task.getRemovedLines());
					chartInfo.addValueSeriesBarLabel(valueSeries, userName);
				}

			}
		}

		return chartInfo;
	}

	/**
	 * Milestone listener
	 * 
	 */
	class ChildrenListener implements ChildEventListener {
		private Milestone milestone;

		public ChildrenListener(Milestone milestone) {
			this.milestone = milestone;
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
				this.milestone.setName(arg0.getValue(String.class));
				if (this.milestone.getListViewCallback() != null) {
					this.milestone.getListViewCallback().onChange();
				}
			} else if (arg0.getName().equals("description")) {
				this.milestone.setDescription(arg0.getValue(String.class));
			} else if (arg0.getName().equals("due_date")) { 
				this.milestone.setDueDate(arg0.getValue(String.class));
			}  else if (arg0.getName().equals("task_percent")) {
				this.milestone.setTaskPercent(arg0.getValue(Integer.class));
			} else if (arg0.getName().equals("tasks")) {
				for (DataSnapshot child : arg0.getChildren()) {
					Task t = new Task(child.getRef().toString());
					if (!this.milestone.tasks.contains(t)) {
						this.milestone.tasks.add(t);
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
			// nothing
		}

		/**
		 * Do nothing
		 */
		public void onChildRemoved(DataSnapshot arg0) {
			// nothing
		}
	}

	/**
	 * Listener for Tasks
	 */
	class GrandChildrenListener implements ChildEventListener {
		private Milestone milestone;

		public GrandChildrenListener(Milestone milestone) {
			this.milestone = milestone;
		}

		/**
		 * Do nothing
		 */
		public void onCancelled(FirebaseError arg0) {
			// nothing to do
		}

		/**
		 * Fills in the new milestone's properties including the milestone name,
		 * description and list of tasks for that milestone
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			Task t = new Task(arg0.getRef().toString());
			if (!this.milestone.getTasks().contains(t)) {
				this.milestone.getTasks().add(t);
			}
			t.setListChangeNotifier(this.milestone.getTaskListViewCallback());
			if (this.milestone.listViewCallback != null) {
				this.milestone.listViewCallback.onChange();
			}
		}

		/**
		 * This will be called when the milestone data in Firebased is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			// All changes done within Task

		}

		/**
		 * Might do something here for the tablet
		 */
		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// Nada- yet
		}

		/**
		 * Removes a task from a milestone
		 */
		public void onChildRemoved(DataSnapshot arg0) {
			Task t = new Task(arg0.getRef().toString());
			this.milestone.getTasks().remove(t);
			if (this.milestone.listViewCallback != null) {
				this.milestone.listViewCallback.onChange();
			}
		}
	}
	public int compareToIgnoreCase(Milestone p){
		return compareToICHelper(this.getName(), p.getName());
	}
	private int compareToICHelper(String s1, String s2){
		if (s1.equalsIgnoreCase(s2)) return 0;
		else{
			int i= 0;
			int j= 0;
			int s1len=s1.length();
			int s2len=s2.length();
			while (i<s1len && j<s2len){
				char c1= s1.charAt(i);
				char c2= s2.charAt(j);
				char [] sp1 = new char[s1len];
				char [] sp2 = new char[s2len];
				int loc1=0;
				int loc2=0;
				while (Character.isDigit(c1)==Character.isDigit(sp1[0])){
					sp1[loc1++]=c1;
					i++;
					if (i <s1len){
						c1=s1.charAt(i);
					} else{
						break;
					}
				}
				while (Character.isDigit(c2)==Character.isDigit(sp2[0])){
					sp2[loc2++]=c2;
					j++;
					if (j <s2len){
						c2=s2.charAt(j);
					} else{
						break;
					}
				}
				String str1=new String(sp1);
				String str2=new String(sp2);
				int result;
				if (Character.isDigit(sp1[0]) && Character.isDigit(sp2[0])){
					Integer num1= new Integer(Integer.parseInt(str1.trim()));
					Integer num2= new Integer(Integer.parseInt(str2.trim()));
					result=num1.compareTo(num2);
				}
				else{
					result=str1.compareToIgnoreCase(str2);
				}
				if (result!=0){
					return result;
				}
				
			}
		return s1len-s2len;
		}
		
	}
}
