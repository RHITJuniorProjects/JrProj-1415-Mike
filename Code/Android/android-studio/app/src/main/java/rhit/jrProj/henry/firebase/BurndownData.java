package rhit.jrProj.henry.firebase;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rhit.jrProj.henry.bridge.FirebaseObject;

/**
 * Created by daveyle on 4/28/2015.
 */
public abstract class BurndownData<T extends FirebaseObject> {
    T fo;
    List<BurndownObject> bdo=new ArrayList<BurndownObject>();
    public BurndownData(T fo){
        this.fo=fo;
        this.bdo=getBurndownObjects();
    }
    public List<Double> getEstimatedHoursRemaining(){
        ArrayList<Double> rem=new ArrayList<Double>();
        for (BurndownObject bo : this.bdo){
            rem.add(bo.getHoursRem());
        }
        return rem;
    }
    public List<Double> getHoursWorked(){
        ArrayList<Double> done=new ArrayList<Double>();
        for (BurndownObject bo : this.bdo){
            done.add(bo.getHoursDone());
        }
        return done;
    }
    public List<Double> getTasksRemaining(){
        ArrayList<Double> rem=new ArrayList<Double>();
        for (BurndownObject bo : this.bdo){
            rem.add(bo.getTasksRem());
        }
        return rem;
    }
    public List<Double> getTasksDone(){
        ArrayList<Double> done=new ArrayList<Double>();
        for (BurndownObject bo : this.bdo){
            done.add(bo.getTasksDone());
        }
        return done;
    }
   
    public abstract List<BurndownObject> getBurndownObjects();

    private String getURL(){
        return fo.getURL();
    }
    public class BurndownObject {

        double hoursRem=-1;
        double hoursDone=-1;
        double tasksRem=-1;
        double tasksDone=-1;
        long timeStamp=-1;
        public BurndownObject(){}

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
        public boolean unset(){
            return (this.hoursDone==-1 || this.hoursRem == -1 || this.tasksDone ==-1 || this.tasksRem ==-1 || this.timeStamp==-1);
        }

    }
    
}

