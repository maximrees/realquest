package vub.ngui.realquest.minigames;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
//import android.os.PowerManager.WakeLock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

public class Evader extends SurfaceView implements SurfaceHolder.Callback,SensorEventListener  {

	private EvaderThread mThread;
	private Context mContext;
	private Activity mActivity;
	private boolean mThreadIsRunning = false;
	
	private Paint mPaintWhite;
	private Paint mPaintRed;
	private Paint mPaintYellow;
	
	private Sensor mAccelerometer;
	private SensorManager mSensorManager;
	private PowerManager mPowerManager;
	private WindowManager mWindowManager;
	private Display mDisplay;
//	private WakeLock mWakeLock;
	private float[] mSensorX;
	private float[] mSensorY;
	private long mSensorTimeStamp;
	private long mCpuTimeStamp;
	
	/**Screen metrics**/
	private float mScreenCentreX;
	private float mScreenCentreY;
	private float mScreenRatio;
	private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	
	public Evader(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mContext = context;
		mActivity = (Activity) context;
		
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
		mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
//        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());
        
        initialiseVariables();
        
        /**We create the thread.
         * The thread will be started in surfaceCreated()
         */
        mThread = new EvaderThread(holder, mContext, new Handler() {
        	@Override
            public void handleMessage(Message m) {
            }
        });

        setFocusable(true); 
    }
	
	private void initialiseVariables(){
		mPaintWhite = new Paint();
		mPaintWhite.setColor(0xFFFFFFFF);
		mPaintWhite.setStrokeWidth(1);
		mPaintRed = new Paint();
		mPaintRed.setColor(0xFFCC0000);
		mPaintRed.setStrokeWidth(1);
		mPaintYellow = new Paint();
		mPaintYellow.setColor(0xFFFFFF00);
		mPaintYellow.setStrokeWidth(1);
		
		
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        mScreenRatio = metrics.densityDpi / 160f;
        
        mScreenCentreX = metrics.widthPixels / 2f;
        
        switch (metrics.densityDpi) {
        	case DisplayMetrics.DENSITY_HIGH:
        		mScreenCentreY = (metrics.heightPixels - HIGH_DPI_STATUS_BAR_HEIGHT) / 2f;
        		break;
        	case DisplayMetrics.DENSITY_MEDIUM:
        		mScreenCentreY = (metrics.heightPixels - MEDIUM_DPI_STATUS_BAR_HEIGHT) / 2f;
        		break;
        	case DisplayMetrics.DENSITY_LOW:
        		mScreenCentreY = (metrics.heightPixels - LOW_DPI_STATUS_BAR_HEIGHT) / 2f;
        		break;
        	default:
        		mScreenCentreY = (metrics.heightPixels - MEDIUM_DPI_STATUS_BAR_HEIGHT) / 2f;
        		break;
        }
        
        mSensorX = new float[10];
        mSensorX[0] = 0f;
        mSensorX[1] = 0f;
        mSensorX[2] = 0f;
        mSensorX[3] = 0f;
        mSensorX[4] = 0f;
        mSensorX[5] = 0f;
        mSensorX[6] = 0f;
        mSensorX[7] = 0f;
        mSensorX[8] = 0f;
        mSensorX[9] = 0f;
        mSensorY = new float[10];
        mSensorY[0] = 0f;
        mSensorY[1] = 0f;
        mSensorY[2] = 0f;
        mSensorY[3] = 0f;
        mSensorY[4] = 0f;
        mSensorY[5] = 0f;
        mSensorY[6] = 0f;
        mSensorY[7] = 0f;
        mSensorY[8] = 0f;
        mSensorY[9] = 0f;
    }
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	public void surfaceCreated(SurfaceHolder holder) {
//		mWakeLock.acquire();
        mThread.setRunning(true);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        if (!mThreadIsRunning){
        	mThread.start();
        } else {
        }
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
        mThread.setRunning(false);
        mSensorManager.unregisterListener(this);
//        mWakeLock.release();
        while (retry) {
            try {
                mThread.join();
                retry = false;
                setThreadIsRunning(false);
            } catch (InterruptedException e) {
            }
        }	
	}
	
	private boolean getThreadIsRunning(){
		return mThreadIsRunning;
	}
	
	
	private void setThreadIsRunning(boolean is){
		mThreadIsRunning = is;
	}
	
	public EvaderThread getThread() {
        return mThread;
    }
	
