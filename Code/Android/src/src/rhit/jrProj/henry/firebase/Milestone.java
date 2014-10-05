package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

public class Milestone {

	/**
	 * A List of tasks that are contained within the Milestone
	 */
	private ArrayList<Task> tasks = new ArrayList<Task>();

	/**
	 * The milestone Number within the project
	 */
	private int milestoneNumber;

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
}
