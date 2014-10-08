package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Project implements Parcelable {

	/**
	 * A List of tasks that are contained within the Milestone
	 */
	private ArrayList<Milestone> milestones = new ArrayList<Milestone>();

	/**
	 * The milestone Number within the project
	 */
	private int projectNumber;

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
	 * Creates a Milestone object
	 * 
	 * @param tasks
	 * @param number
	 */
	public Project(ArrayList<Milestone> milestones, int number) {
		this.milestones = milestones;
		this.projectNumber = number;
	}

	/**
	 * 
	 * Ctor from Parcel, reads back fields IN THE ORDER they were written
	 *
	 * @param in
	 */
	Project(Parcel in) {
		this.projectNumber = in.readInt();
		this.milestones = new ArrayList<Milestone>();
		in.readTypedList(this.milestones, Milestone.CREATOR);
	}

	public int getMilestoneNumber() {
		return this.projectNumber;
	}

	@Override
	public String toString() {
		return "Project " + this.projectNumber;
	}

	/**
	 * Gets an ArrayList of milestones associated with this project.
	 */
	public ArrayList<Milestone> getMilestones() {
		return this.milestones;
	}

	public int describeContents() {
		// Do nothing.
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.projectNumber);
		dest.writeTypedList(this.milestones);
	}
}
