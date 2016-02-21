package activity_classes;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import com.danceme.R;
import data_classes.Dance;
import database.DanceDB;

public class TapScreen extends Activity {
	private static String TAG = "TapScreen";
	
	private Button createButton;
	private Button loadButton;
	
	public void onCreate(Bundle savedInstanceState){
		Log.d(TAG, "Beginning OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tap_main_screen);
		
		createButton = (Button) findViewById(R.id.createButton);
		loadButton = (Button) findViewById(R.id.loadButton);
		
		createButton.getBackground().setAlpha(225);   
		loadButton.getBackground().setAlpha(225);

		createButton.setOnClickListener(new CreateButtonListener());		
		loadButton.setOnClickListener(new LoadButtonListener());
	}
	
	private class CreateButtonListener implements Button.OnClickListener{

		@Override
		public void onClick(View v) {
			showDialog();
		}
		
		/*
		 * Shows a NumberPicker spinner so the user can choose how many moves they want in their dance
		 */
		@SuppressLint("InflateParams")
		public void showDialog(){	
			AlertDialog.Builder dialog = new AlertDialog.Builder(TapScreen.this);
			dialog.setTitle("   Number of Moves");
			dialog.setCancelable(false);
			LayoutInflater li = (LayoutInflater)TapScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = li.inflate(R.layout.sub_layout_1, null, false); 
			final NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker1);

			dialog.setNegativeButton("CREATE",new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	Intent newIntent = new Intent(TapScreen.this, ViewTapDance.class);
	            	newIntent.putExtra("numOfMoves", np.getValue());
	            	startActivity(newIntent);
	            }  
	        });    

		    np.setWrapSelectorWheel(false);
		    np.setMaxValue(40);
			np.setMinValue(5);
			dialog.setView(v);

			AlertDialog alertDialog = dialog.create();
		    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
			alertDialog.show();
			alertDialog.getWindow().setLayout((int)(getWindowManager().getDefaultDisplay().getWidth()/(1.5)),(int)((getWindowManager().getDefaultDisplay().getHeight()/(1.5)))); //Controlling width and height.
		}
	}
	
	/*
	 * Letting the user load which saved tap dance they wish to view
	 */
	private class LoadButtonListener implements Button.OnClickListener{
		
		private AlertDialog.Builder dialog;
		private AlertDialog alertDialog;
		
		@Override
		public void onClick(View v) {
			Handler newThread = new Handler();
			newThread.post(new Runnable(){
				@SuppressLint("InflateParams")
				public void run(){
					dialog = new AlertDialog.Builder(TapScreen.this,R.style.Theme_Trans);
					Log.i(TAG, "Starting dialog to view saved tap dances");
					DanceDB db = DanceDB.getInstance(TapScreen.this);
					List<Dance> danceList = new ArrayList<Dance>();
					try{
						danceList = db.getAllDanceNames();
						Log.i(TAG, "Dance name: " + danceList.get(0).getName());
					} catch (Exception e){
						Log.e(TAG, "Dance moves did not generate successfully." + e.getMessage());
					}
					
					LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View v = li.inflate(R.layout.dialog_list_view, null, false);
					ListView hold = (ListView)v.findViewById(R.id.view_saved_modern_moves);
					ArrayAdapter<Dance> adapter = new ArrayAdapter<Dance>(getApplicationContext(), R.layout.modern_move_listview_textview, danceList);
					hold.setAdapter(adapter);
					dialog.setView(v);
					dialog.setTitle(" Saved Dances ");
					dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					
					alertDialog = dialog.create();
					alertDialog.show();
					Log.d(TAG, "Finishing Loading Dances");
					
					// On ListView Item Click
					hold.setOnItemClickListener(new EditTextListener());
				}
			});
		}

		/*
		 * Getting the specified dance and starting the ViewSavedTapDance activity to view the specified dance
		 */
		private class EditTextListener implements AdapterView.OnItemClickListener{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Dance d = (Dance) parent.getItemAtPosition(position);
				Intent intent = new Intent(TapScreen.this, ViewSavedTapDance.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("danceName", d.getName());
				intent.putExtra("danceID", d.getId());
				startActivity(intent);						
				alertDialog.dismiss();
			}
		}
	}
}
