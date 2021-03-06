package tests;

import json.JSONObject;
import junit.framework.TestCase;
import rhit.jrProj.henry.MainActivity;

public class TaskTest extends TestCase {

	private JSONObject task;
	private String itemUrl;
	
	private final String firebaseUrl = "https://henry-test.firebaseio.com/";

	public TaskTest(String name) {
		super(name);
		try {
			JSONObject json = TestHelpers
					.getFirebaseSync(firebaseUrl
							+ ".json?shallow=true");
			assertEquals(json.get("projects"), Boolean.TRUE);
			json = TestHelpers.getFirebaseSync(firebaseUrl
					+ "projects.json?shallow=true");
			String projectKey = TestHelpers.getFirstKey(json);
			json = TestHelpers.getFirebaseSync(firebaseUrl
					+ String.format("projects/%s/.json?shallow=true",
							projectKey));
			assertEquals(json.get("milestones"), Boolean.TRUE);
			json = TestHelpers.getFirebaseSync(firebaseUrl
					+ String.format("projects/%s/milestones.json?shallow=true",
							projectKey));
			String milestoneKey = TestHelpers.getFirstKey(json);
			json = TestHelpers.getFirebaseSync(firebaseUrl
					+ String.format(
							"projects/%s/milestones/%s.json?shallow=true",
							projectKey, milestoneKey));
			assertEquals(json.get("tasks"), Boolean.TRUE);
			json = TestHelpers
					.getFirebaseSync(firebaseUrl
							+ String.format(
									"projects/%s/milestones/%s/tasks.json?shallow=true",
									projectKey, milestoneKey));
			String taskKey = TestHelpers.getFirstKey(json);
			this.itemUrl = firebaseUrl
					+ String.format("projects/%s/milestones/%s/tasks/%s",
							projectKey, milestoneKey, taskKey);
			this.task = TestHelpers.getFirebaseSync(this.itemUrl
					+ ".json?shallow=true");
		} catch (Exception e) {
			// do nothing
		}
	}

	public void testGetDescription() {
		assertNotNull(this.task);
		assertTrue(this.task.has("description"));
	}

	public void testGetName() {
		assertNotNull(this.task);
		assertTrue(this.task.has("name"));
	}

	public void testGetAssignedUserId() {
		assertNotNull(this.task);
		assertTrue(this.task.has("assignedTo"));
	}

	public void testGetAssignedUserName() {
		assertNotNull(this.task);
		// get the assigned user.
		String userId = "";
		try {
			userId = TestHelpers.getFirebaseSync(this.itemUrl + ".json")
					.getString("assignedTo");
		} catch (Exception exception) {
			fail();
		}
		JSONObject json;
		// Test that that is a valid user.
		try {
			json = TestHelpers.getFirebaseSync(firebaseUrl
					+ "users.json?shallow=true");
			assertTrue(json.has(userId));
		} catch (Exception e) {
			fail(e.getMessage());
		}

	}

	public void testGetStatus() {
		assertNotNull(this.task);
		assertTrue(this.task.has("status"));
	}

	public void testGetAddedLines() {
		assertNotNull(this.task);
		assertTrue(this.task.has("added_lines_of_code"));
	}

	public void testGetRemovedLines() {
		assertNotNull(this.task);
		assertTrue(this.task.has("removed_lines_of_code"));
	}

	public void testGetTotalLines() {
		assertNotNull(this.task);
		assertTrue(this.task.has("total_lines_of_code"));
	}

	public void testSetStatus() {
		try {
			String originalStatus = TestHelpers.getFirebaseSync(
					this.itemUrl + ".json").getString("status");
			String status = "Closed";
			TestHelpers
					.patchFirebaseSync(this.itemUrl + ".json",
							"{ \"status\":\"" + status + "\" }");
			String newStatus = TestHelpers.getFirebaseSync(
					this.itemUrl + ".json").getString("status");
			assertEquals(status, newStatus);
			TestHelpers
					.patchFirebaseSync(this.itemUrl + ".json",
							"{ \"status\":\"" + originalStatus + "\" }");
		} catch (Exception e) {
			fail();
		}
		
	}
}
