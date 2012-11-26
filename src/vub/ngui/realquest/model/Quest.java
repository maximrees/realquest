package vub.ngui.realquest.model;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Quest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4237081863152163539L;
	private String title;
	private String description;
	private ArrayList<MiniGame> miniGameInfo = new ArrayList<MiniGame>(); 

	public Quest(String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}
	public Quest(String title, String description,
			ArrayList<MiniGame> arrayList) {
		super();
		this.title = title;
		this.description = description;
		this.miniGameInfo = arrayList;
	}
	public ArrayList<MiniGame> getMiniGameInfo() {
		return miniGameInfo;
	}
	public void setMiniGameInfo(ArrayList<MiniGame> miniGameInfo) {
		this.miniGameInfo = miniGameInfo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(description);
//		dest.writeString(title);
//		dest.writeList(miniGameInfo);		
//	}
//    public static final Parcelable.Creator<Quest> CREATOR = new Parcelable.Creator<Quest>() {
//        public Quest createFromParcel(Parcel in) {
//            return new Quest(in);
//        }
//
//        public Quest[] newArray(int size) {
//            return new Quest[size];
//        }
//    };
//	public Quest(Parcel in) {
//		this.title = in.readString();
//		this.description = in.readString();
//		this.miniGameInfo = in.readArrayList(MiniGame.class.getClassLoader());
//	}


}
