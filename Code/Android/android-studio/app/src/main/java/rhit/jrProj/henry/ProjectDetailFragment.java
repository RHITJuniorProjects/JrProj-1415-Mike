package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.firebase.BurndownData;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.helpers.GraphHelper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * either contained in a {ProjectListActivity} in two-pane mode (on
 * tablets) or a {ProjectDetailActivity} on handsets.
 */
public class ProjectDetailFragment extends Fragment  implements
        AdapterView.OnItemSelectedListener {

    /**
     * The dummy content this fragment is presenting.
     */
    private Project projectItem;

    private LinearLayout mMembersList;

    private boolean mTwoPane;

    private Callbacks mCallbacks;
    private String url;
    private int type=-1;

    public interface Callbacks {
        public Project getSelectedProject();
        public void setIsProject(boolean b);
        public void setUrl(String url);
        public void setType(int i);
        public void openChartView(View v);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        public Project getSelectedProject() {
            return null;
        }
        public void setIsProject(boolean b){}
        public void setUrl(String url){}
        public void setType(int i){}
        public void openChartView(View v){}
    };


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Enums.TWOPANE)) {
            this.mTwoPane = getArguments()
                    .getBoolean(Enums.TWOPANE);
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
        MenuItem allTasks = menu.findItem(R.id.action_all_tasks);
        allTasks.setVisible(false);
        allTasks.setEnabled(false);


        MenuItem createTask = menu.findItem(R.id.action_task);
        createTask.setVisible(false);
        createTask.setEnabled(false);
        MenuItem sorting = menu.findItem(R.id.action_sorting);

        sorting.setEnabled(false);
        sorting.setVisible(false);
        MenuItem createbounty = menu.findItem(R.id.action_bounty);
        createbounty.setVisible(false);
        createbounty.setEnabled(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_project_detail,
                container, false);
        this.projectItem = this.mCallbacks.getSelectedProject();
        this.url=GlobalVariables.getFirebaseUrl()+"/projects/"+this.projectItem.getID();
        this.mMembersList = (LinearLayout) rootView.findViewById(R.id.projectMembers);

        if (this.projectItem != null) {
            ((TextView) rootView.findViewById(R.id.project_name))
                    .setText(this.projectItem.getName());
            ((TextView) rootView.findViewById(R.id.project_due_date))
                    .setText("Due: " + this.projectItem.getDueDateFormatted());
            ((TextView) rootView.findViewById(R.id.project_description))
                    .setText("Description: " + this.projectItem.getDescription());

            for (final Member m : this.projectItem.getMembers().getAllKeys()) {
                View projectMemberView = LayoutInflater.from(getActivity()).inflate(R.layout.project_member_view, null);
                TextView memberName = (TextView) projectMemberView.findViewById(R.id.member_name);
                TextView memberEmail = (TextView) projectMemberView.findViewById(R.id.member_email);
                TextView memberRole = (TextView) projectMemberView.findViewById(R.id.member_role);

                Role r = this.projectItem.getMembers().getValue(m);
                memberName.setText(m.getName());
                memberRole.setText(r.toString());
                memberEmail.setText(m.getEmail());

                if (this.mTwoPane) {
                    projectMemberView.findViewById(R.id.metrics_label).setVisibility(View.VISIBLE);
                    ProgressBar metricProgress = (ProgressBar) projectMemberView.findViewById(R.id.metricsProgressBar);
                    metricProgress.setVisibility(View.VISIBLE);
//					Log.d("Henry", metricProgress.getProgress()+"|"+m.getProjectMetrics(this.projectItem.getProjectId()).getHoursPercent());
                    metricProgress.setProgress(m.getProjectMetrics(this.projectItem.getProjectId()).getHoursPercent());
//					Log.d("Henry", metricProgress.getProgress()+"");
                }

                Button emailButton = (Button) projectMemberView.findViewById(R.id.email_button);
                int blue = getResources().getColor(R.color.blue);
                emailButton.setTextColor(blue);
                emailButton.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                                Uri.fromParts("mailto", m.getEmail(), null));


                        startActivity(Intent.createChooser(emailIntent,
                                "Send email to " + m.getName()));
                    }

                });


                this.mMembersList.addView(projectMemberView);
            }

            //Progress Bars for Hours, Tasks, and Milestones
            ((TextView) rootView.findViewById(R.id.project_hours_percent))
                    .setText("Hours Logged: " + this.projectItem.getHoursPercent() + "%");
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
                            R.array.project_charts,
                            android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            // Set the default for the spinner
            spinner.setSelection(0);
            spinner.setOnItemSelectedListener(this);
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
            for (int x = 0; x < keys.size(); x++) {
                for (int y = x + 1; y < keys.size(); y++) {
                    if (keys.get(x).equals(keys.get(y))) {
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
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        if (this.type!=-1){
            position=type;
        }
        ClickableFrameLayout chartView = (ClickableFrameLayout) this.getActivity().findViewById(
                R.id.pieChart);

        chartView.removeAllViews();
        GraphicalView chart;


        if (position == 0) {
            GraphHelper.LineChartInfo chartInfo =  this.projectItem
                    .getLocInfo();
            int totalLoc=(int)chartInfo.getMaxY();
            int chartMax=(int)1.25*totalLoc;
            if (chartMax-totalLoc<10){
                chartMax=totalLoc+10;
            }

            chart = GraphHelper.makeLineChart("Lines of Code Added for "
                    + this.projectItem.getName(), "Milestones", "Lines of Code", chartInfo, 0, this.projectItem.getMilestones().size(), 0, chartMax, this.getActivity());
            chartView.addView(chart, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            chart.repaint();


        } else if (position== 3) {
            GraphHelper.LineChartInfo chartInfo = this.projectItem
                    .getEstimateAccuracyInfo();
            chart = GraphHelper.makeLineChart("Accuracy of Estimated Hours", "Milestones", "Ratio of Estimated/Actual",
                    chartInfo, 0, this.projectItem.getMilestones().size(), -5, 5, this.getActivity());
            for (int i=1; i<this.projectItem.getMilestones().size(); i++) {
                chartInfo.addNewTick(i + "");
            }
            chartView.addView(chart, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            chart.repaint();

        }
        else if (position == 1) { //BDHours
            GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();
            BurndownData burndownData =projectItem.getBurndownData();
            List<BurndownData.BurndownPoint> bdPts = burndownData.getBurndownPoints();

            for (int i = 0; i < bdPts.size(); i++) {
                chartInfo.addNewPoint("Estimated Hours Remaining",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getEstimatedHoursRemaining()));
                chartInfo.addNewPoint("Hours Completed",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getHoursWorked()));


            }
            for (int i = 1; i < projectItem.getMilestones().size(); i++) {
                chartInfo.addNewTick(i + "");
            }
            int totalLoc = burndownData.getMaxYHours();
            int chartMax = (int) 1.25 * totalLoc;
            if (chartMax - totalLoc < 10) {
                chartMax = totalLoc + 10;
            }
            int maxX = bdPts.get(bdPts.size() - 1).getX().intValue() + 2;

            chart = GraphHelper.makeLineChart(getString(R.string.burndown),
                    getString(R.string.days), getString(R.string.hours),
                    chartInfo, 0, maxX, 0, chartMax, this.getActivity());
            chartView.addView(chart, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            chart.repaint();
            chartView.setOnClickListener(new ProjectChartOnClickListener(mCallbacks, chartView));

        } else if (position == 2) { //BDTasks
            GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();
            BurndownData burndownData =projectItem.getBurndownData();
            List<BurndownData.BurndownPoint> bdPts = burndownData.getBurndownPoints();

            for (int i = 0; i < bdPts.size(); i++) {
                chartInfo.addNewPoint("Tasks Remaining",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getTasksRemaining()));
                chartInfo.addNewPoint("Tasks Completed",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getTasksDone()));


            }
            for (int i = 1; i < projectItem.getMilestones().size(); i++) {
                chartInfo.addNewTick(i + "");
            }
            int totalLoc = burndownData.getMaxYTasks();
            int chartMax = (int) 1.25 * totalLoc;
            if (chartMax - totalLoc < 5) {
                chartMax = totalLoc + 5;
            }
            int maxX = bdPts.get(bdPts.size() - 1).getX().intValue() + 2;

            chart = GraphHelper.makeLineChart(getString(R.string.burndown),
                    getString(R.string.days), getString(R.string.hours),
                    chartInfo, 0, maxX, 0, chartMax, this.getActivity());
            chartView.addView(chart, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            chart.repaint();
            chartView.setOnClickListener(new ProjectChartOnClickListener(mCallbacks, chartView));


        }
        mCallbacks.setIsProject(true);
        mCallbacks.setUrl(this.url);
        mCallbacks.setType(position);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }
    public class ProjectChartOnClickListener implements OnClickListener {
        Callbacks mCallbacks;
        FrameLayout chartView;
        public ProjectChartOnClickListener(Callbacks callbacks, FrameLayout chartView){
            super();
            this.mCallbacks=callbacks;
            this.chartView=chartView;
        }
        @Override
        public void onClick(View view) {
            this.chartView.removeAllViews();
            mCallbacks.openChartView(view);
        }
    }
}
