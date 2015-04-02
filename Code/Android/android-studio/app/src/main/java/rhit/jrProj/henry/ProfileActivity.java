package rhit.jrProj.henry;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Trophy;

public class ProfileActivity extends Activity implements ChangeNotifier {

    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mPointTextView;
    private TrophyGridViewAdapter mAdapter;
    private Member mMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAdapter = new TrophyGridViewAdapter(this);
        GridView trophies = (GridView) findViewById(R.id.trophies);
        trophies.setAdapter(mAdapter);
        mNameTextView = (TextView) findViewById(R.id.user_name);
        mEmailTextView = (TextView) findViewById(R.id.user_email);
        mPointTextView = (TextView) findViewById(R.id.user_points);

        final String fireBaseUrl = GlobalVariables.getFirebaseUrl();

        String userKey = getIntent().getStringExtra("USER");
        mMember = new Member(fireBaseUrl + "users/" + userKey);
        mMember.setListChangeNotifier(this);
    }



    private void updateName(String name) {
        mNameTextView.setText(name);
    }

    private void updateEmail(String email) {
        mEmailTextView.setText(email);
    }

    private void updatePoints(int newPoints) {
        mPointTextView.setText("Total Points: " + newPoints);
    }

    @Override
    public void onChange() {
        Log.d("RHH", "onProfileActivityChange!");
        updateName(mMember.getName());
        updateEmail(mMember.getEmail());
        updatePoints(mMember.getTotalPoints());
        mAdapter.setTrophies(mMember.getTrophies());
        mAdapter.notifyDataSetChanged();
    }
}
