package vub.ngui.realquest.model;

import java.util.ArrayList;

import vub.ngui.realquest.MiniGameActivity;
import vub.ngui.realquest.R;
import vub.ngui.realquest.R.id;
import vub.ngui.realquest.R.layout;
import vub.ngui.realquest.model.EvaderSurfaceView.EvaderThread;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Evader extends MiniGame {

	private EvaderThread mGameThread;
	private EvaderSurfaceView mGameView;
	private int score;
	
	public Evader(Location point, ArrayList<Diversion> fail) {
		super(point, new ArrayList<Diversion>());
		super.setFailureRoutes(fail);
		
	}

	@Override
	public void launchMinigame(Activity minigameActivity, ViewGroup view) {
		int result;
		minigameActivity.setContentView(R.layout.activity_evader);
		minigameActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mGameView = (EvaderSurfaceView) minigameActivity.findViewById(R.id.MiniGameRelativeLayout);		
		
        mGameThread = mGameView.getThread();
        mGameThread.setCustomEventListener(new OnCustomEventListener(){
            public void onEvent(){
            	score = mGameThread.getAttempts();
            	MiniGameActivity parent = (MiniGameActivity) mGameThread.getActivity();
            	if (score < 2) {
            		parent.checkFailRoute(0);
            	} else {
            		parent.checkFailRoute(1);
            	}
            }
            });
        mGameView.setPlayActivity(minigameActivity);
	}
	
	
	/** Called when the activity is first created. */
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//	
//        createSurfaceView(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
////        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }
    
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
	
//	@Override
//    protected void onResume(){
//    	super.onResume();
//    }
	
//	private void createSurfaceView(Bundle savedInstanceState){
//		setContentView(R.layout.activity_evader);
//        
//		mGameView = (EvaderSurfaceView) findViewById(R.id.gameview);
//        mGameThread = mGameView.getThread();
//        
//        mGameView.setPlayActivity(this);
//    }
	
//	@Override
//    protected void onStop() {
//        super.onStop();
//    }
	
//	@Override
//    protected void onRestart(){
//    	super.onRestart();
//    	createSurfaceView(null);
//     }
	
	
}