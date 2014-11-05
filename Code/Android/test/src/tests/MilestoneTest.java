package tests;

import json.JSONObject;
import junit.framework.TestCase;
import rhit.jrProj.henry.MainActivity;

/**
 * 
 * Tests the Milestone class by ensuring that firebase data is correctly
 * structured.
 *
 * @author rockwotj. Created Nov 3, 2014.
 */
public class MilestoneTest extends TestCase {

	/*
	 * The first milestone of the first Project in the FirebaseURL Repo
	 */
	private JSONObject milestone;
	private String itemUrl;

	public MilestoneTest(String name) {
		super(name);
		try {
			JSONObject json = TestHelpers
					.getFirebaseSync(MainActivity.firebaseUrl
							+ ".json?shallow=true");
			assertEquals(json.get("projects"), Boolean.TRUE);
			json = TestHelpers.getFirebaseSync(MainActivity.firebaseUrl
					+ "projects.json?shallow=true");
			String projectKey = TestHelpers.getFirstKey(json);
			json = TestHelpers.getFirebaseSync(MainActivity.firebaseUrl
					+ String.format("projects/%s/.json?shallow=true",
							projectKey));
			assertEquals(json.get("milestones"), Boolean.TRUE);
			json = TestHelpers.getFirebaseSync(MainActivity.firebaseUrl
					+ String.format("projects/%s/milestones.json?shallow=true",
							projectKey));
			String milestoneKey = TestHelpers.getFirstKey(json);
			this.itemUrl = MainActivity.firebaseUrl
					+ String.format("projects/%s/milestones/%s", projectKey,
							milestoneKey);
			this.milestone = TestHelpers.getFirebaseSync(this.itemUrl
					+ ".json?shallow=true");
			System.out.println(this.itemUrl);
		} catch (Exception e) {
			// Do nothing, the tests will catch
		}
	}

	public void testGetTasks() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("tasks"));
	}

	public void testReplaceName() {
		// fail("Not yet implemented");
	}

	public void testReplaceDescription() {
		// fail("Not yet implemented");
	}

	public void testGetDueDate() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("due_date"));
	}

	public void testSetDueDate() {
		// fail("Not yet implemented");
	}

	public void testGetDescription() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("description"));
	}

	public void testGetTaskPercent() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("task_percent"));
	}

	public void testSetTaskPercent() {
		// fail("Not yet implemented");
	}

	public void testSetDescription() {
		// fail("Not yet implemented");
	}

	public void testGetName() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("name"));
	}

	public void testSetName() {
		// fail("Not yet implemented");
	}

	public void testGetAddedLines() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("added_lines_of_code"));
	}

	public void testGetRemovedLines() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("removed_lines_of_code"));
	}

	public void testGetTotalLines() {
		assertNotNull(this.milestone);
		assertTrue(this.milestone.has("total_lines_of_code"));
	}
}
