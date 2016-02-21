package activity_classes;
   
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import com.danceme.R;

import data_classes.ModernMove;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import database.DanceDB;

public class ViewModernMoves extends Activity {
	private static String TAG = "ViewModernMoves";
	
	private Button saveButton;
	private Button shuffleButton;
	private ListView listView;
	
	private ArrayAdapter<String> adapter;   
	
	private String move[] = new String[6];
	
	private SharedPreferences myPreferences;
	
	private HashMap<String, String> directions;
	private HashMap<String, String> planes;
	private HashMap<String, String> movement_qualities;
	private HashMap<String, String> movement_pathways;
	private HashMap<String, String> augmentation;
	private HashMap<String, String> bodyparts;  
	
	private ModernMove m;
	
	public void onCreate(Bundle savedInstanceState){
		Log.i(TAG, "OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modern_move_view_screen);
		
		saveButton = (Button)findViewById(R.id.saveModernMove);
		shuffleButton= (Button)findViewById(R.id.shuffleModernMove);
		listView = (ListView)findViewById(R.id.modernmovelistview);
		
		
		m = new ModernMove();
		move[0] = m.getBodypart();
		move[1] = m.getDirection();
		move[2] = m.getPlane();
		move[3] = m.getMovement_quality();
		move[4] = m.getMovement_pathway();
		move[5] = m.getAugmentation();

		myPreferences = PreferenceManager.getDefaultSharedPreferences(ViewModernMoves.this);
		
		new CreateMove().execute();
		
		saveButton.setOnClickListener(new SaveButtonListener());
		shuffleButton.setOnClickListener(new ShuffleButtonListener());
		
		adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.modern_move_listview_textview, move);
		listView.setAdapter(adapter);
	}
	
	public void onDestroy(){
		Log.i(TAG, "OnDestroy");
		super.onDestroy();
	}
	
