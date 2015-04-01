package rhit.jrProj.henry.bridge;

import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.bridge.ListChangeNotifier;

/**
 * Created by daveyle on 3/19/2015.
 */
public interface ListChangeNotifiable {
    public void setListChangeNotifier(ChangeNotifier lcn);

    public ChangeNotifier getListChangeNotifier();

}
