package activity_classes;

import java.util.ArrayList;

import preferences.ModernPreferences;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.danceme.R;

import data_classes.ModernMove;
import database.DanceDB;

public class ModernScreen extends Activity{
	private static String TAG = "ModernScreen";
	
	private Button createButton;
	private Button loadButton;
	private Button preferencesButton;

	public void onCreate(Bundle savedInstanceState){
		Log.d(TAG, "Beginning OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modern_main_screen);

		createButton = (Button) findViewById(R.id.create_button);
		loadButton = (Button) findViewById(R.id.load_button);
		preferencesButton = (Button) findViewById(R.id.preferences_button);
		
		createButton.getBackground().setAlpha(200);
		loadButton.getBackground().setAlpha(200);
		preferencesButton.getBackground().setAlpha(200);

		
		createButton.setOnClickListener(new CreateButtonListener());
		loadButton.setOnClickListener(new LoadButtonListener());
		preferencesButton.setOnClickListener(new PreferencesButtonListener()); 
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:{
				NavUtils.navigateUpFromSameTask(ModernScreen.this);
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * When the user chooses to load a saved modern move it will display a dialog to the user and let them choose which move to view
	 */
	private class LoadButtonListener implements Button.OnClickListener{

		private AlertDialog.Builder dialog;
		private AlertDialog alertDialog;
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
		
		public void showDialog(){
			Handler newThread = new Handler();
			newThread.post(new Runnable(){
				@SuppressLint("InflateParams")
				public void run(){
					dialog = new AlertDialog.Builder(ModernScreen.this,R.style.Theme_Trans);
					Log.i(TAG, "Starting dialog to view moves");
					DanceDB db = DanceDB.getInstance(ModernScreen.this);
					ArrayList<ModernMove> moveList;
					try{
						moveList = new ArrayList<ModernMove>(db.getAllModernMoves());
						Log.i(TAG, "Modern Database gotten successfully");
					} catch(Exception e){
						moveList = new ArrayList<ModernMove>();
						Log.e(TAG, "Database not gotten successfully");
					}
					
					LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v = li.inflate(R.layout.dialog_list_view, null, false);
					ListView hold = (ListView)v.findViewById(R.id.view_saved_modern_moves);
					ArrayAdapter<ModernMove> adapter = new ArrayAdapter<ModernMove>(getApplicationContext(), R.layout.modern_move_listview_textview, moveList);
					hold.setAdapter(adapter);
					dialog.setView(v);
					dialog.setTitle(" Saved Moves ");
					dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					
					alertDialog = dialog.create();
					alertDialog.show();
					Log.d(TAG, "Finishing Load Modern Moves");
					
					// On ListView Item Click
					hold.setOnItemClickListener(new EditTextListener());
				}
			});
		}
		
		private class EditTextListener implements AdapterView.OnItemClickListener{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final ModernMove move = (ModernMove) parent.getItemAtPosition(position);
				Intent intent = new Intent(ModernScreen.this, ViewSavedModernMove.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("moveName", move.getName());
				intent.putExtra("moveID", move.getId());
				startActivity(intent);
				
				alertDialog.dismiss();
			}			
		}	
	}
	
	/*
	 * If the user chooses to create a new modern move it will start the ViewModernMoves class
	 */
	private class CreateButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {			
			Intent newIntent = new Intent(ModernScreen.this, ViewModernMoves.class);
			startActivity(newIntent);
		}
	}
	
	/*
	 * So the user can choose their preferences for the modern move
	 */
	private class PreferencesButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			Intent modernPreferencesIntent = new Intent(ModernScreen.this, ModernPreferences.class);
			startActivity(modernPreferencesIntent);	
		}		
	}
}
