package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Project;

/**
 * Created by daveyle on 3/23/2015.
 */
public interface HasCallbacks<T> {
    public void onItemSelected(T p);

    public String getSortMode();

    public Project getSelectedProject();
}
