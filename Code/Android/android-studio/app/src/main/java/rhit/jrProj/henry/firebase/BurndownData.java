package rhit.jrProj.henry.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rhit.jrProj.henry.GlobalVariables;
import rhit.jrProj.henry.bridge.ChangeNotifiable;
import rhit.jrProj.henry.bridge.ChangeNotifier;
import rhit.jrProj.henry.bridge.FirebaseObject;

/**
 * Created by daveyle on 4/28/2015.
 */
public class BurndownData<T extends FirebaseObject> implements ChangeNotifiable<BurndownData> {
    T fo;
    ArrayList<BurndownObject> bdo = new ArrayList<BurndownObject>();
    ChangeNotifier changeNotifier;

    public BurndownData(T fo) {
        this.fo = fo;

    }



    public void setChangeNotifier(ChangeNotifier<BurndownData> cn) {
        changeNotifier = cn;
    }

    public ChangeNotifier<BurndownData> getChangeNotifier() {
        return this.changeNotifier;
    }

    public int getMaxYHours(){
        int maxY=-1;
        for (BurndownObject bo: bdo){
            if (bo.getHoursDone()>maxY){
                maxY=(int)bo.getHoursDone();
            }
            else if (bo.getHoursRem() > maxY){
                maxY= (int) bo.getHoursRem();
            }
        }
        return maxY;
    }
    public int getMaxYTasks(){
        int maxY=-1;
        for (BurndownObject bo: bdo){
            if (bo.getTasksDone()>maxY){
                maxY=(int)bo.getTasksDone();
            }
            else if (bo.getTasksRem() > maxY){
                maxY= (int) bo.getTasksRem();
            }
        }
        return maxY;
    }

    public List<BurndownPoint> getBurndownPoints() {
        ArrayList<BurndownPoint> bdp = new ArrayList<BurndownPoint>();

        ArrayList<BurndownObject> sortedBO=this.bdo;
        Collections.sort(bdo);
        for (int i=0; i<bdo.size(); i++){
            bdp.add(new BurndownPoint((double)i, bdo.get(i)));
        }
        return bdp;

    }
    public void addBurndownObject(BurndownObject bo){
        this.bdo.add(bo);
    }
    public void combine(BurndownData bd){
        this.bdo.addAll(bd.getBurndownObjects());
    }
    public List<BurndownObject> getBurndownObjects(){
        return this.bdo;
    }




    private String getURL(){
        return fo.getURL();
    }

    public class BurndownPoint{
        Double x;
        BurndownObject bo;
        public BurndownPoint(double d, BurndownObject bo){
            this.x=d;
            this.bo=bo;
        }
        public BurndownPoint(){}
        public void setX(double d){
            this.x=d;
        }
        public Double getX(){
            return this.x;
        }
        public void setBurndownObject(BurndownObject bo){
            this.bo=bo;
        }
        public BurndownObject getBurndownObject(){
            return this.bo;
        }
        public Double getEstimatedHoursRemaining() {

                return bo.getHoursRem();

        }

        public Double getHoursWorked() {
            return bo.getHoursDone();
        }

        public Double getTasksRemaining() {
            return bo.getTasksRem();
        }

        public Double getTasksDone() {
            return bo.getTasksDone();

        }


        public int compareTimeStamps(BurndownPoint o) {
           return this.getBurndownObject().compareTo(o.getBurndownObject());
        }
    }
    
}

