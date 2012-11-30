package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

import com.google.android.maps.GeoPoint;

public class MultipleChoice extends MiniGame implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3283280322827700376L;
	private String question;
	private Map<String, Diversion>  answers = new HashMap<String, Diversion>();

	public MultipleChoice(Location point, String question, Map<String, Diversion> answers) {
		super(point, new ArrayList<Diversion>());
		this.question = question;
		ArrayList<Diversion> diversions = new ArrayList<Diversion>(answers.values());
		super.setFailureRoutes(diversions);
		this.answers = answers;
	}

	public Map<String, Diversion> getAnswers() {
		return answers;
	}

	public void setQuestions(Map<String, Diversion> answers) {
		this.answers = answers;
	}

	@Override
	public void launchMinigame(Activity minigameActivity, ViewGroup view) {
		LayoutInflater inflator = minigameActivity.getLayoutInflater();
		TextView questionView = new TextView(minigameActivity);
		questionView.setText(question);
		questionView.setGravity(Gravity.CENTER_HORIZONTAL);
		view.addView(questionView);
		RadioGroup radgroup = new RadioGroup(minigameActivity);
		for( String key : answers.keySet()){
			RadioButton answerX = new RadioButton(minigameActivity);
			answerX.setText(key);
			radgroup.addView(answerX);
		}
		view.addView(radgroup);
		Button confirm = (Button) new Button(minigameActivity);
		confirm.setText(minigameActivity.getResources().getString(R.string.button_solve_multiple_choice));
		confirm.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//return to mapactivity with new route data to generate map
				
			}
		});
		view.addView(confirm);		
	}




	

}
