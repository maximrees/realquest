package vub.ngui.realquest.model;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

public class MiniGame {
	private GeoPoint location;
	public GeoPoint getLocation() {
		return location;
	}
	public void setLocation(GeoPoint location) {
		this.location = location;
	}
	public ArrayList<GeoPoint> getFailureRoutes() {
		return failureRoutes;
	}
	public void setFailureRoutes(ArrayList<GeoPoint> failureRoutes) {
		this.failureRoutes = failureRoutes;
	}
	public MiniGame(GeoPoint location) {
		super();
		this.location = location;
	}
	private ArrayList<GeoPoint> failureRoutes = new ArrayList<GeoPoint>();
	
}
