package rhit.jrProj.henry.bridge;

import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.bridge.ListChangeNotifier;

/**
 * Created by daveyle on 3/19/2015.
 */
public interface ChangeNotifiable {
    public void setChangeNotifier(ChangeNotifier lcn);

    public ChangeNotifier getChangeNotifier();

}
