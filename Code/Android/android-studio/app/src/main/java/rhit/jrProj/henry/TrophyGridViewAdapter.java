package rhit.jrProj.henry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Trophy;
import rhit.jrProj.henry.ui.TrophySquareImageView;


/**
 * Created by johnsoaa & rockwotj on 3/14/2015.
 */
public class TrophyGridViewAdapter extends BaseAdapter {


    private Context mContext;
    private List<Trophy> mItems;
    private boolean mIsStore;


    public TrophyGridViewAdapter(Context context) {
        mItems = new ArrayList<Trophy>();
        mContext = context;
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Trophy getItem(int i) {
        return mItems.get(i);
    }

    public void addItem(Trophy t) {
        mItems.add(t);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TrophySquareImageView trophySquareImageView;
        if (view == null) {
            if (mIsStore) {
                trophySquareImageView = new TrophyStoreImageView(mContext);
            } else {
                trophySquareImageView = new TrophySquareImageView(mContext);
            }
        } else {
            trophySquareImageView = (TrophySquareImageView) view;
        }
        trophySquareImageView.initialize(getItem(i));
        return trophySquareImageView;
    }

    public void setIsStore(boolean isStore) {
        this.mIsStore = isStore;
    }

    public void setTrophies(List<Trophy> trophies) {
        mItems = trophies;
    }

    private class TrophyStoreImageView extends TrophySquareImageView {
        public TrophyStoreImageView(Context context) {
            super(context);
        }

        public TrophyStoreImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public TrophyStoreImageView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected String getLabel(Trophy trophy) {
            return trophy.getName() + " - " + trophy.getCost() + " points";
        }
    }
}
