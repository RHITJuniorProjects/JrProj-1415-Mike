package rhit.jrProj.henry.firebase;

import java.util.ArrayList;

/**
 * Mapping data structure used to associate projects with a user's role.
 */

public class Map<Key, Value> {

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
     * Replaces the value of a preexisting key
     *
     * @param k
     * @param v
     * @return
     */
    public boolean replaceValue(Key k, Value v) {
        if (!this.keys.contains(k)) {
            return false;
        }
        int index = this.keys.indexOf(k);
        this.values.set(index, v);
        return true;
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

    /**
     * Gets the size of the map.
     *
     * @return
     */
    public int size() {
        return this.keys.size();
    }

    /**
     * That no keys or values were added of removed on the underlying lists (via the getAll methods).
     * However if values where replaced than this will return true.
     *
     * @return
     */
    public boolean verify() {
        return this.keys.size() == this.values.size();
    }

    /**
     * Returns true if the key is in the collection.
     *
     * @param k
     * @return
     */
    public boolean containsKey(Key k) {
        return this.keys.contains(k);
    }
}
