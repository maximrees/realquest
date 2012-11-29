package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.location.Location;

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


	

}
