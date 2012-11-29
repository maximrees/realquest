package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.maps.GeoPoint;

public class MiniGame implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6922685170538864737L;
	private Location point;
	private ArrayList<Diversion> failureRoutes = new ArrayList<Diversion>();
	public Location getLocation() {
		return point;
	}
	public void setLocation(Location point) {
		this.point = point;
	}
	public ArrayList<Diversion> getFailureRoutes() {
		return failureRoutes;
	}
	public void setFailureRoutes(ArrayList<Diversion> failureRoutes) {
		this.failureRoutes = failureRoutes;
	}
	public MiniGame(Location point, ArrayList<Diversion> fail) {
		super();
		this.point = point;
		this.failureRoutes = fail;
	}
	
	
//	public MiniGame(Parcel in) {
//		this.location = (Location) in.readValue(Location.class.getClassLoader());
//		this.failureRoutes = in.readArrayList(Location.class.getClassLoader());
//	}	
//    public static final Parcelable.Creator<MiniGame> CREATOR = new Parcelable.Creator<MiniGame>() {
//        public MiniGame createFromParcel(Parcel in) {
//            return new MiniGame(in);
//        }
//
//        public MiniGame[] newArray(int size) {
//            return new MiniGame[size];
//        }
//    };
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeValue(location);
//		dest.writeList(failureRoutes);
//		
//	}
	
}