package rhit.jrProj.henry;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;

import org.achartengine.GraphicalView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.firebase.BurndownData;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.helpers.GraphHelper;
import rhit.jrProj.henry.ui.LandscapeFrameLayout;

public class ChartsDialogFragment extends DialogFragment {
    private String url;
    private boolean isProject;
    private Project projectItem;
    private Milestone milestoneItem;
    private int position;
    private GraphicalView chart;

    public static ChartsDialogFragment newInstance(boolean project, String url, int chartType) {
        ChartsDialogFragment f = new ChartsDialogFragment();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("isProject", project);
        args.putString("url", url);
        args.putInt("chartType", chartType);
        f.setArguments(args);


        return f;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.url=this.getArguments().getString("url");
        this.isProject=this.getArguments().getBoolean("isProject");
        this.position=this.getArguments().getInt("chartType");
        if(isProject) {
            this.projectItem = MainActivity.getSelectedProjectStatic();
        }else{
            this.milestoneItem=MainActivity.getSelectedMilestoneStatic();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_charts,
                container, false);
        FrameLayout chartView = (FrameLayout)rootView
                .findViewById(R.id.chartplace);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        if(isProject) {
            GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();
            BurndownData burndownData =projectItem.getBurndownData();
            List<BurndownData.BurndownPoint> bdPts = burndownData.getBurndownPoints();
            if (position == 1) { //BDHours

                //Log.i(bdPts.size()+"", "Size");
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


            } else if (position == 2) { //BDTasks


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

            }
        }
        else{
            GraphHelper.LineChartInfo chartInfo = new GraphHelper.LineChartInfo();
            BurndownData burndownData=this.milestoneItem.getBurndownData();
            List<BurndownData.BurndownPoint> bdPts=burndownData.getBurndownPoints();
            if (position == 2) {//BD hours


                for (int i=0; i<bdPts.size(); i++){
                    chartInfo.addNewPoint("Estimated Hours Remaining",
                            new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getEstimatedHoursRemaining()));
                    chartInfo.addNewPoint("Hours Completed",
                            new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getHoursWorked()));
                    chartInfo.addNewTick(i+"");

                }
                int totalLoc=(int)chartInfo.getMaxY();
                int chartMax=(int)1.25*totalLoc;
                if (chartMax-totalLoc<10){
                    chartMax=totalLoc+10;
                }


                chart = GraphHelper.makeLineChart(getString(R.string.burndown),
                        getString(R.string.days), getString(R.string.hours),
                        chartInfo, 0,bdPts.size()+2, 0, chartMax, this.getActivity());


            } else if (position == 3) {//BD tasks

                for (int i = 0; i < bdPts.size(); i++) {
                    chartInfo.addNewPoint("Tasks Remaining",
                            new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getTasksRemaining()));
                    chartInfo.addNewPoint("Tasks Completed",
                            new GraphHelper.Point(bdPts.get(i).getX(), bdPts.get(i).getTasksDone()));
                    chartInfo.addNewTick(i + "");

                }
                int totalLoc = (int) chartInfo.getMaxY();
                int chartMax = (int) 1.25 * totalLoc;
                if (chartMax - totalLoc < 5) {
                    chartMax = totalLoc + 5;
                }


                chart = GraphHelper.makeLineChart(getString(R.string.burndown),
                        getString(R.string.days), getString(R.string.hours),
                        chartInfo, 0, bdPts.size() + 2, 0, chartMax, this.getActivity());

            }
        }




        chartView.addView(chart, params);
        chart.repaint();
        chartView.setRotation(90);
        Button cancel = (Button) rootView.findViewById(R.id.button);
        cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                ChartsDialogFragment.this.dismiss();
            }
        });
        //getDialog().addContentView(chartView, params);
        return rootView;


    }

    /**
     * Opens a dialog window that displays the given error message.
     *
     * @param message
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this.getActivity()).setTitle("Error").setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }



}