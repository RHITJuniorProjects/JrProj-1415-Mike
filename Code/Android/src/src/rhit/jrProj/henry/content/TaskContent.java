package rhit.jrProj.henry.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import rhit.jrProj.henry.firebase.Task;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskContent {

	/**
	 * An array of sample (Task) items.
	 */
	public static List<Task> ITEMS = new ArrayList<Task>();

	/**
	 * A map of Task (dummy) items, by ID.
	 */
	public static Map<String, Task> ITEM_MAP = new HashMap<String, Task>();

	static {
		// Add 3 sample items.
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(new Task());
		tasks.add(new Task());
		addItem(new Task());
		tasks = new ArrayList<Task>();
		tasks.add(new Task());
		addItem(new Task());
		tasks = new ArrayList<Task>();
		tasks.add(new Task());
		tasks.add(new Task());
		tasks.add(new Task());
		addItem(new Task());
	}

	private static void addItem(Task item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.toString(), item);
	}
}
