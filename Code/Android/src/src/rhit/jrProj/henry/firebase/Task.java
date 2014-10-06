package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
	
	private int taskNumber;
	
	public Task(int number)
	{
		this.taskNumber = number;
	}
	
	public Task(Parcel pc) {
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
	};

	public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

		public Task createFromParcel(Parcel pc) {
			return new Task(pc);
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
}
