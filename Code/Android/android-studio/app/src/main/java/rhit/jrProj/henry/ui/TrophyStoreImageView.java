package rhit.jrProj.henry.ui;

import android.content.Context;
import android.util.AttributeSet;

import rhit.jrProj.henry.firebase.Trophy;

/**
 * Created by millerna on 3/15/2015.
 */
public class TrophyStoreImageView extends TrophySquareImageView {
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
