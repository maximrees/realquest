package vub.ngui.realquest.model;

import com.google.android.maps.GeoPoint;

public class Diversion {
	private GeoPoint toPoint;
	public Diversion(GeoPoint toPoint, long time) {
		super();
		this.toPoint = toPoint;
		this.time = time;
	}
	public GeoPoint getToPoint() {
		return toPoint;
	}
	public void setToPoint(GeoPoint toPoint) {
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
