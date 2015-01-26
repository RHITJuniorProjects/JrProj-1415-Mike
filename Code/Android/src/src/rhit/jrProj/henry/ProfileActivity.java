package rhit.jrProj.henry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ProfileActivity extends Activity {

	private TextView mNameTextView;
	private TextView mEmailTextView;
	private TextView mPointTextView;
	private GlobalVariables mGlobalVariables;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		mGlobalVariables = ((GlobalVariables) getApplicationContext());

		mNameTextView = (TextView) findViewById(R.id.user_name);
		mEmailTextView = (TextView) findViewById(R.id.user_email);
		mPointTextView = (TextView) findViewById(R.id.user_points);

		String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
		Firebase firebase = new Firebase(fireBaseUrl);

		String userKey = firebase.getAuth().getUid();

		// String userKey = mGlobalVariables.getUser().getKey();
		firebase.child("users").child(userKey)
				.addChildEventListener(new ChildEventListener() {

					@Override
					public void onCancelled(FirebaseError arg0) {
						// do nothing
					}

					@Override
					public void onChildAdded(DataSnapshot arg0, String arg1) {
						updateUI(arg0);
					}

					@Override
					public void onChildChanged(DataSnapshot arg0, String arg1) {
						updateUI(arg0);
					}

					@Override
					public void onChildMoved(DataSnapshot arg0, String arg1) {
						// do nothing
					}

					@Override
					public void onChildRemoved(DataSnapshot arg0) {
						// do nothing
					}

					public void updateUI(DataSnapshot arg0) {
						if (arg0.getKey().equals("name")) {
							updateName(arg0.getValue(String.class));
						} else if (arg0.getKey().equals("email")) {
							updateEmail(arg0.getValue(String.class));
						} else if (arg0.getKey().equals("total_points")) {
							updatePoints(arg0.getValue(Integer.class));
						}
					}

				});

	}

	private void updateName(String name) {
		mNameTextView.setText(name);
	}

	private void updateEmail(String email) {
		mEmailTextView.setText(email);
	}

	private void updatePoints(int newPoints) {
		mPointTextView.setText("Points: " + newPoints);
	}
}
