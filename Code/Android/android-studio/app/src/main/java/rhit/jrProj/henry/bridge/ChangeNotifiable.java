package rhit.jrProj.henry.bridge;

import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.bridge.ListChangeNotifier;

/**
 * Created by daveyle on 3/19/2015.
 */
public interface ChangeNotifiable<T> {
    public void setChangeNotifier(ChangeNotifier<T> lcn);

    public ChangeNotifier<T> getChangeNotifier();

}
