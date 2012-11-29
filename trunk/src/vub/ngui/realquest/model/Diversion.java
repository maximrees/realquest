package vub.ngui.realquest.model;

import java.io.Serializable;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class Diversion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 408121011241156933L;
	private Location toPoint;
	public Diversion(Location toPoint, long time) {
		super();
		this.toPoint = toPoint;
		this.time = time;
	}
	public Location getToPoint() {
		return toPoint;
	}
	public void setToPoint(Location toPoint) {
		this.toPoint = toPoint;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	private long time;
}
