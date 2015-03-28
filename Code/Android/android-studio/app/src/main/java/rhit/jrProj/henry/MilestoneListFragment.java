package rhit.jrProj.henry;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;

/**
 * A list fragment representing a list of Milestones. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link MilestoneDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MilestoneListFragment extends DataListFragment<Milestone> {


    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    @Override
    public void onItemSelected(Milestone p) {
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
        public void onItemSelected(Milestone m);

        public ArrayList<Milestone> getMilestones();

        public Project getSelectedProject();

        public String getSortMode();

    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        public void onItemSelected(Milestone m) {
            // Do nothing
        }

        public ArrayList<Milestone> getMilestones() {
            return null;
        }

        public Project getSelectedProject() {
            return null;
        }

        public String getSortMode() {
            // TODO Auto-generated method stub
            return null;
        }

    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MilestoneListFragment() {
    }

    /**
     *
     */

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createAdapter();
        super.createHeaders("Milestones in:", "Project: "
                + this.mCallbacks.getSelectedProject().getName());
    }

    @Override
    public void createAdapter() {
        SortedArrayAdapter<Milestone> arrayAdapter = new SortedArrayAdapter<Milestone>(
                getActivity(), android.R.layout.simple_list_item_activated_2,
                android.R.id.text1, this.items,
                Enums.ObjectType.MILESTONE, false);
        attachAdapter(arrayAdapter);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ArrayList<Integer> toHide = new ArrayList<Integer>();
        ArrayList<Integer> toShow = new ArrayList<Integer>();
        toHide.add(R.id.action_all_tasks);

        Firebase ref = new Firebase(super.getGlobalVariables().getFirebaseUrl());
        Enums.Role role = this.mCallbacks
                .getSelectedProject()
                .getMembers()
                .getValue(
                        new Member(ref.getRoot().toString() + "/users/"
                                + ref.getAuth().getUid()));

        if (this.getArguments().getBoolean(Enums.TWOPANE)) {
            if (role != null && role.equals(Enums.Role.LEAD)) {
                toShow.add(R.id.action_milestone);

            }
        }
        toHide.add(R.id.action_bounty);
        hideMenuOptions(menu, toHide);
        showMenuOptions(menu, toShow);

    }


    @Override
    public void selectItem(int position) {
//        int headerCount=
//        Log.i("Position " + position, "Is Valid? "+this.getSelectedItemPosition());


        mCallbacks.onItemSelected(this.items.get(position));
    }

    @Override
    public ArrayList<Milestone> getDataItems() {
        return mCallbacks.getMilestones();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position - 2, id);
        //selectItem(position);
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