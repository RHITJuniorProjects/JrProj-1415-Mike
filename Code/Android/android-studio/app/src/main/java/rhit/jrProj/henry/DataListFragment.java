package rhit.jrProj.henry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.ListChangeNotifiable;
import rhit.jrProj.henry.firebase.Project;

/**
 * Created by daveyle on 3/19/2015.
 */
public abstract class DataListFragment<T extends ListChangeNotifiable> extends ListFragment implements HasCallbacks<T> {

    ArrayList<T> items;

    String sortMode = Enums.AZ;
    private ArrayAdapter mAdapter;
    HasCallbacks mCallbacks=sDummyCallbacks;
    private ListChangeNotifier<T> lcn;

    public interface Callbacks extends HasCallbacks{
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Object p);

        public String getSortMode();

        public Project getSelectedProject();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Object p) {
            // Do nothing
        }

        public String getSortMode() {
            return Enums.AZ;
        }

        @Override
        public Project getSelectedProject() {
            return null;
        }

    };

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private GlobalVariables mGlobalVariables;

    protected Enums.ObjectType type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlobalVariables = ((GlobalVariables) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.items = this.getDataItems();
        this.sortMode = mCallbacks.getSortMode();

        this.setActivateOnItemClick(this.getArguments().getBoolean(Enums.TWOPANE));
    }

    public abstract void createAdapter();

    public void attachAdapter(ArrayAdapter<T> adapter) {
        lcn = new SortedListChangeNotifier<T>(
                adapter);
        for (ListChangeNotifiable m : this.items) {
            m.setListChangeNotifier(lcn);
        }
        this.mAdapter = adapter;
        setListAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }

    }



    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position,
                                long id) {
        super.onListItemClick(listView, view, position, id);
        this.selectItem(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.mActivatedPosition != AdapterView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, this.mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(
                activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
                        : AbsListView.CHOICE_MODE_NONE);
    }

    protected void setActivatedPosition(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            getListView().setItemChecked(this.mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }
        this.mActivatedPosition = position;
    }

    public ArrayList<T> getItems() {
        return this.items;
    }

    public abstract void selectItem(int position);

    public abstract ArrayList<T> getDataItems();

    /**
     * Notifies the Projects that the sorting mode has changed
     * and calls the changeSorting() method on their respective adapters.
     */
    public void sortingChanged() {
        this.sortMode = this.mCallbacks.getSortMode();
        for (T p : this.items) {
            ((SortedListChangeNotifier<T>) p.getListChangeNotifier()).changeSorting(this.sortMode);
        }
    }

    public ListChangeNotifier<T> getListChangeNotifier() {
        return this.lcn;
    }

    public void setListChangeNotifier(ListChangeNotifier<T> lcn) {
        this.lcn = lcn;
    }

    public GlobalVariables getGlobalVariables() {
        return this.mGlobalVariables;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        setListAdapter(null);
    }

    protected void hideMenuOptions(Menu menu, ArrayList<Integer> toHide) {
        for (int i : toHide) {
            MenuItem menuItem = menu.findItem(i);
            menuItem.setVisible(false);
        }
    }

    protected void showMenuOptions(Menu menu, ArrayList<Integer> toShow) {
        for (int i : toShow) {
            MenuItem menuItem = menu.findItem(i);
            menuItem.setVisible(true);
            menuItem.setEnabled(true);
        }
    }

    @SuppressLint("ResourceAsColor")
    protected void createHeaders(String text1, String text2) {
        TextView textView = new TextView(this.getActivity().getBaseContext());
        textView.setTextSize(24);
        textView.setTextColor(R.color.light_blue);
        textView.setText(text1);
        textView.setClickable(false);
        textView.setEnabled(false);
        textView.setPadding(16, 0, 16, 0);
        this.getListView().addHeaderView(textView, null, false);
        TextView textView2 = new TextView(this.getActivity().getBaseContext());
        textView2.setTextSize(18);
        textView2.setTextColor(R.color.light_blue);
        textView2.setText(text2);
        textView2.setClickable(false);
        textView2.setEnabled(false);
        textView2.setPadding(16, 0, 16, 0);
        this.getListView().addHeaderView(textView2, null, false);
    }

}
