package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

/**
 * 
 * @author Tyler Rockwood
 * 
 *  Mapping data structure used to associate projects with a user's role. 
 *
 */

public class ProjectMap<Key, Value> {

	/**
	 * ArrayList of projects. Parallel with user roles. 
	 */
	private ArrayList<Key> keys = new ArrayList<Key>();

	/**
	 * ArrayList of roles. Parallel with user projects.
	 */
	private ArrayList<Value> values = new ArrayList<Value>();

	/**
	 * Return the user's role for the given project.
	 */
	public Value getValue(Key k) {
		int index = this.keys.indexOf(k);
		return this.values.get(index);
	}

	/**
	 * Return all roles for the user.
	 */
	public ArrayList<Value> getAllValues() {
		return this.values;
	}
	
	/**
	 * Return all projects for the user.
	 */
	public ArrayList<Key> getAllKeys() {
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
	public boolean put(Key k, Value v) {
		if (this.keys.contains(k)) {
			return false;
		}
		this.keys.add(k);
		this.values.add(v);
		return true;
	}

	/**
	 * 
	 * Remove a project and associated role from the map.
	 * 
	 * @param p
	 * @return
	 */
	public boolean remove(Key k) {
		if (!this.keys.contains(k)) {
			return false;
		}
		int index = this.keys.indexOf(k);
		this.keys.remove(index);
		this.values.remove(index);
		return true;
	}
}
