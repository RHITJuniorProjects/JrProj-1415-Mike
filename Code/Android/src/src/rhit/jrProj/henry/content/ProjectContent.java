package rhit.jrProj.henry.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProjectContent {

	/**
	 * An array of sample (Milestone) items.
	 */
	public static ArrayList<Project> ITEMS = new ArrayList<Project>();

	/**
	 * A map of milestone (dummy) items, by ID.
	 */
	public static Map<String, Project> ITEM_MAP = new HashMap<String, Project>();

	static {
		// Add 3 sample items.
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task(1));
		tasks.add(new Task(2));
		tasks.add(new Task(3));
		tasks.add(new Task(4));
		ArrayList<Milestone> milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(tasks, 1));
		tasks.remove(0);
		milestones.add(new Milestone(tasks, 2));
		addItem(new Project(milestones, 1));
		milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(tasks, 1));
		addItem(new Project(milestones, 2));
		tasks.remove(0);
		milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(tasks, 1));
		milestones.add(new Milestone(tasks, 2));
		milestones.add(new Milestone(tasks, 3));
		addItem(new Project(milestones, 3));
	}

	private static void addItem(Project item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}
}
