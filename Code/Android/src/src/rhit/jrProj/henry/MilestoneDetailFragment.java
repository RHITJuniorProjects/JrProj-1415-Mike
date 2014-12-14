package rhit.jrProj.henry;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.helpers.GraphHelper;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A fragment representing a single Milestone detail screen. This fragment is
 * either contained in a {@link MilestoneListActivity} in two-pane mode (on
 * tablets) or a {@link MilestoneDetailActivity} on handsets.
 */
public class MilestoneDetailFragment extends Fragment implements
		OnItemSelectedListener {

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
		final View rootView = inflater.inflate(
				R.layout.fragment_milestone_detail, container, false);
		if (this.milestoneItem != null) {
			((TextView) rootView.findViewById(R.id.milestone_name))
					.setText("Name of Milestone: "
							+ this.milestoneItem.getName());
			((TextView) rootView.findViewById(R.id.milestone_due_date))
					.setText("Due on: "
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
			// /////
			((Switch) rootView.findViewById(R.id.milestoneMemberSwitch))
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							rootView.findViewById(R.id.milestoneDetails)
									.setVisibility(
											isChecked ? View.GONE
													: View.VISIBLE);
							rootView.findViewById(R.id.milestoneMembers)
									.setVisibility(
											isChecked ? View.VISIBLE
													: View.GONE);
						}
					});
			/*
			 * FrameLayout chartView = (FrameLayout) rootView
			 * .findViewById(R.id.pieChart); GraphHelper.PieChartInfo chartInfo
			 * = this.milestoneItem.getLocAddedInfo(); GraphicalView pieChart =
			 * GraphHelper.makePieChart( "Lines Added for " +
			 * this.milestoneItem.getName(), chartInfo.getValues(),
			 * chartInfo.getKeys(), this.getActivity());
			 * 
			 * 
			 * GraphHelper.StackedBarChartInfo chartInfo = this.milestoneItem
			 * .getLocTotalInfo(); GraphicalView pieChart =
			 * GraphHelper.makeStackedBarChart( "Lines Total Added",
			 * "Developer", "Lines of Code", chartInfo.getValues(),
			 * chartInfo.getBarLabels(), chartInfo.getKeys(),
			 * this.getActivity()); chartView.addView(pieChart, new
			 * LayoutParams( LayoutParams.MATCH_PARENT,
			 * LayoutParams.MATCH_PARENT)); pieChart.repaint();
			 */
		}

		return rootView;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		// This code shows the "Create Task" option when
		// viewing tasks.
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

		} else {
			GraphHelper.StackedBarChartInfo chartInfo = this.milestoneItem
					.getLocTotalInfo();

			chart = GraphHelper.makeStackedBarChart("", "", "Lines of Code",
					chartInfo.getValues(), chartInfo.getBarLabels(),
					chartInfo.getKeys(), this.getActivity());
			chartView.addView(chart, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			chart.repaint();
		}
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// do nothing
	}
}
