package vub.ngui.realquest;

import vub.ngui.realquest.proximitygauge.ProximityGauge;
import android.os.Bundle;
import android.app.Activity;
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
import android.support.v4.app.NavUtils;

public class ProximityGaugeActivity extends Activity {
	ProximityGauge pg;
	FrameLayout mainHolder;
	RelativeLayout buttonHolder;
	Button pgButton;
	
	// variables for testing
	private int current_proximity = 3;
	private boolean test = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        float scale = getResources().getDisplayMetrics().density;
        
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        
        pg = new ProximityGauge(this);
        mainHolder = new FrameLayout(this);
        buttonHolder = new RelativeLayout(this);       
        pgButton = new Button(this);
        pgButton.setText("MiniGame");
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
        
        // TODO: not relative to screendensity yet...
        int margin = 135;
        b1.setMargins(0, 0, 0, margin);
        pgButton.setLayoutParams(b1);
        
        pgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: Perform action on click
            }
        });
        
        mainHolder.addView(pg);  
        mainHolder.addView(buttonHolder);
        
        setContentView(mainHolder);
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
	
	private void update(int x) {
			pgButton.setEnabled(pg.updateGauge(x));
	}
}