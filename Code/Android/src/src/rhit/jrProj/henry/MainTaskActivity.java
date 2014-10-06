package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainTaskActivity extends Activity {

	protected static ArrayList<TaskContent.Task> taskList = new ArrayList<TaskContent.Task>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Firebase f = new Firebase(
				"https://sizzling-inferno-2459.firebaseio.com/");
		f.addValueEventListener(new ValueEventListener() {

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDataChange(DataSnapshot arg0) {
				// nothing.

			}

		});

		// Retrieve new posts as they are added to Firebase
		f.addChildEventListener(new ChildEventListener() {
			// Retrieve new posts as they are added to Firebase
			@Override
			public void onChildAdded(DataSnapshot snapshot,
					String previousChildName) {

				for (DataSnapshot child : snapshot.getChildren()) {
					MainTaskActivity.taskList.add(new TaskContent.Task(
							new Integer(taskList.size()).toString(), child
									.getValue().toString()));
				}
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildChanged(DataSnapshot snapshot,
					String previousChildName) {
				String title = (String) snapshot.child("title").getValue();
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onChildRemoved(DataSnapshot snapshot) {
				// String title = (String) snapshot.child("title").getValue();
				MainTaskActivity.taskList.remove(0);
				TaskContent.removeItem(taskList.get(0));

			}
		});
	}

	public void tasks(View view) {
		Intent intent = new Intent(this, TaskListActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static ArrayList<TaskContent.Task> getTaskList() {
		return taskList;
	}
}
