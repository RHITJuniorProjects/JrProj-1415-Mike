package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rhit.jrProj.henry.TaskListFragment.Callbacks;
import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.helpers.HorizontalPicker;

import com.firebase.client.Firebase;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment
		 {
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;
	private DatePicker mDatePicker;


	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static DatePickerFragment newInstance(String projectid, Callbacks call) {
		DatePickerFragment f = new DatePickerFragment(call);

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("projectid", projectid);
		f.setArguments(args);

		return f;
	}
	public DatePickerFragment(Callbacks f){
		super();
		this.mCallbacks=f;
	}

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void setDate(String d);
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {

		public void setDate(String s){
			
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle("Choose Due Date");

		View v = inflater.inflate(R.layout.fragment_date_picker, container,
				false);
		mDatePicker=(DatePicker)v.findViewById(R.id.datePicker1);
		Button add=(Button)v.findViewById(R.id.BountyAddButton);
		Button cancel=(Button)v.findViewById(R.id.BountyCancelButton);
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setDate();
				DatePickerFragment.this.dismiss();
				
			}
			
		});
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DatePickerFragment.this.dismiss();
				
			}
			
		});

		

		return v;
	}

	/**
	 * Opens a dialog window that displays the given error message.
	 * 
	 * @param message
	 */
	private void showErrorDialog(String message) {
		new AlertDialog.Builder(this.getActivity()).setTitle("Error")
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.setIcon(android.R.drawable.ic_dialog_alert).show();
	}
	private void setDate(){
		String s=mDatePicker.getYear()+"-"+(mDatePicker.getMonth()+1)+"-"+mDatePicker.getDayOfMonth();
		mCallbacks.setDate(s);
	}

	
	
	
}