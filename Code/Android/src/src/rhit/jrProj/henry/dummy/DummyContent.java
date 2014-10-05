package rhit.jrProj.henry.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.firebase.Milestone;
import rhit.jrProj.firebase.Project;
import rhit.jrProj.firebase.Task;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<Milestone> ITEMS = new ArrayList<Milestone>();
	
	public static List<Project> PROJECT_ITEMS = new ArrayList<Project>();

	/**
	 * A map of milestone (dummy) items, by ID.
	 */
	public static Map<String, Milestone> ITEM_MAP = new HashMap<String, Milestone>();
	
	public static Map<String, Project> PROJECT_MAP = new HashMap<String, Project>();

	static {
		ArrayList<Milestone> milestones = new ArrayList<Milestone>();
		
		// Add 3 sample items.
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task());
		tasks.add(new Task());
		milestones.add(new Milestone(tasks, 1));
		tasks = new ArrayList<Task>();
		tasks.add(new Task());
		milestones.add(new Milestone(tasks, 2));
		tasks = new ArrayList<Task>();
		tasks.add(new Task());
		tasks.add(new Task());
		tasks.add(new Task());
		milestones.add(new Milestone(tasks, 3));
		
		for (Milestone milestone : milestones) {
			addItem(milestone);
		}
		
		//  Add 3 sample Projects
		ArrayList<Project> projects = new ArrayList<Project>();
		projects.add(new Project(milestones, 1));
		projects.add(new Project(new ArrayList<Milestone>(), 2));
		projects.add(new Project(new ArrayList<Milestone>(), 3));
		
		for (Project project : projects) {
			addItem(project);
		}
	}

	private static void addItem(Milestone item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}
	
	private static void addItem(Project item) {
		PROJECT_ITEMS.add(item);
		PROJECT_MAP.put(item.toString(), item);
	}
}
