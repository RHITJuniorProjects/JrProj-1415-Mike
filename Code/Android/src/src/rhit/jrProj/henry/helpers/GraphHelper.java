package rhit.jrProj.henry.helpers;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class GraphHelper {

	static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA,
			Color.CYAN };

	public static GraphicalView makePieChart(String title,
			List<Integer> values, List<String> keys, Activity activity) {
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

		GraphicalView mChartView = ChartFactory.getPieChartView(activity,
				mSeries, mRenderer);

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

		public void addValueToKey(String key, int addValue) {
			int index = keys.indexOf(key);
			int oldValue = values.get(index);
			values.set(index, oldValue + addValue);
		}

	}

	public static GraphicalView makeStackedBarChart(String title,
			String xAxisLabel, String yAxisLabel, List<List<Double>> values,
			List<String> barLabels, List<String> keys, Activity activity) {

		int[] COLORS = new int[] { Color.GREEN, Color.BLUE};
		XYMultipleSeriesRenderer renderer = buildBarRenderer(COLORS);
		setChartSettings(renderer, title, xAxisLabel, yAxisLabel, 0.5, values.get(0).size() + 0.5, -10,
				10, Color.GRAY, Color.LTGRAY);
		for (int i = 0; i < values.size(); i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setDisplayChartValues(true);
		}
		renderer.setXLabels(values.get(0).size());
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.25f);

		return ChartFactory.getBarChartView(activity,
				buildBarDataset(keys, values), renderer, Type.STACKED);
	}

	/**
	 * Builds a bar multiple series dataset using the provided values.
	 * 
	 * @param titles
	 *            the series titles
	 * @param values
	 *            the values
	 * @return the XY multiple bar dataset
	 */
	protected static XYMultipleSeriesDataset buildBarDataset(
			List<String> titles, List<List<Double>> values) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		for (int i = 0; i < values.size(); i++) {
			CategorySeries series = new CategorySeries(titles.get(i));
			List<Double> v = values.get(i);
			int seriesLength = v.size();
			for (int k = 0; k < seriesLength; k++) {
				series.add(v.get(k));
			}
			dataset.addSeries(series.toXYSeries());
		}
		return dataset;
	}

	/**
	 * Builds a bar multiple series renderer to use the provided colors.
	 * 
	 * @param colors
	 *            the series renderers colors
	 * @return the bar multiple series renderer
	 */
	protected static XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(20);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}

	/**
	 * Sets a few of the series renderer settings.
	 * 
	 * @param renderer
	 *            the renderer to set the properties to
	 * @param title
	 *            the chart title
	 * @param xTitle
	 *            the title for the X axis
	 * @param yTitle
	 *            the title for the Y axis
	 * @param xMin
	 *            the minimum value on the X axis
	 * @param xMax
	 *            the maximum value on the X axis
	 * @param yMin
	 *            the minimum value on the Y axis
	 * @param yMax
	 *            the maximum value on the Y axis
	 * @param axesColor
	 *            the axes color
	 * @param labelsColor
	 *            the labels color
	 */
	protected static void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

}
