package rhit.jrProj.henry.bridge;

import java.util.List;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import rhit.jrProj.henry.R;

/**
 * 
 * An ArrayAdapter subclass designed for the project list. It is used for sorting.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class SortedArrayAdapter<T> extends ArrayAdapter<T> {
	private final Context context;
	private final List<T> objects;
	private final int type;
	public SortedArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects, int type) {
		super(context, R.layout.list_image_layout, textViewResourceId, objects);
		this.context=context;
		this.objects=objects;
		this.type=type;
	}
	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}

		@Override
		  public View getView(int position, View convertView, android.view.ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = inflater.inflate(R.layout.list_image_layout, parent, false);
		    TextView text1 = (TextView) view.findViewById(R.id.firstline);
		    TextView text2 = (TextView) view.findViewById(R.id.secondline);
		    ImageView img1= (ImageView) view.findViewById(R.id.imageView);
		    if (this.type==0){
		    	Log.i("PRoject*******", "");
		    	img1.setVisibility(View.GONE);
		    	text1.setText(((Project)super.getItem(position)).getName());
		    	if (((Project)super.getItem(position))!=null){
		    	text2.setText("Due: "+((Project)super.getItem(position)).getDueDateFormatted());
		    	
		    	
		    	}
		    }
		    else if(this.type==1){
		    	img1.setVisibility(View.GONE);
		    	text1.setText(((Milestone)super.getItem(position)).getName());
		    	text2.setText("Due: "+((Milestone)super.getItem(position)).getDueDateFormatted());
		    	
		    	
		    }
		    else if (this.type==2){
		    	if ((((Task)super.getItem(position)).getAssignedUserName()).equals("Adam Michael")){
			    	img1.setVisibility(View.VISIBLE);
//			    	img1.setImageResource(android.R.drawable.ic_menu_myplaces);
			    }
		    	text1.setText(((Task)super.getItem(position)).getName());
		    	text2.setText("Assigned to: "+((Task)super.getItem(position)).getAssignedUserName());
		    	text1.setTextSize(20);
		    	text2.setTextSize(14);
		    	
		    	
		    	view.refreshDrawableState();
			    
			    

		    }
		    
		    return view;
			}

	}


