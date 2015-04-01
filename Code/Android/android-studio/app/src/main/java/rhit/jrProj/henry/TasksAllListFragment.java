package rhit.jrProj.henry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.bridge.DescendantsListener;
import rhit.jrProj.henry.bridge.FilteredArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TasksAllListFragment extends ListFragment implements OnItemSelectedListener {
    private String sortMode = "A-Z";
    private User user;
    private TextView textView;
    private TextView textView2;
    private View header;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private ArrayList<Task> tasks;
    private ArrayList<Task> finishedTasks;
    private ArrayList<Task> unfinishedTasks;
    private ArrayList<List<Task>> lists;
    private FilteredArrayAdapter<Task> allTasksAdapter;
    private SortedListChangeNotifier<Task> lcn;

    private GlobalVariables mGlobalVariables;


    private String userID;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Task t);

        public ArrayList<Task> getTasks();

        public Project getSelectedProject();

        public User getUser();

        public Milestone getSelectedMilestone();

        public String getSortMode();

        public boolean isFromMainActivity();

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

        public User getUser() {
            return null;
        }

        public Milestone getSelectedMilestone() {
            return null;

        }

        public String getSortMode() {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isFromMainActivity() {
            return false;
        }

    };

    /**
     * The wrapper class for the list's assignee.
     *
     * @author rockwotj. Created Nov 7, 2014.
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
    public TasksAllListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        mGlobalVariables = ((GlobalVariables) (this.getActivity().getApplicationContext()));
        String fireBaseUrl = GlobalVariables.getFirebaseUrl();
        Firebase firebase = new Firebase(fireBaseUrl);

        String userKey = firebase.getAuth().getUid();
        firebase.child("users").child(userKey)
                .addChildEventListener(new DescendantsListener(this));
        this.user = new User(firebase.child("users").child(userKey).toString());
        this.userID = user.getKey();
        this.tasks = new ArrayList<Task>();
        this.finishedTasks = new ArrayList<Task>();
        this.unfinishedTasks = new ArrayList<Task>();
//		this.projects=new ArrayList<Project>();
//		SortedArrayAdapter<Project> arrayAdapter = new SortedArrayAdapter<Project>(
//				getActivity(), android.R.layout.simple_list_item_activated_2,
//				android.R.id.text1, this.projects, Enums.ObjectType.PROJECT, false);
////		setListAdapter(arrayAdapter);
//		
//		SortedListChangeNotifier<Project> lcn2 = new SortedListChangeNotifier<Project>(
//				arrayAdapter, this.sortMode);
//		
//		user.setChangeNotifier(lcn2);
        getAllMyTasks();
//
//		// This still doesn't account for dynamically adding and removing tasks

        lists = new ArrayList<List<Task>>();
        lists.add(this.tasks);
        lists.add(this.unfinishedTasks);
        lists.add(this.finishedTasks);
        allTasksAdapter = new FilteredArrayAdapter<Task>(
                getActivity(), android.R.layout.simple_list_item_activated_2,
                android.R.id.text1, lists, Enums.ObjectType.TASK,
                true, true);

        lcn = new SortedListChangeNotifier<Task>(allTasksAdapter);

//		for (Task t : this.tasks) {
//			t.setChangeNotifier(lcn);
//		}
        setListAdapter(allTasksAdapter);
        if (this.mCallbacks.isFromMainActivity()) {
            this.setActivateOnItemClick(this.getArguments().getBoolean("TwoPane"));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // This code shows the "Create Task" option when
        // viewing tasks.
        if (this.mCallbacks.isFromMainActivity()) {
            MenuItem createMilestone = menu.findItem(R.id.action_milestone);
            createMilestone.setVisible(false);
            createMilestone.setEnabled(false);


            MenuItem createTask = menu.findItem(R.id.action_task);
            createTask.setVisible(false);
            createTask.setEnabled(false);
            MenuItem sorting = menu.findItem(R.id.action_sorting);

            sorting.setEnabled(true);
            sorting.setVisible(true);
            SubMenu submenu = menu.findItem(R.id.action_sorting).getSubMenu();
            MenuItem dateOldest = submenu.findItem(R.id.sortOldest);
            MenuItem dateNewest = submenu.findItem(R.id.sortNewest);
            dateOldest.setVisible(false);
            dateOldest.setEnabled(false);
            dateNewest.setVisible(false);
            dateNewest.setEnabled(false);
            MenuItem createbounty = menu.findItem(R.id.action_bounty);
            createbounty.setVisible(false);
            createbounty.setEnabled(false);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }

        this.mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        this.mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
//		super.onListItemClick(listView, view, position, id);
//
//		// Notify the active callbacks interface (the activity, if the
//		// fragment is attached to one) that an item has been selected.
//		this.mCallbacks.onItemSelected(this.tasks.get(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, this.mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(this.mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        this.mActivatedPosition = position;
    }

    public void changeName(String s) {
        textView.setText(s + "'s Tasks");
    }

    public void fireChangeInData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Task task : this.tasks) {
            Map<String, Object> datum = new HashMap<String, Object>(2);
            datum.put("title", task);
            datum.put("assignee", new Assignee(task));
            data.add(datum);
        }

    }

    @SuppressLint("ResourceAsColor")
    private void getAllMyTasks() {
        textView = new TextView(this.getActivity().getBaseContext());
        textView.setTextSize(24);
        textView.setTextColor(R.color.light_blue);
        textView.setPadding(16, 0, 16, 0);
        textView.setText(this.user.getName() + "'s Tasks");
//		textView2 = new TextView(this.getActivity().getBaseContext());
//		textView2.setTextSize(18);
//		textView2.setTextColor(R.color.light_blue);
//		textView2.setClickable(false);
//		textView2.setEnabled(false);
//		textView2.setPadding(16, 0, 16, 0);
//		this.getListView().addHeaderView(textView2, null, false);
//		header=(View)View.inflate(this.getActivity(), R.layout.switch_header_layout, null);
//		Spinner spinner = (Spinner) header.findViewById(R.id.show_spinner);
//		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//				this.getActivity().getBaseContext(), R.array.tasks_all,
//				android.R.layout.simple_spinner_item);
//		// Specify the layout to use when the list of choices appears
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spinner.setAdapter(adapter);
//		spinner.setOnItemSelectedListener(this);
////		View headerView = View.inflate(this.getActivity(), R.layout.switch_header_layout, null);
////		this.getListView().addHeaderView(headerView);
//		this.getListView().addHeaderView(header);
        fireChangeInData();
        textView.setClickable(false);
        textView.setEnabled(false);
        this.getListView().addHeaderView(textView, null, false);
//		textView2.setClickable(false);
//		textView2.setEnabled(false);
//		this.getListView().addHeaderView(textView2, null, false);
    }

    public void sortingChanged() {
        this.sortMode = this.mCallbacks.getSortMode();
        for (Task p : this.tasks) {
            ((SortedListChangeNotifier<Task>) p.getChangeNotifier()).changeSorting(this.sortMode);
        }
    }


    public void onSpinnerHeaderChange(int nu) {
        Log.i("filter changed", "hi");
        this.allTasksAdapter.changeFilter(nu);

    }

    public void addTask(Task t) {
        this.tasks.add(t);
        if (t.getStatus().equals(Enums.CLOSED)) {
            Log.i("CLOSED", t.getName());
            this.finishedTasks.add(t);
        } else {
            this.unfinishedTasks.add(t);
        }
        t.setChangeNotifier(lcn);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        this.onSpinnerHeaderChange(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }


}
