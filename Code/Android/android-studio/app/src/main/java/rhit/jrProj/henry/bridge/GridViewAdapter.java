package rhit.jrProj.henry.bridge;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Trophy;
import rhit.jrProj.henry.ui.TrophySquareImageView;

/**
 * Created by johnsoaa & rockwotj on 3/14/2015.
 */
public abstract class GridViewAdapter<T> extends BaseAdapter {

    private Context mContext;
    private ArrayList<T> mItems;


    public GridViewAdapter(Context context) {
        mItems = new ArrayList<T>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int i) {
        return mItems.get(i);
    }

    public void addItem(T t) {
        mItems.add(t);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public Context getContext() {
        return mContext;
    }
}
