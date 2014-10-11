package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Project implements Parcelable, ChildEventListener {

	/**
	 * A reference to firebase to keep the data up to date.
	 */
	private Firebase firebase;

	// Below are the data of the Project Object

	/**
	 * A List of milestones that are contained within the project
	 */
	private ArrayList<Milestone> milestones = new ArrayList<Milestone>();

	/**
	 * The project's name
	 */
	private String name;

	/**
	 * The members that are working on the project
	 */
	private Map<User, Enums.Role> members = new HashMap<User, Enums>();

	/**
	 * A description of the project.
	 */
	private String description;

	/**
	 * Do we need to do anything with the backlog?
	 */
	// private Backlog;

	private ListChangeNotifier<Project> lcn;

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
		this.firebase.addChildEventListener(this);
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 *
	 * @param in
	 */
	Project(Parcel in) {
		this.firebase = new Firebase(in.readString());
		this.firebase.addChildEventListener(this);
		this.name = in.readString();
		this.description = in.readString();
		// this.members = new HashMap<User, Role>(); // How to transport? Loop?
		in.readTypedList(this.milestones, Milestone.CREATOR);
	}

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
	 * Gets an ArrayList of milestones associated with this project.
	 */
	public ArrayList<Milestone> getMilestones() {
		return this.milestones;
	}

	public void setListChangeNotifier(ListChangeNotifier<Project> lcn) {
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
		// TODO Members?
		// number for the loop and then loop through it all?
	}

	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.

	}

	public void onChildAdded(DataSnapshot arg0, String arg1) {

		if (arg0.getName().equals("name")) {
			this.name = arg0.getValue(String.class);
			if (this.lcn != null) {
				this.lcn.onChange();
			}
		} else if (arg0.getName().equals("description")) {
			this.description = arg0.getValue(String.class);
		} else if (arg0.getName().equals("milestones")) {
			// TODO
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
