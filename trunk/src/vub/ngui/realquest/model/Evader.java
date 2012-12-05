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

	private int score;
	
	public Evader(Location point, ArrayList<Diversion> fail) {
		super(point, new ArrayList<Diversion>());
		super.setFailureRoutes(fail);
		
	}

	@Override
	public void launchMinigame(Activity minigameActivity, ViewGroup view) {
		minigameActivity.getActionBar().hide();
		minigameActivity.setContentView(R.layout.activity_evader);
		minigameActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		EvaderSurfaceView mGameView = (EvaderSurfaceView) minigameActivity.findViewById(R.id.gameview);		
		
        EvaderThread mGameThread = mGameView.getThread();
        mGameThread.setCustomEventListener(new OnCustomEventListener(){
            public void onEvent(EvaderThread mGameThread){
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
}