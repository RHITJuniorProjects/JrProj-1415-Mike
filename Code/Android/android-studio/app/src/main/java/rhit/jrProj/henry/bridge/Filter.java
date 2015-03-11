package rhit.jrProj.henry.bridge;

import java.util.List;

public interface Filter<T> {
	public List<T> filter(List<T> list);

}
