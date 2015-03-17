package rhit.jrProj.henry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.firebase.Trophy;
import rhit.jrProj.henry.firebase.User;


public class TrophyStoreActivity extends Activity {

    private GlobalVariables mGlobalVariables;
    private TrophyGridViewAdapter mAdapter;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_store);
        Firebase.setAndroidContext(this);
        GridView trophyGridView = (GridView) findViewById(R.id.trophies);
        mAdapter = new TrophyGridViewAdapter(this);
        mAdapter.setIsStore(true);
        trophyGridView.setAdapter(mAdapter);
        mGlobalVariables = ((GlobalVariables) getApplicationContext());
        String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
        Firebase firebase = new Firebase(fireBaseUrl);
        AuthData authData = firebase.getAuth();
        if (authData != null) {
            mGlobalVariables.setUser(new User(mGlobalVariables.getFirebaseUrl()
                    + "users/" + authData.getUid()));
        } else if (this.getIntent().getStringExtra("user") != null) {
            // If logged in get the user's project list
            mGlobalVariables.setUser(new User(this.getIntent().getStringExtra(
                    "user")));
        }
        mUser = mGlobalVariables.getUser();

        trophyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int position2 = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(TrophyStoreActivity.this);
                builder.setTitle("Purchase Trophy");
                if (mUser.hasTrophy(mAdapter.getItem(position))) {
                    builder.setMessage("You already own that trophy!");
                    builder.setNeutralButton("Ok", null);
                } else if (mUser.getAvailablePoints() < mAdapter.getItem(position).getCost()) {
                    builder.setMessage("Oops, you do not have enough points to purchase this trophy.");
                    builder.setNeutralButton("Ok", null);
                } else {
                    builder.setMessage("Are you sure you want to buy a " + mAdapter.getItem(position).getName() + " trophy for " + mAdapter.getItem(position).getCost() + " points?");
                    builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mUser.buyTrophy(mAdapter.getItem(position2));
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                }
                builder.show();
            }
        });
        firebase.child("trophies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot child, String s) {
                mAdapter.addTrophy(new Trophy(GlobalVariables.getFirebaseUrl() + "trophies/" + child.getKey()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trophy_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
