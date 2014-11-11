package rhit.jrProj.henry.bridge;

import java.util.List;

import rhit.jrProj.henry.firebase.Task;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwoLineArrayAdapter<T> extends ArrayAdapter<T> {

	public TwoLineArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
	}
	@Override
	  public View getView(int position, View convertView, android.view.ViewGroup parent) {
		
	    View view = super.getView(position, convertView, parent);
	    TextView text1 = (TextView) view.findViewById(android.R.id.text1);
	    TextView text2 = (TextView) view.findViewById(android.R.id.text2);

	    text1.setText(((Task)super.getItem(position)).getName());
	    text2.setText("Assigned to: "+((Task)super.getItem(position)).getAssignedUserName());
	    return view;
		}
		
	  

}
