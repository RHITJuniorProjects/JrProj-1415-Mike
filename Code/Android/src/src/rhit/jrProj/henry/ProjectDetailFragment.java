package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.helpers.GraphHelper;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
 * tablets) or a {@link ProjectDetailActivity} on handsets.
 */
public class ProjectDetailFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Project projectItem;
	
	private LinearLayout mMembersList;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("Project")) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			this.projectItem = (Project) getArguments()
					.getParcelable("Project");
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
		final View rootView = inflater.inflate(R.layout.fragment_project_detail,
				container, false);
		
		mMembersList = (LinearLayout) rootView.findViewById(R.id.projectMembers);
		
		if (this.projectItem != null) {
			((TextView) rootView.findViewById(R.id.project_name))
				.setText("Name of project: " + this.projectItem.getName());
			((TextView) rootView.findViewById(R.id.project_due_date))
				.setText("Due on: " + this.projectItem.getDueDateFormatted());
			((TextView) rootView.findViewById(R.id.project_description))
				.setText("Description: " + this.projectItem.getDescription());
			
			for (final Member m : this.projectItem.getMembers().getAllKeys()) {
				View projectMemberView = LayoutInflater.from(getActivity()).inflate(R.layout.project_member_view, null);
				TextView memberName = (TextView)projectMemberView.findViewById(R.id.member_name);
				TextView memberEmail = (TextView)projectMemberView.findViewById(R.id.member_email);
				TextView memberRole = (TextView)projectMemberView.findViewById(R.id.member_role);
				
				Role r = this.projectItem.getMembers().getValue(m);
				memberName.setText(m.getName());
				memberRole.setText(r.toString());
				memberEmail.setText(m.getEmail());
				
				Button emailButton = (Button)projectMemberView.findViewById(R.id.email_button);
				emailButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
				            	Uri.fromParts("mailto", m.getEmail(), null));


				 	startActivity(Intent.createChooser(emailIntent,
				            	"Send email to " + m.getName()));
					}
					
				});
				
				
				mMembersList.addView(projectMemberView);
			}
			
			//Progress Bars for Hours, Tasks, and Milestones
			((TextView) rootView.findViewById(R.id.project_hours_percent))
				.setText("Hours Completed: " + this.projectItem.getHoursPercent() + "%");
			ProgressBar hoursCompleteBar = ((ProgressBar) rootView.findViewById(R.id.project_hours_progress_bar));
			hoursCompleteBar.setMax(100);
			hoursCompleteBar.setProgress(this.projectItem.getHoursPercent());
			((TextView) rootView.findViewById(R.id.project_tasks_percent))
			.setText("Tasks Completed: " + this.projectItem.getTasksPercent() + "%");
		ProgressBar tasksCompleteBar = ((ProgressBar) rootView.findViewById(R.id.project_tasks_progress_bar));
		tasksCompleteBar.setMax(100);
		tasksCompleteBar.setProgress(this.projectItem.getTasksPercent());
		
		((TextView) rootView.findViewById(R.id.project_milestones_percent))
			.setText("Milestones Completed: " + this.projectItem.getMilestonesPercent() + "%");
		ProgressBar milestonesCompleteBar = ((ProgressBar) rootView.findViewById(R.id.project_milestones_progress_bar));
		milestonesCompleteBar.setMax(100);
		milestonesCompleteBar.setProgress(this.projectItem.getMilestonesPercent());
		
		
		Spinner spinner = (Spinner) rootView
				.findViewById(R.id.project_chart_spinner);
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
		// /////
		((Switch) rootView.findViewById(R.id.projectMemberSwitch)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				rootView.findViewById(R.id.projectDetails).setVisibility(isChecked ? View.GONE : View.VISIBLE);
				rootView.findViewById(R.id.projectMembers).setVisibility(isChecked ? View.VISIBLE : View.GONE);
			}
		});	
		
		FrameLayout chartView = (FrameLayout) rootView
				.findViewById(R.id.pieChart);
		List<Integer> values = new ArrayList<Integer>();
		List<String> keys = new ArrayList<String>();
		for (Milestone milestone : this.projectItem.getMilestones()) {
			GraphHelper.PieChartInfo chartInfo = milestone.getLocAddedInfo();
			values.addAll(chartInfo.getValues());
			keys.addAll(chartInfo.getKeys());
		}
		
		//used to remove duplicate names, people assigned to multiple milestones
		for(int x = 0; x < keys.size(); x++)
		{
			for(int y = x + 1; y < keys.size(); y++)
			{
				if(keys.get(x).equals(keys.get(y)))
				{
					values.set(x, values.get(x) + values.get(y));
					keys.remove(y);
					values.remove(y);
					y--;
				}
				
			}
			
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
	
	

}
