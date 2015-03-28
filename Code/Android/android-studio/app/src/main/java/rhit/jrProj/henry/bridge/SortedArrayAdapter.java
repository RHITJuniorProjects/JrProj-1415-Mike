package rhit.jrProj.henry.bridge;

import java.util.List;

import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import rhit.jrProj.henry.R;

/**
 * An ArrayAdapter subclass designed for the project list. It is used for
 * sorting.
 *
 * @author daveyle. Created Nov 6, 2014.
 */
public class SortedArrayAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    private final List<T> objects;
    private final Enums.ObjectType type;
    private String usersName;
    private boolean flag = false;
    private boolean viewAllMyTasks = false;

    public SortedArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<T> objects, Enums.ObjectType type, boolean flag) {
        super(context, R.layout.list_image_layout, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.type = type;
        this.usersName = "";
        this.flag = flag;
    }

    public SortedArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<T> objects, Enums.ObjectType type, boolean flag, boolean viewAllMyTasks) {
        super(context, R.layout.list_image_layout, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.type = type;
        this.usersName = "";
        this.flag = flag;
        this.viewAllMyTasks = viewAllMyTasks;
    }

    /**
     * This constructor is used for tasks, which require knowledge of the user's
     * name so that it can flag appropriately.
     *
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param objects
     * @param type
     * @param usersName
     */
    public SortedArrayAdapter(Context context, int resource,
                              int textViewResourceId, List<T> objects, Enums.ObjectType type,
                              String usersName, boolean flag) {
        super(context, R.layout.list_image_layout, textViewResourceId, objects);
        this.context = context;
        this.objects = objects;
        this.type = type;
        this.usersName = usersName;
        this.flag = flag;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * This method overrides the default getView method to show a two line view
     * that has a due date. It also controls the displaying of icons such as
     * flags and trophies.
     */
    @Override
    public View getView(int position, View convertView,
                        android.view.ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_image_layout, parent, false);
        TextView text1 = (TextView) view.findViewById(R.id.firstline);
        TextView text2 = (TextView) view.findViewById(R.id.secondline);
        ImageView img1 = (ImageView) view.findViewById(R.id.imageView);
        img1.setVisibility(View.GONE);
        if (this.type == Enums.ObjectType.PROJECT) {
            Project p = (Project) super.getItem(position);
            if (p != null) {
                text1.setText(p.getName());
                text2.setText("Due: "
                        + p.getDueDateFormatted());

            }

        } else if (this.type == Enums.ObjectType.MILESTONE) {
            Milestone m = (Milestone) super.getItem(position);
            text1.setText(m.getName());
            text2.setText("Due: "
                    + m.getDueDateFormatted());

        } else if (this.type == Enums.ObjectType.TASK) {
            Task t = (Task) super.getItem(position);
            if ((t.getAssignedUserName()).equals(this.usersName) && this.flag) {
                //Should only show flags/trophies on tasks assigned to the current logged in user.
                if (t.getStatus().equals(Enums.CLOSED)) {
                    //If task is done, show a little green trophy.
                    img1.setImageResource(R.drawable.ic_action_achievement);
                } else {
                    //If task is not done, show a red flag.
                    img1.setImageResource(R.drawable.ic_action_flag);
                }
                //Show the image if the task is assigned to the logged in user.
                img1.setVisibility(View.VISIBLE);
                text2.setText("Assigned to: " + t.getAssignedUserName());
            } else if (this.viewAllMyTasks) {
                text2.setText("Project: " + t.getParentProjectName() + "\nMilestone: " + t.getParentMilestoneName());
            } else {
                text2.setText("Assigned to: " + t.getAssignedUserName());
            }
            text1.setText(t.getName());

            text1.setTextSize(20);
            view.refreshDrawableState();
        } else if (this.type == Enums.ObjectType.MEMBER) {
            Member m = (Member) super.getItem(position);
            text1.setText(m.toString());
            text2.setText(m.getEmail());
            text1.setTextSize(20);
            view.refreshDrawableState();
        } else if (this.type == Enums.ObjectType.BOUNTY) {
            Bounty m = (Bounty) super.getItem(position);
            text1.setText(m.getName());
            text2.setText("Points: " + m.getPoints());
            text1.setTextSize(20);
            view.refreshDrawableState();
        }


        return view;
    }

}
