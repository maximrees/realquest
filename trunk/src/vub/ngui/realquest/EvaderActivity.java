package vub.ngui.realquest;

import vub.ngui.realquest.minigames.Evader;
import vub.ngui.realquest.minigames.Evader.EvaderThread;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class EvaderActivity extends Activity {
    
	private EvaderThread mGameThread;
	private Evader mGameView;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        createSurfaceView(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
	
	@Override
    protected void onResume(){
    	super.onResume();
    }
	
	private void createSurfaceView(Bundle savedInstanceState){
		setContentView(R.layout.activity_evader);
        
		mGameView = (Evader) findViewById(R.id.gameview);
        mGameThread = mGameView.getThread();
        
        mGameView.setPlayActivity(this);
    }
	
	@Override
    protected void onStop() {
        super.onStop();
    }
	
	@Override
    protected void onRestart(){
    	super.onRestart();
    	createSurfaceView(null);
     }
	
	
}