	public Activity getPlayActivity(){
		return mActivity;
	}
	
	public void setPlayActivity(Activity act){
		mActivity = act;
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER){
		    return;
	    }
		/*
         * record the accelerometer data, the event's timestamp as well as
         * the current time. The latter is needed so we can calculate the
         * "present" time during rendering. In this application, we need to
         * take into account how the screen is rotated with respect to the
         * sensors (which always return data in a coordinate space aligned
         * to with the screen in its native orientation).
         */

        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                addSensorValue(event.values[0],-event.values[1]);
            	break;
            case Surface.ROTATION_90:
            	addSensorValue(-event.values[1],event.values[0]);
            	break;
            case Surface.ROTATION_180:
            	addSensorValue(-event.values[0],-event.values[1]);
            	break;
            case Surface.ROTATION_270:
            	addSensorValue(event.values[1],-event.values[0]);
            	break;
        }
        mSensorTimeStamp = event.timestamp;
        mCpuTimeStamp = System.nanoTime();
	}
	
	private void addSensorValue(float x, float y){
		int length = mSensorX.length;
		int i = 0;
		while (i < length - 1){
			mSensorX[i] = mSensorX[i+1];
			mSensorY[i] = mSensorY[i+1];
			i += 1;
		}
		mSensorX[9] = x;
		mSensorY[9] = y;
	}

	public class EvaderThread extends Thread {
		
		private SurfaceHolder mSurfaceHolder;
		private Handler mHandler;
        private Canvas mCanvas;
        private boolean mRun = false;
         
        /**Variable and constants related to the state of the current game**/
        private int mRunningMode;
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_READY = 3;
        public static final int STATE_RUNNING = 4;
        public static final int STATE_WIN = 5;
        public static final int STATE_INSTRUCTIONS = 6;
        public static final int STATE_PAUSEINSTRUCTIONS = 7;
        
        /**Variables for the user circle**/
        private float mCircleCentreX = 0;
        private float mCircleCentreY = 0;
        private int mCircleRadius = 20;
        
        /**Variables for the red circles**/
        private float[] mRedCirclesCentreX;
        private float[] mRedCirclesCentreY;
        private float[] mRedCirclesRadii;
        private float[] mRedCirclesXVariation;
        
        /**Variables for collision**/
        private boolean[] mCollidedCircles;
        private int maxCollisions = 2;
        private int collisions;
        
        /**Variables for winning**/
        private int attempts = 1;
//        private boolean mTarget = false;
        
		public EvaderThread(SurfaceHolder surfaceHolder, Context context,
                Handler handler) {
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mRunningMode = STATE_RUNNING;
            
            initialiseVariables();
        }
		
		private void initialiseVariables(){
			
			mRedCirclesCentreX = new float[10];
	        mRedCirclesCentreX[0] = mScreenCentreX * 2f / 2.5f;
	        mRedCirclesCentreX[1] = mScreenCentreX * 2f / 1.1f;
	        mRedCirclesCentreX[2] = mScreenCentreX * 2f / 1.08f;
	        mRedCirclesCentreX[3] = mScreenCentreX * 2f / 1.6f;
	        mRedCirclesCentreX[4] = mScreenCentreX * 2f / 3.5f;
	        mRedCirclesCentreX[5] = mScreenCentreX * 2f / 4.8f;
	        mRedCirclesCentreX[6] = mScreenCentreX * 2f / 2.2f;
	        mRedCirclesCentreX[7] = mScreenCentreX * 2f / 2.1f;
	        mRedCirclesCentreX[8] = mScreenCentreX * 2f / 1.8f;
	        mRedCirclesCentreX[9] = mScreenCentreX * 2f / 5.1f;
	        
	        mRedCirclesCentreY = new float[10];
	        mRedCirclesCentreY[0] = mScreenCentreY * 2f / 2.4f;
	        mRedCirclesCentreY[1] = mScreenCentreY * 2f / 1.9f;
	        mRedCirclesCentreY[2] = mScreenCentreY * 2f / 3.7f;
	        mRedCirclesCentreY[3] = mScreenCentreY * 2f / 1.5f;
	        mRedCirclesCentreY[4] = mScreenCentreY * 2f / 2.9f;
	        mRedCirclesCentreY[5] = mScreenCentreY * 2f / 1.04f;
	        mRedCirclesCentreY[6] = mScreenCentreY * 2f / 1.11f;
	        mRedCirclesCentreY[7] = mScreenCentreY * 2f / 10.5f;
	        mRedCirclesCentreY[8] = mScreenCentreY * 2f / 1.26f;
	        mRedCirclesCentreY[9] = mScreenCentreY * 2f / 5.1f;
	        
	        Random random = new Random();
	        mRedCirclesRadii = new float[10];
	        for (int i = 0; i < mRedCirclesRadii.length; i++) {
				mRedCirclesRadii[i] = getRandomFloat(5.5f, 25f, random) * mScreenRatio;
			}
			
//	        mRedCirclesRadii[0] = 15f * mScreenRatio;
//	        mRedCirclesRadii[1] = 16.5f * mScreenRatio;
//	        mRedCirclesRadii[2] = 16f * mScreenRatio;
//	        mRedCirclesRadii[3] = 26f * mScreenRatio;
//	        mRedCirclesRadii[4] = 12.1f * mScreenRatio;
//	        mRedCirclesRadii[5] = 5.5f * mScreenRatio;
//	        mRedCirclesRadii[6] = 12f * mScreenRatio;
//	        mRedCirclesRadii[7] = 25f * mScreenRatio;
//	        mRedCirclesRadii[8] = 10f * mScreenRatio;
//	        mRedCirclesRadii[9] = 8f * mScreenRatio;
	        
	        mRedCirclesXVariation = new float[10];
	        for (int i = 0; i < mRedCirclesXVariation.length; i++) {
	        	mRedCirclesXVariation[i] = getRandomFloat(-10f, 10f, random);
			}
	        
//	        mRedCirclesXVariation[0] = -1f;
//	        mRedCirclesXVariation[1] = -1.5f;
//	        mRedCirclesXVariation[2] = -2f;
//	        mRedCirclesXVariation[3] = -3f;
//	        mRedCirclesXVariation[4] = -1f;
//	        mRedCirclesXVariation[5] = -2f;
//	        mRedCirclesXVariation[6] = -1.5f;
//	        mRedCirclesXVariation[7] = 2.5f;
//	        mRedCirclesXVariation[8] = -4f;
//	        mRedCirclesXVariation[9] = -1.1f;
	        
	        mCircleCentreX = mCircleRadius;
	        mCircleCentreY = mCircleRadius;
	        
	        collisions = 0;
	        
	        mCollidedCircles = new boolean[10];
	        mCollidedCircles[0] = false;
	        mCollidedCircles[1] = false;
	        mCollidedCircles[2] = false;
	        mCollidedCircles[3] = false;
	        mCollidedCircles[4] = false;
	        mCollidedCircles[5] = false;
	        mCollidedCircles[6] = false;
	        mCollidedCircles[7] = false;
	        mCollidedCircles[8] = false;
	        mCollidedCircles[9] = false;
	     }
			
		private float getRandomFloat(float aStart, float aEnd, Random aRandom){
		    if ( aStart > aEnd ) {
		      throw new IllegalArgumentException("Start cannot exceed End.");
		    }
		    //get the range, casting to long to avoid overflow problems
		    long range = (long)aEnd - (long)aStart + 1;
		    // compute a fraction of the range, 0 <= frac < range
		    long fraction = (long)(range * aRandom.nextFloat());
		    float randomNumber =  (float)(fraction + aStart);    
		    return randomNumber;
		  }
		
		@Override
        public void run() {
			while (mRun) {
            	try {
                    mCanvas = mSurfaceHolder.lockCanvas(null);
                    if (mCanvas != null) {
	                    synchronized (mSurfaceHolder) {
	                        if (mRunningMode == STATE_RUNNING){
	                        	updatePhysics();
	                        }
	                        doDraw(mCanvas);
	                    }
                    }
                } finally {
                    if (mCanvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                    }
                }
            }
        }
		
		public void setRunning(boolean b) {
			/**This method sets the mRun variable
			 * which is used to make sure the thread is only active between the
			 * phases surfaceCreated() and surfaceDestroyed() of our 
			 * GameView SurfaceHolder cycle
			 */
            mRun = b;
        }
		
		/**the methods below take care of drawing on the screen**/
		private void doDraw(Canvas canvas) {
			/**This method draws the grid, numbers, score, next number
			 * and arrows on the canvas
			 */
			mCanvas.restore();
			mCanvas.drawColor(0,PorterDuff.Mode.CLEAR);
			drawTarget();
			drawCollisions();
			drawOtherCircles();
			drawUserCircle();
			canvas.save();
		}
		
		private void drawUserCircle(){
			mCanvas.drawCircle(mCircleCentreX, mCircleCentreY, mCircleRadius, mPaintWhite);
		}
		
		private void drawOtherCircles(){
			int length = mRedCirclesCentreX.length;
			int i = 0;
			while (i < length){
				mCanvas.drawCircle(mRedCirclesCentreX[i], mRedCirclesCentreY[i], mRedCirclesRadii[i], mPaintRed);
				i +=1 ;
			}
		}
		
		private void drawCollisions(){
			int length = mCollidedCircles.length;
			int i = 0;
			while (i < length){
				if (mCollidedCircles[i]){
					mCanvas.drawCircle(mRedCirclesCentreX[i], mRedCirclesCentreY[i], mRedCirclesRadii[i] + 2f, mPaintYellow);
				}
				i += 1;
			}
		}
		
		private void drawTarget(){
			mCanvas.drawRect(mScreenCentreX * 2 - mCircleRadius * 2, mScreenCentreY * 2 - mCircleRadius * 2, mScreenCentreX * 2, mScreenCentreY * 2, mPaintWhite);
		}
		
		/**The methods below are helper methods for drawing on the screen**/
		private void updatePhysics() {
			
			int length = mSensorX.length;
			int i = 0;
			float totalSensorX = 0f;
			float totalSensorY = 0f;
			while (i < length){
				totalSensorX += mSensorX[i] * 1f;
				totalSensorY += mSensorY[i] * 1f;
				i += 1;
			}
			float averageSensorX = totalSensorX / length;
			float averageSensorY = totalSensorY / length;
			mCircleCentreX = mCircleCentreX - averageSensorX;
			if (mCircleCentreX < mCircleRadius){
				mCircleCentreX = mCircleRadius;
			} else if (mCircleCentreX > mScreenCentreX * 2 - mCircleRadius){
				mCircleCentreX = mScreenCentreX * 2 - mCircleRadius;
			}
			mCircleCentreY = mCircleCentreY - averageSensorY;
			if (mCircleCentreY < mCircleRadius){
				mCircleCentreY = mCircleRadius;
			} else if (mCircleCentreY > mScreenCentreY * 2 - mCircleRadius){
				mCircleCentreY = mScreenCentreY * 2 - mCircleRadius;
			}
			
			length = mRedCirclesCentreX.length;
			i = 0;
			while (i < length){
				mRedCirclesCentreX[i] += mRedCirclesXVariation[i];
				if (mRedCirclesCentreX[i] - mRedCirclesRadii[i] < 1f || mRedCirclesCentreX[i] + mRedCirclesRadii[i] > mScreenCentreX * 2){
					mRedCirclesXVariation[i] = mRedCirclesXVariation[i] * (-1f);
				}
				i += 1;
			}
			
			checkCollision();
			checkTarget();
			
		}
		
		private void checkCollision(){
			int length = mRedCirclesCentreX.length;
			int i = 0;
			while (i < length){
				float centresDistanceSquared = (mCircleCentreX - mRedCirclesCentreX[i]) * (mCircleCentreX - mRedCirclesCentreX[i]) + (mCircleCentreY - mRedCirclesCentreY[i]) * (mCircleCentreY - mRedCirclesCentreY[i]);  
				float radiiDistanceSquared = (mCircleRadius + mRedCirclesRadii[i]) * (mCircleRadius + mRedCirclesRadii[i]);
				if ((centresDistanceSquared <= radiiDistanceSquared) && !mCollidedCircles[i]){
					mCollidedCircles[i] = true;
					collisions++;
					
					// TODO: maybe adding sound
				}
				i += 1;
			}
			if (collisions >= maxCollisions) {
				attempts++;
				initialiseVariables();
				mActivity.runOnUiThread(new Runnable() {
				    public void run() {
				        Toast.makeText(mActivity, "Attempt " + attempts, Toast.LENGTH_SHORT).show();
				    }
				});
			}
		}
		
		private void checkTarget(){
			if (mCircleCentreX > mScreenCentreX * 2 - mCircleRadius * 2 && mCircleCentreY > mScreenCentreY * 2 - mCircleRadius * 2){
				// TODO: enter result
				mActivity.finish();
			}
		}
		
	}
	
}