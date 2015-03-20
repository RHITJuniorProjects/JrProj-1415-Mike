package rhit.jrProj.henry;

import android.os.Bundle;
import android.view.Menu;
import android.view.SubMenu;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.User;


/**
 * A list fragment representing a list of Projects. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ProjectDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ProjectListFragment extends DataListFragment<Project> {

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
    public interface Callbacks extends ICallbacks<Project> {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Project p);

        public ArrayList<Project> getProjects();

        public User getUser();

        public String getSortMode();

        public Project getSelectedProject();
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        public void onItemSelected(Project p) {
            // Do nothing
        }

        public ArrayList<Project> getProjects() {
            return null;
        }

        public User getUser() {
            return null;
        }

        public String getSortMode() {
            return Enums.AZ;
        }

        public Project getSelectedProject() {
            // TODO Auto-generated method stub
            return null;
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectListFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createAdapter();
        this.mCallbacks.getUser()
                .setListChangeNotifier(super.getListChangeNotifier());


    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        ArrayList<Integer> toHide = new ArrayList<Integer>();
        toHide.add(R.id.action_search);
        ArrayList<Integer> toShow = new ArrayList<Integer>();
        toShow.add(R.id.action_sorting);
        ArrayList<Integer> toShow2 = new ArrayList<Integer>();
        SubMenu submenu = menu.findItem(R.id.action_sorting).getSubMenu();
        toShow2.add(R.id.sortOldest);
        toShow2.add(R.id.sortNewest);
        toShow2.add(R.id.sortAZ);
        toShow2.add(R.id.sortZA);

        toHide.add(R.id.action_milestone);
        toHide.add(R.id.action_task);
        toHide.add(R.id.action_bounty);
        toHide.add(R.id.action_all_tasks);
        super.hideMenuOptions(menu, toHide);
        super.showMenuOptions(menu, toShow);
        super.showMenuOptions(submenu, toShow2);
    }


    @Override
    public void selectItem(int position) {
        this.mCallbacks.onItemSelected(this.mCallbacks.getProjects().get(position));
    }

    @Override
    public ArrayList<Project> getDataItems() {
        return this.mCallbacks.getProjects();
    }


}
