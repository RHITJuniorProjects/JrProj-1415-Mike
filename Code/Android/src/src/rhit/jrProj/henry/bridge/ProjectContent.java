package rhit.jrProj.henry.bridge;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

/**
 * A place holder for summy firebase content until we get real data.
 */
public class ProjectContent {

	/**
	 * An array of sample Projects.
	 */
	public static ArrayList<Project> ITEMS = new ArrayList<Project>();

	static {
		// Add 3 sample project.
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task(1));
		tasks.add(new Task(2));
		tasks.add(new Task(3));
		tasks.add(new Task(4));
		ArrayList<Milestone> milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(new ArrayList<Task>(), 1));
		tasks.remove(0);
		milestones.add(new Milestone(tasks, 2));
		ITEMS.add(new Project(milestones, 1));
		milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(tasks, 1));
		ITEMS.add(new Project(milestones, 2));
		tasks.remove(0);
		milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(tasks, 1));
		milestones.add(new Milestone(tasks, 2));
		milestones.add(new Milestone(tasks, 3));
		ITEMS.add(new Project(milestones, 3));
	}
}
