package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class Task implements Parcelable, ChildEventListener {
	
	private String name;
	
	private String description;
	
	//private User assignedUser;
	
	//private Category category;
	
	/**
	 * A Creator object that allows this object to be created by a parcel
	 */
	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

		public Task createFromParcel(Parcel pc) {
			return new Task(pc);
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
	
	public Task(int number)
	{
		this.taskNumber = number;
	}
	
	Task(Parcel pc) {
		this.taskNumber = pc.readInt();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.taskNumber);
	}
	
	@Override
	public String toString() {
		return "Task " + this.taskNumber;
	}

	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub.
		
	}

	public void onChildAdded(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.
		
	}

	public void onChildChanged(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.
		
	}

	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub.
		
	}

	public void onChildRemoved(DataSnapshot arg0) {
		// TODO Auto-generated method stub.
		
	}
}
