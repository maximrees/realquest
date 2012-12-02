package vub.ngui.realquest.proximitygauge;

import vub.ngui.realquest.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ProximityGauge extends SurfaceView implements SurfaceHolder.Callback {
	
	final float scale = getResources().getDisplayMetrics().density;
	private ProximityGaugeThread _pgthread;
	private Paint paint = new Paint();
	private Bitmap[] gauge;
	private int[] gaugeImages_enabled;
	private int[] gaugeImages_disabled;
	private int[] gaugeSounds;
	private SoundPool _soundPool;
	private int _preferredHeight = 484;
	private int _preferredWidth = 347;

	public ProximityGauge(Context context) {
		super(context);
		init();
	}

	public ProximityGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ProximityGauge(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

	public void surfaceCreated(SurfaceHolder holder) {
		_pgthread.setRunning(true);
	    _pgthread.start();		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	    // we have to tell thread to shut down & wait for it to finish, or else
	    // it might touch the Surface after we return and explode
	    boolean retry = true;
	    _pgthread.setRunning(false);
	    while (retry) {
	        try {
	            _pgthread.join();
	            retry = false;
	        } catch (InterruptedException e) {
	            // we will try it again and again...
	        }
	    }
	}
    
	@Override
	protected void onDraw(Canvas canvas) {
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();
		
		// find center of the screen
		int px = width / 2;
		int py = height / 2;
		
		canvas.drawColor(Color.BLACK);
		// draw the bars
		for (int i = 0; i < gauge.length; i++) {		
			drawIMG(canvas, gauge[i], px - gauge[i].getWidth() / 2, py - gauge[i].getHeight() / 2);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// retrieve measure-specifications
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		// use height to calculate the width
		int chosenHeight = chooseDimension(heightMode, heightSize, "height");
		int chosenWidth = (int) (chosenHeight * 0.7169421487603306);
		
		// test if area is wide enough
		if (widthSize < chosenWidth) {
			// if not wide enough, use width to calculate the height
			chosenWidth = chooseDimension(widthMode, widthSize, "width");
			chosenHeight = (int) (chosenWidth * 1.394812680115274);
		}		
		
		setMeasuredDimension(chosenWidth, chosenHeight);
	}
	
	private int chooseDimension(int mode, int size, String dimension) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			if (dimension.equals("height")) {
				return getPreferredHeight();
			} else if (dimension.equals("width")) {
				return getPreferredWidth();
			} else { return 100; }
		} 
	}
	
	private int getPreferredHeight() {
		return _preferredHeight;
	}
	
	private int getPreferredWidth() {
		return _preferredWidth;
	}
	
	private void init() {
		_soundPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
		initSounds();
		
		getHolder().addCallback(this);
		_pgthread = new ProximityGaugeThread(getHolder(), this);
		setFocusable(true);
		initDrawingTools();
	}
	
	private void initSounds() {
		gaugeSounds = new int[11];
		gaugeSounds[0] = _soundPool.load(getContext(), R.raw.gauge0, 0); 
		gaugeSounds[1] = _soundPool.load(getContext(), R.raw.gauge1, 0);
		gaugeSounds[2] = _soundPool.load(getContext(), R.raw.gauge2, 0);
		gaugeSounds[3] = _soundPool.load(getContext(), R.raw.gauge3, 0);
		gaugeSounds[4] = _soundPool.load(getContext(), R.raw.gauge4, 0);
		gaugeSounds[5] = _soundPool.load(getContext(), R.raw.gauge5, 0);
		gaugeSounds[6] = _soundPool.load(getContext(), R.raw.gauge6, 0);
		gaugeSounds[7] = _soundPool.load(getContext(), R.raw.gauge7, 0);
		gaugeSounds[8] = _soundPool.load(getContext(), R.raw.gauge8, 0);
		gaugeSounds[9] = _soundPool.load(getContext(), R.raw.gauge9, 0);
		gaugeSounds[10] = _soundPool.load(getContext(), R.raw.gauge10, 0);
	}
	
	private void initDrawingTools() {
		gaugeImages_disabled = new int[10];
		gaugeImages_disabled[0] = R.drawable.bar_01_disabled;
		gaugeImages_disabled[1] = R.drawable.bar_02_disabled;
		gaugeImages_disabled[2] = R.drawable.bar_03_disabled;
		gaugeImages_disabled[3] = R.drawable.bar_04_disabled;
		gaugeImages_disabled[4] = R.drawable.bar_05_disabled;
		gaugeImages_disabled[5] = R.drawable.bar_06_disabled;
		gaugeImages_disabled[6] = R.drawable.bar_07_disabled;
		gaugeImages_disabled[7] = R.drawable.bar_08_disabled;
		gaugeImages_disabled[8] = R.drawable.bar_09_disabled;
		gaugeImages_disabled[9] = R.drawable.bar_10_disabled;
		
		gaugeImages_enabled = new int[10];
		gaugeImages_enabled[0] = R.drawable.bar_01_enabled;
		gaugeImages_enabled[1] = R.drawable.bar_02_enabled;
		gaugeImages_enabled[2] = R.drawable.bar_03_enabled;
		gaugeImages_enabled[3] = R.drawable.bar_04_enabled;
		gaugeImages_enabled[4] = R.drawable.bar_05_enabled;
		gaugeImages_enabled[5] = R.drawable.bar_06_enabled;
		gaugeImages_enabled[6] = R.drawable.bar_07_enabled;
		gaugeImages_enabled[7] = R.drawable.bar_08_enabled;
		gaugeImages_enabled[8] = R.drawable.bar_09_enabled;
		gaugeImages_enabled[9] = R.drawable.bar_10_enabled;
		
		gauge = new Bitmap[10];
		// Initially gauge is set to 3
		updateGauge(3);
	}
	
	private void drawIMG(Canvas canvas, Bitmap img, int x, int y) {
		canvas.drawBitmap(img, x, y, paint);
	}
	
	// setting the correct amount of bars
	public boolean updateGauge(int x) {
		if (x <= gauge.length) {
			iterateParts(x);
			playSound(x);
		}
		if (x == gauge.length) {
			return true;
		} else {
			return false;
		}
	}
	
	private void iterateParts(int x) {
		synchronized (_pgthread.getSurfaceHolder()) {
			for (int i = 0; i < gauge.length; i++) {
				if (i < x) { // set enabled to x, else disabled
					gauge[i] = BitmapFactory.decodeResource(getResources(), gaugeImages_enabled[i]);
				} else {
					gauge[i] = BitmapFactory.decodeResource(getResources(), gaugeImages_disabled[i]);
				}
			}
	    }
	}
	
	private void playSound(int x) {
		_soundPool.play(gaugeSounds[x], 1, 1, 0, 0, 1);
	}
}