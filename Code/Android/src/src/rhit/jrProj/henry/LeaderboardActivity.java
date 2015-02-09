package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LeaderboardActivity extends Activity {

	private GlobalVariables mGlobalVariables;

	private Map<String, LeaderboardUser> mUsers;

	private TableLayout mLeaderboard;

	private TextView mUserPosition;
	private TextView mUserName;
	private TextView mUserValue;
	
	private String mUserKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);

		mGlobalVariables = ((GlobalVariables) getApplicationContext());

		mLeaderboard = (TableLayout) findViewById(R.id.leaderboard);

		mUserPosition = (TextView) findViewById(R.id.user_position);
		mUserName = (TextView) findViewById(R.id.user_name);
		mUserValue = (TextView) findViewById(R.id.user_value);

		mUsers = new HashMap<String, LeaderboardUser>();

		String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
		Firebase firebase = new Firebase(fireBaseUrl);
		
		mUserKey = firebase.getAuth().getUid();

		Firebase usersRef = firebase.child("users");
		usersRef.addChildEventListener(new ChildEventListener() {

			@Override
			public void onCancelled(FirebaseError arg0) {
				// do nothing
			}

			@Override
			public void onChildAdded(DataSnapshot arg0, String arg1) {
				updateUser(arg0);
				updateDisplay();
			}

			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				updateUser(arg0);
				updateDisplay();
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// do nothing
			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// do nothing
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard, menu);
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

	private void updateUser(DataSnapshot arg0) {
		String id = arg0.getKey();
		String name = arg0.child("name").getValue(String.class);
		if (name == null) {
			// TODO: Throw error instead
			return;
		}

		LeaderboardUser user = mUsers.get(id);
		if (user == null) {
			user = new LeaderboardUser(id, name);
			mUsers.put(id, user);
		}

		Integer total_points = arg0.child("total_points").getValue(
				Integer.class);
		if (total_points != null) {
			user.setTotalPoints(total_points);
		} else {
			mUsers.remove(id);
		}
	}

	private void updateDisplay() {
		Collection<LeaderboardUser> usersCollection = mUsers.values();
		List<LeaderboardUser> usersList = new ArrayList<LeaderboardUser>(
				usersCollection);
		Collections.sort(usersList);

		mLeaderboard.removeAllViews();
		
		TableRow userRow = null;
		List<TableRow> rows = new ArrayList<TableRow>();
		for (int i = 0; i < 25 && i < usersList.size(); i++) {
			LeaderboardUser user = usersList.get(i);
			TableRow row = createRow(user, i + 1, user.getName().equals(mUserKey));
			rows.add(row);
			if (user.getName().equals(mUserKey)) {
				userRow = row;
			}
		}
		
		if (userRow != null) {
			mLeaderboard.addView(userRow);
		}
		
		for (TableRow row : rows) {
			mLeaderboard.addView(row);
		}
	}

	private TableRow createRow(LeaderboardUser user, int position, boolean bold) {
		TableRow row = new TableRow(this);
		row.setLayoutParams(new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.MATCH_PARENT));
		
		TextView positionView = new TextView(this);
		positionView.setText("" + position);
		positionView.setLayoutParams(new TableRow.LayoutParams(1));
		
		TextView nameView = new TextView(this);
		nameView.setText(user.getName());
		nameView.setLayoutParams(new TableRow.LayoutParams(2));
		nameView.setGravity(Gravity.LEFT);
		
		TextView userView = new TextView(this);
		userView.setText("" + user.getTotalPoints());
		userView.setWidth(dpToPixel(50));
		userView.setLayoutParams(new TableRow.LayoutParams(3));
		userView.setGravity(Gravity.RIGHT);	
		
		if (bold) {
			positionView.setTypeface(null, Typeface.BOLD);
			nameView.setTypeface(null, Typeface.BOLD);
			userView.setTypeface(null, Typeface.BOLD);
		}
		
		row.addView(positionView);
		row.addView(nameView);
		row.addView(userView);
		
		return row;
	}
	
	private int dpToPixel(int dp) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}

	private class LeaderboardUser implements Comparable<LeaderboardUser> {

		private String mId;
		private String mName;

		private int mTotal_Points;

		public LeaderboardUser(String id, String name) {
			mId = id;
			mName = name;
		}

		public String getName() {
			return mName;
		}

		public int getTotalPoints() {
			return mTotal_Points;
		}

		public void setTotalPoints(int total_points) {
			mTotal_Points = total_points;
		}

		@Override
		public int compareTo(LeaderboardUser another) {
			if (another.getTotalPoints() > this.getTotalPoints()) {
				return 1;
			}
			return -1;
		}

	}
}
