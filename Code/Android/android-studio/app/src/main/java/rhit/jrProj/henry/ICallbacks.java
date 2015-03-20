package rhit.jrProj.henry;

/**
 * Created by daveyle on 3/19/2015.
 */
public interface ICallbacks<T> {
    public String getSortMode();

    public void onItemSelected(T t);
}
