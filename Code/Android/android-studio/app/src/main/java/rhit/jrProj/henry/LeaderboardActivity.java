package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.bridge.ChangeNotifiable;
import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.bridge.Leaderboard;
import rhit.jrProj.henry.helpers.Checkers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
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

public class LeaderboardActivity extends Activity implements ChangeNotifier<Leaderboard> {

    private Leaderboard mLeaderboardObject;

    private GlobalVariables mGlobalVariables;

    private TableLayout mLeaderboard;

    private String mUserKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        mGlobalVariables = ((GlobalVariables) getApplicationContext());

        mLeaderboard = (TableLayout) findViewById(R.id.leaderboard);

        String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
        Firebase firebase = new Firebase(fireBaseUrl);

        mUserKey = firebase.getAuth().getUid();

        Firebase usersRef = firebase.child("users");
        this.mLeaderboardObject = new Leaderboard(usersRef);
        this.mLeaderboardObject.setChangeNotifier(this);

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

    private void updateDisplay(Map<String, Leaderboard.LeaderboardUser> users) {
        Collection<Leaderboard.LeaderboardUser> usersCollection = users.values();
        List<Leaderboard.LeaderboardUser> usersList = new ArrayList<Leaderboard.LeaderboardUser>(
                usersCollection);
        Collections.sort(usersList);

        mLeaderboard.removeAllViews();

        List<TableRow> rows = new ArrayList<TableRow>();
        for (int i = 0; i < 25 && i < usersList.size(); i++) {
            Leaderboard.LeaderboardUser user = usersList.get(i);
            TableRow row = createRow(user, i + 1, user.getId().equals(mUserKey));
            rows.add(row);
            if (user.getId().equals(mUserKey)) {
                TableRow row2 = createRow(user, i + 1,
                        user.getId().equals(mUserKey));
                row2.setPadding(0, 0, 0, dpToPixel(10));
                rows.add(0, row2);

                // mLeaderboard.addView(row);
            }
        }

        for (TableRow row : rows) {
            mLeaderboard.addView(row);
        }
    }

    private TableRow createRow(Leaderboard.LeaderboardUser user, int position, boolean bold) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));

        TextView positionView = new TextView(this);
        positionView.setText("" + position);
        positionView.setWidth((int) (outMetrics.widthPixels * 0.10));
        positionView.setLayoutParams(new TableRow.LayoutParams(1));

        TextView nameView = new TextView(this);
        nameView.setText(user.getName());
        nameView.setWidth((int) (outMetrics.widthPixels * 0.4));
        nameView.setLayoutParams(new TableRow.LayoutParams(2));
        nameView.setGravity(Gravity.LEFT);

        TextView userView = new TextView(this);
        userView.setText("" + user.getTotalPoints());
        userView.setWidth((int) (outMetrics.widthPixels * 0.30));
        userView.setLayoutParams(new TableRow.LayoutParams(3));
        userView.setGravity(Gravity.RIGHT);

        if (bold) {
            positionView.setTypeface(null, Typeface.BOLD);
            nameView.setTypeface(null, Typeface.BOLD);
            userView.setTypeface(null, Typeface.BOLD);

            row.setBackgroundColor(Color.LTGRAY);
        }

        row.addView(positionView);
        row.addView(nameView);
        row.addView(userView);

        return row;
    }

    private int dpToPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onChange() {
        this.updateDisplay(mLeaderboardObject.getUsers());
    }
}
