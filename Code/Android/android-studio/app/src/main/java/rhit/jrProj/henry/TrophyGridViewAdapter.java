package rhit.jrProj.henry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.GridViewAdapter;
import rhit.jrProj.henry.bridge.ListChangeNotifier;
import rhit.jrProj.henry.firebase.Trophy;
import rhit.jrProj.henry.ui.TrophySquareImageView;


/**
 * Created by johnsoaa & rockwotj on 3/14/2015.
 */
public class TrophyGridViewAdapter extends GridViewAdapter<Trophy> {


    private boolean mIsStore;


    public TrophyGridViewAdapter(Context context) {
        super(context);
    }

    public void addTrophy(Trophy t) {
        super.addItem(t);
        t.setListChangeNotifier(new ListChangeNotifier<Trophy>(this));
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TrophySquareImageView trophySquareImageView;
        if (view == null) {
            if (mIsStore) {
                trophySquareImageView = new TrophyStoreImageView(super.getContext());
            } else {
                trophySquareImageView = new TrophySquareImageView(super.getContext());
            }
        } else {
            trophySquareImageView = (TrophySquareImageView) view;
        }
        trophySquareImageView.initialize(super.getItem(i));
        return trophySquareImageView;
    }

    public void setIsStore(boolean isStore) {
        this.mIsStore = isStore;
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
