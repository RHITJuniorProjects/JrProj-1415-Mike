package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import rhit.jrProj.henry.firebase.Milestone;

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
	public Project(Parcel in) {
		this.projectNumber = in.readInt();
		this.milestones = new ArrayList<Milestone>();
		in.readTypedList(this.milestones, Milestone.Creator);
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

	@Override
	public int describeContents() {
		// Do nothing.
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.projectNumber);
		dest.writeTypedList(this.milestones);
	}
	
	public static final Parcelable.Creator<Project> Creator = new Parcelable.Creator<Project>() {

		public Project createFromParcel(Parcel pc) {
			return new Project(pc);
		}

		public Project[] newArray(int size) {
			return new Project[size];
		}
	};
}
