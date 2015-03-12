package rhit.jrProj.henry.firebase;

import rhit.jrProj.henry.R;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Trophy.ChildrenListener;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;
import rhit.jrProj.henry.helpers.HorizontalPicker;
import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Trophy {
	private int cost;
	private String name;
	private String description;
	Firebase firebase;
	private String id;
	private int iconImagePath=R.drawable.ic_action_achievement; //R.drawable.whatever
	
	

	
	/**
	 * This is the class that onChange is called from to when a field in
	 * Firebase is updated. This then notifies the object that is displaying the
	 * task that this object has been updated.
	 */
	private ListChangeNotifier<Trophy> listViewCallback;
	
	
	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Trophy> CREATOR = new Parcelable.Creator<Trophy>() {

		public Trophy createFromParcel(Parcel pc) {
			return new Trophy(pc);
		}

		public Trophy[] newArray(int size) {
			return new Trophy[size];
		}
	};

	/**
	 * Creates a new Trophy from a parcel
	 * 
	 * @param pc
	 */
	Trophy(Parcel pc) {
		String firebaseURL = pc.readString();
		this.firebase = new Firebase(firebaseURL);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		this.name = pc.readString();
		this.description = pc.readString();
		this.cost = pc.readInt();
		this.iconImagePath=pc.readInt();
	}

	public Trophy(String firebaseURL) {
		this.firebase = new Firebase(firebaseURL);
		this.id=firebaseURL
				.substring(firebaseURL.lastIndexOf('/') + 1);
		this.firebase.addChildEventListener(new ChildrenListener(this));
		
	}
	

	public int describeContents() {
		return 0;
	}

	/**
	 * 
	 * Sets a new list changed notifier
	 * 
	 * @param lcn
	 */
	public void setListChangeNotifier(ListChangeNotifier<Trophy> lcn) {
		this.listViewCallback = lcn;
	}
	/**
	 * 
	 * gets the list changed notifier
	 * 
	 * @param lcn
	 */
	public ListChangeNotifier<Trophy> getListChangeNotifier() {
		return this.listViewCallback;
	}

	/**
	 * Passes the Firebase URL, the name of the task and the description of the
	 * task to the next view
	 */
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.firebase.toString());
		dest.writeString(this.name);
		dest.writeString(this.description);
		dest.writeInt(this.cost);
		dest.writeInt(this.iconImagePath);
	}

	/**
	 * Gets the name of a Task
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/**
	 * Determines if a task is equal to another task
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Task) {
			return ((Task) o).firebase.toString().equals(
					this.firebase.toString());
		}
		return false;
	};

	/**
	 * Gets the description of the task
	 * 
	 * @return the description of the task
	 */
	public String getDescription() {
		return this.description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * returns the Trophy's id
	 * @return
	 */
	public String getID(){
		return this.id;
	}
	public int getIconImagePath(){
		return this.iconImagePath;
	}
	public void setIconImagePath(int s){
		this.iconImagePath=s;
	}
	


	

	/**
	 * retrieves the points value for this task
	 */
	public int getCost() {
		return this.cost;
	}
	/**
	 * Task listener
	 */
	class ChildrenListener implements ChildEventListener {
		/**
		 * Task Object
		 */
		Trophy trophy;

		/**
		 * Creates a new ChildrenListener
		 * 
		 * @param t
		 */
		public ChildrenListener(Trophy b) {
			this.trophy = b;
		}

		public void onCancelled(FirebaseError arg0) {
			// Do nothing
		}

		/**
		 * Fills in the new point's properties.
		 */
		public void onChildAdded(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("name")) {
				this.trophy.name=arg0.getValue().toString();
			}else if (arg0.getKey().equals("description")){
				this.trophy.description=arg0.getValue().toString();
			}else if (arg0.getKey().equals("cost")){
				this.trophy.cost=arg0.getValue(Integer.class);
			}
			if (this.trophy.listViewCallback != null) {
				this.trophy.listViewCallback.onChange();
			}
			
		}

		/**
		 * This will be called when the points data in Firebase is updated
		 */
		public void onChildChanged(DataSnapshot arg0, String arg1) {
			if (arg0.getKey().equals("name")) {
				this.trophy.name=arg0.getValue().toString();
			}else if (arg0.getKey().equals("description")){
				this.trophy.description=arg0.getValue().toString();
			}else if (arg0.getKey().equals("cost")){
				this.trophy.cost=arg0.getValue(Integer.class);
			}
			if (this.trophy.listViewCallback != null) {
				this.trophy.listViewCallback.onChange();
			}
		}

		/**
		 * Might do something here for the tablet
		 */
		public void onChildMoved(DataSnapshot arg0, String arg1) {
			// TODO Auto-generated method stub.
		}

		/**
		 * Do nothing
		 */
		public void onChildRemoved(DataSnapshot arg0) {
			// TODO Auto-generated method stub.

		}
	}

	/**
	 * Compares this Trophy with the other given Trophy.
	 * 
	 * @param p
	 * @return
	 */
	public int compareToIgnoreCase(Trophy p) {
		return GeneralAlgorithms.compareToIgnoreCase(this.getName(),
				p.getName());
	}
	public int compareToByPoints(Trophy p){
		return ((Integer)this.cost).compareTo((Integer)p.getCost());
	}
	
	


	public int convertLimitFromFirebaseForm(Object limitString){
		try{
			return (Integer) limitString;
		}catch(java.lang.ClassCastException e){
			if (limitString instanceof String && limitString.toString().equals("None")){
				return -1;
			}
			else{
				return -10;
			}
		}
	}
	public Object convertLimitToFirebaseForm(int limit){
		if (limit==-1){
			return "None";
		}
		else{
			return limit;
		}
	}

}
