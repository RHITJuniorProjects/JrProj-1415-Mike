package rhit.jrProj.henry;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.firebase.BurndownData;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.helpers.GraphHelper;
import rhit.jrProj.henry.ui.LandscapeFrameLayout;

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

import java.util.List;

/**
 * A fragment representing a single Milestone detail screen. This fragment is
 * either contained in a in two-pane mode (on
 * tablets) or a on handsets.
 */
public class MilestoneDetailFragment extends Fragment implements
        OnItemSelectedListener {

    public interface Callbacks {
        public Milestone getSelectedMilestone();
        public void setIsProject(boolean b);
        public void setUrl(String url);
        public void setType(int i);
        public void openChartView(View v);
    }

    /**
     * The dummy content this fragment is presenting.
     */
    private Milestone milestoneItem;
    private Callbacks mCallbacks;
    private int type=-1;
    private String url;

    private Callbacks sDummyCallbacks = new Callbacks() {

        @Override
        public Milestone getSelectedMilestone() {
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
        this.url=GlobalVariables.getFirebaseUrl()+"/projects/"+
                this.milestoneItem.getParentProjectID()+"/milestones/"+this.milestoneItem.getID();
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
        MenuItem allTasks = menu.findItem(R.id.action_all_tasks);
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
        if (this.type!=-1){
            position=type;
        }
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

        } else if (position == 2) {//BD hours
            GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();
            BurndownData burndownData =this.milestoneItem.getBurndownData();
            List<BurndownData.BurndownPoint> bdPts = burndownData.getBurndownPoints();

            for (int i=0; i<bdPts.size(); i++){
                chartInfo.addNewPoint("Estimated Hours Remaining",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getEstimatedHoursRemaining()));
                chartInfo.addNewPoint("Hours Completed",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getHoursWorked()));
                chartInfo.addNewTick(i+"");

            }
            int totalLoc = burndownData.getMaxYHours();
            int chartMax=(int)1.25*totalLoc;
            if (chartMax-totalLoc<10){
                chartMax=totalLoc+10;
            }


            chart = GraphHelper.makeLineChart(getString(R.string.burndown),
                    getString(R.string.days), getString(R.string.hours),
                    chartInfo, 0,bdPts.size()+2, 0, chartMax, this.getActivity());
            chartView.addView(chart, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            chart.repaint();
            chartView.setOnClickListener(new ProjectChartOnClickListener(mCallbacks, chartView));

        } else if (position == 3) {//BD tasks
            GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();
            BurndownData burndownData =this.milestoneItem.getBurndownData();
            List<BurndownData.BurndownPoint> bdPts = burndownData.getBurndownPoints();

            for (int i=0; i<bdPts.size(); i++){
                chartInfo.addNewPoint("Tasks Remaining",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getTasksRemaining()));
                chartInfo.addNewPoint("Tasks Completed",
                        new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getTasksDone()));
                chartInfo.addNewTick(i+"");

            }
            int totalLoc = burndownData.getMaxYHours();
            int chartMax=(int)1.25*totalLoc;
            if (chartMax-totalLoc<5){
                chartMax=totalLoc+5;
            }


            chart = GraphHelper.makeLineChart(getString(R.string.burndown),
                    getString(R.string.days), getString(R.string.hours),
                    chartInfo, 0,bdPts.size()+2, 0, chartMax, this.getActivity());
            chartView.addView(chart, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            chart.repaint();
            chartView.setOnClickListener(new ProjectChartOnClickListener(mCallbacks, chartView));

        }
        mCallbacks.setIsProject(false);
        mCallbacks.setUrl(this.url);
        mCallbacks.setType(position);


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
    public class ProjectChartOnClickListener implements View.OnClickListener {
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
