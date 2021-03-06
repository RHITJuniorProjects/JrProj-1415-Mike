package rhit.jrProj.henry.firebase;

import rhit.jrProj.henry.GlobalVariables;
import rhit.jrProj.henry.bridge.ChangeNotifier;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private String key = "No key assigned";

    /**
     * This is the class that onChange is called from to when a field in
     * Firebase is updated. This then notifies the object that is displaying the
     * User that this object has been updated.
     */
    private ChangeNotifier listViewCallback;

    /**
     * A user's metrics for a project
     */
    private Map<String, ProjectMetrics> projectMetrics;

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

    private int mTotalPoints;


    private List<Trophy> mTrophies;

    private ArrayList<String> projectTitles;

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
        this.projectMetrics = new Map<String, Member.ProjectMetrics>();
        mTrophies = new ArrayList<Trophy>();
    }

    public Member(String firebaseUrl) {
        this(firebaseUrl, false);
    }

    /**
     * Creates a user from a Firebase url
     *
     * @param firebaseURL
     */
    public Member(String firebaseURL, boolean hasProjects) {
        this.firebase = new Firebase(firebaseURL);
        this.key = firebaseURL.substring(firebaseURL.lastIndexOf("/") + 1);
        this.firebase.addChildEventListener(new ChildrenListener(this));

        this.projectMetrics = new Map<String, Member.ProjectMetrics>();
        mTrophies = new ArrayList<Trophy>();
        projectTitles = new ArrayList<String>();
        if (hasProjects) {
            this.firebase.child("projects").addChildEventListener(
                    new ProjectNameListener(this));
        }
    }

    public int describeContents() {
        return 0; // do nothing
    }

    /**
     * Sets a new list changed notifiers
     *
     * @param lcn
     */
    public void setListChangeNotifier(ChangeNotifier lcn) {
        this.listViewCallback = lcn;
    }

    public List<Trophy> getTrophies() {
        return mTrophies;
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
     * Gets the member's name
     *
     * @return the member's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value of the field called 'projectMetrics'.
     *
     * @return Returns the projectMetrics.
     */
    public ProjectMetrics getProjectMetrics(String projectId) {
        return this.projectMetrics.getValue(projectId);
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
    ChangeNotifier getListChangeNotifier() {
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
    }

    public int getTotalPoints() {
        return mTotalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.mTotalPoints = mTotalPoints;
    }

    public ArrayList<String> getProjectTitles() {
        return this.projectTitles;
    }

    public void addProjectTitle(String title){
        this.projectTitles.add(title);
    }

    public void removeProjectTitle(String title){
        this.projectTitles.remove(title);
    }
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
            if (arg0.getKey().equals("trophies")) {
                for (DataSnapshot child : arg0.getChildren()) {
                    Trophy t = new Trophy(GlobalVariables.getFirebaseUrl() + "trophies/" + child.getKey());
                    t.setChangeNotifier(getListChangeNotifier());
                    mTrophies.add(t);
                }
            }
            if (arg0.getKey().equals("name")) {
                this.user.setName(arg0.getValue().toString());
            } else if (arg0.getKey().equals("git")) {
                this.user.setGitName(arg0.getValue().toString());
            } else if (arg0.getKey().equals("email")) {
                this.user.setEmail(arg0.getValue().toString());
            } else if (arg0.getKey().equals("projects")) {
                for (DataSnapshot grandChild : arg0.getChildren()) {
                    this.user.projectMetrics.put(grandChild.getKey(),
                            new ProjectMetrics(this.user, grandChild.getKey()));
                }
            }
            if (getListChangeNotifier() != null) {
                getListChangeNotifier().onChange();
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

    public class ProjectMetrics implements ChildEventListener {

        private int addedLinesOfCode = 0;
        private int addedLOCPercent = 0;
        private int hoursPercent = 0;
        private int removedLinesOfCode = 0;
        private int removedLOCPercent = 0;
        private int totalHours = 0;
        private int totalLOC = 0;

        public ProjectMetrics(Member member, String projectKey) {
            Firebase fb = member.firebase.child("projects/" + projectKey);
//			Log.d("Henry", fb.toString());
            fb.addChildEventListener(this);
        }

        public void onCancelled(FirebaseError arg0) {
            // Do nothing
        }

        public void onChildAdded(DataSnapshot arg0, String arg1) {
            if (arg0.getKey().equals("added_lines_of_code")) {
                this.addedLinesOfCode = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("added_loc_percent")) {
                this.addedLOCPercent = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("hours_percent")) {
                this.hoursPercent = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("loc_percent")) {
                this.hoursPercent = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("removed_lines_of_code")) {
                this.removedLinesOfCode = arg0.getValue(Integer.class)
                        .intValue();
            } else if (arg0.getKey().equals("removed_loc_percent")) {
                this.removedLOCPercent = arg0.getValue(Integer.class)
                        .intValue();
            } else if (arg0.getKey().equals("total_hours")) {
                this.totalHours = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("total_lines_of_code")) {
                this.totalLOC = arg0.getValue(Integer.class).intValue();
            }
        }

        public void onChildChanged(DataSnapshot arg0, String arg1) {
            if (arg0.getKey().equals("added_lines_of_code")) {
                this.addedLinesOfCode = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("added_loc_percent")) {
                this.addedLOCPercent = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("hours_percent")) {
                this.hoursPercent = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("loc_percent")) {
                this.hoursPercent = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("removed_lines_of_code")) {
                this.removedLinesOfCode = arg0.getValue(Integer.class)
                        .intValue();
            } else if (arg0.getKey().equals("removed_loc_percent")) {
                this.removedLOCPercent = arg0.getValue(Integer.class)
                        .intValue();
            } else if (arg0.getKey().equals("total_hours")) {
                this.totalHours = arg0.getValue(Integer.class).intValue();
            } else if (arg0.getKey().equals("total_lines_of_code")) {
                this.totalLOC = arg0.getValue(Integer.class).intValue();
            }
        }

        public void onChildMoved(DataSnapshot arg0, String arg1) {
            // Do nothing
        }

        public void onChildRemoved(DataSnapshot arg0) {
            // Do nothing
        }

        /**
         * Returns the value of the field called 'addedLinesOfCode'.
         *
         * @return Returns the addedLinesOfCode.
         */
        public int getAddedLinesOfCode() {
            return this.addedLinesOfCode;
        }

        /**
         * Returns the value of the field called 'addedLOCPercent'.
         *
         * @return Returns the addedLOCPercent.
         */
        public int getAddedLOCPercent() {
            return this.addedLOCPercent;
        }

        /**
         * Returns the value of the field called 'hoursPercent'.
         *
         * @return Returns the hoursPercent.
         */
        public int getHoursPercent() {
            return this.hoursPercent;
        }

        /**
         * Returns the value of the field called 'removedLinesOfCode'.
         *
         * @return Returns the removedLinesOfCode.
         */
        public int getRemovedLinesOfCode() {
            return this.removedLinesOfCode;
        }

        /**
         * Returns the value of the field called 'removedLOCPercent'.
         *
         * @return Returns the removedLOCPercent.
         */
        public int getRemovedLOCPercent() {
            return this.removedLOCPercent;
        }

        /**
         * Returns the value of the field called 'totalHours'.
         *
         * @return Returns the totalHours.
         */
        public int getTotalHours() {
            return this.totalHours;
        }

        /**
         * Returns the value of the field called 'totalLOC'.
         *
         * @return Returns the totalLOC.
         */
        public int getTotalLOC() {
            return this.totalLOC;
        }

    }

    private class ProjectNameListener implements ChildEventListener {
        private Member mm;

        public ProjectNameListener(Member m) {
            this.mm = m;
        }

        @Override
        public void onChildAdded(DataSnapshot arg0, String s) {
            Firebase projectName = arg0.getRef().getRoot().child("projects").child(arg0.getKey());
            //new Firebase(arg0.getRef().getRepo().toString() + "/projects/" + arg0.getKey() + "/name");
            Log.d("RHH","Project at:" + projectName.toString());
            projectName.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("RHH","Project at:" + dataSnapshot.getValue().toString());
                    if (dataSnapshot.getKey().equals("name")) {
                        mm.addProjectTitle(dataSnapshot.getValue(String.class));
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot arg0) {
            Firebase projectName = new Firebase(arg0.getRef().getRepo().toString() + "/projects/" + arg0.getKey() + "/name");
            projectName.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mm.removeProjectTitle(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //de nada
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }

    }
}