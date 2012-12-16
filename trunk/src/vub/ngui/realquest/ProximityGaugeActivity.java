package vub.ngui.realquest;

import vub.ngui.realquest.model.MiniGame;
import vub.ngui.realquest.model.Quest;
import vub.ngui.realquest.proximitygauge.ProximityGauge;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class ProximityGaugeActivity extends Activity {
	private LocationManager	locman;
	private LocationListener loclis;
	private Quest quest;
	private MiniGame game;
	private ProximityGauge pg;
	private FrameLayout mainHolder;
	private RelativeLayout buttonHolder;
	private Button pgButton;
	private Activity myself;
	
	// variables for testing
	private int current_proximity = 9;
	private boolean test = true;   
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float scale = getResources().getDisplayMetrics().density;
        Intent intent = getIntent();
        myself = this;
        
        myself.setTitle(intent.getStringExtra("title"));
        
        pg = new ProximityGauge(this);
        mainHolder = new FrameLayout(this);
        buttonHolder = new RelativeLayout(this);       
        pgButton = new Button(this);
        pgButton.setText(R.string.button_proximity_gauge);
        pgButton.setId(123456);
        pgButton.setBackgroundResource(R.drawable.btn_proximity_gauge);
        pgButton.setEnabled(false);
        
        RelativeLayout.LayoutParams b1 = new LayoutParams(  
        		RelativeLayout.LayoutParams.WRAP_CONTENT,  
        		RelativeLayout.LayoutParams.WRAP_CONTENT);  
        
        RelativeLayout.LayoutParams params = new LayoutParams(
        		RelativeLayout.LayoutParams.FILL_PARENT,  
        		RelativeLayout.LayoutParams.FILL_PARENT);
        buttonHolder.setLayoutParams(params); 
        
        buttonHolder.addView(pgButton);
        b1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        b1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        int margin = (int) (45 * scale);
        b1.setMargins(0, 0, 0, margin);
        pgButton.setLayoutParams(b1);
        
        pgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {            	
            	Intent intent = new Intent(ProximityGaugeActivity.this, MiniGameActivity.class);
                startActivity(intent);
                myself.finish();
            }
        });
        
        mainHolder.addView(pg);  
        mainHolder.addView(buttonHolder);
        
        setContentView(mainHolder);
        locman = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        quest =  MainActivity.getInstance().quest;
        game = (MiniGame) quest.getMiniGameInfo().get(0);        
        
        loclis = new LocationListener() {
		    public void onStatusChanged(String provider, int status, Bundle extras) {}
		    public void onProviderEnabled(String provider) {}
		    
		    public void onProviderDisabled(String provider) {
		    	Toast.makeText(
		    			getApplicationContext(),
		    			getResources().getString(R.string.provider_disabled),
		    			Toast.LENGTH_LONG).show();			    	
		    }

			public void onLocationChanged(android.location.Location location) {
					Location dest = game.getLocation();
					Location source = locman.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					float distance = source.distanceTo(dest);
					
					switch (Math.round(distance/11)) {
					case 0: 
						update(0);
						locman.removeUpdates(loclis);
						break;
					case 1: 
						update(0);		
						locman.removeUpdates(loclis);
						break;
					case 2: 
						update(1);						
						break;
					case 3: 
						update(2);						
						break;
					case 4: 
						update(3);						
						break;
					case 5: 
						update(4);						
						break;
					case 6: 
						update(5);						
						break;
					case 7: 
						update(6);					
						break;
					case 8: 
						update(7);						
						break;
					case 9: 
						update(8);					
						break;
					case 10: 
						update(9);					
						break;
					default: 
						update(10);	
						break;
					}								
			}
		  };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_proximity_gauge, menu);
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
    
	private void update(int x) {
		pgButton.setEnabled(pg.updateGauge(x));
	}
    
	// testing the workings of the bars and button
	@Override
	public boolean onTouchEvent(MotionEvent event) {	    
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	if (test) {
	        	if (current_proximity == 10) {
	        		current_proximity--;
	        		test = false;
	        	} else {
	        		current_proximity++;
	        	}
        	} else {
        		if (current_proximity == 0) {
        			current_proximity++;
        			test = true;
        		} else {
        			current_proximity--;
        		}
        	}
        	update(current_proximity);
        }
        return true;
	}
}