package rhit.jrProj.henry;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A List item representing a piece of content.
 */
public class Task implements Parcelable {
	public String id;
	public String content;

	public Task(String id, String content) {
		this.id = id;
		this.content = content;
	}

	@Override
	public String toString() {
		return content;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}