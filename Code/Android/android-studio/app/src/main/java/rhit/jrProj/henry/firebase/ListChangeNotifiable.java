package rhit.jrProj.henry.firebase;

import rhit.jrProj.henry.bridge.ListChangeNotifier;

/**
 * Created by daveyle on 3/19/2015.
 */
public interface ListChangeNotifiable<T> {
    public void setListChangeNotifier(ListChangeNotifier<T> lcn);

    /**
     * gets the list changed notifier
     *
     * @param lcn
     */
    public ListChangeNotifier<T> getListChangeNotifier();

}
