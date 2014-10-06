package rhit.jrProj.henry.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	public static List<Project> ITEMS = new ArrayList<Project>();

	/**
	 * A map of milestone (dummy) items, by ID.
	 */
	public static Map<String, Project> ITEM_MAP = new HashMap<String, Project>();

	static {
		// Add 3 sample items.
		ArrayList<Milestone> milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(new ArrayList<Task>(), 1));
		milestones.add(new Milestone(new ArrayList<Task>(), 2));
		addItem(new Project(milestones, 1));
		milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(new ArrayList<Task>(), 1));
		addItem(new Project(milestones, 2));
		milestones = new ArrayList<Milestone>();
		milestones.add(new Milestone(new ArrayList<Task>(), 1));
		milestones.add(new Milestone(new ArrayList<Task>(), 2));
		milestones.add(new Milestone(new ArrayList<Task>(), 3));
		addItem(new Project(milestones, 3));
	}

	private static void addItem(Project item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}
}
