package vub.ngui.realquest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vub.ngui.realquest.R;
import vub.ngui.realquest.R.id;
import vub.ngui.realquest.R.layout;
import vub.ngui.realquest.R.menu;
import vub.ngui.realquest.R.string;
import vub.ngui.realquest.model.Location;
import vub.ngui.realquest.model.Quest;
import vub.ngui.realquest.ui.custom.MiniGamesItemizedOverlay;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.ReticleDrawMode;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ZoomControls;



public class MapQuestActivity extends MapActivity {

    private MapView mapView;
	private MapController mapController;
	private Quest quest;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    
        setContentView(R.layout.activity_map_quest);
        
        /* Getting information concerning the child views */
		mapView = (MapView) findViewById(R.id.questMap);
		/* Initiate the map controls and display */
		mapController = mapView.getController();
		
		quest = (Quest) getIntent().getSerializableExtra("Quest");
		
		drawQuestToMap();// !!!
}

    private void drawQuestToMap() {
    	mapView.setClickable(true);
		mapView.setReticleDrawMode(ReticleDrawMode.DRAW_RETICLE_NEVER);
		

		ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		Geocoder geo = new Geocoder( this.getApplicationContext() , Locale.getDefault());
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		//TODO: make a point to display when you've "done" a minigame
		Drawable greenmarker = this.getResources().getDrawable(android.R.drawable.star_on);
		greenmarker.setBounds(0, 0, greenmarker.getIntrinsicWidth(), greenmarker.getIntrinsicHeight());
		
		
		//for each element of the protocol if protocol has been handled :
		
		MiniGamesItemizedOverlay itemizedoverlay = new MiniGamesItemizedOverlay(greenmarker, this);
		int i = 0;
//		while( itr.hasNext()){
//			Protocol proto = itr.next();
//			try {
//				addresses = geo.getFromLocation((double) (proto.getLatitude()/1E6), (double) (proto.getLongitude()/1E6), 1);
//			} catch (IOException e) {
//				Logger.getInstance().error("io exception geo .getfromlocation tabmap activity)");
//			}
//			GeoPoint p = new GeoPoint(
//		            (int) proto.getLatitude(), 
//		            (int) proto.getLongitude());
//			OverlayItem objective;
//			String info = res.getString(R.string.objective) + " " +(proto.getDone()? res.getString(R.string.handled) : res.getString(R.string.unhandled));
//			String constraints = res.getString(R.string.constraints) + "\n\n" + res.getString(R.string.months_info) + " " + proto.getMonths() + "\n" + res.getString(R.string.days_info)+ " " + proto.getDays() + "\n" + res.getString(R.string.location_info) + "\n" + res.getString(R.string.address) +  ": " + addresses.get(0).getAddressLine(0) + "\n" + res.getString(R.string.city) + ": " + addresses.get(0).getAddressLine(1)  ;
//			//i here should be the number of the protocol in the array, so we can retrieve protocoldata in the onclick
//			objective	 = new OverlayItem(p, i + ". " + info,  proto.getDone()? "" : constraints  );
//			objective.setMarker( proto.getDone()? redmarker : greenmarker);
//			itemizedoverlay.addOverlay(objective);
//			i++;			
//		}
		Location location = quest.getMiniGameInfo().get(0).getLocation();
		GeoPoint point = new GeoPoint(location.getLatitude(), location.getLongitude());
		OverlayItem overlayitem = new OverlayItem(point, "something", "something");
		itemizedoverlay.addOverlay(overlayitem);
		
		mapOverlays.add(itemizedoverlay);
		
		

//		int minLon = Integer.MAX_VALUE;	
//		int minLat = Integer.MAX_VALUE;
//		while( itr.hasNext() ){	
//			Protocol temp = (Protocol) itr.next();
//			int lati = (int) temp.getLatitude();
//			int longi = (int) temp.getLongitude();
//			if( lati < minLat ) minLat = lati;
//			if( longi < minLon ) minLon = longi;
//			
//		}
//		itr = protocols.iterator();
//		int maxLon = Integer.MIN_VALUE;	
//		int maxLat = Integer.MIN_VALUE;
//		while( itr.hasNext() ){	
//			Protocol temp = (Protocol) itr.next();
//			int lati = (int) temp.getLatitude();
//			int longi = (int) temp.getLongitude();
//			if( lati > maxLat ) maxLat = lati;
//			if( longi > maxLon ) maxLon = longi;
//		}
//		mapView.getController().zoomToSpan(Math.abs(maxLat - minLat), Math.abs(maxLon - minLon));
//	   mapView.getController().animateTo(new GeoPoint( ((maxLat+minLat)/2) , ((maxLon+minLon)/2) ));
//



//		ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
//		zoomControls.setOnZoomInClickListener(
//				new View.OnClickListener() {
//					public void onClick(View v) {
//						mapController.zoomIn();
//					}
//				});
//		zoomControls.setOnZoomOutClickListener(
//				new View.OnClickListener() {
//					public void onClick(View v) {
//						mapController.zoomOut();
//					}
//				});

		//Zoom controls in right-bottom corner:
		//mapView.setBuiltInZoomControls(true);
		/*
		ZoomButtonsController zbc = mapView.getZoomButtonsController();
		ViewGroup container = zbc.getContainer();
		for(int i = 0; i < container.getChildCount(); i++)
		{
			View child = container.getChildAt(i);
			if (child instanceof ZoomControls)
			{
				FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
				lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
				child.requestLayout();
				break;
			} 
		}
		 */
		//TODO fix bug which makes zoom in/out buttons appear under instead of next to each other

		//Add points for measurements that are already in the track
		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_quest, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
