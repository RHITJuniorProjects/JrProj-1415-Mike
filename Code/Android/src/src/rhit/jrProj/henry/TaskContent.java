package rhit.jrProj.henry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.HashMap;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskContent {

	/**
	 * An array of sample (List) items.
	 */
	public static List<Task> ITEMS = new ArrayList<Task>();

	/**
	 * A map of sample (List) items, by ID.
	 */
	public static Map<String, Task> ITEM_MAP = new HashMap<String, Task>();

	static {
		ArrayList<Task> taskList = MainTaskActivity.getTaskList();

		if (taskList.size() > 0) {
			for (int i = 0; i < taskList.size(); i++) {
				addItem(taskList.get(i));
			}
		}
	}

	public static void addItem(Task item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	public static void removeItem(Task item) {
		ITEMS.remove(item);
		ITEM_MAP.remove(item);

	}

	/**
	 * A List item representing a piece of content.
	 */
	public static class Task implements Serializable {
		public String id;
		public String content;

		public Task(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
