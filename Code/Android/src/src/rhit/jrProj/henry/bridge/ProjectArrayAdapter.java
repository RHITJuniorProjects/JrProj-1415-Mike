package rhit.jrProj.henry.bridge;

import java.util.Comparator;
import java.util.List;

import rhit.jrProj.henry.firebase.Project;
import android.content.Context;
import android.widget.ArrayAdapter;

public class ProjectArrayAdapter<T> extends ArrayAdapter<T> {

	public ProjectArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}

}
