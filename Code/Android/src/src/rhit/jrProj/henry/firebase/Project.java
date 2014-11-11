package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Project implements Parcelable {

	/**
	 * A reference to Firebase to keep the data up to date.
	 */
	private Firebase firebase;

	/**
	 * A List of milestones that are contained within the project
	 */
	private ArrayList<Milestone> milestones = new ArrayList<Milestone>();

	/**
	 * The project's name
	 */
	private String name = "No name assigned";
	/**
	 * The due date of the project
	 */
	private String dueDate = "No Due Date";

	/**
	 * The members that are working on the project
	 */
	private Map<Member, Enums.Role> members = new Map<Member, Enums.Role>();

	/**
	 * A description of the project.
	 */
	private String description = "No Description Assigned";

	/**
	 * The percentage of hours complete for this project
	 */
	private int hoursPercent = 0;

	/**
	 * The percentage of tasks complete for this project
	 */
	private int tasksPercent = 0;

	/**
	 * The percentage of milestones compelte for this project
	 */
	private int milestonesPercent = 0;
	/**
	 * The project id for this project
	 */
	private String projectId = "No ProjectID Assigned";

	/**
	 * Do we need to do anything with the backlog?
	 */
	// private Backlog;

	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * project that this object has been updated.
	 */
	private ListChangeNotifier<Project> listViewCallback;

	private ListChangeNotifier<Milestone> milestoneListViewCallback;
	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {

		public Project createFromParcel(Parcel pc) {
			return new Project(pc);
		}

		public Project[] newArray(int size) {
			return new Project[size];
		}
	};

	/**
	 * 
	 * This constructor builds a new project that updates its self from
	 * firebase.
	 * 
	 * @param firebaseUrl
	 *            i.e. https://henry371.firebaseio.com/projects/-
	 *            JYcg488tAYS5rJJT4Kh
	 */
	public Project(String firebaseUrl) {
		this.firebase = new Firebase(firebaseUrl);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.firebase.child("milestones").addChildEventListener(
				new GrandChildrenListener(this));
		this.projectId = firebaseUrl
				.substring(firebaseUrl.lastIndexOf('/') + 1);
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 * 
	 * @param in
	 */
	Project(Parcel in) {
		String firebaseURL = in.readString();
		int lastindex = firebaseURL.lastIndexOf("/");
		this.projectId = firebaseURL.substring(lastindex + 1);
		this.firebase = new Firebase(firebaseURL);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.firebase.child("milestones").addChildEventListener(
				new GrandChildrenListener(this));
		this.name = in.readString();
		this.dueDate = in.readString();
		this.description = in.readString();
		this.hoursPercent = in.readInt();
		this.tasksPercent = in.readInt();
		this.milestonesPercent = in.readInt();
		this.members = new Map<Member, Enums.Role>(); // How to transport?
		//TODO Transport members
		in.readTypedList(this.milestones, Milestone.CREATOR);
	}

	/**
	 * Gets an ArrayList of milestones associated with this project.
	 */
	public ArrayList<Milestone> getMilestones() {
		return this.milestones;
	}

	/**
	 * 
	 * Sets what should be calledback to when the project's data is modified.
	 * 
	 * @param lcn
	 */
	public void setListChangeNotifier(ListChangeNotifier<Project> lcn) {
		this.listViewCallback = lcn;
	}
	/**
	 * gets the ListChangeNotifier (aka ListViewCallback) for a project
	 * 
	 * @return
	 */
	public ListChangeNotifier<Project> getListChangeNotifier(){
		return this.listViewCallback;
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * If both of the firebase URLs are the same, then they are referencing the
	 * same project.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Project) {
			return ((Project) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	}

	public int describeContents() {
		// Do nothing.
		return 0;
	}

	/**
	 * Passes the Firebase URL, the name of the project, the description of the
	 * project and the milestones
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.dueDate);
		dest.writeString(this.description);
		dest.writeInt(this.hoursPercent);
		dest.writeInt(this.tasksPercent);
		dest.writeInt(this.milestonesPercent);
		dest.writeTypedList(this.milestones);
		// TODO Members?
		// number for the loop and then loop through it all?
	}

	/**
	 * Returns the name of this project
	 * 
	 * @return the name of this project
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the description of the project
	 * 
	 * @return
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Returns the due date of the project
	 * 
	 * @return the due date of the project
	 */
	public String getDueDate() {
		return this.dueDate;
	}

	/**
	 * Returns the percentage of hours complete for this project
	 * 
	 * @return the percentage of hours complete for this project
	 */
	public int getHoursPercent() {
		return this.hoursPercent;
	}

	/**
	 * Returns the percentage of tasks complete for this project
	 * 
	 * @return the percentage of tasks complete for this project
	 */
	public int getTasksPercent() {
		return this.tasksPercent;
	}

	/**
	 * Returns the percentage of milestones complete for this project
	 * 
	 * @return the percentage of milestones complete for this project
	 */
	public int getMilestonesPercent() {
		return this.milestonesPercent;
	}

	/**
	 * Returns the project id for this project.
	 * 
	 * @return the project id
	 * 
	 */
	public String getProjectId() {
		return this.projectId;
	}

	/**
	 * Returns the list of members in a project.
	 * 
	 * @return
	 */
	public Map<Member, Enums.Role> getMembers() {
		return this.members;
	}
	
	/**
	 * Child Listener to handle the Project & its changes
	 * 
	 */
	class ChildrenListener implements ChildEventListener {
		Project project;

		public ChildrenListener(Project project) {
			this.project = project;
		}

		/**
		 * Nothing to do here
		 */
		public void onCancelled(FirebaseError arg0) {
			// TODO Auto-generated method stub.
			// nothing to do here
		}

		/**
		 * Method that is called when a project is added to the list
		 * 
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (arg0.getName().equals("name")) {
				this.project.name = arg0.getValue(String.class);
				if (this.project.listViewCallback != null) {
					this.project.listViewCallback.onChange();
				}
			} else if (arg0.getName().equals("description")) {
				this.project.description = arg0.getValue(String.class);
			} else if (arg0.getName().equals("due_date")) {
				this.project.dueDate = arg0.getValue(String.class);
			} else if (arg0.getName().equals("hours_percent")) {
				this.project.hoursPercent = arg0.getValue(Integer.class);
			} else if (arg0.getName().equals("task_percent")) {
				this.project.tasksPercent = arg0.getValue(Integer.class);
			} else if (arg0.getName().equals("milestone_percent")) {
				this.project.milestonesPercent = arg0.getValue(Integer.class);
			} else if (arg0.getName().equals("milestones")) {
				for (DataSnapshot child : arg0.getChildren()) {
					Milestone m = new Milestone(child.getRef().toString());
					if (!this.project.milestones.contains(m)) {
						this.project.milestones.add(m);
					}
				}
			} else if (arg0.getName().equals("members")) {
				for (DataSnapshot member : arg0.getChildren()) {
					Log.i("Member Url", arg0.getRef().getRoot().toString() + "/users/"+member.getName());
					Member toAdd = new Member(arg0.getRef().getRoot().toString() + "/users/"+member.getName());
					if (!this.project.members.containsKey(toAdd)) {
						try {
							this.project.members.put(
									toAdd,
									Enums.Role.valueOf(member.getValue(
											String.class).toLowerCase()));
						} catch (Exception e) {
							Log.i("FAILED",
									"Adding a role to a member failed: "
											+ e.getMessage());
						}
					}
				}
			}
		}

		/**
		 * Will be called when any project value is changed
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getName().equals("name")) {
				this.project.name = arg0.getValue(String.class);
				if (this.project.listViewCallback != null) {
					this.project.listViewCallback.onChange();
				}
			} else if (arg0.getName().equals("description")) {
				this.project.description = arg0.getValue(String.class);
			} else if (arg0.getName().equals("milestones")) {
				Log.i("Henry", "Milestone Changed!?!");
			}
		}

		/**
		 * Nothing to do here
		 */
		public void onChildMoved(DataSnapshot arg0, String arg1) {
			Log.i("Henry", "Something Moved!?!");
		}

		/**
		 * Until further notice from Mike: do nothing
		 */
		public void onChildRemoved(DataSnapshot arg0) {
			Log.i("Henry", arg0.getName());
		}
	}

	/**
	 * Listener for Tasks
	 * 
	 * @author johnsoaa
	 * 
	 */
	class GrandChildrenListener implements ChildEventListener {
		private Project project;

		public GrandChildrenListener(Project project) {
			this.project = project;
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
			Milestone m = new Milestone(arg0.getRef().toString());
			if (!this.project.getMilestones().contains(m)) {
				this.project.getMilestones().add(m);
			}
			m.setListChangeNotifier(milestoneListViewCallback);
			if (this.project.listViewCallback != null) {
				this.project.listViewCallback.onChange();
			}
		}

		/**
		 * This will be called when the milestone data in Firebased is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			// All changes done within Milestone

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
			Milestone m = new Milestone(arg0.getRef().toString());
			this.project.getMilestones().remove(m);
			if (this.project.listViewCallback != null) {
				this.project.listViewCallback.onChange();
			}
		}
	}
	
	/**
	 *  Compares this project with the other given project. This implementation treats lower 
	 *  case letters the same as upper case letters. Also treats numbers differently,
	 *   i.e. puts 10 after 9 instead of after 1
	 * @param p
	 * @return
	 */
	public int compareToIgnoreCase(Project p){
		return compareToICHelper(this.getName(), p.getName());
	}
	/**
	 * A helper method for compareToIgnoreCase
	 * @param s1
	 * @param s2
	 * @return
	 */
	private int compareToICHelper(String s1, String s2){
		if (s1 == s2) return 0;
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