package rhit.jrProj.henry.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.bridge.ChangeNotifiable;
import rhit.jrProj.henry.bridge.ChangeNotifier;

/**
 * Created by daveyle on 4/30/2015.
 */
public class BurndownObject implements ChangeNotifiable<BurndownObject>, Comparable<BurndownObject>{

    double hoursRem=-1;
    double hoursDone=-1;
    double tasksRem=-1;
    double tasksDone=-1;
    long timeStamp=-1;
    private ChangeNotifier changeNotifier;
    private Firebase firebase;
    public BurndownObject(String url){
        this.firebase=new Firebase(url);
        this.firebase.addChildEventListener(new BurndownChildListener(this));
    }

    public double getHoursRem() {return hoursRem;}
    public void setHoursRem(double hoursRem) {this.hoursRem = hoursRem;}
    public double getHoursDone() {return hoursDone;}
    public void setHoursDone(double hoursDone) {this.hoursDone = hoursDone;}
    public double getTasksRem() {return tasksRem;}
    public void setTasksRem(double tasksRem) {this.tasksRem = tasksRem;}
    public double getTasksDone() {return tasksDone;}
    public void setTasksDone(double tasksDone) {this.tasksDone = tasksDone;}
    public long getTimeStamp(){return this.timeStamp;}
    public void setTimeStamp(long ts){this.timeStamp=ts;}


    @Override
    public void setChangeNotifier(ChangeNotifier<BurndownObject> lcn) {
        this.changeNotifier=lcn;

    }

    @Override
    public ChangeNotifier<BurndownObject> getChangeNotifier() {
        return this.changeNotifier;
    }

    @Override
    public int compareTo(BurndownObject o) {
        if (o.getTimeStamp()>this.getTimeStamp()){
            return -1;
        }else if (o.getTimeStamp()==this.getTimeStamp()){
            return 0;
        }else {
            return 1;
        }
    }

    private class BurndownChildListener implements ChildEventListener{
        BurndownObject bo;
        public BurndownChildListener(BurndownObject bo){
            this.bo=bo;
        }


        @Override
        public void onChildAdded(DataSnapshot arg0, String s) {
            if (arg0.getKey().equals("estimated_hours_remaining")) {
                this.bo.setHoursRem(arg0.getValue(Double.class));
            } else if (arg0.getKey().equals("hours_completed")) {
                this.bo.setHoursDone(arg0.getValue(Double.class));
            } else if (arg0.getKey().equals("tasks_remaining")) {
                this.bo.setTasksRem(arg0.getValue(Double.class));
            } else if (arg0.getKey().equals("tasks_completed")) {
                this.bo.setTasksDone(arg0.getValue(Double.class));
            }
            if (this.bo.getChangeNotifier()!=null){
                this.bo.getChangeNotifier().onChange();
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }
}