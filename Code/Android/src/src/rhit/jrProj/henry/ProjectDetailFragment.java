package rhit.jrProj.henry;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import rhit.jrProj.henry.content.ProjectContent;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;

/**
 * A fragment representing a single Project detail screen. This fragment is
 * either contained in a {@link ProjectListActivity} in two-pane mode (on
 * tablets) or a {@link ProjectDetailActivity} on handsets.
 */
public class ProjectDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Project projectItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			projectItem = ProjectContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_project_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (projectItem != null) {
			((TextView) rootView.findViewById(R.id.project_detail))
					.setText(projectItem.toString());
		}

		return rootView;
	}
	
	/**
	 * The method that is called when the "View Tasks" button
	 * is pressed.
	 * @param view
	 */
	public void openMilestoneView(View view)
	{
		//TODO: Start intent to feature 2.
		Intent intent = new Intent();
		ArrayList<Milestone> milestones = projectItem.getMilestones();
	//	intent.put
		this.startActivity(intent);
	}
}
