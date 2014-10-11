package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Project implements Parcelable {

	/**
	 * A reference to firebase to keep the data up to date.
	 */
	private Firebase firebase;

	// Below are the data of the Project Object

	/**
	 * A List of milestones that are contained within the project
	 */
	ArrayList<Milestone> milestones = new ArrayList<Milestone>();

	/**
	 * The project's name
	 */
	String name;

	/**
	 * The members that are working on the project
	 */
	Map<User, Role> members = new HashMap<User, Role>();

	/**
	 * A description of the project.
	 */
	String description;

	/**
	 * Do we need to do anything with the backlog?
	 */
	// private Backlog;

	ListChangeNotifier lcn;

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
		this.firebase.addChildEventListener(new FirebaseProjectListener(this));
	}

	/**
	 * Creates a Project object from static data
	 * 
	 * @param tasks
	 * @param number
	 */
	public Project(ArrayList<Milestone> milestones, int number) {
		this.milestones = milestones;
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 *
	 * @param in
	 */
	Project(Parcel in) {
		this.firebase = new Firebase(in.readString());
		this.firebase.addChildEventListener(new FirebaseProjectListener(this));
		this.name = in.readString();
		this.description = in.readString();
		this.milestones = new ArrayList<Milestone>();
		// this.members = new HashMap<User, Role>(); // How to transport? Loop?
		in.readTypedList(this.milestones, Milestone.CREATOR);
	}

	public int getMilestoneNumber() {
		return 0;
	}

	/**
	 * Gets an ArrayList of milestones associated with this project.
	 */
	public ArrayList<Milestone> getMilestones() {
		return this.milestones;
	}

	public void setListChangeNotifier(ListChangeNotifier lcn) {
		this.lcn = lcn;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public int describeContents() {
		// Do nothing.
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeTypedList(this.milestones);
		// Members?
		// number for the loop and then loop through it all?
	}

	/**
	 * 
	 * This class is the listener for when data in firebase changes.
	 *
	 * @author rockwotj. Created Oct 10, 2014.
	 */
	class FirebaseProjectListener implements ChildEventListener {

		private Project project;

		public FirebaseProjectListener(Project project) {
			this.project = project;
		}

		public void onCancelled(FirebaseError arg0) {
			// TODO Auto-generated method stub.

		}

		public void onChildAdded(DataSnapshot arg0, String arg1) {
			Log.i("FB", arg0.getName());

			if (arg0.getName().equals("name")) {
				this.project.name = arg0.getValue().toString();
				Log.i("FB", this.project.name);
				if (this.project.lcn != null) {
					this.project.lcn.onChange();
				}
			} else if (arg0.getName().equals("description")) {
				this.project.description = arg0.getValue().toString();
			} else if (arg0.getName().equals("milestones")) {
				// for(DataSnapshot grandchild : arg0.getChildren())
				// {
				// this.project.milestones.add(new
				// Milestone(grandchild.getRef().toString()));
				// }
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
}
