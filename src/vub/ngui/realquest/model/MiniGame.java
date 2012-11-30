package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.maps.GeoPoint;

public abstract class MiniGame implements Serializable {
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
	//IMPORTANT NOTE:
	//i would like to make this method abstract because i want minigames to implement loading their own elements into the minigame layout
	//alas making this abstract would mess with gson and id have to make modifications in the saver to account for this abstract class
	//so im justn ot gunna do that and write this here that u need to enforce this overriding method in subclasses	
	abstract public void launchMinigame(Activity minigameActivity, ViewGroup view);
	
	
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