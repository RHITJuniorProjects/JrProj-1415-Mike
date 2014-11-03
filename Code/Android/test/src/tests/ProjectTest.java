package tests;

import json.JSONObject;
import junit.framework.TestCase;
import rhit.jrProj.henry.MainActivity;

/**
 * 
 * Tests the Project class by ensuring that firebase data is correctly
 * structured.
 *
 * @author rockwotj. Created Nov 3, 2014.
 */
public class ProjectTest extends TestCase {

	/*
	 * The first Project in the FirebaseURL Repo
	 */
	private JSONObject project;

	public ProjectTest(String name) {
		super(name);
		try {
			JSONObject json = TestHelpers
					.callFirebaseSync(MainActivity.firebaseUrl
							+ ".json?shallow=true");
			assertEquals(json.get("projects"), Boolean.TRUE);
			json = TestHelpers.callFirebaseSync(MainActivity.firebaseUrl
					+ "projects.json?shallow=true");
			String projectKey = TestHelpers.getFirstKey(json);
			this.project = TestHelpers
					.callFirebaseSync(MainActivity.firebaseUrl
							+ String.format("projects/%s/.json?shallow=true",
									projectKey));
		} catch (Exception e) {
			//Do nothing, the tests will catch	
		}
	}

	public void testGetMilestones() {
		assertNotNull(this.project);
		assertTrue(this.project.has("milestones"));
	}

	public void testGetName() {
		assertNotNull(this.project);
		assertTrue(this.project.has("name"));
	}

	public void testGetDescription() {
		assertNotNull(this.project);
		assertTrue(this.project.has("description"));
	}

	public void testGetDueDate() {
		assertNotNull(this.project);
		assertTrue(this.project.has("due_date"));
	}

	public void testGetHoursPercent() {
		assertNotNull(this.project);
		assertTrue(this.project.has("hours_percent"));
	}

	public void testGetTasksPercent() {
		assertNotNull(this.project);
		assertTrue(this.project.has("task_percent"));
	}

	public void testGetMilestonesPercent() {
		assertNotNull(this.project);
		assertTrue(this.project.has("milestone_percent"));
	}

	public void testGetAddedLines() {
		assertNotNull(this.project);
		assertTrue(this.project.has("added_lines_of_code"));
	}

	public void testGetRemovedLines() {
		assertNotNull(this.project);
		assertTrue(this.project.has("removed_lines_of_code"));
	}

	public void testGetTotalLines() {
		assertNotNull(this.project);
		assertTrue(this.project.has("total_lines_of_code"));
	}

}
