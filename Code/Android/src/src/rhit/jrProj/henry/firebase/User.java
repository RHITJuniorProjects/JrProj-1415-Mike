package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums.Role;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class User implements Parcelable, ChildEventListener {

	/**
	 * A reference to firebase to keep the data up to date.
	 */
	private Firebase firebase;

	/**
	 * The User's name
	 */
	private String name;

	/**
	 * The User's gitname
	 */
	private String gitName;
	/**
	 * Email of the User
	 */
	private String email;
	/**
	 * Key is the Firebase Key of the user.
	 */
	private String key;

	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * User that this object has been updated.
	 */
	private ListChangeNotifier<User> listViewCallback;
	/**
	 * projects is a Set in the form: [project_key, this_Users_role]
	 */
	private Map<Project, Role> projects = new HashMap<Project, Role>();
	/**
	 * tasks is a Set in the form: task_key
	 */
	private Set<String> tasks = new HashSet<String>();
	/**
	 * The User's assignee(s)
	 */
	// private User assignedUser;

	/**
	 * the User's category
	 */
	// private Category category;

	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		public User createFromParcel(Parcel pc) {
			return new User(pc);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	// public void setKey(String s){
	// this.key=s;
	// }

	/**
	 * Creates a new User from a parcel
	 * 
	 * @param pc
	 */

	User(Parcel pc) {
		this.firebase = new Firebase(pc.readString());
		this.name = pc.readString();
		this.gitName = pc.readString();
		this.email = pc.readString();
		// this.key=pc.readString();
	}

	public User(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		this.key = firebaseURL.substring(firebaseURL.lastIndexOf("/") + 1);
		Log.i("FIREBASEURL", this.key);
		this.firebase.addChildEventListener(this);
	}

	public int describeContents() {
		return 0;
	}

	public Set<String> getTasks() {
		return this.tasks;
	}

	/**
	 * 
	 * Sets a new list changed notifier
	 * 
	 * @param lcn
	 */
	public void setListChangeNotifier(ListChangeNotifier<User> lcn) {
		this.listViewCallback = lcn;
	}

	/**
	 * Passes the Firebase URL, the name of the User and the description of the
	 * User to the next view
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.gitName);
		dest.writeString(this.email);
		// dest.writeString(this.key);
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			return ((User) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	};

	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.

	}

	public void onChildAdded(DataSnapshot arg0, String arg1) {
		Log.i("I'm getting called!", arg0.getName());
		
		if (arg0.getName().equals("name")) {
			
			this.name = arg0.getValue().toString();
			Log.i("name", this.name);
			if (this.listViewCallback != null) {
				this.listViewCallback.onChange();
			}
		} else if (arg0.getName().equals("git")) {
			
			this.gitName = arg0.getValue().toString();
			Log.i("Child", this.gitName);
		} else if (arg0.getName().equals("email")) {
			
			this.email = arg0.getValue().toString();
			Log.i("Child", this.email);
		} else if (arg0.getName().equals("projects")) {
			Log.i("Child", "project");
			for (DataSnapshot project : arg0.getChildren()) {
				Role r = Role.Developer;
				if (project.getValue().equals("lead")) {
					r = Role.Lead;
				}
				this.projects.put(new Project(
						"https://shining-inferno-2277.firebaseio.com/projects/"
								+ project.getName()), r);
			}
		} else if (arg0.getName().equals("tasks")) {
			for (DataSnapshot task : arg0.getChildren()) {
				this.tasks.add(task.getName());
			}
		}
	}

	public Map<Project, Role> getProjects() {
		return this.projects;
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
	 * Gets the description of the User
	 * 
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}
}
