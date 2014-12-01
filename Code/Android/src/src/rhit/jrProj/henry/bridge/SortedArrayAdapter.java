package rhit.jrProj.henry.bridge;

import java.util.List;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 * 
 * An ArrayAdapter subclass designed for the project list. It is used for sorting.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class SortedArrayAdapter<T> extends ArrayAdapter<T> {
	public SortedArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
	}
	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}

		@Override
		  public View getView(int position, View convertView, android.view.ViewGroup parent) {
			
		    View view = super.getView(position, convertView, parent);
		    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		    TextView text2 = (TextView) view.findViewById(android.R.id.text2);
		    if (super.getItem(position) instanceof Project){
		    	text1.setText(((Project)super.getItem(position)).getName());
		    	if (((Project)super.getItem(position))!=null){
		    	text2.setText("Due: "+((Project)super.getItem(position)).getDueDateFormatted());
		    	}
		    }
		    else if(super.getItem(position) instanceof Milestone){
		    	text1.setText(((Milestone)super.getItem(position)).getName());
		    	text2.setText("Due: "+((Milestone)super.getItem(position)).getDueDateFormatted());
		    }
		    else if (super.getItem(position) instanceof Milestone){
		    	text1.setText(((Task)super.getItem(position)).getName());
			    text2.setText("Assigned to: "+((Task)super.getItem(position)).getAssignedUserName());
		    }
		    return view;
			}

	}


