package vub.ngui.realquest;

import vub.ngui.realquest.R;
import vub.ngui.realquest.R.layout;
import vub.ngui.realquest.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_main);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void startQuest(View v) {
        Intent intent = new Intent(MainActivity.this, QuestLoaderActivity.class);
        startActivity(intent);
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
        Intent intent = new Intent(MainActivity.this, ProximityGaugeActivity.class);
        startActivity(intent);
    }
    
    public void quitMain(View v) {
        MainActivity.this.finish();
    }
}
