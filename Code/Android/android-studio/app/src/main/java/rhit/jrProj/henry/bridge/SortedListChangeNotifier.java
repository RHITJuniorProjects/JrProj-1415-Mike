package rhit.jrProj.henry.bridge;

import java.util.Comparator;

import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * The callback for a dynamic item in an android listview container designed for
 * sorting of projects.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class SortedListChangeNotifier<T> extends ListChangeNotifier<T> {

    /**
     * A simple string that stores what sorting mode the user chose
     */
    private String sortType = Enums.AZ;
    /**
     * Stores the comparator so it is not recreated every time.
     */
    private Comparator<T> c;

    /**
     * Standard constructor
     *
     * @param adapter
     */
    public SortedListChangeNotifier(ArrayAdapter<T> adapter) {
        super(adapter);
        createComparator();

    }

    /**
     * Standard constructor
     *
     * @param adapter
     */
    public SortedListChangeNotifier(ArrayAdapter<T> adapter,
                                    String sorttype) {
        super(adapter);
        if (sorttype != null) {
            this.sortType = sorttype;
        }
        createComparator();

    }

    /**
     * This method is called when the user selects a new sorting mode. It is
     * called by the ProjectListFragment
     *
     * @param sorttype
     */
    public void changeSorting(String sorttype) {
        //Log.i("Changed sorting", sorttype);
        if (!(sorttype.equals(this.sortType))) {
            this.sortType = sorttype;
            createComparator();
            onChange();
        }
    }

    /**
     * sets this.c to be specific comparators based on the type of sorting
     * requested.
     */
    private void createComparator() {
        if (this.sortType.equals(Enums.AZ)) {
            this.c = new Comparator<T>() {
                public int compare(T lhs, T rhs) {
                    if (lhs instanceof Project && rhs instanceof Project) {
                        return ((Project) lhs)
                                .compareToIgnoreCase((Project) rhs);
                    }
                    if (lhs instanceof Milestone && rhs instanceof Milestone) {
                        return ((Milestone) lhs)
                                .compareToIgnoreCase((Milestone) rhs);
                    }
                    if (lhs instanceof Task && rhs instanceof Task) {
                        return ((Task) lhs)
                                .compareToIgnoreCase((Task) rhs);
                    }
                    if (lhs instanceof Bounty && rhs instanceof Bounty) {
                        return ((Bounty) lhs)
                                .compareToIgnoreCase((Bounty) rhs);
                    }

                    return 0;
                }
            };
        } else if (this.sortType.equals(Enums.ZA)) {
            this.c = new Comparator<T>() {
                public int compare(T lhs, T rhs) {
                    if (lhs instanceof Project && rhs instanceof Project) {
//						Log.i("SLCN", "PROJECT");
                        return -1
                                * ((Project) lhs)
                                .compareToIgnoreCase((Project) rhs);
                    }
                    if (lhs instanceof Milestone && rhs instanceof Milestone) {
//						Log.i("SLCN", "Milestone");
                        return -1
                                * ((Milestone) lhs)
                                .compareToIgnoreCase((Milestone) rhs);
                    }
                    if (lhs instanceof Task && rhs instanceof Task) {
//						Log.i("SLCN", "Task");
                        return -1
                                * ((Task) lhs)
                                .compareToIgnoreCase((Task) rhs);
                    }

                    return 0;
                }
            };
        } else if (this.sortType.equals(Enums.NEWFIRST)) {
            // Not yet implemented
            // Standard date form needed!
            this.c = new Comparator<T>() {
                public int compare(T lhs, T rhs) {
                    if (lhs instanceof Project && rhs instanceof Project) {
                        return ((Project) lhs)
                                .compareToByDate((Project) rhs, true);
                    }
                    if (lhs instanceof Milestone && rhs instanceof Milestone) {
                        return ((Milestone) lhs)
                                .compareToByDate((Milestone) rhs, true);
                    }

                    return 0;
                }
            };
        } else if (this.sortType.equals(Enums.OLDFIRST)) {
            // Not yet implemented
            // Standard date form needed!
            this.c = new Comparator<T>() {
                public int compare(T lhs, T rhs) {
                    if (lhs instanceof Project && rhs instanceof Project) {
                        return ((Project) lhs)
                                .compareToByDate((Project) rhs, false);
                    }
                    if (lhs instanceof Milestone && rhs instanceof Milestone) {
                        return ((Milestone) lhs)
                                .compareToByDate((Milestone) rhs, false);
                    }
                    return 0;
                }
            };
        } else {
            // Not yet implemented
            this.c = new Comparator<T>() {
                public int compare(T lhs, T rhs) {
                    return 0;
                }
            };
        }
    }

    /**
     * The method that will be triggered when data is changed in the object.
     * This method sorts on every change.
     */
    public void onChange() {
//		Log.i("sortmode", this.sortType);
        if (super.getAdapter() instanceof SortedArrayAdapter) {
            ((SortedArrayAdapter<T>) super.getAdapter()).sort(this.c);
        } else if (super.getAdapter() instanceof FilteredArrayAdapter) {

        }
        super.getAdapter().notifyDataSetChanged();

    }
}