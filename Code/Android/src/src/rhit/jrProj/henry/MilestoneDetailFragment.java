package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.helpers.GraphHelper;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A fragment representing a single Milestone detail screen. This fragment is
 * either contained in a {@link MilestoneListActivity} in two-pane mode (on
 * tablets) or a {@link MilestoneDetailActivity} on handsets.
 */
public class MilestoneDetailFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Milestone milestoneItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MilestoneDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("Milestone")) {
			this.milestoneItem = this.getArguments().getParcelable("Milestone");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_milestone_detail,
				container, false);
		if (this.milestoneItem != null) {
			((TextView) rootView.findViewById(R.id.milestone_name))
					.setText(this.milestoneItem.getName());
			((TextView) rootView.findViewById(R.id.milestone_due_date))
					.setText(this.milestoneItem.getDueDate());
			((TextView) rootView.findViewById(R.id.milestone_description))
					.setText(this.milestoneItem.getDescription());

			((TextView) rootView.findViewById(R.id.milestone_task_percent))
					.setText("Tasks Completed: "
							+ this.milestoneItem.getTaskPercent() + "%");
			ProgressBar taskCompleteBar = ((ProgressBar) rootView
					.findViewById(R.id.milestone_task_progress_bar));
			taskCompleteBar.setMax(100);
			taskCompleteBar.setProgress(this.milestoneItem.getTaskPercent());

			FrameLayout chartView = (FrameLayout) rootView
					.findViewById(R.id.pieChart);
			GraphHelper.PieChartInfo chartInfo = this.milestoneItem.getLocAddedInfo();
			/*GraphicalView pieChart = GraphHelper.makePieChart(
					"Lines Added for " + this.milestoneItem.getName(),
					chartInfo.getValues(), chartInfo.getKeys(),
					this.getActivity());*/
			
			List<List<Double>> values = new ArrayList<List<Double>>();
			List<Double> value1 = new ArrayList<Double>();
			value1.add(3.0);
			value1.add(4.0);
			value1.add(5.0);
			value1.add(2.0);
			values.add(value1);
			List<Double> value2 = new ArrayList<Double>();
			value2.add(-2.0);
			value2.add(-4.0);
			value2.add(-2.0);
			value2.add(-3.0);
			values.add(value2);
			
			List<String> keys = new ArrayList<String>();
			keys.add("Lines Added");
			keys.add("Lines Removed");
			
			GraphicalView pieChart = GraphHelper.makeStackedBarChart("Chart Title",
					"X Axis Label", "Y Axis Label", values, new ArrayList<String>(),
					keys, this.getActivity());
			Log.i("Henry", chartView.toString());
			chartView.addView(pieChart, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			pieChart.repaint();
		}

		return rootView;
	}
}
