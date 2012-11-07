package vub.ngui.realquest.saving;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;

import vub.ngui.realquest.model.Quest;

import com.google.gson.Gson;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;;

public class FileSaver {
	
	final static String path = "data/vub.ngui.realquest/questfiles";
	
	private File directory;

	private boolean mExternalStorageAvailable;

	private boolean mExternalStorageWriteable;
	
	private Gson gson;
	
	
	public FileSaver(Context context){
		directory = context.getExternalFilesDir(path);
		gson = new Gson();
		
	}
	
	public void Save(Object data){
		checkState();
		if( mExternalStorageWriteable ){
			try {
				//write converted json data to a file named "file.json"
				String datastring = gson.toJson(data);
				FileWriter writer = new FileWriter(directory+data.toString());
				writer.write(datastring);
				writer.close();
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else{
			
		}
	}
	
	public File[] loadFileNames(){
		checkState();
		if(mExternalStorageAvailable){
			return directory.listFiles();
		} else{
			return null;
			
		}
	}
	
	public Object load(File file){
		checkState();
		if(mExternalStorageAvailable){
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));		
				Quest quest = gson.fromJson(reader, Quest.class);
				return quest;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else{
			return null;
		}

	}
	
	
	
	public void checkState(){
		mExternalStorageAvailable = false;
		mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
	}

}
