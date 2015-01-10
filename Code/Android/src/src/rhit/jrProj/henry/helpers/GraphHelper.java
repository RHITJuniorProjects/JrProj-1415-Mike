package rhit.jrProj.henry.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import rhit.jrProj.henry.MainActivity;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import java.util.Random;

public class GraphHelper {

	static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA,
			Color.CYAN };
	static int density = MainActivity.DENSITY;
	final static int fontSize = 30 * (density/480);

	public static GraphicalView makePieChart(String title,
			List<Integer> values, List<String> keys, Activity activity) {
		DefaultRenderer mRenderer = new DefaultRenderer();
		//mRenderer.setChartTitle(title);
		//mRenderer.setChartTitleTextSize(35);
		mRenderer.setLabelsTextSize(fontSize);
		mRenderer.setLegendTextSize(fontSize);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setStartAngle(0);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setAntialiasing(true);
		mRenderer.setPanEnabled(false);
		mRenderer.setZoomEnabled(false);

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
	public static GraphicalView makeLineChart(String title,
			String xAxisLabel, String yAxisLabel,
			  List<String> seriesTitles, List<List<Point>> values, List<String> x_ticks, Activity activity){

		double yMax = 0;
		double yMin = 0;
		double xMax = 0;
		double max = 0;
		if(values.isEmpty())
		{
			//there are no values in the list, i.e no users. In this case, just display a blank graph
			yMax = 10;
			yMin = 0;
			xMax = 10;
		}
		else
		{
		
		xMax=values.get(0).size();
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i).size() > xMax){
				xMax=values.get(i).size();
			}
			max = maxX(values.get(i));
			if (max > yMax) {
				yMax = max;
			}
			double min = minX(values.get(i));
			if (min < yMin) {
				yMin = min;
			}
		}
		xMax += .5;
		
		}
		Random rand=new Random();
		int[] COLORS = new int[seriesTitles.size()];
		for (int i=0; i<COLORS.length; i++){
			COLORS[i]=Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		}
		XYMultipleSeriesRenderer renderer = buildLineRenderer(COLORS);
		setChartSettings(renderer, title, xAxisLabel, yAxisLabel, 0.5, xMax, yMin * 1.5,
				yMax * 1.25, Color.BLACK, Color.BLACK);
		for (int i = 0; i < seriesTitles.size(); i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setDisplayChartValues(true);
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
			.setChartValuesTextSize(fontSize);
			
		}
		renderer.setXLabels(0);
		for (int i = 0; i < x_ticks.size(); i++) {
			renderer.addXTextLabel(i + 0.75, x_ticks.get(i));
		}
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.25f);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);
		renderer.setShowGridX(false);
		renderer.setAntialiasing(true);
		renderer.setMargins(new int[] {0, 30, 10, 0});
		
		return ChartFactory.getLineChartView(activity,
				buildLineDataset(seriesTitles, values), renderer);
		
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
	public static Double maxX(List<Point> points){
		Double max= points.get(0).getX();
		for (int i=0; i< points.size(); i++){
			if (max < points.get(i).getX()){
				max=points.get(i).getX();
			}
		}
		return max;
	}
	public static Double maxY(List<Point> points){
		Double max= points.get(0).getY();
		for (int i=0; i< points.size(); i++){
			if (max < points.get(i).getY()){
				max=points.get(i).getY();
			}
		}
		return max;
	}
	public static Double minX(List<Point> points){
		Double min= points.get(0).getX();
		for (int i=0; i< points.size(); i++){
			if (min > points.get(i).getX()){
				min=points.get(i).getX();
			}
		}
		return min;
	}
	public static Double minY(List<Point> points){
		Double min= points.get(0).getY();
		for (int i=0; i< points.size(); i++){
			if (min > points.get(i).getY()){
				min=points.get(i).getY();
			}
		}
		return min;
	}
	public static GraphicalView makeStackedBarChart(String title,
			String xAxisLabel, String yAxisLabel, List<List<Double>> values,
			List<String> barLabels, List<String> keys, Activity activity) {

		int[] COLORS = new int[] { Color.GREEN, Color.RED, Color.BLUE};
		XYMultipleSeriesRenderer renderer = buildBarRenderer(COLORS);
		double yMax = 0;
		double yMin = 0;
		double xMax = 0;
		double max = 0;
		if(values.get(0).isEmpty())
		{
			//there are no values in the list, i.e no users. In this case, just display a blank graph
			yMax = 10;
			yMin = 0;
			xMax = 10;
		}
		else
		{
		

		for (int i = 0; i < values.size(); i++) {
			max = Collections.max(values.get(i));
			if (max > yMax) {
				yMax = max;
			}
			double min = Collections.min(values.get(i));
			if (min < yMin) {
				yMin = min;
			}
		}
		xMax = values.get(0).size() + .5;
		
		}
		
		setChartSettings(renderer, title, xAxisLabel, yAxisLabel, 0.5, xMax, yMin * 1.5,
				yMax * 1.25, Color.BLACK, Color.BLACK);
		for (int i = 0; i < values.size(); i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setDisplayChartValues(true);
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
			.setChartValuesTextSize(fontSize);
			
		}
		renderer.setXLabels(0);
		for (int i = 0; i < barLabels.size(); i++) {
			renderer.addXTextLabel(i + 0.75, barLabels.get(i));
		}
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setXLabelsColor(Color.BLACK);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setYLabelsColor(0, Color.BLACK);
		renderer.setPanEnabled(true, false);
		renderer.setZoomEnabled(false);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.25f);
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);
		renderer.setShowGridX(false);
		renderer.setAntialiasing(true);
		renderer.setMargins(new int[] {0, 30, 10, 0});
		

		return ChartFactory.getBarChartView(activity,
				buildBarDataset(keys, values), renderer, Type.STACKED);
	}
	
	public static class StackedBarChartInfo {

		private List<List<Double>> values;
		private List<String> keys;
		private List<String> barLabels;

		public StackedBarChartInfo() {
			values = new ArrayList<List<Double>>();
			keys = new ArrayList<String>();
			barLabels = new ArrayList<String>();
		}
		
		public void addKey(String key) {
			keys.add(key);
			values.add(new ArrayList<Double>());
		}

		public void addValueSeriesBarLabel(List<Double> valueSeries, String barLabel) {
			barLabels.add(barLabel);
			for (int i = 0; i < valueSeries.size(); i++) {
				values.get(i).add(valueSeries.get(i));
			}
		}

		public List<List<Double>> getValues() {
			return values;
		}

		public List<String> getKeys() {
			return keys;
		}
		
		public List<String> getBarLabels() {
			return barLabels;
		}

		public void addValueToKeyBarLabel(String key, String barLabel, double addValue) {
			int keyIndex = keys.indexOf(key);
			int labelsIndex = barLabels.indexOf(barLabel);
			List<Double> valueSeries = values.get(keyIndex);
			double oldValue = valueSeries.get(labelsIndex);
			valueSeries.set(labelsIndex, oldValue + addValue);
		}

	}
	public static class Point{
		private Double x;
		private Double y;
		public Point(){
			x=0.0;
			y=0.0;
		}
		public void setX(Double x){
			this.x=x;
		}
		public void setY(Double y){
			this.y=y;
		}
		public Double getX(){
			return this.x;
		}
		public Double getY(){
			return this.y;
		}
	}
	public static class LineChartInfo {

		private HashMap<String, List<Point>> values;
		private List<String> x_ticks;

		public LineChartInfo() {
			values = new HashMap<String, List<Point>>();
			x_ticks = new ArrayList<String>();
		}
		
		public void addNewPoint(String title, Point p){
			if (values.containsKey(title)){
				values.get(title).add(p);
			}
			else{
				List<Point> pts=new ArrayList<Point>();
				pts.add(p);
				values.put(title, pts);
			}
		}
		public void addNewTick(String tick){
			this.x_ticks.add(tick);
		}
		public List<List<Point>> getValues(){
			return new ArrayList<List<Point>>(this.values.values());
		}
		public List<String> getTitles(){
			return new ArrayList<String>(this.values.keySet());
		}
		public List<String> getXTicks(){
			return this.x_ticks;
		}
		

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
	protected static XYMultipleSeriesDataset buildLineDataset(List<String> titles, List<List<Point>> values){
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		
		for (int i=0; i < titles.size(); i++){
			XYSeries series = new XYSeries(titles.get(i));
			for (int j=0; j< values.get(i).size(); j++){
				series.add(values.get(i).get(j).getX(), values.get(i).get(j).getY());
			}
			dataset.addSeries(series);
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
		renderer.setAxisTitleTextSize(fontSize);
		renderer.setChartTitleTextSize(fontSize);
		renderer.setLabelsTextSize(fontSize);
		renderer.setLegendTextSize(fontSize);
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
	protected static XYMultipleSeriesRenderer buildLineRenderer(int[] colors) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(fontSize);
		renderer.setChartTitleTextSize(fontSize);
		renderer.setLabelsTextSize(fontSize);
		renderer.setLegendTextSize(fontSize);
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
