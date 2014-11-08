package rhit.jrProj.henry.bridge;

import android.util.Log;
import android.widget.BaseAdapter;

/**
 * 
 * The callback for a dynamic item in an android listview container
 *
 * @author rockwotj. Created Oct 10, 2014.
 */
public class ListChangeNotifier<T> {

	private BaseAdapter adapter;

	public ListChangeNotifier(BaseAdapter adapter) {
		this.adapter = adapter;
	}
	/**
	 * 
	 * The method that will be triggered when data is changed in the object.
	 *
	 */
	public void onChange() {
		Log.i("ListCN called", "true");
		this.adapter.notifyDataSetChanged();
	}
	public BaseAdapter getAdapter(){
		return this.adapter;
	}

}
