package rhit.jrProj.henry.firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

	public Task(Parcel pc) {
		// TODO Auto-generated constructor stub.
	}

	public int describeContents() {
		// TODO Auto-generated method stub.
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub.

	}

	public static final Parcelable.Creator<Task> Creator = new Parcelable.Creator<Task>() {

		public Task createFromParcel(Parcel pc) {
			return new Task(pc);
		}

		public Task[] newArray(int size) {
			return new Task[size];
		}
	};
}
