package rhit.jrProj.henry;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Trophy;
import rhit.jrProj.henry.ui.TrophySquareImageView;

/**
 * Created by johnsoaa & rockwotj on 3/14/2015.
 */
public class TrophyGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Trophy> mTrophies;


    public TrophyGridViewAdapter(Context context) {
        mTrophies = new ArrayList<Trophy>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTrophies.size();
    }

    @Override
    public Trophy getItem(int i) {
        return mTrophies.get(i);
    }

    public void addTrophy(Trophy t) {
        mTrophies.add(t);
        t.setListChangeNotifier(new ListChangeNotifier<Trophy>(this));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TrophySquareImageView trophySquareImageView;
        if (view == null) {
            trophySquareImageView = new TrophySquareImageView(mContext);
        } else {
            trophySquareImageView = (TrophySquareImageView) view;
        }
        trophySquareImageView.initialize(mTrophies.get(i));
        return trophySquareImageView;
    }
}
