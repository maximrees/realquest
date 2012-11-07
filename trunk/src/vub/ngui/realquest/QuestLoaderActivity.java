package vub.ngui.realquest;

import java.io.File;

import vub.ngui.realquest.saving.FileSaver;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;


public class QuestLoaderActivity extends Activity {
	
	private ListView questListView;
	private FileSaver saver;

    @SuppressLint("ParserError")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest_loader);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        questListView = (ListView) findViewById(R.id.questListView);
        saver = new FileSaver(getApplicationContext());
        
		ArrayAdapter<File> aa = new ArrayAdapter<File>(this, android.R.layout.simple_list_item_multiple_choice , saver.loadFileNames());
		questListView.setAdapter(aa);

		
		questListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

}
