package rhit.jrProj.henry.firebase;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

public class Member implements Parcelable {

	/**
	 * A reference to Firebase to keep the data up to date.
	 */
	private Firebase firebase;

	/**
	 * The User's name
	 */
	private String name = "No Name Assigned";

	/**
	 * The User's gitname
	 */
	private String gitName = "No Name assigned";
	/**
	 * Email of the User
	 */
	private String email = "No e-mail assigned";
	/**
	 * Key is the Firebase Key of the user.
	 */
	private String key = "no key assigned";

	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * User that this object has been updated.
	 */
	private ListChangeNotifier<Project> listViewCallback;

	/**
	 * the User's category
	 */
	// private Category category;

	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {

		public Member createFromParcel(Parcel pc) {
			return new Member(pc);
		}

		public Member[] newArray(int size) {
			return new Member[size];
		}
	};

	/**
	 * Creates a new User from a parcel
	 * 
	 * @param pc
	 */

	Member(Parcel pc) {
		this.firebase = new Firebase(pc.readString());
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.name = pc.readString();
		this.gitName = pc.readString();
		this.email = pc.readString();
		this.key = this.firebase.toString().substring(
				this.firebase.toString().lastIndexOf("/") + 1);
	}

	/**
	 * Creates a user from a Firebase url
	 * 
	 * @param firebaseURL
	 */
	public Member(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		this.key = firebaseURL.substring(firebaseURL.lastIndexOf("/") + 1);
		this.firebase.addChildEventListener(new ChildrenListener(this));
	}

	public int describeContents() {
		return 0; // do nothing
	}

	/**
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
	public String getFirebaseUrl() {
		return this.firebase.toString();
	}

	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Returns the Firebase key of the user
	 * 
	 * @return
	 */
	public String getKey() {
		return this.key;
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

	/**
	 * Sets the email associated with a user
	 */
	void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets a user's name
	 * 
	 * @param name
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the user's git name
	 * 
	 * @param git
	 */
	void setGitName(String git) {
		this.gitName = git;
	}

	/**
	 * Gets the listChangeNotifier associated with a User
	 * 
	 * @return
	 */
	ListChangeNotifier<Project> getListChangeNotifier() {
		return this.listViewCallback;
	}

	/**
	 * TODO
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Member) {
			return ((Member) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	};

	class ChildrenListener implements ChildEventListener {
		private Member user;

		public ChildrenListener(Member user) {
			this.user = user;
		}

		/**
		 * Nada
		 */
		public void onCancelled(FirebaseError arg0) {
			// TODO Auto-generated method stub.

		}

		/**
		 * Implement the onChildAdded method which is called once for each
		 * existing child and once for every added child.
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("name")) {
				this.user.setName(arg0.getValue().toString());
			} else if (arg0.getKey().equals("git")) {
				this.user.setGitName(arg0.getValue().toString());
			} else if (arg0.getKey().equals("email")) {
				this.user.setEmail(arg0.getValue().toString());
			}
		}

		/**
		 * Implement the onChildChanged method which is called once for every
		 * changed child.
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("name")) {
				this.user.setName(arg0.getValue().toString());
			} else if (arg0.getKey().equals("git")) {
				this.user.setGitName(arg0.getValue().toString());
			} else if (arg0.getKey().equals("email")) {
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

}