package vub.ngui.realquest.proximitygauge;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

class ProximityGaugeThread extends Thread {
    private SurfaceHolder _surfaceHolder;
    private ProximityGauge _pg;
    private boolean _run = false;
 
    public ProximityGaugeThread(SurfaceHolder surfaceHolder, ProximityGauge pg) {
        _surfaceHolder = surfaceHolder;
        _pg = pg;
    }
 
    public void setRunning(boolean run) {
        _run = run;
    }
 
    @Override
	public void run() {
	    Canvas c;
	    while (_run) {
	        c = null;
	        try {
	            c = _surfaceHolder.lockCanvas(null);
	            synchronized (_surfaceHolder) {
	                _pg.onDraw(c);
	            }
	        } finally {
	            // do this in a finally so that if an exception is thrown
	            // during the above, we don't leave the Surface in an
	            // inconsistent state
	            if (c != null) {
	                _surfaceHolder.unlockCanvasAndPost(c);
	            }
	        }
	    }
	}
    
    public SurfaceHolder getSurfaceHolder() {
        return _surfaceHolder;
    }
}