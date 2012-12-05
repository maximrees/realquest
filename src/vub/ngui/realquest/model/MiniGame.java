package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;

import vub.ngui.realquest.MainActivity;
import vub.ngui.realquest.MiniGameActivity;

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
	//1
	//i would like to make this method abstract because i want minigames to implement loading their own elements into the minigame layout
	//sadly we have to screw around with gson to get inheritance into play when deserializing it from a minigame and casting it to the proper type
	//2 
	//an int must be returned for the map to see which failroute has been loaded, and the fact that it is returning from a minigame should be indicated
	//(done by setting this int. sadly we cant just have launchminigame return an int, because the int that is returned probably depends on userinteraction
	//with the minigame : ie multiplechoice: onclicklistener for the confirm button therefore TODO: a second obbligation overwrite a setint method see further
	//atm the extending class must somewhere call checkfailroute on the parent with the correct index when it wants to finish
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