	public void onBackPressed(){
		super.onBackPressed();
		finish();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:{
				NavUtils.navigateUpFromSameTask(ViewModernMoves.this);
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class SaveButtonListener implements Button.OnClickListener{
		
		@Override
		public void onClick(View v) {
			showDialog();
		}
		
		public void showDialog(){
			Handler newThread = new Handler();
			newThread.post(new Runnable(){
				@SuppressLint("InflateParams")
				public void run(){
					Log.i(TAG, "Starting show dialog");
					LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View v = li.inflate(R.layout.save_dialog_layout, null, false);
					AlertDialog.Builder dialog = new AlertDialog.Builder(ViewModernMoves.this,R.style.Theme_Trans);
					dialog.setView(v);
					dialog.setTitle(" Sequence");  
					dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							final EditText hold = ((EditText)v.findViewById(R.id.saveModernEditText));
							m.setName(hold.getText().toString());
							DanceDB db = DanceDB.getInstance(ViewModernMoves.this);
							db.addModernMove(m);		
							Toast.makeText(ViewModernMoves.this, hold.getText().toString() + " has been saved.", Toast.LENGTH_SHORT).show();
							finish();  
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which){
							dialog.cancel();
						}
					});
					AlertDialog alertDialog = dialog.create();
					alertDialog.show();
				}
			});
		}
	}
	
	private class ShuffleButtonListener implements Button.OnClickListener{

		@Override
		public void onClick(View v) {
			new CreateMove().execute();
		}
		
	}
	
	private class CreateMove extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {			
			Random r = new Random();
			List<String> keys;
			String randomKey;
			String value;			
			initHashMaps();
			if(!bodyparts.isEmpty()){
				keys = new ArrayList<String>(bodyparts.keySet());
				randomKey = keys.get(r.nextInt(keys.size()));
				value = bodyparts.get(randomKey);
				m.setBodypart(value);
				move[0] = "Body Part: " + value;
			}
			else{
				move[0] = "Body Part: " + "None";
			}
			if (!directions.isEmpty()){
				keys = new ArrayList<String>(directions.keySet());   
				randomKey = keys.get(r.nextInt(keys.size()));
				value = directions.get(randomKey);
				m.setDirection(value);
				move[1] = "Direction: " + value;
			}
			else{
				move[1] = "Direction: " + "None";
			}
			if (!planes.isEmpty()){
				keys = new ArrayList<String>(planes.keySet());
				randomKey = keys.get(r.nextInt(keys.size()));
				value = planes.get(randomKey);
				m.setPlane(value);
				move[2] = "Plane: " + value;
			}
			else{
				move[2] = "Plane: " + "None";
			}
			if (!movement_qualities.isEmpty()){
				keys = new ArrayList<String>(movement_qualities.keySet());
				randomKey = keys.get(r.nextInt(keys.size()));
				value = movement_qualities.get(randomKey);
				m.setMovement_quality(value);
				move[3] = "Movement Quality: " + value;
			}
			else{
				move[3] = "Movement Quality: " + "None";
			}
			if (!movement_pathways.isEmpty()){
				keys = new ArrayList<String>(movement_pathways.keySet());
				randomKey = keys.get(r.nextInt(keys.size()));
				value = movement_pathways.get(randomKey);
				m.setMovement_pathway(value);   
				move[4] = "Movement Pathway: " + value;
			}
			else{
				move[4] = "Movement Pathway: " + "None";
			}
			if (!augmentation.isEmpty()){
				keys = new ArrayList<String>(augmentation.keySet());
				randomKey = keys.get(r.nextInt(keys.size()));
				value = augmentation.get(randomKey);
				
				m.setAugmentation(value);
				move[5] = "Augmentation: " + value;
			}
			else{
				move[5] = "Augmentation: " + "None";
			}
			return "worked";
		}
		protected void onPostExecute(String id){
			Log.i(TAG, "Updating adapter");
			Log.i(TAG, id);
			adapter.notifyDataSetChanged();
		}
	}
	
	/*
	 * initializing all the hashmaps for the preferences
	 */
	private void initHashMaps(){
		directions = new HashMap<String, String>();
		planes = new HashMap<String, String>();
		movement_qualities = new HashMap<String, String>();
		movement_pathways = new HashMap<String, String>();
		augmentation = new HashMap<String, String>();
		bodyparts = new HashMap<String, String>();		
		
		//for body parts
		if (myPreferences.getBoolean("body_head", true)){
			bodyparts.put("body_head", "Head");
		}
		if(myPreferences.getBoolean("body_shoulder_right", true)){
			bodyparts.put("body_shoulder_right", "Right Shoulder");
		}
		if(myPreferences.getBoolean("body_shoulder_left", true)){
			bodyparts.put("body_shoulder_left", "Left Shoulder");
		}
		if(myPreferences.getBoolean("body_elbow_right", true)){
			bodyparts.put("body_elbow_right", "Right Elbow");
		}
		if(myPreferences.getBoolean("body_elbow_left",true)){
			bodyparts.put("body_elbow_left", "Left Elbow");
		}
		if(myPreferences.getBoolean("body_wrist_right", true)){
			bodyparts.put("body_wrist_right", "Right Wrist");
		}
		if(myPreferences.getBoolean("body_wrist_left", true)){
			bodyparts.put("body_wrist_left", "Left Wrist");
		}
		if(myPreferences.getBoolean("body_hand_right", true)){
			bodyparts.put("body_hand_right", "Right Hand");
		}
		if(myPreferences.getBoolean("body_hand_left", true)){ 
			bodyparts.put("body_hand_left", "Left Hand");
		}
		if(myPreferences.getBoolean("body_chest", true)){
			bodyparts.put("body_chest", "Chest");
		}
		if(myPreferences.getBoolean("body_ribs", true)){
			bodyparts.put("body_ribs", "Ribs");
		}
		if(myPreferences.getBoolean("body_pelvis", true)){
			bodyparts.put("body_pelvis", "Pelvis");
		}
		if(myPreferences.getBoolean("body_knee_right", true)){
			bodyparts.put("body_knee_right", "Right Knee");
		}
		if(myPreferences.getBoolean("body_knee_left", true)){
			bodyparts.put("body_knee_left", "Left Knee");
		}
		if(myPreferences.getBoolean("body_ankle_right", true)){
			bodyparts.put("body_ankle_right", "Right Ankle");
		}
		if(myPreferences.getBoolean("body_ankle_left", true)){
			bodyparts.put("body_ankle_left", "Left Ankle");
		}
		if(myPreferences.getBoolean("body_foot_right", true)){
			bodyparts.put("body_foot_right", "Right Foot");
		}
		if(myPreferences.getBoolean("body_foot_left", true)){
			bodyparts.put("body_foot_left", "Left Foot");
		}
		//for directions
		if (myPreferences.getBoolean("direction_none", false)){
			directions.put("direction_none", "None");
		}
		if (myPreferences.getBoolean("direction_up", true)){
			directions.put("direction_up", "Up");
		}
		if (myPreferences.getBoolean("direction_down", true)){
			directions.put("direction_down", "Down");
		}
		if (myPreferences.getBoolean("direction_forward", true)){
			directions.put("direction_forward", "Forward");
		}
		if (myPreferences.getBoolean("direction_back", true)){
			directions.put("direction_back", "Back");
		}
		if (myPreferences.getBoolean("direction_left", true)){
			directions.put("direction_left", "Left");
		}
		if (myPreferences.getBoolean("direction_right", true)){
			directions.put("direction_right", "Right");
		}
		// Get plane preferences
		if (myPreferences.getBoolean("plane_none", false)){
			planes.put("plane_none", "None");
		}
		if (myPreferences.getBoolean("plane_horizontal", true)){
			planes.put("plane_horizontal", "Horizontal");
		}
		if (myPreferences.getBoolean("plane_vertical", true)){
			planes.put("plane_vertical", "Vertical");
		}
		if (myPreferences.getBoolean("plane_transverse", true)){
			planes.put("plane_transverse", "Transverse");
		}
		
		// Get movement quality
		if (myPreferences.getBoolean("movement_quality_none", false)){
			movement_qualities.put("movement_quality_none", "None");
		}
		if (myPreferences.getBoolean("movement_quality_direct", true)){
			movement_qualities.put("movement_quality_direct", "Direct");
		}
		if (myPreferences.getBoolean("movement_quality_indirect", true)){
			movement_qualities.put("movement_quality_indirect", "Indirect");
		}
		if (myPreferences.getBoolean("movement_quality_sustained", true)){
			movement_qualities.put("movement_quality_sustained", "Sustained");
		}
		if (myPreferences.getBoolean("movement_quality_sudden", true)){
			movement_qualities.put("movement_quality_sudden", "Sudden");
		}
		if (myPreferences.getBoolean("movement_quality_strong", true)){
			movement_qualities.put("movement_quality_strong", "Strong");
		}
		if (myPreferences.getBoolean("movement_quality_light", true)){
			movement_qualities.put("movement_quality_light", "Light");
		}
		if (myPreferences.getBoolean("movement_quality_bound", true)){
			movement_qualities.put("movement_quality_bound", "Bound");
		}
		if (myPreferences.getBoolean("movement_quality_free_flow", true)){
			movement_qualities.put("movement_quality_free_flow", "Free Flow");
		}
		if (myPreferences.getBoolean("movement_quality_vibratory", true)){
			movement_qualities.put("movement_quality_vibratory", "Vibratory");
		}
		
		//Get movement pathways
		if (myPreferences.getBoolean("movement_pathway_none", false)){
			movement_pathways.put("movement_pathway_none", "None");
		}
		if (myPreferences.getBoolean("movement_pathway_spoke", true)){
			movement_pathways.put("movement_pathway_spoke", "Spoke");
		}
		if (myPreferences.getBoolean("movement_pathway_arc", true)){
			movement_pathways.put("movement_pathway_arc", "Arc");
		}
		if (myPreferences.getBoolean("movement_pathway_carve", true)){
			movement_pathways.put("movement_pathway_carve", "Carve");
		}
		if (myPreferences.getBoolean("movement_pathway_spiral", true)){
			movement_pathways.put("movement_pathway_spiral", "Spiral");
		}
		if (myPreferences.getBoolean("movement_pathway_pendular", true)){
			movement_pathways.put("movement_pathway_pendular", "Pendular");
		}
		
		//Get augmentation
		
		if (myPreferences.getBoolean("augmentation_none", false)){
			augmentation.put("augmentation_none", "None");
		}   
		if (myPreferences.getBoolean("augmentation_fragmentation", true)){
			augmentation.put("augmentation_fragmentation", "Fragmentation");
		}
		if (myPreferences.getBoolean("augmentation_repetition", true)){
			augmentation.put("augmentation_repetition", "Repetition");
		}
		if (myPreferences.getBoolean("augmentation_canon", true)){
			augmentation.put("augmentation_canon", "Canon");
		}
		if (myPreferences.getBoolean("augmentation_accumulation", true)){
			augmentation.put("augmentation_accumulation", "Accumulation");
		}
		if (myPreferences.getBoolean("augmentation_level_change", true)){
			augmentation.put("augmentation_level_change", "Level Change");
		}
		if (myPreferences.getBoolean("augmentation_plane_change", true)){
			augmentation.put("augmentation_plane_change", "Plane Change");
		}
		if (myPreferences.getBoolean("augmentation_background_change", true)){
			augmentation.put("augmentation_background_change", "Background Change");
		}
		if (myPreferences.getBoolean("augmentation_embellishment", true)){
			augmentation.put("augmentation_embellishment", "Embellishment");
		}		
	}
}
