package rhit.jrProj.henry.firebase;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
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
    List<BurndownObject> bdo = new ArrayList<BurndownObject>();
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

    public List<BurndownPoint> getBurndownPoints() {
        ArrayList<BurndownPoint> bdp = new ArrayList<BurndownPoint>();
        if (this.fo instanceof Project) {
            ArrayList<Milestone> milestones = ((Project) fo).getMilestones();
            for (int m = 0; m < milestones.size(); m++) {

                ArrayList<BurndownObject> bdo = (ArrayList<BurndownObject>) milestones.get(m).getBurndownObjects();
                for (int i = 0; i < bdo.size(); i++) {
                    BurndownPoint bp = new BurndownPoint();
                    bp.setX(m + (new Double(i) / bdo.size()));
                    bp.setBurndownObject(bdo.get(i));
                    bdp.add(bp);
                }
            }

        } else if (this.fo instanceof Milestone) {

            ArrayList<BurndownObject> bdo = (ArrayList<BurndownObject>) ((Milestone) fo).getBurndownObjects();
            for (int i = 0; i < bdo.size(); i++) {
                BurndownPoint bp = new BurndownPoint();
                bp.setX(new Double(i));
                bp.setBurndownObject(bdo.get(i));
                bdp.add(bp);
            }
        }
        return bdp;
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

    }
    
}

