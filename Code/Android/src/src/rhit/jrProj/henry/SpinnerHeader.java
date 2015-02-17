package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.helpers.HorizontalPicker.Callbacks;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class SpinnerHeader extends LinearLayout implements OnItemSelectedListener{
	Spinner mShowType;
	private Callbacks mCallbacks = sDummyCallbacks;
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */

		public void onSpinnerHeaderChange(int nu);

	}
	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onSpinnerHeaderChange(int nu){
		}
	};

	public void setCallbacks(Callbacks c) {
		this.mCallbacks = c;
	}
	public SpinnerHeader(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.switch_header_layout, this);
		this.mShowType = (Spinner) this.findViewById(R.id.show_spinner);
		// Create an ArrayAdapter using the string array and a default
		// spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				context, R.array.tasks_all,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		mShowType.setAdapter(adapter);
		mShowType.setOnItemSelectedListener(this);
	}

	

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		this.mCallbacks.onSpinnerHeaderChange(position);
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
