package rhit.jrProj.henry.bridge;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import rhit.jrProj.henry.helpers.Checkers;

/**
 * Created by willisaj on 4/3/2015.
 */
public class Leaderboard implements ChangeNotifiable<Leaderboard> {

    private ChangeNotifier<Leaderboard> mChangeNotifier;

    private Firebase mFirebaseRef;

    private Map<String, LeaderboardUser> mUsers;

    public Leaderboard(Firebase firebaseRef) {
        mUsers = new HashMap<String, LeaderboardUser>();

        this.mFirebaseRef = firebaseRef;
        this.initializeFirebaseListener(mFirebaseRef);
    }

    @Override
    public void setChangeNotifier(ChangeNotifier<Leaderboard> changeNotifier) {
        this.mChangeNotifier = changeNotifier;
    }

    @Override
    public ChangeNotifier<Leaderboard> getChangeNotifier() {
        return this.mChangeNotifier;
    }

    public Map<String, LeaderboardUser> getUsers() {
        return this.mUsers;
    }

    private void initializeFirebaseListener(Firebase firebaseRef) {
        firebaseRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onCancelled(FirebaseError arg0) {
                // do nothing
            }

            @Override
            public void onChildAdded(DataSnapshot arg0, String arg1) {
                updateUser(arg0);
                if (mChangeNotifier != null) {
                    mChangeNotifier.onChange();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot arg0, String arg1) {
                updateUser(arg0);
                if (mChangeNotifier != null) {
                    mChangeNotifier.onChange();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot arg0, String arg1) {
                // do nothing
            }

            @Override
            public void onChildRemoved(DataSnapshot arg0) {
                // do nothing
            }
        });
    }

    private void updateUser(DataSnapshot arg0) {
        String id = arg0.getKey();
        String name = arg0.child("name").getValue(String.class);
        try {
            Checkers.checkNotNull(name);
        } catch (Exception e) {
            // Reference was null,
            return;
        }

        LeaderboardUser user = mUsers.get(id);
        if (user == null) {
            user = new LeaderboardUser(id, name);
            mUsers.put(id, user);
        }

        Integer total_points = arg0.child("total_points").getValue(
                Integer.class);
        try {
            Checkers.checkNotNull(total_points);
            Checkers.checkNotNegative(total_points);
            user.setTotalPoints(total_points);
        } catch (Exception e) {
            // Reference did not exist or corrupt data
            mUsers.remove(id);
        }
    }

    public class LeaderboardUser implements Comparable<LeaderboardUser> {

        private String mId;
        private String mName;

        private int mTotal_Points;

        public LeaderboardUser(String id, String name) {
            mId = id;
            mName = name;
        }

        public String getName() {
            return mName;
        }

        public int getTotalPoints() {
            return mTotal_Points;
        }

        public void setTotalPoints(int total_points) {
            mTotal_Points = total_points;
        }

        public String getId() {
            return this.mId;
        }

        @Override
        public int compareTo(LeaderboardUser another) {
            if (another.getTotalPoints() > this.getTotalPoints()) {
                return 1;
            }
            return -1;
        }

    }
}
