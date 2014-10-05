package rhit.jrProj.firebase;

import java.util.ArrayList;

public class Project {

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

	public int getMilestoneNumber() {
		return this.projectNumber;
	}

	@Override
	public String toString() {
		return "Project " + this.projectNumber;
	}
}
