package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Milestone implements Parcelable {

	/**
	 * A List of tasks that are contained within the Milestone
	 */
	private ArrayList<Task> tasks = new ArrayList<Task>();

	/**
	 * The milestone Number within the project
	 */
	private int milestoneNumber;

	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Milestone> CREATOR = new Parcelable.Creator<Milestone>() {

		public Milestone createFromParcel(Parcel pc) {
			return new Milestone(pc);
		}

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
	public Milestone(ArrayList<Task> tasks, int number) {
		this.tasks = tasks;
		this.milestoneNumber = number;
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 *
	 * @param in
	 */
	public Milestone(Parcel in) {
		this.milestoneNumber = in.readInt();
		this.tasks = new ArrayList<Task>();
		in.readTypedList(this.tasks, Task.CREATOR);
	}

	public Milestone(String firebaseUrl) {
		// TODO Auto-generated constructor stub.
	}

	public int getMilestoneNumber() {
		return this.milestoneNumber;
	}

	@Override
	public String toString() {
		return "Milestone " + this.milestoneNumber;
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

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.milestoneNumber);
		dest.writeTypedList(this.tasks);
	}
	
}
