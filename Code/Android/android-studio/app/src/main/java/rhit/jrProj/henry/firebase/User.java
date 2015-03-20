package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

public class User implements Parcelable, ListChangeNotifiable<Project> {

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
     * Accumulated bounty points for leaderboards.
     */
    private long mTotalPoints = 0;

    /**
     * Available points for the trophy store.
     */
    private long mAvailablePoints = 0;

    /**
     * Set of trophies the user has purchased.
     */
    private HashMap<String, Trophy> mTrophies = new HashMap<String, Trophy>();

    /**
     * Amount of points the user has.
     */

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
        this.firebase = new Firebase(pc.readString());
        this.firebase.addChildEventListener(new ChildrenListener(this));
        this.firebase.child("projects").addChildEventListener(
                new GrandChildrenListener(this));
        this.firebase.child("trophies").addChildEventListener(new TrophyGrandChildrenListener(this));
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
    public User(String firebaseURL) {
        this.firebase = new Firebase(firebaseURL);
        this.key = firebaseURL.substring(firebaseURL.lastIndexOf("/") + 1);
        this.firebase.addChildEventListener(new ChildrenListener(this));
        this.firebase.child("projects").addChildEventListener(
                new GrandChildrenListener(this));
        this.firebase.child("trophies").addChildEventListener(new TrophyGrandChildrenListener(this));
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
    @Override
    public String toString() {
        return this.firebase.toString();
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
     * Returns a list of the projects
     *
     * @return
     */
    public ArrayList<Project> getProjects() {
        return this.projects.getAllKeys();
    }

    /**
     * Returns a role of a project
     *
     * @param p
     * @return
     */
    public Role getRole(Project p) {
        return this.projects.getValue(p);
    }

    /**
     * Gets the description of the User
     *
     * @return
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the name of the User
     *
     * @return
     */
    public String getName() {
        return this.name;
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
     * Returns a map of projects associated with the user
     *
     * @return
     */
    Map<Project, Role> getMap() {
        return this.projects;
    }

    /**
     * Purchase a trophy from the store.
     */
    public void buyTrophy(Trophy trophy) {
        changeAvailablePoints(-1 * trophy.getCost());
        Firebase f1 = this.firebase.getRef().child("trophies/" + trophy.getKey());
        f1.setValue(trophy.getName());
    }

    /**
     * Checks to see if the user already owns the given trophy.
     */
    public boolean hasTrophy(Trophy trophy) {
        return mTrophies.containsKey(trophy.getKey());
    }

    /**
     * Add a trophy to the user's set of trophies.
     *
     * @param trophy
     */
    public void addTrophy(Trophy trophy) {
        mTrophies.put(trophy.getKey(), trophy);
    }

    /**
     * Replace the given trophy with the new trophy.
     *
     * @param trophy
     */
    public void replaceTrophy(Trophy trophy, Trophy newTrophy) {
        mTrophies.remove(trophy.getKey());
        mTrophies.put(newTrophy.getKey(), newTrophy);
    }

    /**
     * Returns the amount of points the user has.
     */
    public long getTotalPoints() {
        return mTotalPoints;
    }

    /**
     * Return the amount of points that are available to spend.
     *
     * @return
     */
    public long getAvailablePoints() {
        return this.mAvailablePoints;
    }

    /**
     * Set total amount of points to the given value.
     *
     * @param s
     */
    public void setTotalPoints(long s) {
        this.mTotalPoints = s;
    }

    /**
     * Set available amount of points to the given value.
     *
     * @param s
     */
    public void setAvailablePoints(long s) {
        this.mAvailablePoints = s;
    }

    /**
     * @param s
     */
    public void changeAvailablePoints(long s) {
        long newTotal = this.mAvailablePoints + s;
        this.firebase.child("available_points").setValue(newTotal);
    }


    /**
     * Gets the listChangeNotifier associated with a User
     *
     * @return
     */
    public ListChangeNotifier<Project> getListChangeNotifier() {
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
    }

    ;

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
            } else if (arg0.getKey().equals("total_points")) {
                this.user.setTotalPoints((Long) arg0.getValue());
            } else if (arg0.getKey().equals("available_points")) {
                this.user.setAvailablePoints((Long) arg0.getValue());
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
            } else if (arg0.getKey().equals("total_points")) {
                this.user.setTotalPoints((Long) arg0.getValue());
            } else if (arg0.getKey().equals("available_points")) {
                this.user.setAvailablePoints((Long) arg0.getValue());
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
     * TODO waiting for updated Firebase information
     *
     * @author rockwotj. Created Oct 19, 2014.
     */
    class GrandChildrenListener implements ChildEventListener {

        private User user;

        /**
         * Creates a Grandchildren listener for Projects
         *
         * @param user
         */
        public GrandChildrenListener(User user) {
            this.user = user;
        }

        public void onCancelled(FirebaseError arg0) {
            // do nothing here

        }

        /**
         * Adds a Project to a user's list of projects
         */
        public void onChildAdded(DataSnapshot arg0, String arg1) {
            Role r = Role.DEVELOPER;
            if (arg0.getValue().equals("lead")
                    || arg0.getValue().equals("Lead")) {
                r = Role.LEAD;
            }

            Project p = new Project(arg0.getRef().getRepo().toString()
                    + "/projects/" + arg0.getKey());
            this.user.getMap().put(p, r);
            p.setListChangeNotifier(this.user.getListChangeNotifier());
            if (this.user.getListChangeNotifier() != null) {
                this.user.getListChangeNotifier().onChange();
            }
        }

        /**
         * Updates a user's information after a change
         */
        public void onChildChanged(DataSnapshot arg0, String arg1) {
            Role r = Role.DEVELOPER;
            if (arg0.getValue().equals("lead")
                    || arg0.getValue().equals("Lead")) {
                r = Role.LEAD;
            }
            Project p = new Project(arg0.getRef().getRepo().toString()
                    + "/projects/" + arg0.getKey());
            this.user.getMap().replaceValue(p, r);
        }

        public void onChildMoved(DataSnapshot arg0, String arg1) {
            // do nothing

        }

        /**
         * Removes a project from a user's project list
         */
        public void onChildRemoved(DataSnapshot arg0) {
            Project p = new Project(arg0.getRef().getRepo().toString()
                    + "/projects/" + arg0.getKey());
            this.user.getMap().remove(p);
            if (this.user.getListChangeNotifier() != null) {
                this.user.getListChangeNotifier().onChange();
            }
        }

    }

    class TrophyGrandChildrenListener implements ChildEventListener {

        private User user;

        /**
         * Creates a Grandchildren listener for Trophies
         *
         * @param user
         */
        public TrophyGrandChildrenListener(User user) {
            this.user = user;
        }

        public void onCancelled(FirebaseError arg0) {
            // do nothing here
        }

        /**
         * Adds a Trophy to a user's list of Trophies
         */
        public void onChildAdded(DataSnapshot arg0, String arg1) {
            Trophy t = new Trophy(arg0.getRef().getRepo().toString()
                    + "/trophies/" + arg0.getKey());
            this.user.addTrophy(t);
        }

        /**
         * Updates a user's information after a change
         */
        public void onChildChanged(DataSnapshot arg0, String arg1) {
            // Not implemented
        }

        public void onChildMoved(DataSnapshot arg0, String arg1) {
            // do nothing

        }

        public void onChildRemoved(DataSnapshot arg0) {
            // do nothing
        }
    }
}