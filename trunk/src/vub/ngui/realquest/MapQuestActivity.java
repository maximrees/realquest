package vub.ngui.realquest;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.test.mock.MockContentProvider;
import android.view.Menu;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ZoomControls;



public class MapQuestActivity extends MapActivity {

    private MapView mapView;
	private MapController mapController;
	private Quest quest;
	private LocationManager locman = null;
	private LocationListener loclis = null;
	private MiniGamesItemizedOverlay itemizedoverlay;
	private MyLocationOverlay myLocationOverlay;
	private float miniGameproximityRadius = 110f;
	private long lastUpdateTime = 0;
	private long locationTimedOut = 2000;
	
	private long myTime = 0;
	private boolean flagz = true;
	private boolean inMiniGame = false;
	
	private Location lastUpdateLocation = null;;
	
	private OnTouchListener loctesting = new OnTouchListener() {
		GeoPoint p;
		public boolean onTouch(View v, MotionEvent event) {
			p = mapView.getProjection().fromPixels((int) event.getX(),
                    (int) event.getY());			
			Location mockLocation = new Location(LocationManager.GPS_PROVIDER); // a string
			double latitude = p.getLatitudeE6() / 1E6;
			double longitude = p.getLongitudeE6() / 1E6;
			mockLocation.setLatitude(latitude);  // double 
			mockLocation.setLongitude(longitude); 
			mockLocation.setTime(System.currentTimeMillis()); 
			locman.setTestProviderLocation( LocationManager.GPS_PROVIDER, mockLocation); 
			return false;
		}
	};
	private int selection;

	
	public static int PROXIMITY_REQ_CODE = 551;
	
	@Override
	protected void onResume() {
		callonResumeAndCreate();
		super.onResume();
	}

