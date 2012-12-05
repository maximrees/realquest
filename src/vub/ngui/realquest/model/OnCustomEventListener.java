package vub.ngui.realquest.model;

import vub.ngui.realquest.MiniGameActivity;
import vub.ngui.realquest.model.EvaderSurfaceView.EvaderThread;

public interface OnCustomEventListener {
	public void onEvent(EvaderThread thread);
}