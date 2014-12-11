package tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import junit.framework.TestCase;

public class ProjectSortTest extends TestCase {

	private ArrayList<String> projects;
	private Comparator<String> compAZ;
	private Comparator<String> compZA;

	@Override
	protected void setUp() throws Exception {

		this.projects = new ArrayList<String>();
		this.projects.add("ABC");
		this.projects.add("Project B");
		this.projects.add("Android Project");
		this.projects.add("IOS Project");
		
		this.compAZ = new Comparator<String>() {
			public int compare(String lhs, String rhs) {
				if (lhs instanceof String && rhs instanceof String) {
					return ((String) lhs).compareToIgnoreCase((String) rhs);
				}
				return 0;
			}
		};
		
		this.compZA = new Comparator<String>() {
			public int compare(String lhs, String rhs) {
				if (lhs instanceof String && rhs instanceof String) {
					return -1
							* ((String) lhs).compareToIgnoreCase((String) rhs);
				}
				return 0;
			}
		};
	}

	public void testSortSmall() {
		Collections.sort(projects, compAZ);
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
		assertEquals(answer, this.projects);
	}

	public void testSortWithLowerCase() {
		this.projects = new ArrayList<String>();
		this.projects.add("bagels");
		this.projects.add("ABC");
		this.projects.add("Project B");
		this.projects.add("Android Project");
		this.projects.add("iOS Project");

		Collections.sort(projects, compAZ);
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("ABC");
		answer.add("Android Project");
		answer.add("bagels");
		answer.add("iOS Project");
		answer.add("Project B");
		assertEquals(answer, this.projects);

	}
	
	public void testSortWithNumbers() {
		this.projects = new ArrayList<String>();
		this.projects.add("Android Project 1");
		this.projects.add("Bagels 1");
		this.projects.add("Bagels 3");
		this.projects.add("iOS project 0");
		this.projects.add("bagels 2");
		this.projects.add("Android Project 0");
		this.projects.add("iOS Project 2");
		Collections.sort(this.projects, compAZ);
		
		ArrayList<String> answer = new ArrayList<String>();
		answer.add("Android Project 0");
		answer.add("Android Project 1");
		answer.add("Bagels 1");
		answer.add("bagels 2");
		answer.add("Bagels 3");
		answer.add("iOS project 0");
		answer.add("iOS Project 2");
		assertEquals(answer, this.projects);
	}
	
	public void testSortReverseAlphabetical() {
		
		this.projects = new ArrayList<String>();
		this.projects.add("Android Project 1");
		this.projects.add("Bagels 1");
		this.projects.add("Bagels 3");
		this.projects.add("iOS project 0");
		this.projects.add("bagels 2");
		this.projects.add("Android Project 0");
		this.projects.add("iOS Project 2");
		
		Collections.sort(this.projects, compZA);
		ArrayList<String> answer = new ArrayList<String>();
		
		answer.add("iOS Project 2");
		answer.add("iOS project 0");
		answer.add("Bagels 3");
		answer.add("bagels 2");
		answer.add("Bagels 1");
		answer.add("Android Project 1");
		answer.add("Android Project 0");
		assertEquals(answer, this.projects);
	}
}
