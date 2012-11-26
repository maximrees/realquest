package vub.ngui.realquest.model;

import java.io.Serializable;

public class Location implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8167229900596203142L;
	public int longitude;
	public int latitude;
	public int elevation;
	
	public Location(int longitude, int latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = 0;
	}
	public Location(int longitude, int latitude, int elevation) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getElevation() {
		return elevation;
	}
	public void setElevation(int elevation) {
		this.elevation = elevation;
	}
	
	
}
