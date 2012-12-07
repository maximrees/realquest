package vub.ngui.realquest.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vub.ngui.realquest.MiniGameActivity;
import vub.ngui.realquest.R;
import vub.ngui.realquest.ui.custom.myOnClickListener;
import android.app.ActionBar;
import android.app.Activity;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class MultipleChoice extends MiniGame  {
	


	private String question;
	private Map<String, Diversion>  answers = new HashMap<String, Diversion>();


	public MultipleChoice(Location point, String question, Map<String, Diversion> answers) {
		super(point, new ArrayList<Diversion>() );
		this.question = question;
		ArrayList<Diversion> diversions = new ArrayList<Diversion>(answers.values());
		super.setFailureRoutes(diversions);
		this.answers = answers;
	}

//	public Map<String, Diversion> getAnswers() {
//		return answers;
//	}
//
//	public void setQuestions(Map<String, Diversion> answers) {
//		this.answers = answers;
//	}
	

	@Override
	public void launchMinigame(Activity minigameActivity, ViewGroup view) {
		minigameActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ActionBar bar = minigameActivity.getActionBar();
		bar.setTitle("   " + question);
		
		
		// TODO: listview met radiobuttons

		RadioGroup radgroup = new RadioGroup(minigameActivity);
		int i = 1;
		for( String key : answers.keySet()){
			RadioButton answerX = new RadioButton(minigameActivity);
			answerX.setText(Integer.toString(i) + ". " +key);
			radgroup.addView(answerX);
			i++;
		}
		view.addView(radgroup);
		
		Button confirm = (Button) new Button(minigameActivity);
		
		confirm.setText(minigameActivity.getResources().getString(R.string.button_solve_multiple_choice));
		confirm.setOnClickListener(new myOnClickListener(radgroup, (MiniGameActivity) minigameActivity) {			
			public void onClick(View v) {
				
				if( this.getGroup().getCheckedRadioButtonId() == -1){
					Toast.makeText(this.getActivity().getApplicationContext(), R.string.confirm_button_no_radio_selected, Toast.LENGTH_SHORT).show();
				} else{
					RadioButton button = (RadioButton) this.getActivity().findViewById(this.getGroup().getCheckedRadioButtonId());
					String text = (String) button.getText();	
					//IMPORTANT NOTE: this part of the code should be enforced NOTDONE, a minigame implementation should give back a valid int 
					//that references to one of their failpaths, wether this int is valid should be checked by minigameactivityDONE
					//and minigameactivity should implement setting the int in mainactivity DONE KINDOF
					this.getActivity().checkFailRoute(Integer.parseInt(text.substring(0, 1))-1);//questions from 1- .. arrays from 0-...
					this.getActivity().finish();
				}			
			}
		});
		
		RelativeLayout buttonHolder = new RelativeLayout(minigameActivity);  
        
        RelativeLayout.LayoutParams params = new LayoutParams(
        		RelativeLayout.LayoutParams.FILL_PARENT,  
        		RelativeLayout.LayoutParams.FILL_PARENT);
        buttonHolder.setLayoutParams(params); 
        
        RelativeLayout.LayoutParams b1 = new LayoutParams(  
        		RelativeLayout.LayoutParams.FILL_PARENT,  
        		RelativeLayout.LayoutParams.WRAP_CONTENT);
        b1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        b1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        confirm.setLayoutParams(b1);
        
        buttonHolder.addView(confirm);
		view.addView(buttonHolder);
	}
}