	public static String QUEST_STARTING_TIME = "start";

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        this.setTitle(intent.getStringExtra("title"));
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_map_quest);
        if(flagz){
        	myTime = System.currentTimeMillis();
        	flagz = false;
        }
        

		mapView = (MapView) findViewById(R.id.questMap);
		//mocklocation testing 
		//mapView.setOnTouchListener(loctesting);

		mapController = mapView.getController();
		
		
		//callonResumeAndCreate();

		
		
		//following things need to happen:
		//generate the user' location (this needs to happen by registering to the incoming location update events) DONE
		//and an arrow or a route needs to be generated to the next minigame location. DONE
		//incoming location events check if close to current minigame NOT LOC EVENT BUT PROX INTENT DONE
		
		//load proximity activity (with minigame data, ...) launch the minigame from there DONE
		//upon succes load the actual minigameactivity DONE
		
		//the minigame activity writes a new current minigame, which incase of failure to answer the question correctly  DONE KINDA
		//is a new failminigame (remember the old minigame) to which the arrow or route will be generatedfor a specific number of time, after which
		//the old minigame will be reloaded DONE KIDNA
		
		//if it is answered correctly then we will remove the minigame from teh quest and load the next one (however the hasmap implementation allows us) DONE
		//fi there are no more minigames then weve played out the game and need to load scores DONE

		//call on resume and create in the onresume method DONE
		
		

		
}

    private void callonResumeAndCreate() {
    	// Acquire a reference to the system Location Manager
    	locman = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	//locman.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
    	//      false, false, true, true, true, 0, 5);
    	//locman.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);    	
		this.quest = MainActivity.getInstance().quest;
		this.selection = MainActivity.getInstance().selection;
		
		if( quest.getMiniGameInfo().isEmpty()){
		    Intent intent = new Intent(MapQuestActivity.this, ScoreActivity.class);
		    intent.putExtra(MapQuestActivity.QUEST_STARTING_TIME , this.myTime);
	        startActivity(intent);
		} else{		
			MiniGame mini = quest.getMiniGameInfo().get(0); 			
			if(selection == -1 ){	
				inMiniGame = false;
				setupLocationManaging();
				drawQuestToMap();
			} else{
				drawMinigameToMap();
				Location source = locman.getLastKnownLocation(LocationManager.NETWORK_PROVIDER );
				Location dest = mini.getFailureRoutes().get(selection).getToPoint();	
				drawPathToMap(source.getLatitude(), source.getLongitude(), dest.getLatitude(), dest.getLongitude());
				if( mini.getFailureRoutes().get(selection).getTime() == 0 ){
					//remove minigame and proceed to next
					quest.getMiniGameInfo().remove(0);
					setupLocationManaging();
				} else{
					//TODO: draw "fake completed minigame", drawpath to next minigame and start timer
					Timer timy = new Timer();				
					timy.schedule(new TimerTask() {					
						@Override
						public void run() {
							Toast.makeText(getApplicationContext(), "You have failed the previous minigame and have been rerouted as punishment", Toast.LENGTH_SHORT).show();
							Toast.makeText(getApplicationContext(), "please return to previous minigame (see route) and complete it", Toast.LENGTH_LONG).show();
							callonResumeAndCreate();						
						}
					}, new Date(System.currentTimeMillis()+mini.getFailureRoutes().get(selection).getTime())  );
				}	
				
			}		
		}
		MainActivity.getInstance().selection = -1;
	}

	private void setupLocationManaging() {
		if(locman != null){
			
			// Define a listener that responds to location updates
			// I'm only using this one so as to wait for a starting point from which to draw a path to the next minigame
			// it would be more logical to do this in the oncreate but you need a starting point so ...
			loclis = new LocationListener() {

			    public void onStatusChanged(String provider, int status, Bundle extras) {}

			    public void onProviderEnabled(String provider) {}

			    public void onProviderDisabled(String provider) {
			    	Toast.makeText(getApplicationContext(), getResources().getString(R.string.provider_disabled), Toast.LENGTH_LONG).show();			    	
			    }

				public void onLocationChanged(android.location.Location source) {	
					long timepassed = System.currentTimeMillis() - lastUpdateTime;
					if( timepassed > locationTimedOut && !inMiniGame ){
						MiniGame game = (MiniGame) quest.getMiniGameInfo().get(0);
						Location dest = game.getLocation();
						if(lastUpdateLocation == null){
							lastUpdateLocation = source;
							drawPathToMap(source.getLatitude(), source.getLongitude(), dest.getLatitude(), dest.getLongitude());	
						} else{
							if(source.getLatitude() != lastUpdateLocation.getLatitude() || source.getLongitude() != lastUpdateLocation.getLongitude() ){
								lastUpdateLocation = source;	
								if(source.distanceTo(dest) <= miniGameproximityRadius ){
									inMiniGame = true;
									Intent intent = new Intent(MapQuestActivity.this, ProximityGaugeActivity.class);
									intent.putExtra("title", quest.getTitle());
									startActivity(intent);
								} else {
									drawPathToMap(source.getLatitude(), source.getLongitude(), dest.getLatitude(), dest.getLongitude());
								}
							}	
						}
					} 
					lastUpdateTime = System.currentTimeMillis();
				}
			  };

			// Register the listener with the Location Manager to receive location updates
			locman.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loclis);
		}
		
	}
    
    private void drawPathToMap(double lat, double lon, double lat2, double lon2){
//    	 mapController.setZoom(10);
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
				try {
					mapView.getController().animateTo(myLocationOverlay.getMyLocation());
				} catch (NullPointerException exc) {
//					Toast.makeText(getApplicationContext(), "Searching current location...", Toast.LENGTH_SHORT).show();
				}
			}});
		//overlay to display minigames
		Drawable minigame = this.getResources().getDrawable(android.R.drawable.star_on);
		minigame.setBounds(0, 0, minigame.getIntrinsicWidth(), minigame.getIntrinsicHeight());
		itemizedoverlay = new MiniGamesItemizedOverlay(minigame, this);
		//init with invisible item
		OverlayItem overlayitem = new OverlayItem(new GeoPoint(-80074222,13359375), "QUIT TOUCHING ME", "sob ^^");
		itemizedoverlay.addOverlay(overlayitem);
		mapView.getOverlays().add(itemizedoverlay);
	}
	
	private void drawMinigameToMap(){		
		Location loc = ((MiniGame) quest.getMiniGameInfo().get(0)).getLocation();
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
		return false;
	}
}
