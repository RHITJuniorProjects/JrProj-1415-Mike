package rhit.jrProj.henry;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import rhit.jrProj.henry.bridge.ExpandableListChangeNotifier;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.User;


public class ProfileListActivity extends Activity {

    private GlobalVariables mGlobalVariables;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        mGlobalVariables = ((GlobalVariables) getApplicationContext());
        String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
        Firebase firebase = new Firebase(fireBaseUrl);
        mUser = new User(mGlobalVariables.getFirebaseUrl()
                + "users/" + firebase.getAuth().getUid());
        ExpandableListView stickyList = (ExpandableListView) findViewById(R.id.list);
        ProfileListAdapter adapter = new ProfileListAdapter();
        mUser.setListChangeNotifier(new ExpandableListChangeNotifier<Project>(adapter));
        stickyList.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_list, menu);
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

    private class ProfileListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mUser.getProjects().size();
        }

        @Override
        public int getChildrenCount(int i) {
            return getGroup(i).getMembers().size();
        }

        @Override
        public Project getGroup(int i) {
            return mUser.getProjects().get(i);
        }

        @Override
        public Member getChild(int i, int i2) {
            return mUser.getProjects().get(i).getMembers().getAllKeys().get(i2);
        }

        @Override
        public long getGroupId(int i) {
            return getGroup(i).hashCode();
        }

        @Override
        public long getChildId(int i, int i2) {
            return getChild(i, i2).hashCode();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            TextView tv;
            if (view == null) {
                tv = (TextView) LayoutInflater.from(ProfileListActivity.this).inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);
            } else {
                tv = (TextView) view;
            }
            tv.setText(getGroup(i).getName());
            return tv;
        }

        @Override
        public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
            TextView tv;
            if (view == null) {
                tv = (TextView) LayoutInflater.from(ProfileListActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            } else {
                tv = (TextView) view;
            }
            tv.setText(getChild(i, i2).getName());
            return tv;
        }

        @Override
        public boolean isChildSelectable(int i, int i2) {
            return true;
        }
    }
}
