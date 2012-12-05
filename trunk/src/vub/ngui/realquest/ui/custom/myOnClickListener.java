package vub.ngui.realquest.ui.custom;

import vub.ngui.realquest.MiniGameActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;

public class myOnClickListener implements OnClickListener {
	
	private RadioGroup group;
	private MiniGameActivity activity;
	
	public myOnClickListener(RadioGroup group, MiniGameActivity activity){
		super();
		this.group = group;
		this.activity = activity;
		
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	public RadioGroup getGroup() {
		return group;
	}

	public void setGroup(RadioGroup group) {
		this.group = group;
	}

	public MiniGameActivity getActivity() {
		return activity;
	}

	public void setActivity(MiniGameActivity activity) {
		this.activity = activity;
	}
	

	

}
