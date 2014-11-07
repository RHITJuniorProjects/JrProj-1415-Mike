package rhit.jrProj.henry.bridge;

import java.util.Comparator;

import rhit.jrProj.henry.firebase.Project;

/**
 * 
 * The callback for a dynamic item in an android listview container designed for sorting of projects.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class ProjectListChangeNotifier<T> extends ListChangeNotifier<T> {

	public ProjectListChangeNotifier(ProjectArrayAdapter<T> adapter) {
		super(adapter);
	}
	
	/**
	 * 
	 * The method that will be triggered when data is changed in the object. This method sorts on every change.
	 *
	 */
	public void onChange() {
		((ProjectArrayAdapter<T>) super.getAdapter()).sort(new Comparator<T>(){

			public int compare(T lhs, T rhs) {
				if (lhs instanceof Project && rhs instanceof Project){
					return ((Project) lhs).compareToIgnoreCase((Project) rhs);
				}
				return 0;
			}
			
			
		});
		super.getAdapter().notifyDataSetChanged();
	}
}