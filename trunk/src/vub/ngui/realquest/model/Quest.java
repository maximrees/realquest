package vub.ngui.realquest.model;

import java.util.ArrayList;

public class Quest {
	
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

}
