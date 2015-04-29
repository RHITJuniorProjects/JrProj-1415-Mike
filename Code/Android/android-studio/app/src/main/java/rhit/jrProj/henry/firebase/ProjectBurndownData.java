package rhit.jrProj.henry.firebase;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rhit.jrProj.henry.GlobalVariables;

/**
 * Created by daveyle on 4/28/2015.
 */
public class ProjectBurndownData extends BurndownData<Project> {

    public ProjectBurndownData(Project p){
        super(p);
    }
    @Override 
    public List<BurndownObject> getBurndownObjects(){
        ArrayList<BurndownObject> bdo=new ArrayList<BurndownObject>();
        for (Milestone m: ((Project)fo).getMilestones()){
            BurndownObject bo=new BurndownObject();
            Firebase fb=new Firebase(GlobalVariables.getFirebaseUrl()+"/projects/"+fo.getID()+"/milestones/"+m.getID()+"/burndown_data/");
//            fb.addListenerForSingleValueEvent(new BurndownListener(bo));
            fb.addListenerForSingleValueEvent(new BurndownListener(bo));
//            while (bo.unset()){
//
//            }
            Log.i(((Double)bo.getTasksRem()).toString(), "hi");
            bdo.add(bo);
        }
        return bdo;
    }

class BurndownListener implements ValueEventListener {
        BurndownObject bo;

        public BurndownListener(BurndownObject bo) {
            this.bo = bo;
        }


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChildren()) {
                Log.i("HAS CHILDREN", "HELLO");
                Iterator<DataSnapshot> iter = dataSnapshot.getChildren().iterator();
                DataSnapshot dS = iter.next();
//                while (iter.hasNext()) {
//                    dS = iter.next();
//                }
                dS.getRef().addListenerForSingleValueEvent(new BurndownChildListener(this.bo));

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
    private class BurndownChildListener implements ValueEventListener{
        BurndownObject bo;
        public BurndownChildListener(BurndownObject bo){
            this.bo=bo;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot arg0 : dataSnapshot.getChildren()) {
                if (arg0.getKey().equals("estimated_hours_remaining")) {
                    this.bo.setHoursRem(arg0.getValue(Double.class));
                } else if (arg0.getKey().equals("hours_completed")) {
                    this.bo.setHoursDone(arg0.getValue(Double.class));
                } else if (arg0.getKey().equals("tasks_remaining")) {
                    this.bo.setTasksRem(arg0.getValue(Double.class));
                } else if (arg0.getKey().equals("tasks_completed")) {
                    this.bo.setTasksDone(arg0.getValue(Double.class));


                }

            }
            bo.setTimeStamp(new Long(dataSnapshot.getKey()));

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }



}
