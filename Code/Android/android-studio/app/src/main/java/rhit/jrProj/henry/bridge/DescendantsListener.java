package rhit.jrProj.henry.bridge;

import java.util.ArrayList;

import rhit.jrProj.henry.GlobalVariables;
import rhit.jrProj.henry.TasksAllListFragment;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class DescendantsListener implements ChildEventListener {
    final TasksAllListFragment f;


    public DescendantsListener(TasksAllListFragment f) {
        super();
        this.f = f;
    }


    @Override
    public void onCancelled(FirebaseError arg0) {
        // do nothing
    }

    @Override
    public void onChildAdded(DataSnapshot arg0, String arg1) {
        if (arg0.getKey().equals("name")) {
            updateUI(arg0);
        } else if (arg0.getKey().equals("projects")) {
            for (DataSnapshot child : arg0.getChildren()) {
                child.getRef().addChildEventListener(new ProjectListener(child.getKey()));
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot arg0, String arg1) {
        updateUI(arg0);
    }

    @Override
    public void onChildMoved(DataSnapshot arg0, String arg1) {
        // do nothing
    }

    @Override
    public void onChildRemoved(DataSnapshot arg0) {
        // do nothing
    }

    public void updateUI(DataSnapshot arg0) {
        if (arg0.getKey().equals("name")) {
            f.changeName(arg0.getValue(String.class));
        } else {
            f.fireChangeInData();
        }
    }

    class ProjectListener implements ChildEventListener {

        String projectID;

        public ProjectListener(String projectID) {
            this.projectID = projectID;
        }

        @Override
        public void onCancelled(FirebaseError arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onChildAdded(DataSnapshot arg0, String arg1) {
            if (arg0.getKey().equals("milestones")) {
                for (DataSnapshot child : arg0.getChildren()) {
                    child.getRef().addChildEventListener(new MilestoneListener(projectID, child.getKey()));
                }
            }

        }

        @Override
        public void onChildChanged(DataSnapshot arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onChildMoved(DataSnapshot arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onChildRemoved(DataSnapshot arg0) {
            // TODO Auto-generated method stub

        }

    }

    class MilestoneListener implements ChildEventListener {
        String projectID;
        String milestoneID;

        public MilestoneListener(String projectID, String milestoneID) {
            this.projectID = projectID;
            this.milestoneID = milestoneID;
        }

        @Override
        public void onCancelled(FirebaseError arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onChildAdded(DataSnapshot arg0, String arg1) {
            if (arg0.getKey().equals("tasks")) {
                for (DataSnapshot child : arg0.getChildren()) {
                    //Log.i("child", child.getKey());


                    final Task t = new Task(GlobalVariables.getFirebaseUrl() + "projects/" + projectID + "/milestones/" + milestoneID + "/tasks/" + child.getKey());
                    new Firebase(GlobalVariables.getFirebaseUrl() + "projects/" + projectID).child("name").getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onCancelled(FirebaseError arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onDataChange(DataSnapshot arg0) {
                            t.setParentProjectName(arg0.getValue(String.class));

                        }

                    });
                    new Firebase(GlobalVariables.getFirebaseUrl() + "projects/" + projectID + "/milestones/" + milestoneID).child("name").getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onCancelled(FirebaseError arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onDataChange(DataSnapshot arg0) {
                            t.setParentMilestoneName(arg0.getValue(String.class));

                        }

                    });
                    new Firebase(GlobalVariables.getFirebaseUrl() + "projects/" + projectID + "/milestones/" + milestoneID + "/tasks/" + child.getKey()).child("status").getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onCancelled(FirebaseError arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onDataChange(DataSnapshot arg0) {
                            t.setStatus(arg0.getValue(String.class));

                        }

                    });
                    new Firebase(GlobalVariables.getFirebaseUrl() + "projects/" + projectID + "/milestones/" + milestoneID + "/tasks/" + child.getKey()).child("assignedTo")
                            .getRef().addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onCancelled(FirebaseError arg0) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onDataChange(DataSnapshot arg0) {
                            if (arg0.getValue(String.class).equals(new Firebase(GlobalVariables.getFirebaseUrl()).getAuth().getUid())) {
                                f.addTask(t);
                            }

                        }

                    });

                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onChildMoved(DataSnapshot arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onChildRemoved(DataSnapshot arg0) {
            // TODO Auto-generated method stub

        }

    }
}


