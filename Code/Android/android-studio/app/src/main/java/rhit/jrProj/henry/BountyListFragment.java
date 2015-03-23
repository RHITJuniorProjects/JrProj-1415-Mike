package rhit.jrProj.henry;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class BountyListFragment extends DataListFragment<Bounty> {

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    @Override
    public void onItemSelected(Bounty p) {
        mCallbacks.onItemSelected(p);
    }

    @Override
    public String getSortMode() {
        return mCallbacks.getSortMode();
    }

    @Override
    public Project getSelectedProject() {
        return mCallbacks.getSelectedProject();
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks{
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Bounty t);

        public ArrayList<Bounty> getBounties();

        public Project getSelectedProject();

        public String getUserName();

        public String getSortMode();

        public Task getSelectedTask();

        public Milestone getSelectedMilestone();

    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        public void onItemSelected(Bounty t) {
            // do nothing
        }

        public ArrayList<Bounty> getBounties() {
            return null;
        }

        public Project getSelectedProject() {
            return null;
        }

        public Task getSelectedTask() {
            return null;
        }

        public String getUserName() {
            return "";
        }

        public String getSortMode() {
            // TODO Auto-generated method stub
            return null;
        }

        public Milestone getSelectedMilestone() {
            return null;
        }


    };

    /**
     * The wrapper class for the list's assignee.
     *
     * @author rockwotj.
     *         Created Nov 7, 2014.
     */
    private class PointListing {
        Bounty bounty;

        public PointListing(Bounty bounty) {
            this.bounty = bounty;
        }

        @Override
        public String toString() {
            return "Points: " + this.bounty.getPoints();
        }
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BountyListFragment() {
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        super.createHeaders("  Bounties in:", "Project: " + this.mCallbacks.getSelectedProject().getName() +
                "\nMilestone: " + this.mCallbacks.getSelectedMilestone().getName() +
                "\nTask: " + this.mCallbacks.getSelectedTask().getName());
        //This still doesn't account for dynamically adding and removing bounties
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Bounty bounty : this.items) {
            Map<String, Object> datum = new HashMap<String, Object>(2);
            datum.put("title", bounty);
            datum.put("assignee", new PointListing(bounty));
            data.add(datum);
        }
        SortedArrayAdapter<Bounty> adapter = new SortedArrayAdapter<Bounty>(getActivity(), android.R.layout.simple_list_item_activated_2,
                android.R.id.text1, this.items, Enums.ObjectType.BOUNTY, mCallbacks.getUserName(), true);
        super.attachAdapter(adapter);
    }

    @Override
    public void createAdapter() {
        SortedArrayAdapter<Bounty> arrayAdapter = new SortedArrayAdapter<Bounty>(
                getActivity(), android.R.layout.simple_list_item_activated_2,
                android.R.id.text1, this.items,
                Enums.ObjectType.BOUNTY, false);
        attachAdapter(arrayAdapter);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // This code shows the "Create Bounty" option when
        // viewing bounties.
        ArrayList<Integer> toHide = new ArrayList<Integer>();
        toHide.add(R.id.action_task);
        SubMenu submenu = menu.findItem(R.id.action_sorting).getSubMenu();
        ArrayList<Integer> toHide2 = new ArrayList<Integer>();
        toHide2.add(R.id.sortOldest);
        toHide2.add(R.id.sortNewest);


        Firebase ref = new Firebase(super.getGlobalVariables().getFirebaseUrl());
        Enums.Role role = this.mCallbacks
                .getSelectedProject()
                .getMembers()
                .getValue(
                        new Member(ref.getRoot().toString() + "/users/"
                                + ref.getAuth().getUid()));

        if (role != null && role.equals(Enums.Role.LEAD)) {
            ArrayList<Integer> toShow = new ArrayList<Integer>();
            toShow.add(R.id.action_bounty);
            showMenuOptions(menu, toShow);
        }

        hideMenuOptions(menu, toHide);
        hideMenuOptions(submenu, toHide2);
    }


    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position - 2, id);
        selectItem(position);
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.

    }

    @Override
    public void selectItem(int position) {
        mCallbacks.onItemSelected(this.items.get(position - 2));

    }

    @Override
    public ArrayList<Bounty> getDataItems() {
        return mCallbacks.getBounties();
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }
        this.mCallbacks = (Callbacks) activity;
    }
}
