
package rhit.jrProj.henry.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class User implements ChildEventListener{

	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.
		
	}

	public void onChildAdded(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.
		
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
	//TODO
}
=======
package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import rhit.jrProj.henry.MainActivity;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums.Role;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class User implements Parcelable {
	/**
	 * A reference to the Main URL of the Firebase being used.
	 */
	private String firebaseURL= MainActivity.firebaseLoc;
	
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
	private ListChangeNotifier<Project> listViewCallback;
	/**
	 * projects is a Set in the form: [project_key, this_Users_role]
	 */
	private Map<Project, Enums.Role> projects = new Map<Project, Enums.Role>();

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

	

	/**
	 * Creates a new User from a parcel
	 * 
	 * @param pc
	 */

	User(Parcel pc) {
		String firebaseURL=pc.readString();
		this.firebase = new Firebase(firebaseURL);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.firebase.child("projects").addChildEventListener(new GrandChildrenListener(this));
		this.name = pc.readString();
		this.gitName = pc.readString();
		this.email = pc.readString();
		this.key = firebaseURL.substring(firebaseURL.lastIndexOf("/") + 1);
	}

	public User(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		this.key = firebaseURL.substring(firebaseURL.lastIndexOf("/") + 1);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.firebase.child("projects").addChildEventListener(new GrandChildrenListener(this));
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
	public void setListChangeNotifier(ListChangeNotifier<Project> lcn) {
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
	}

	/**
	 * Returns the Firebase url
	 */
	@Override
	public String toString() {
		return this.firebase.toString();
	}

	/**
	 * 
	 * returns the Firebase key of the user
	 *
	 * @return
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * 
	 * Returns a list of the projects
	 *
	 * @return
	 */
	public ArrayList<Project> getProjects() {
		return this.projects.getAllKeys();
	}

	/**
	 * 
	 * Returns a role of a project
	 *
	 * @param p
	 * @return
	 */
	public Role getRole(Project p) {
		return this.projects.getValue(p);
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

	void setEmail(String email) {
		this.email = email;
	}

	void setName(String name) {
		this.name = name;
	}

	void setGitName(String git) {
		this.gitName = git;
	}

	Map<Project, Role> getMap() {
		return this.projects;
	}

	ListChangeNotifier<Project> getListChangeNotifier() {
		return this.listViewCallback;
	}

	/**
	 * TODO
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			return ((User) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	};

	class ChildrenListener implements ChildEventListener {
		private User user;

		public ChildrenListener(User user) {
			this.user = user;
		}

		/**
		 * Nada
		 */
		public void onCancelled(FirebaseError arg0) {
			// TODO Auto-generated method stub.

		}

		/**
		 * Implement the onChildAdded method which is called once for each existing child and once for every added child.
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (arg0.getName().equals("name")) {
				this.user.setName(arg0.getValue().toString());
			} else if (arg0.getName().equals("git")) {
				this.user.setGitName(arg0.getValue().toString());
			} else if (arg0.getName().equals("email")) {
				this.user.setEmail(arg0.getValue().toString());
			}
		}

		/**
		 * Implement the onChildChanged method which is called once for every changed child.
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getName().equals("name")) {
				this.user.setName(arg0.getValue().toString());
			} else if (arg0.getName().equals("git")) {
				this.user.setGitName(arg0.getValue().toString());
			} else if (arg0.getName().equals("email")) {
				this.user.setEmail(arg0.getValue().toString());
			}
		}

		/**
		 * TODO nada
		 */
		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub.

		}

		/**
		 * TODO fill this in
		 */
		public void onChildRemoved(DataSnapshot arg0) {
			// TODO Auto-generated method stub.
		}
	}
/**
 * 
 * TODO waiting for updated Firebase information
 *
 * @author rockwotj.
 *         Created Oct 19, 2014.
 */
	class GrandChildrenListener implements ChildEventListener {

		private User user;

		public GrandChildrenListener(User user) {
			this.user = user;
		}

		public void onCancelled(FirebaseError arg0) {
			// TODO Auto-generated method stub.

		}

		public void onChildAdded(DataSnapshot arg0, String arg1) {
			Role r = Role.Developer;
			if (arg0.getValue().equals("lead")) {
				r = Role.Lead;
			}
			Project p = new Project(
					firebaseURL+"projects/"
							+ arg0.getName());
			this.user.getMap().put(p, r);
			p.setListChangeNotifier(this.user.getListChangeNotifier());
			if (this.user.getListChangeNotifier() != null) {
				this.user.getListChangeNotifier().onChange();
			}
		}

		public void onChildChanged(DataSnapshot arg0, String arg1) {
			Role r = Role.Developer;
			if (arg0.getValue().equals("lead")) {
				r = Role.Lead;
			}
			Project p = new Project(
					firebaseURL+"projects/"
							+ arg0.getName());
			this.user.getMap().replaceValue(p, r);
		}

		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub.

		}

		public void onChildRemoved(DataSnapshot arg0) {
			Project p = new Project(
					firebaseURL+"projects/"
							+ arg0.getName());
			this.user.getMap().remove(p);
			if (this.user.getListChangeNotifier() != null) {
				this.user.getListChangeNotifier().onChange();
			}
		}

	}
}
