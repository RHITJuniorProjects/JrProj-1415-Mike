package rhit.jrProj.henry.firebase;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rhit.jrProj.henry.GlobalVariables;

/**
 * Created by daveyle on 4/28/2015.
 */
public class MilestoneBurndownData extends BurndownData<Milestone> {

    public MilestoneBurndownData(Milestone p){
        super(p);
    }
    @Override 
    public List<BurndownObject> getBurndownObjects(){
        ArrayList<BurndownObject> bdo=new ArrayList<BurndownObject>();
        Firebase fb=new Firebase(((Milestone)fo).getURL()+"/burndown_data/");
        fb.orderByKey().addListenerForSingleValueEvent(new BurndownListener(bdo));
        return bdo;
    }

    
   class BurndownListener implements ValueEventListener {
        List<BurndownObject> bo;

        public BurndownListener(List<BurndownObject> bo) {
            this.bo = bo;
        }


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot dataPoint : dataSnapshot.getChildren()){
                BurndownObject bdo=new BurndownObject();
              for (DataSnapshot arg0 : dataPoint.getChildren()){
                if (arg0.getKey().equals("estimated_hours_remaining")) {
                    bdo.setHoursRem(arg0.getValue(Double.class));
                } else if (arg0.getKey().equals("hours_completed")) {
                    bdo.setHoursDone(arg0.getValue(Double.class));
                } else if (arg0.getKey().equals("tasks_remaining")) {
                    bdo.setTasksRem(arg0.getValue(Double.class));
                } else if (arg0.getKey().equals("tasks_completed")) {
                    bdo.setTasksDone(arg0.getValue(Double.class));


                }

            }   bdo.setTimeStamp(new Long(dataPoint.getKey()));
            bo.add(bdo);
            }
            
           
        }

        /**
         * Nothing to do here
         */
        public void onCancelled(FirebaseError arg0) {
            // TODO Auto-generated method stub.
            // nothing to do here
        }


    }


}
