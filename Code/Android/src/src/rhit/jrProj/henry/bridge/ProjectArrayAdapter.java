package rhit.jrProj.henry.bridge;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
/**
 * 
 * An ArrayAdapter subclass designed for the project list. It is used for sorting.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class ProjectArrayAdapter<T> extends ArrayAdapter<T> {

	public ProjectArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}

}
