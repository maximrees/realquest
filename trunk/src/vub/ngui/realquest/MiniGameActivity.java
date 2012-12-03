package vub.ngui.realquest;

import vub.ngui.realquest.model.MiniGame;
import vub.ngui.realquest.model.MultipleChoice;
import vub.ngui.realquest.model.Quest;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MiniGameActivity extends Activity {
	
	private Quest quest;
	private LinearLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mini_game);
		parentLayout = (LinearLayout) findViewById(R.id.MiniGameLinearLayout);
		quest = MainActivity.getInstance().quest;
		
		
	
		((MultipleChoice) quest.getMiniGameInfo().get(0)).launchMinigame(this, parentLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mini_game, menu);
		return true;
	}

}
