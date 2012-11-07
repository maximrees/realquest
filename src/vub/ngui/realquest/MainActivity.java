package vub.ngui.realquest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void startQuest(View v) {
        // Intent intent = new Intent(MainActivity.this, Activity.class);
        // startActivity(intent);
    }
    
    public void startScores(View v) {
        Intent intent = new Intent(MainActivity.this, ScoresActivity.class);
        startActivity(intent);
    }
    
    public void startSettings(View v) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
    
    public void startCredits(View v) {
        Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
        startActivity(intent);
    }
    
    public void quitMain(View v) {
        MainActivity.this.finish();
    }
}
