package rhit.jrProj.henry.helpers;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;

public class GraphHelper {
	
	static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,
			Color.MAGENTA, Color.CYAN };
	
	public static GraphicalView makePieChart(String title, List<Integer> values, List<String> keys, Activity activity) {		
		DefaultRenderer mRenderer = new DefaultRenderer();
		mRenderer.setChartTitle(title);
		mRenderer.setChartTitleTextSize(35);
		mRenderer.setLabelsTextSize(30);
		mRenderer.setLegendTextSize(30);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setStartAngle(90);
		
		CategorySeries mSeries = new CategorySeries("");
		
		for (int i = 0; i < values.size(); i++) {
			mSeries.add(keys.get(i) + " " + values.get(i), values.get(i));
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mSeries.getItemCount() - 1)
					% COLORS.length]);
			mRenderer.addSeriesRenderer(renderer);
		}
		
		GraphicalView mChartView = ChartFactory.getPieChartView(
				activity, mSeries, mRenderer);
		
		return mChartView;
	}
	
	public static class PieChartInfo {
		
		private List<Integer> values;
		private List<String> keys;
		
		public PieChartInfo() {
			values = new ArrayList<Integer>();
			keys = new ArrayList<String>();
		}
		
		public void addValueKey(int value, String key) {
			values.add(value);
			keys.add(key);
		}
		
		public List<Integer> getValues() {
			return values;
		}
		
		public List<String> getKeys() {
			return keys;
		}
		
	}
	
	/*mRenderer.setApplyBackgroundColor(true);
	mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
	mRenderer.setChartTitleTextSize(20);
	mRenderer.setLabelsTextSize(15);
	mRenderer.setLegendTextSize(15);
	mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	mRenderer.setZoomButtonsVisible(true);
	mRenderer.setStartAngle(90);*/

	

	/*if (mChartView != null) {
		mChartView.repaint();
	}*/

/*@Override
protected void onResume() {
	super.onResume();
	if (mChartView == null) {
		
		mRenderer.setClickEnabled(true);
		mRenderer.setSelectableBuffer(10);

		mChartView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SeriesSelection seriesSelection = mChartView
						.getCurrentSeriesAndPoint();

				if (seriesSelection == null) {
					Toast.makeText(AChartEnginePieChartActivity.this,
							"No chart element was clicked",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							AChartEnginePieChartActivity.this,
							"Chart element data point index "
									+ (seriesSelection.getPointIndex() + 1)
									+ " was clicked" + " point value="
									+ seriesSelection.getValue(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		mChartView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				SeriesSelection seriesSelection = mChartView
						.getCurrentSeriesAndPoint();
				if (seriesSelection == null) {
					Toast.makeText(AChartEnginePieChartActivity.this,
							"No chart element was long pressed",
							Toast.LENGTH_SHORT);
					return false;
				} else {
					Toast.makeText(AChartEnginePieChartActivity.this,
							"Chart element data point index "
									+ seriesSelection.getPointIndex()
									+ " was long pressed",
							Toast.LENGTH_SHORT);
					return true;
				}
			}
		});
		layout.addView(mChartView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	} else {
		mChartView.repaint();
	}
}*/

}
