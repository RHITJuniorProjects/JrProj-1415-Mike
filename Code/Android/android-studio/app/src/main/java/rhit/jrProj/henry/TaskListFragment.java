package rhit.jrProj.henry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TaskListFragment extends DataListFragment<Task> {

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks extends ICallbacks<Task> {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Task t);

        public ArrayList<Task> getTasks();

        public Project getSelectedProject();

        public String getUserName();

        public String getSortMode();

        public Milestone getSelectedMilestone();

    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        public void onItemSelected(Task t) {
            // do nothing
        }

        public ArrayList<Task> getTasks() {
            return null;
        }

        public Project getSelectedProject() {
            return null;
        }

        public Milestone getSelectedMilestone() {
            return null;
        }

        public String getUserName() {
            return "";
        }

        public String getSortMode() {
            // TODO Auto-generated method stub
            return null;
        }


    };

    /**
     * The wrapper class for the list's assignee.
     *
     * @author rockwotj.
     *         Created Nov 7, 2014.
     */
    private class Assignee {
        Task task;

        public Assignee(Task task) {
            this.task = task;
        }

        @Override
        public String toString() {
            return "Assigned to: " + this.task.getAssignedUserName();
        }
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskListFragment() {
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        super.createHeaders("  Tasks in:", "Project: " + this.mCallbacks.getSelectedProject().getName() + "\nMilestone: " + this.mCallbacks.getSelectedMilestone().getName());
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Task task : this.items) {
            Map<String, Object> datum = new HashMap<String, Object>(2);
            datum.put("title", task);
            datum.put("assignee", new Assignee(task));
            data.add(datum);
        }
        SortedArrayAdapter<Task> adapter = new SortedArrayAdapter<Task>(getActivity(), android.R.layout.simple_list_item_activated_2,
                android.R.id.text1, this.items, Enums.ObjectType.TASK, mCallbacks.getUserName(), true);
        super.attachAdapter(adapter);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ArrayList<Integer> toHide = new ArrayList<Integer>();
        toHide.add(R.id.action_all_tasks);
        toHide.add(R.id.action_milestone);
        Menu subMenu = menu.findItem(R.id.action_sorting).getSubMenu();
        ArrayList<Integer> toHide2 = new ArrayList<Integer>();
        toHide2.add(R.id.sortNewest);
        toHide2.add(R.id.sortOldest);
        Firebase ref = new Firebase(super.getGlobalVariables().getFirebaseUrl());
        Enums.Role role = this.mCallbacks
                .getSelectedProject()
                .getMembers()
                .getValue(
                        new Member(ref.getRoot().toString() + "/users/"
                                + ref.getAuth().getUid()));

        if (role != null && role.equals(Enums.Role.LEAD)) {
            ArrayList<Integer> toShow = new ArrayList<Integer>();
            toShow.add(R.id.action_task);
            showMenuOptions(menu, toShow);
        }
        toHide.add(R.id.action_bounty);
        hideMenuOptions(menu, toHide);
        hideMenuOptions(subMenu, toHide2);

    }


    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position - 2, id);
        selectItem(position);
    }


    @Override
    public void selectItem(int position) {
        mCallbacks.onItemSelected(this.items.get(position - 2));
    }

    @Override
    public ArrayList<Task> getDataItems() {
        return mCallbacks.getTasks();
    }


}
