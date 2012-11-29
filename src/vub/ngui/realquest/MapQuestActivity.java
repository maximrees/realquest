package vub.ngui.realquest;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vub.ngui.realquest.R;
import vub.ngui.realquest.R.id;
import vub.ngui.realquest.R.layout;
import vub.ngui.realquest.R.menu;
import vub.ngui.realquest.R.string;
import vub.ngui.realquest.model.MiniGame;
import vub.ngui.realquest.model.Quest;
import vub.ngui.realquest.ui.custom.DirectionPathOverlay;
import vub.ngui.realquest.ui.custom.MiniGamesItemizedOverlay;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.ReticleDrawMode;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ZoomControls;



public class MapQuestActivity extends MapActivity {

    private MapView mapView;
	private MapController mapController;
	private Quest quest;
	private LocationManager locman = null;
	private LocationListener loclis = null;
	private MiniGamesItemizedOverlay itemizedoverlay;
	private MyLocationOverlay myLocationOverlay;
	private boolean flag = false;

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
		
		this.quest = MainActivity.getInstance().quest;
		
		setupLocationManaging();
		
		//following things need to happen:
		//generate the user' location (this needs to happen by registering to the incoming location update events)
		//and an arrow or a route needs to be generated to the next minigame location.
		//incoming location events check if close to current minigame
		
		//load yves proximity activity (with minigame data, ...) launch the minigame from there
		//upon succes load the actual minigameactivity
		
		//the minigame activity writes a new current minigame, which incase of failure to answer the question correctly 
		//is a new failminigame (remember the old minigame) to which the arrow or route will be generatedfor a specific number of time, after which
		//the old minigame will be reloaded
		
		//if it is answered correctly then we will remove the minigame from teh quest and load the next one (however the hasmap implementation allows us)
		//fi there are no more minigames then weve played out the game and need to load scores
		flag = false;
		
		drawQuestToMap();// !!!
		drawPathToMap(10.504956, 76.390316, 10.154929, 76.390376);

		
}

    private void setupLocationManaging() {
		if(locman == null){
			// Acquire a reference to the system Location Manager
			locman = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

			// Define a listener that responds to location updates
			loclis = new LocationListener() {

			    public void onStatusChanged(String provider, int status, Bundle extras) {}

			    public void onProviderEnabled(String provider) {}

			    public void onProviderDisabled(String provider) {}

				public void onLocationChanged(android.location.Location location) {
					if(!flag){
						MiniGame game = quest.getMiniGameInfo().get(0);
						Location dest = game.getLocation();
						Location source = locman.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						drawPathToMap(source.getLatitude(), source.getLongitude(), dest.getLatitude(), dest.getLongitude());
					}
					
					
				}
			  };

			// Register the listener with the Location Manager to receive location updates
			locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclis);
		}
		
	}
    
    private void drawPathToMap(double lat, double lon, double lat2, double lon2){
    	 mapController.setZoom(10);
    	 MapController mc = mapView.getController();
    	 class DirectionDownloader extends AsyncTask<Double, Void, ArrayList<GeoPoint>> {
    		    // Do the long-running work in here
    		    protected ArrayList<GeoPoint> doInBackground(Double... coordinates) {
    		        try { 
    		        	
    		        	String url = "http://maps.googleapis.com/maps/api/directions/xml?origin=" +((Double) coordinates[0]).toString() + "," + ((Double) coordinates[1]).toString()  + "&destination=" + ((Double) coordinates[2]).toString() + "," +((Double) coordinates[3]).toString() + "&sensor=false&units=metric";
    		            String tag[] = { "lat", "lng" };
    		            ArrayList<GeoPoint> list_of_geopoints = new ArrayList<GeoPoint>();
    		            HttpResponse response = null;
    		           
    		                HttpClient httpClient = new DefaultHttpClient();
    		                HttpContext localContext = new BasicHttpContext();
    		                HttpPost httpPost = new HttpPost(url);
    		                response = httpClient.execute(httpPost, localContext);
    		                InputStream in = response.getEntity().getContent();
    		                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    		                Document doc = builder.parse(in);
    		                if (doc != null) {
    		                    NodeList nl1, nl2;
    		                    nl1 = doc.getElementsByTagName(tag[0]);
    		                    nl2 = doc.getElementsByTagName(tag[1]);
    		                    if (nl1.getLength() > 0) {
    		                        list_of_geopoints = new ArrayList<GeoPoint>();
    		                        for (int i = 0; i < nl1.getLength(); i++) {
    		                            Node node1 = nl1.item(i);
    		                            Node node2 = nl2.item(i);
    		                            double lat = Double.parseDouble(node1.getTextContent());
    		                            double lng = Double.parseDouble(node2.getTextContent());
    		                            list_of_geopoints.add(new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6)));
    		                        }
    		                    } else {
    		                        // No points found
    		                    }
    		                }
    		            
    		            return list_of_geopoints;
    		        } catch (Exception e) {
    		        	return null;
    		        }
					
    		    }
    		    @Override
    		    protected void onPostExecute(ArrayList<GeoPoint> result) {
    		    	super.onPostExecute(result);
    		    	mapView.getOverlays().add(new DirectionPathOverlay(result));
    		    	mapView.invalidate();
    		    }
    	 }
    	 new DirectionDownloader().execute(Double.valueOf(lat), Double.valueOf(lon),Double.valueOf(lat2),Double.valueOf(lon2));


    }

	private void drawQuestToMap() {
    	mapView.setClickable(true);
		mapView.setReticleDrawMode(ReticleDrawMode.DRAW_RETICLE_NEVER);
		

//		ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
//
//		Geocoder geo = new Geocoder( this.getApplicationContext() , Locale.getDefault());
		
		
		//overlay to display player and animate to himself on first fix
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
		    mapView.getController().animateTo(myLocationOverlay.getMyLocation());
		}});
		//overlay to display minigames
		Drawable minigame = this.getResources().getDrawable(android.R.drawable.star_on);
		minigame.setBounds(0, 0, minigame.getIntrinsicWidth(), minigame.getIntrinsicHeight());
		itemizedoverlay = new MiniGamesItemizedOverlay(minigame, this);
		//init with invisible item
		OverlayItem overlayitem = new OverlayItem(new GeoPoint(-80074222,13359375), "QUIT TOUCHING ME", "sob ^^");
		itemizedoverlay.addOverlay(overlayitem);
		mapView.getOverlays().add(itemizedoverlay);
	
		
		
		
		//int i = 0;
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
		//overlay to display a done minigame
				
		
		

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
	
	private void drawMinigameToMap(){		
		Location loc = quest.getMiniGameInfo().get(0).getLocation();
		int latitude = (int) (loc.getLatitude() * 1E6);
		int longitude =  (int) (loc.getLongitude() * 1E6);
		GeoPoint point = new GeoPoint(latitude, longitude);
		OverlayItem overlayitem = new OverlayItem(point, "completed minigame", "");
		itemizedoverlay.addOverlay(overlayitem);
		mapView.invalidate();

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
