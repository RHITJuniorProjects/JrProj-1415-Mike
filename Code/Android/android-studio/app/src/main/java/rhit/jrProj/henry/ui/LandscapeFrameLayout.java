package rhit.jrProj.henry.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.R;

/**
 * Created by daveyle on 4/30/2015.
 */
public class LandscapeFrameLayout extends FrameLayout {
        Context context;
        public LandscapeFrameLayout(Context context){
            super(context);
            this.context=context;
        }
    public LandscapeFrameLayout(Context context, AttributeSet attr){
        super(context, attr);
        this.context=context;

    }
        public void makeFromXML(GraphicalView chart) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View topView =  inflater.inflate(R.layout.charts, this, false);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            this.addView(chart);
            chart.repaint();
            // Get all the sub Views here using topView.findViewById()

            // Do any other initiation of the View you need here

            // Make sure you this otherwise it won't actually appear!
            super.addView(topView);
        }
        public void dispatchConfigurationChanged(Configuration newConfig) {
            Configuration c = new Configuration(newConfig);
            c.orientation = Configuration.ORIENTATION_LANDSCAPE;
            super.dispatchConfigurationChanged(c);
        }

    }

