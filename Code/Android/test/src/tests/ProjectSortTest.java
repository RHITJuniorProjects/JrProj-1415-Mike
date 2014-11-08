package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import junit.framework.TestCase;

public class ProjectSortTest extends TestCase {

	private ArrayList<String> projects;
	private Comparator<String> comp;

	@Override
	protected void setUp() throws Exception {

		this.projects = new ArrayList<String>();
		this.projects.add("ABC");
		this.projects.add("Project B");
		this.projects.add("Android Project");
		this.projects.add("IOS Project");
		this.comp = new Comparator<String>() {

			public int compare(String lhs, String rhs) {

				if (lhs instanceof String && rhs instanceof String) {
					return ((String) lhs).compareToIgnoreCase((String) rhs);
				}
				return 0;

			}

		};

	}

	public void testSortSmall() {
		Collections.sort(projects, comp);
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("ABC");
		answer.add("Android Project");
		answer.add("IOS Project");
		answer.add("Project B");
		assertEquals(answer, this.projects);
	}

	public void testSortLarge() {
		this.projects.add("Henry");
		this.projects.add("Bravo");
		this.projects.add("Jenkins");
		this.projects.add("Swift");
		this.projects.add("Coffee");
		this.projects.add("Tablet");
		this.projects.add("Xilinx");
		this.projects.add("Drive");
		Collections.sort(this.projects);

		ArrayList<String> answer = new ArrayList<String>();
		answer.add("ABC");
		answer.add("Android Project");
		answer.add("Bravo");
		answer.add("Coffee");
		answer.add("Drive");
		answer.add("Henry");
		answer.add("IOS Project");
		answer.add("Jenkins");
		answer.add("Project B");
		answer.add("Swift");
		answer.add("Tablet");
		answer.add("Xilinx");
		System.out.println(this.projects);
		System.out.println(answer);
		assertEquals(answer, this.projects);
	}

	public void testSortWithLowerCase() {
		this.projects = new ArrayList<String>();
		this.projects.add("bagels");
		this.projects.add("ABC");
		this.projects.add("Project B");
		this.projects.add("Android Project");
		this.projects.add("iOS Project");

		Collections.sort(projects, comp);
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("ABC");
		answer.add("Android Project");
		answer.add("bagels");
		answer.add("iOS Project");
		answer.add("Project B");
		assertEquals(answer, this.projects);
		
	}
}
