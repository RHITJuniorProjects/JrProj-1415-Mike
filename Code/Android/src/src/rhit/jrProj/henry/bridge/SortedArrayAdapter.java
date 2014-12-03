package rhit.jrProj.henry.bridge;

import java.util.List;

import rhit.jrProj.henry.firebase.Enums;
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
	private final Enums.ObjectType type;
	private String usersName;
	public SortedArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects, Enums.ObjectType type) {
		super(context, R.layout.list_image_layout, textViewResourceId, objects);
		this.context=context;
		this.objects=objects;
		this.type=type;
		this.usersName="";
	}
	public SortedArrayAdapter(Context context, int resource, int textViewResourceId, List<T> objects, Enums.ObjectType type, String usersName) {
		super(context, R.layout.list_image_layout, textViewResourceId, objects);
		this.context=context;
		this.objects=objects;
		this.type=type;
		this.usersName=usersName;
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
		    if (this.type==Enums.ObjectType.PROJECT){
		    	Log.i("PRoject*******", "");
		    	img1.setVisibility(View.GONE);
		    	text1.setText(((Project)super.getItem(position)).getName());
		    	if (((Project)super.getItem(position))!=null){
		    	text2.setText("Due: "+((Project)super.getItem(position)).getDueDateFormatted());
		    	
		    	
		    	}
		    }
		    else if(this.type==Enums.ObjectType.MILESTONE){
		    	img1.setVisibility(View.GONE);
		    	text1.setText(((Milestone)super.getItem(position)).getName());
		    	text2.setText("Due: "+((Milestone)super.getItem(position)).getDueDateFormatted());
		    	
		    	
		    }
		    else if (this.type==Enums.ObjectType.TASK){
		    	//this line for testing only remove later
//		    	this.usersName="Adam Michael";
		    	Task t= (Task)super.getItem(position);
		    	if ((t.getAssignedUserName()).equals(this.usersName)){
		    		if (t.getStatus().equals(Enums.CLOSED)){
		    			img1.setImageResource(R.drawable.ic_action_achievement);
		    		}
		    		else{
		    			img1.setImageResource(R.drawable.ic_action_flag);
		    		}
		    		img1.setVisibility(View.VISIBLE);			    	
			    }
		    	else{
		    		img1.setVisibility(View.GONE);
		    	}
		    	text1.setText(t.getName());
		    	text2.setText("Assigned to: "+t.getAssignedUserName());
		    	text1.setTextSize(20);
		    	text2.setTextSize(14);
		    	
		    	
		    	view.refreshDrawableState();
			    
			    

		    }
		    
		    return view;
			}

	}


