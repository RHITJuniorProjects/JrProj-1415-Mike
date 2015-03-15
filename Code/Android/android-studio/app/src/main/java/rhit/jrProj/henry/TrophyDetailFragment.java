package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.GraphicalView;

import rhit.jrProj.henry.BountyDetailFragment.Callbacks;
import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.Trophy;
import rhit.jrProj.henry.helpers.GraphHelper;
import rhit.jrProj.henry.helpers.HorizontalPicker;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A fragment representing a single Trophy detail screen. This fragment is
 * either contained in a {@link TrophyListActivity} in two-pane mode (on
 * tablets) or a {@link TrophyDetailActivity} on handsets.
 */
public class TrophyDetailFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Trophy trophyItem;
	
	private LinearLayout mMembersList;

	private boolean mTwoPane;

	private Callbacks mCallbacks;


	private TextView pointsField;
	
	private Button buyButton;

	public interface Callbacks {
		public Trophy getSelectedTrophy();
		
		public int getAvailablePoints();
	}

	/**
	 * A dummy implementation of the {@link rhit.jrProj.henry.TrophyDetailFragment.Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		public Trophy getSelectedTrophy() {
			return null;
		}
		public int getAvailablePoints(){
			return 0;
		}
	};

	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TrophyDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey("TwoPane")) {
			this.mTwoPane = getArguments()
					.getBoolean("TwoPane");
		}

	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		MenuItem sorting= menu.findItem(R.id.action_sorting);
		
		sorting.setEnabled(false);
		sorting.setVisible(false);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(
				R.layout.fragment_trophy_detail, container, false);
		this.trophyItem = this.mCallbacks.getSelectedTrophy();
		if (this.trophyItem != null) {
			((TextView) rootView.findViewById(R.id.trophy_name))
					.setText(this.trophyItem.getName());
			((TextView) rootView.findViewById(R.id.trophy_description))
					.setText("Description: "
							+ this.trophyItem.getDescription());
			this.pointsField = ((TextView) rootView
					.findViewById(R.id.trophy_cost));
			this.pointsField.setText("Point Cost: \t"
					+ this.trophyItem.getCost());
			this.buyButton=((Button) rootView.findViewById(R.id.buyTrophyButton));
			if (this.mCallbacks.getAvailablePoints()<this.trophyItem.getCost()){
				this.buyButton.setEnabled(false);
			}
			else{
				this.buyButton.setEnabled(true);
			}
		}

		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		this.mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mCallbacks = sDummyCallbacks;
	}

}
