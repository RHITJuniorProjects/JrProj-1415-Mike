package rhit.jrProj.henry.bridge;

import java.util.Comparator;

import rhit.jrProj.henry.firebase.Project;
import android.widget.BaseAdapter;

/**
 * 
 * The callback for a dynamic item in an android listview container
 *
 * @author rockwotj and daveyle. Created Oct 10, 2014.
 */
public class ProjectListChangeNotifier<T> extends ListChangeNotifier<T> {

	private ProjectArrayAdapter<T> adapter;

	public ProjectListChangeNotifier(ProjectArrayAdapter<T> adapter) {
		super(adapter);
	}
	/**
	 * 
	 * The method that will be triggered when data is changed in the object.
	 *
	 */
	public void onChange() {
		((ProjectArrayAdapter<T>) super.getAdapter()).sort(new Comparator<T>(){
			@Override
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