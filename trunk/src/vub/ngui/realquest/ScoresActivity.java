package vub.ngui.realquest;

import java.sql.Date;

import vub.ngui.realquest.R;
import vub.ngui.realquest.R.layout;
import vub.ngui.realquest.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class ScoresActivity extends Activity {
	
	private long mytime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mytime = getIntent().getLongExtra(MapQuestActivity.QUEST_STARTING_TIME, 0);
        if (mytime == 0) {
			Toast.makeText(getApplicationContext(), "SOMETHING TERRIBLE WENT WRONG", Toast.LENGTH_LONG).show();
		} else {
			Date myDate = new Date(mytime);
			if (mytime < MainActivity.getInstance().quest.getTime()) ((TextView) findViewById(R.id.winLoss)).setText("YOU WIN");
			else ((TextView) findViewById(R.id.winLoss)).setText("YOU LOSE");
			Date questDate = new Date(MainActivity.getInstance().quest.getTime());
			((TextView) findViewById(R.id.winLossInfo)).setText("you finished in: "+myDate.toString()+ " vs The set time of: "+ questDate.toString());
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_scores, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
