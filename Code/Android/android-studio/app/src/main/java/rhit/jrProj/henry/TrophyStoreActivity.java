package rhit.jrProj.henry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.firebase.Trophy;


public class TrophyStoreActivity extends Activity {

    private GlobalVariables mGlobalVariables;
    private TrophyGridViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy_store);
        GridView trophyGridView = (GridView) findViewById(R.id.trophies);
        mAdapter = new TrophyGridViewAdapter(this);
        mAdapter.setIsStore(true);
        trophyGridView.setAdapter(mAdapter);
        mGlobalVariables = ((GlobalVariables) getApplicationContext());
        String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
        Firebase firebase = new Firebase(fireBaseUrl);
        trophyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrophyStoreActivity.this);
                builder.setTitle("Trophy");
                builder.setMessage("You clicked a trophy! " + mAdapter.getItem(position).getName());
                builder.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("RHH", "You clicked buy!");
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
        firebase.child("trophies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot child, String s) {
                Log.d("RHH", child.getKey());
                mAdapter.addTrophy(new Trophy("https://henry-test.firebaseio.com/trophies/" + child.getKey()));
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
