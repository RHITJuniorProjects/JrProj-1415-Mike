package rhit.jrProj.henry.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Task;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MilestoneContent {

	/**
	 * An array of sample (Milestone) items.
	 */
	public static List<Milestone> ITEMS = new ArrayList<Milestone>();

	/**
	 * A map of milestone (dummy) items, by ID.
	 */
	public static Map<String, Milestone> ITEM_MAP = new HashMap<String, Milestone>();

	static {
		// Add 3 sample items.
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task());
		tasks.add(new Task());
		addItem(new Milestone(tasks, 1));
		tasks = new ArrayList<Task>();
		tasks.add(new Task());
		addItem(new Milestone(tasks, 2));
		tasks = new ArrayList<Task>();
		tasks.add(new Task());
		tasks.add(new Task());
		tasks.add(new Task());
		addItem(new Milestone(tasks, 3));
	}

	private static void addItem(Milestone item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}
}
