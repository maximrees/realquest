package vub.ngui.realquest;

import vub.ngui.realquest.model.MiniGame;
import vub.ngui.realquest.model.MultipleChoice;
import vub.ngui.realquest.model.Quest;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MiniGameActivity extends Activity {
	
	private Quest quest;
	private RelativeLayout parentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mini_game);
		parentLayout = (RelativeLayout) findViewById(R.id.MiniGameRelativeLayout);
		quest = MainActivity.getInstance().quest;
		String klasse = quest.getMiniGameInfo().get(0).getClass().toString();		
		//TODO: if i wanna do this properly i should just sent a listener with this function but yeh wutever
		 quest.getMiniGameInfo().get(0).launchMinigame(this, parentLayout);
		 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_mini_game, menu);
		return true;
	}
	
	
	public void checkFailRoute(int index){
		if(quest.getMiniGameInfo().size() <= index || index < 0){
			Toast.makeText(getApplicationContext(), "invalid arrayindex", Toast.LENGTH_SHORT).show();
			//TODO: kill the quest and go back to menu, remove quest from file. complain to creator quest
		} else{
			MainActivity.getInstance().selection = index;
		}
	}

}
