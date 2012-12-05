package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import vub.ngui.realquest.MainActivity;
import vub.ngui.realquest.MiniGameActivity;
import vub.ngui.realquest.R;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class MultipleChoice extends MiniGame implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3283280322827700376L;
	private String question;
	private Map<String, Diversion>  answers = new HashMap<String, Diversion>();
	private RadioGroup radgroup;
	private Activity parent;
	private MultipleChoice myself;

	public MultipleChoice(Location point, String question, Map<String, Diversion> answers) {
		super(point, new ArrayList<Diversion>());
		this.question = question;
		ArrayList<Diversion> diversions = new ArrayList<Diversion>(answers.values());
		super.setFailureRoutes(diversions);
		this.answers = answers;
		myself = this;
	}

	public Map<String, Diversion> getAnswers() {
		return answers;
	}

	public void setQuestions(Map<String, Diversion> answers) {
		this.answers = answers;
	}

	@Override
	public void launchMinigame(Activity minigameActivity, ViewGroup view) {
		parent = minigameActivity;
		TextView questionView = new TextView(parent);
		questionView.setText(question);
		questionView.setGravity(Gravity.CENTER_HORIZONTAL);
		view.addView(questionView);
		radgroup = new RadioGroup(parent);
		int i = 1;
		for( String key : answers.keySet()){
			RadioButton answerX = new RadioButton(parent);
			answerX.setText(Integer.toString(i) + ". " +key);
			radgroup.addView(answerX);
			i++;
		}
		view.addView(radgroup);
		Button confirm = (Button) new Button(parent);
		confirm.setText(minigameActivity.getResources().getString(R.string.button_solve_multiple_choice));
		confirm.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				if( radgroup.getCheckedRadioButtonId() == -1){
					Toast.makeText(parent.getApplicationContext(), R.string.confirm_button_no_radio_selected, Toast.LENGTH_SHORT).show();
				} else{
					RadioButton button = (RadioButton) parent.findViewById(radgroup.getCheckedRadioButtonId());
					String text = (String) button.getText();	
					//IMPORTANT NOTE: this part of the code should be enforced NOTDONE, a minigame implementation should give back a valid int 
					//that references to one of their failpaths, wether this int is valid should be checked by minigameactivityDONE
					//and minigameactivity should implement setting the int in mainactivity DONE KINDOF
					myself.setFailRoute(Integer.parseInt(text.substring(0, 1))-1, parent);//questions from 1- .. arrays from 0-...
				}				
			}
		});
		view.addView(confirm);	
	}






	

}
