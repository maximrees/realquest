package vub.ngui.realquest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.maps.GeoPoint;


import vub.ngui.realquest.model.Diversion;

import vub.ngui.realquest.model.Evader;
import vub.ngui.realquest.model.MiniGame;
import vub.ngui.realquest.model.MultipleChoice;

import vub.ngui.realquest.model.Quest;
import vub.ngui.realquest.saving.FileSaver;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.support.v4.app.NavUtils;


public class QuestLoaderActivity extends ExpandableListActivity {
	
	private ListView questListView;
	private FileSaver saver;
	private Button startQuest;
	private ExpandableListAdapter questAdapter;
	//not sure if hell remember this might be a problem
	private int state = 0;
	private Map<String, Quest> questContainer = new HashMap<String,Quest>();
		
	private static final String TITLE = "TITLE";
	private static final String DESCRIPTION = "DESCRIPTION";
	 

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_loader);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //android.r.id.list ??? > checkout this > http://developer.android.com/reference/android/app/ListActivity.html, this also contains a cursor for binding data to this listview instead of the simpleexplistadapter below
        questListView = (ListView) findViewById(android.R.id.list);
        startQuest = (Button) findViewById(R.id.startQuest);
        startQuest.setOnClickListener(startQuestListener);
        saver = new FileSaver(getApplicationContext());        
       
