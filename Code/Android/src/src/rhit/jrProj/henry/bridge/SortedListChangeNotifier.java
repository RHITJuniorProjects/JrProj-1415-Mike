package rhit.jrProj.henry.bridge;

import java.util.Comparator;

import android.util.Log;
import android.widget.SimpleAdapter;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Task;

/**
 * 
 * The callback for a dynamic item in an android listview container designed for sorting of projects.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class SortedListChangeNotifier<T> extends ListChangeNotifier<T> {
	
	/**
	 * A simple string that stores what sorting mode the user chose
	 */
	private String sortType="A-Z";
	/**
	 * Stores the comparator so it is not recreated every time.
	 */
	private Comparator<T> c;
	/**
	 * This is a temporary variable that will be removed once Task does not use a SimpleAdapter
	 */
	private boolean simple =false;
	/**
	 * Standard constructor
	 * @param adapter
	 */
	public SortedListChangeNotifier(SortedArrayAdapter<T> adapter) {
		super(adapter);
		createComparator();
		
	}
	/**
	 * Standard constructor
	 * @param adapter
	 */
	public SortedListChangeNotifier(SortedArrayAdapter<T> adapter, String sorttype){
		super(adapter);
		if (sorttype!=null){
			this.sortType=sorttype;
		}
		createComparator();
		
	}
	/**
	 * constructor using SimpleAdapter. Currently only used by Task. Will be removed eventually
	 * @param adapter
	 */
	public SortedListChangeNotifier(SimpleAdapter adapter, String sorttype){
		super(adapter);
		if (sorttype!=null){
			this.sortType=sorttype;
			simple=true;
		}
		createComparator();
	}
	/**
	 * This method is called when the user selects a new sorting mode.
	 * @param sorttype
	 */
	public void changeSorting(String sorttype){
		if (!(sorttype.equals(this.sortType))){
			this.sortType=sorttype;
			createComparator();
			onChange();
		}
	}
	/**
	 * sets this.c to be specific comparators based on the type of sorting requested.
	 */
	private void createComparator(){
		if (this.sortType.equals("A-Z")){
			this.c=new Comparator<T>(){
				@Override
				public int compare(T lhs, T rhs) {
					if (lhs instanceof Project && rhs instanceof Project){
						return ((Project) lhs).compareToIgnoreCase((Project) rhs);
						}
					else if (lhs instanceof Milestone && rhs instanceof Milestone){
						return ((Milestone) lhs).compareToIgnoreCase((Milestone) rhs);
						}
					else if (lhs instanceof Task && rhs instanceof Task){
						return ((Task) lhs).compareToIgnoreCase((Task) rhs);
						}
					return 0;
					}
				};
		}
		else if (this.sortType.equals("Z-A")){
			this.c=new Comparator<T>(){
				@Override
				public int compare(T lhs, T rhs) {
					if (lhs instanceof Project && rhs instanceof Project){
						Log.i("SLCN", "PROJECT");
						return -1*((Project) lhs).compareToIgnoreCase((Project) rhs);
						}
					else if (lhs instanceof Milestone && rhs instanceof Milestone){
						Log.i("SLCN", "MILESTONE");
						return -1*((Milestone) lhs).compareToIgnoreCase((Milestone) rhs);
						}
					else if (lhs instanceof Task && rhs instanceof Task){
						Log.i("SLCN", "TASK");
						return -1*((Task) lhs).compareToIgnoreCase((Task) rhs);
						}
					return 0;
					}
				};
		}
		else if (this.sortType.equals("Newest First")){
			//Not yet implemented
			//Standard date form needed!
			this.c=new Comparator<T>(){
				@Override
				public int compare(T lhs, T rhs) {
					return 0;
					}
				};
		}
		else if (this.sortType.equals("Oldest First")){
			//Not yet implemented
			//Standard date form needed!
			this.c=new Comparator<T>(){
				@Override
				public int compare(T lhs, T rhs) {
					return 0;
					}
				};
		}
		else{
			//Not yet implemented
			//Standard date form needed!
			this.c=new Comparator<T>(){
				@Override
				public int compare(T lhs, T rhs) {
					return 0;
					}
				};
		}
	}
	

	/**
	 * 
	 * The method that will be triggered when data is changed in the object. 
	 * This method sorts on every change.
	 *
	 */
	public void onChange() {
		Log.i("sortmode", this.sortType);
		if (!simple){
		((SortedArrayAdapter<T>) super.getAdapter()).sort(this.c);
		super.getAdapter().notifyDataSetChanged();
		}
		else{
		super.getAdapter().notifyDataSetChanged();
		}
	}
}