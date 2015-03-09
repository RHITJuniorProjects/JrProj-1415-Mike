package rhit.jrProj.henry;

import org.achartengine.GraphicalView;
import org.achartengine.model.Point;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.helpers.GraphHelper;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A fragment representing a single Milestone detail screen. This fragment is
 * either contained in a {@link MilestoneListActivity} in two-pane mode (on
 * tablets) or a {@link MilestoneDetailActivity} on handsets.
 */
public class MilestoneDetailFragment extends Fragment implements
		OnItemSelectedListener {

	public interface Callbacks {
		public Milestone getSelectedMilestone();
	}

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Milestone milestoneItem;
	private Callbacks mCallbacks;
	private Callbacks sDummyCallbacks = new Callbacks() {

		@Override
		public Milestone getSelectedMilestone() {
			return null;
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MilestoneDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(
				R.layout.fragment_milestone_detail, container, false);
		this.milestoneItem = this.mCallbacks.getSelectedMilestone();
		if (this.milestoneItem != null) {
			((TextView) rootView.findViewById(R.id.milestone_name))
					.setText(this.milestoneItem.getName());
			((TextView) rootView.findViewById(R.id.milestone_due_date))
					.setText("Due: "
							+ this.milestoneItem.getDueDateFormatted());
			((TextView) rootView.findViewById(R.id.milestone_description))
					.setText("Description: "
							+ this.milestoneItem.getDescription());

			((TextView) rootView.findViewById(R.id.milestone_task_percent))
					.setText("Tasks Completed: "
							+ this.milestoneItem.getTaskPercent() + "%");
			ProgressBar taskCompleteBar = ((ProgressBar) rootView
					.findViewById(R.id.milestone_task_progress_bar));
			taskCompleteBar.setMax(100);
			taskCompleteBar.setProgress(this.milestoneItem.getTaskPercent());

			// //////
			// Task status spinner
			Spinner spinner = (Spinner) rootView
					.findViewById(R.id.milestone_chart_spinner);
			// Create an ArrayAdapter using the string array and a default
			// spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this.getActivity(),
							R.array.milestone_charts,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);

			// Set the default for the spinner
			spinner.setSelection(0);

			spinner.setOnItemSelectedListener(this);
		}

		return rootView;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem allTasks=menu.findItem(R.id.action_all_tasks);
		allTasks.setVisible(false);
		allTasks.setEnabled(false);

		// This code shows the "Create Task" option when
		// viewing tasks.
		MenuItem createbounty = menu.findItem(R.id.action_bounty);
		createbounty.setVisible(false);
		createbounty.setEnabled(false); 
		
		MenuItem createMilestone = menu.findItem(R.id.action_milestone);
		createMilestone.setVisible(false);
		createMilestone.setEnabled(false);

		MenuItem createTask = menu.findItem(R.id.action_task);
		createTask.setVisible(false);
		createTask.setEnabled(false);

	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		FrameLayout chartView = (FrameLayout) this.getActivity().findViewById(
				R.id.pieChart);
		chartView.removeAllViews();
		GraphicalView chart;
		if (position == 0) {
			GraphHelper.PieChartInfo chartInfo = this.milestoneItem
					.getLocAddedInfo();

			chart = GraphHelper.makePieChart("Lines Added for "
					+ this.milestoneItem.getName(), chartInfo.getValues(),
					chartInfo.getKeys(), this.getActivity());
			chartView.addView(chart, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		} else if (position == 1) {
			GraphHelper.StackedBarChartInfo chartInfo = this.milestoneItem
					.getLocTotalInfo();

			chart = GraphHelper.makeStackedBarChart("", "", "Lines of Code",
					chartInfo.getValues(), chartInfo.getBarLabels(),
					chartInfo.getKeys(), this.getActivity());
			chartView.addView(chart, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			chart.repaint();
		} else if (position == 2) {
			GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();

			chartInfo.addNewPoint("estimated",
					new GraphHelper.Point(1.0, 150.0));

			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(0.0, 0.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(1.0, 10.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(2.0, 20.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(3.0, 30.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(4.0, 40.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(5.0, 50.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(6.0, 65.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(7.0, 75.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(8.0, 80.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(9.0, 85.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(10.0, 90.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(11.0, 100.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(12.0, 110.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(13.0, 120.0));
			chartInfo.addNewPoint("Estimated Hours Remaining",
					new GraphHelper.Point(14.0, 130.0));

			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(0.0,
					150.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(1.0,
					140.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(2.0,
					130.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(3.0,
					122.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(4.0,
					112.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(5.0,
					106.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(6.0,
					101.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(7.0,
					91.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(8.0,
					87.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(9.0,
					82.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(10.0,
					77.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(11.0,
					67.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(12.0,
					58.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(13.0,
					50.0));
			chartInfo.addNewPoint("Hours Worked", new GraphHelper.Point(14.0,
					40.0));

			chartInfo.addNewTick("0");
			chartInfo.addNewTick("1");
			chartInfo.addNewTick("2");
			chartInfo.addNewTick("3");
			chartInfo.addNewTick("4");
			chartInfo.addNewTick("5");
			chartInfo.addNewTick("6");
			chartInfo.addNewTick("7");
			chartInfo.addNewTick("8");
			chartInfo.addNewTick("9");
			chartInfo.addNewTick("10");
			chartInfo.addNewTick("11");
			chartInfo.addNewTick("12");
			chartInfo.addNewTick("13");
			chartInfo.addNewTick("14");

			chart = GraphHelper.makeLineChart(getString(R.string.burndown),
					getString(R.string.days), getString(R.string.hours),
					chartInfo, 0, 21, 0, 150, this.getActivity());
			chartView.addView(chart, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			chart.repaint();
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// do nothing
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		this.mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mCallbacks = sDummyCallbacks;
	}

}