//        makeMockFile();
        
        
        LoadQuestFiles();
        
		
    }

    private void LoadQuestFiles() {
        File[] files = saver.loadFileNames();
        if( files.length == 0 ){
        	//some action to obtain files online
        	//a warning to the user that he has no current quest file
        } else{
        	
        	List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
 	        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
        	for( File file : files){        		
        		Quest quest = (Quest) saver.load(file);  
        		questContainer.put(quest.getTitle(), quest);
	            Map<String, String> curGroupMap = new HashMap<String, String>();
	            groupData.add(curGroupMap);
	            curGroupMap.put(TITLE, quest.getTitle());
	            curGroupMap.put(DESCRIPTION, "i dont know is this the description of the hover over ?");
	            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
	           	//if you want more then a description in this part you can just add it to the child map
	            //additionally you could make a for loop that scans the number of variables in a class somehow and is able to loop
	            //over the number of variables so everything concerning a (in this case quest) object is put into the child data
	                Map<String, String> curChildMap = new HashMap<String, String>();
	                children.add(curChildMap);
	               // curChildMap.put(NAME, "Child " + j);
	                curChildMap.put(DESCRIPTION, quest.getDescription());	                 
                childData.add(children); 
	         }     		                                                                                                                  //groupfrom                     //groupto      		                                                                                                          
        	                                                                                                              // list of keys well be desplaying  //resource id's of the textviews (or other elements) well use to display our data in
        	questAdapter = new SimpleExpandableListAdapter(this, groupData, android.R.layout.simple_expandable_list_item_1, new String[] {TITLE, DESCRIPTION} , new int[] {android.R.id.text1, android.R.id.text2}, childData, android.R.layout.simple_expandable_list_item_2, new String[] {TITLE, DESCRIPTION}, new int[] {android.R.id.text1, android.R.id.text2} );
        	setListAdapter(questAdapter);	

        }
		
	}

	private void makeMockFile() {
    //how to make mock files:
    		//minigame multiple choice:
    			//questions and their diversion:
		
		Map<String, Diversion> map1 = new HashMap<String, Diversion>();
		ArrayList<MiniGame> listofgames = new ArrayList<MiniGame>();
		
		// 1ste minigame: multiple choice
		// answer 1: incorrect - richting zwembad
		Location loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.824115);
		loc.setLongitude(4.394939);
		map1.put("1834", new Diversion(loc, 60000));
		
		// answer 2: correct - info punt
		//the correct answer contains the next minigame location as location and zero as timeout;
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.821747);
		loc.setLongitude(4.394877);
		map1.put("1969", new Diversion(loc, 0));
		
		// answer 3: incorrect - etterbeek station
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822353);
		loc.setLongitude(4.389725);
		map1.put("1973:", new Diversion(loc, 20000));
		
		// question + gamelocation: sigaar vub	
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822882);
		loc.setLongitude(4.392375);
		MultipleChoice mini1 = new MultipleChoice(loc, "In what year was de VUB founded?", map1);
		// put game in arraylist
		listofgames.add(mini1);
		
		// 2nd minigame: evader
		ArrayList<Diversion> map2 = new ArrayList<Diversion>();
		
		// evader succeed - resto
		// first location for succeed
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.821579);
		loc.setLongitude(4.395921);
		map2.add(new Diversion(loc, 0));
		
		// second for fail - campuskoten
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822448);
		loc.setLongitude(4.395947);
		map2.add(new Diversion(loc, 10000));
		
		// game + gamelocation: info punt
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.821747);
		loc.setLongitude(4.394877);
		Evader mini2 = new Evader(loc, map2);
    	//put game in arraylist
    	listofgames.add(mini2);
    	
    	// 3th minigame: multiple choice
		// answer 1: correct - science fac
    	map1 = new HashMap<String, Diversion>();
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822126);
		loc.setLongitude(4.396929);
		map1.put("€ 4,80", new Diversion(loc, 0));
		
		// answer 2: incorrect - stoa
		//the correct answer contains the next minigame location as location and zero as timeout;
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.821991);
		loc.setLongitude(4.396414);
		map1.put("€ 4,65", new Diversion(loc, 20000));
		
		// answer 3: incorrect - sigaar
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822882);
		loc.setLongitude(4.392375);
		map1.put("€ 6,25", new Diversion(loc, 100000));
		
		// question + gamelocation: resto	
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.821579);
		loc.setLongitude(4.395921);
		mini1 = new MultipleChoice(loc, "How much will a daymenu cost for a student?", map1);
		// put game in arraylist
		listofgames.add(mini1);
		
		// 4th minigame: multiple choice
		// answer 1: incorrect - den G
    	map1 = new HashMap<String, Diversion>();
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822448);
		loc.setLongitude(4.397165);
		map1.put("13", new Diversion(loc, 40000));
		
		// answer 2: incorrect - sportveld
		//the correct answer contains the next minigame location as location and zero as timeout;
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.823926);
		loc.setLongitude(4.394907);
		map1.put("-1", new Diversion(loc, 60000));
		
		// answer 3: correct - tent
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.820367);
		loc.setLongitude(4.394478);
		map1.put("10", new Diversion(loc, 0));
		
		// question + gamelocation: science fac	
		loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setLatitude(50.822126);
		loc.setLongitude(4.396929);
		mini1 = new MultipleChoice(loc, "On what flour resides the Computer Science Department?", map1);
		// put game in arraylist
		listofgames.add(mini1);    	
    	
    	//put arraylist in quest        
    	Quest quest = new Quest("VUB Quest", "A quest guiding you arround the VUB campus.", listofgames, 3*1000*60*10);
    	//save it
    	saver.Save(quest);		
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quest_loader, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onGroupCollapse(int groupPosition) {
		super.onGroupCollapse(groupPosition);
		startQuest.setEnabled(false);			
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		super.onGroupExpand(groupPosition);
		startQuest.setEnabled(true);
		state = groupPosition;
	}
	
	private OnClickListener startQuestListener = new OnClickListener() {
		
		public void onClick(View v) {			
			//food for thought: if quests get unusually big, we might not wanna load save all of them, might wanna save title and description and only load a full quest when we need it
			String string = (String) ((HashMap) questAdapter.getGroup(state)).get(TITLE);
			MainActivity.getInstance().quest = questContainer.get(string);
			//TODO:close this activity, remember teh quest and load the quest activity from the main menu, not from here (we want backpress from map to go to main menu, not this activity and we want to relaunch this activity then from mian menu if necessary/desireable)
			Intent intent = new Intent(QuestLoaderActivity.this, MapQuestActivity.class);
			
		    startActivity(intent);
		}
	};
	
	

    
}
