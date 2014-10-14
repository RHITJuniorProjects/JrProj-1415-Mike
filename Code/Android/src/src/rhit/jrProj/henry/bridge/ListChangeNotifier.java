package rhit.jrProj.henry.bridge;

import android.widget.ArrayAdapter;

/**
 * 
 * The callback for a dynamic item in an android listview container
 *
 * @author rockwotj. Created Oct 10, 2014.
 */
public class ListChangeNotifier<T> {

	private ArrayAdapter<T> adapter;

	public ListChangeNotifier(ArrayAdapter<T> adapter) {
		this.adapter = adapter;
	}
	/**
	 * 
	 * The method that will be triggered when data is changed in the object.
	 *
	 */
	public void onChange() {
		this.adapter.notifyDataSetChanged();
	}

}
