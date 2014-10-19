package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

/**
 * 
 * @author Tyler Rockwood
 * 
 *  Mapping data structure used to associate projects with a user's role. 
 *
 */

public class ProjectMap {

	/**
	 * ArrayList of projects. Parallel with user roles. 
	 */
	private ArrayList<Project> keys = new ArrayList<Project>();

	/**
	 * ArrayList of roles. Parallel with user projects.
	 */
	private ArrayList<Enums.Role> values = new ArrayList<Enums.Role>();

	/**
	 * Return the user's role for the given project.
	 */
	public Enums.Role getValue(Project key) {
		int index = this.keys.indexOf(key);
		return this.values.get(index);
	}

	/**
	 * Return all roles for the user.
	 */
	public ArrayList<Enums.Role> getAllValues() {
		return this.values;
	}
	
	/**
	 * Return all projects for the user.
	 */
	public ArrayList<Project> getAllKeys() {
		return this.keys;
	}

	/**
	 * 
	 * Add a project with an associated role to the map.
	 * 
	 * @param p
	 * @param r
	 * @return
	 */
	public boolean put(Project p, Enums.Role r) {
		if (this.keys.contains(p)) {
			return false;
		}
		this.keys.add(p);
		this.values.add(r);
		return true;
	}

	/**
	 * 
	 * Remove a project and associated role from the map.
	 * 
	 * @param p
	 * @return
	 */
	public boolean remove(Project p) {
		if (!this.keys.contains(p)) {
			return false;
		}
		int index = this.keys.indexOf(p);
		this.keys.remove(index);
		this.values.remove(index);
		return true;
	}
}
