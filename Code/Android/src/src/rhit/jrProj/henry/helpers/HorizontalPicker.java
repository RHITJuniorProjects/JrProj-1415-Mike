package rhit.jrProj.henry.helpers;

import rhit.jrProj.henry.R;
import rhit.jrProj.henry.TaskDetailFragment.Callbacks;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Map;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class HorizontalPicker extends LinearLayout{
	private Button minusButton;
	private Button plusButton;
	private EditText text;
	int max_value;
	int min_value;
	private Callbacks mCallbacks = sDummyCallbacks;
	private Task task;
	public HorizontalPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
  
 
        LayoutInflater layoutInflater = (LayoutInflater)context
                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.horizontal_number_picker, this);
        this.minusButton=(Button)this.findViewById(R.id.btn_minus);
        this.plusButton=(Button)this.findViewById(R.id.btn_plus);
        this.text=(EditText)this.findViewById(R.id.edit_text);
        this.text.addTextChangedListener(new TextChangedListener(this));
        this.minusButton.setOnClickListener(new MinusButtonActionListener(this));
        this.plusButton.setOnClickListener(new PlusButtonActionListener(this));
        
    }
	public interface Callbacks {
		public void fireChange(int i);
	}
	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void fireChange(int i){
		}
	};
	public void setCallbacks(Callbacks c){
		this.mCallbacks=c;
	}
	public void setTask(Task t){
		this.task=t;
//		this.setValue(task.getPoints());
	}
	
	public void setValue(int val){
		this.text.setText(val+"");
	}
	
	public int getValue(){
		return new Integer(this.text.getText().toString());
	}
	public void setMinValue(int min){
		this.min_value=min;
	}
	public void setMaxValue(int max){
		this.max_value=max;
	}
	public void textChanged(){
		if (this.mCallbacks!=null){
			this.mCallbacks.fireChange(new Integer(this.text.getText().toString()));
		}
	}
	
	class TextChangedListener implements TextWatcher{
		private HorizontalPicker picker;
		public TextChangedListener(HorizontalPicker hp){
			this.picker=hp;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.length()>0){
			Log.i("Text", "changed");
//			this.picker.setValue(new Integer(s.toString()));
			this.picker.textChanged();
			}
			
		}
		
	}
	class PlusButtonActionListener implements OnClickListener{
		private HorizontalPicker picker;
		public PlusButtonActionListener(HorizontalPicker hp){
			this.picker=hp;
		}

		@Override
		public void onClick(View v) {
			Log.i("Plus", "increase");
			int value=this.picker.getValue();
			if (value+1 > picker.max_value){
				this.picker.setValue(this.picker.min_value);
			}
			else{
				this.picker.setValue(value+1);
			}
			this.picker.textChanged();
			
		}
		
	}
	class MinusButtonActionListener implements OnClickListener{
		private HorizontalPicker picker;
		public MinusButtonActionListener(HorizontalPicker hp){
			this.picker=hp;
		}

		@Override
		public void onClick(View v) {
			Log.i("Minus", "decrease");
			int value=this.picker.getValue();
			if (value-1 < picker.min_value){
				this.picker.setValue(this.picker.max_value);
			}
			else{
				this.picker.setValue(value-1);
			}
			this.picker.textChanged();
			
		}
		
	}
	
	

}