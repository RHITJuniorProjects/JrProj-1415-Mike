package rhit.jrProj.henry.firebase;

import java.util.ArrayList;
import java.util.List;

import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.bridge.ListChangeNotifiable;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;
import rhit.jrProj.henry.helpers.GraphHelper;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Milestone implements Parcelable, ListChangeNotifiable {

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
    private DueDate dueDate = new DueDate();

    /**
     * The percentage of tasks completed for this milestone
     */
    private int taskPercent = 0;
    /**
     * The parent project id
     */

    private String parentProjectID;
    /**
     * The parent project name
     */

    private String parentProjectName;
    /**
     * A description of the work that needs to happen in this milestone.
     */
    private String description = "No description assigned";

    /**
     * This is the class that onChange is called from to when a field in
     * Firebase is updated. This then notifies the object that is displaying the
     * Milestone that this object has been updated.
     */
    private ChangeNotifier listViewCallback;

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


    public Milestone(String firebaseUrl) {
        this.firebase = new Firebase(firebaseUrl);
        this.firebase.addChildEventListener(new ChildrenListener(this));
        this.firebase.child("tasks").addChildEventListener(
                new GrandChildrenListener(this));
        this.milestoneId = firebaseUrl
                .substring(firebaseUrl.lastIndexOf('/') + 1);
        //Might be useful for later
        //setParentID(firebaseUrl);
    }

    /**
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
        this.setDueDate(new DueDate(in.readString()));
        this.setDescription(in.readString());
        this.setTaskPercent(in.readInt());
        in.readTypedList(this.tasks, Task.CREATOR);
    }

    /**
     * Sets a new list changed notifier
     *
     * @param lcn
     */
    public void setListChangeNotifier(ChangeNotifier lcn) {
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

    public void setParentName(String projName) {
        this.parentProjectName = projName;
    }
    //Might be useful for later
//	private void setParentID(String firebaseURL){
//		String proj="/projects/";
//		String ms= "/milestones/";
//		int indexProj=firebaseURL.indexOf(proj);
//		int indexMS=firebaseURL.indexOf(ms);
//		if (indexProj!=-1 && indexMS!=-1){
//			int indexProjEnd=indexProj+proj.length();
//			this.parentProjectID=firebaseURL.substring(indexProjEnd, indexMS);
//		}
//	}

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
        dest.writeString(this.getDueDate().toString());
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
     * Returns the parent project id
     *
     * @return the parent project id
     */
    public String getParentProjectID() {
        return this.parentProjectID;
    }


    /**
     * Returns the due date of the milestone
     *
     * @return the due date of the milestone
     */
    public DueDate getDueDate() {
        return this.dueDate;
    }

    /**
     * Sets the due date of the milestone
     *
     * @param dueDate the due date of the milestone
     */
    public void setDueDate(DueDate dueDate) {
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
     *
     * @return the percentage of tasks completed in this milestone
     */
    public int getTaskPercent() {
        return this.taskPercent;
    }

    /**
     * Sets the percentage of tasks completed in this milestone
     *
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
     * gets the ListChangeNotifier (aka ListViewCallback) for a milestone
     *
     * @return
     */

    public ChangeNotifier getListChangeNotifier() {
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
     * Formats Due date to dd/mm/yyyy
     *
     * @return formatted due date as String
     */
    public String getDueDateFormatted() {
        return this.dueDate.toStringFormatted();
    }


    /**
     * Sets the ListViewCallback on a milestone
     *
     * @param listViewCallback
     */
    public void setListViewCallback(
            ChangeNotifier listViewCallback) {
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

            if (arg0.getKey().equals("name")) {
                this.milestone.setName(arg0.getValue(String.class));
                if (this.milestone.getListChangeNotifier() != null) {
                    this.milestone.getListChangeNotifier().onChange();
                }
            } else if (arg0.getKey().equals("description")) {
                this.milestone.setDescription(arg0.getValue(String.class));
            } else if (arg0.getKey().equals("due_date")) {
                this.milestone.setDueDate(new DueDate(arg0.getValue(String.class)));
            } else if (arg0.getKey().equals("task_percent")) {
                this.milestone.setTaskPercent(arg0.getValue(Integer.class));
            } else if (arg0.getKey().equals("tasks")) {
                for (DataSnapshot child : arg0.getChildren()) {
                    Task t = new Task(child.getRef().toString());

                    if (!this.milestone.tasks.contains(t)) {
                        t.setParentNames(this.milestone.parentProjectName, this.milestone.getName());
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
                t.setParentNames(this.milestone.parentProjectName, this.milestone.name);
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

    /**
     * Compares this project with the other given project. This implementation treats lower
     * case letters the same as upper case letters. Also treats numbers differently,
     * i.e. puts 10 after 9 instead of after 1
     *
     * @param p
     * @return
     */
    public int compareToIgnoreCase(Milestone p) {
        return GeneralAlgorithms.compareToIgnoreCase(this.getName(), p.getName());
    }

    /**
     * Compares this Project to another Project by comparing dates
     *
     * @param p
     * @param newestFirst
     * @return
     */
    public int compareToByDate(Milestone p, boolean newestFirst) {
        return this.getDueDate().compareTo(p.getDueDate(), newestFirst);
    }
}
