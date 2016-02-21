package activity_classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.danceme.R;

import data_classes.ModernMove;
import database.DanceDB;

public class ViewSavedModernMove extends Activity {
	private static String TAG = "ViewSavedModernMove";
	
	private ArrayAdapter<String> adapter;
	
	private String move[] = new String[6];
	
	private TextView moveName;
	private Button deleteButton;
	private ListView moveListView;
	
	private DanceDB db = new DanceDB(this);
	
	private Bundle intentBundle;
	private String modernMoveName;
	private long modernMoveID;
	private ModernMove m;

	public void onCreate(Bundle savedInstanceState){
		Log.i(TAG, "Beginning OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_saved_modern_move);
	
		deleteButton = (Button) findViewById(R.id.deleteButton);
		moveListView = (ListView) findViewById(R.id.modernMoveListView);
	    moveName = (TextView) findViewById(R.id.title);
				
		intentBundle = getIntent().getExtras();
		try{
			modernMoveName = intentBundle.getString("moveName");
			modernMoveID = intentBundle.getLong("moveID");
			m = db.getModernMove(modernMoveName, modernMoveID);
			moveName.setText(m.getName().toString());
			move[0] = "Body Part: " + m.getBodypart();
			move[1] = "Direction: " + m.getDirection();
			move[2] = "Plane: " + m.getPlane();
			move[3] = "Movement Quality: " + m.getMovement_quality();
			move[4] = "Movement Pathway: " + m.getMovement_pathway();
			move[5] = "Augmentation: " + m.getAugmentation();
			
			adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.modern_move_listview_textview, move);
			moveListView.setAdapter(adapter);
		}
		catch (Exception e){	
			Log.e(TAG, "Thrown Exception");
		}
	
		deleteButton.setOnClickListener(new DeleteButtonListener());	  
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:{
				NavUtils.navigateUpFromSameTask(ViewSavedModernMove.this);
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Dialog for whether or not to delete a saved modern move
	 */
	private class DeleteButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			showDialog();
		}
		
		@SuppressLint("InflateParams")
		private void showDialog(){
			LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View v = li.inflate(R.layout.delete_dialog_layout, null, false);
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(ViewSavedModernMove.this,R.style.Theme_Trans);
			dialog.setView(v);
			dialog.setTitle("Deleting " + modernMoveName);
			dialog.setMessage("Are you sure you want to delete?");
			dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					db.deleteModernMove(m);	
					Toast.makeText(ViewSavedModernMove.this, modernMoveName + " has been deleted.", Toast.LENGTH_SHORT).show();
					dialog.cancel();
					finish();   
				}
			});
			dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			final AlertDialog alertDialog = dialog.create();
			alertDialog.show();
		}
	}
	   
	     
}
