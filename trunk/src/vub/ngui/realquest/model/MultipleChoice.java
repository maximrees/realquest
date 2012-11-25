package vub.ngui.realquest.model;

import java.util.HashMap;
import java.util.Map;

import com.google.android.maps.GeoPoint;

public class MultipleChoice extends MiniGame {
	
	private Map<String, Diversion>  questions = new HashMap<String, Diversion>();

	public MultipleChoice(GeoPoint location, Map<String, Diversion> questions) {
		super(location);
		this.questions = questions;
	}

	public Map<String, Diversion> getQuestions() {
		return questions;
	}

	public void setQuestions(Map<String, Diversion> questions) {
		this.questions = questions;
	}


	

}
