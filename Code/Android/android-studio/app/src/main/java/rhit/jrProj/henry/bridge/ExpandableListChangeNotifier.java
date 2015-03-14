package rhit.jrProj.henry.bridge;

import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

/**
 * A hacky adapter for the adapter....
 *
 * @author rockwotj. Created Oct 10, 2014.
 */
public class ExpandableListChangeNotifier<T> extends ListChangeNotifier<T> {

    private BaseExpandableListAdapter adapter;

    public ExpandableListChangeNotifier(BaseExpandableListAdapter adapter) {
        super(null);
        this.adapter = adapter;
    }

    /**
     * The method that will be triggered when data is changed in the object.
     */
    public void onChange() {
//		Log.i("ListCN called", "true");
        this.adapter.notifyDataSetChanged();
    }

    public BaseAdapter getAdapter() {
        return null;
    }

}