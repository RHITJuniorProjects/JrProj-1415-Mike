package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.helpers.GraphHelper;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A fragment representing a single Project detail screen. This fragment is
 * either contained in a {@link ProjectListActivity} in two-pane mode (on
 * tablets) or a {@link ChartsActivity} on handsets.
 */
public class ChartsFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Project projectItem;
	
	private LinearLayout mMembersList;

	private boolean mTwoPane;

	private Callbacks mCallbacks;

	public interface Callbacks {
		public Project getSelectedProject();
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		public Project getSelectedProject() {
			return null;
		}
	};

	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ChartsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey("TwoPane")) {
			this.mTwoPane = getArguments()
					.getBoolean("TwoPane");
		}

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
			MenuItem sorting= menu.findItem(R.id.action_sorting);
			
			sorting.setEnabled(false);
			sorting.setVisible(false);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_charts,
				container, false);
		this.projectItem = this.mCallbacks.getSelectedProject();
//		this.mMembersList = (LinearLayout) rootView.findViewById(R.id.projectMembers);
		
		if (this.projectItem != null) {
			((TextView) rootView.findViewById(R.id.project_name))
				.setText("Name of project: " + this.projectItem.getName());
//			((TextView) rootView.findViewById(R.id.project_due_date))
//				.setText("Due on: " + this.projectItem.getDueDateFormatted());
//			((TextView) rootView.findViewById(R.id.project_description))
//				.setText("Description: " + this.projectItem.getDescription());
			
			
			
		
		Spinner spinner = (Spinner) rootView
				.findViewById(R.id.project_chart_spinner);
		// Create an ArrayAdapter using the string array and a default
		// spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this.getActivity(),
						R.array.longitude_charts,
						android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		// Set the default for the spinner
		spinner.setSelection(0);
		// /////
		
		
		FrameLayout chartView = (FrameLayout) rootView
				.findViewById(R.id.pieChart);
		List<Integer> values = new ArrayList<Integer>();
		List<String> keys = new ArrayList<String>();
		for (Milestone milestone : this.projectItem.getMilestones()) {
			GraphHelper.PieChartInfo chartInfo = milestone.getLocAddedInfo();
			values.addAll(chartInfo.getValues());
			keys.addAll(chartInfo.getKeys());
		}
		
		
		
		
		GraphicalView chart = GraphHelper.makePieChart(
				"Lines Added for " + this.projectItem.getName(),
				values, keys,
				this.getActivity());
		chartView.addView(chart, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		chart.repaint();
			
		}

		return rootView;
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
