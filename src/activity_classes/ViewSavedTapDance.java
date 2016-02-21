package activity_classes;

import java.util.List;
import java.util.Vector;
import com.danceme.R;
import data_classes.Dance;
import data_classes.TapMove;
import database.DanceDB;
import fragment_classes.TapImageFragment;
import fragment_classes.TapPagerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;  
import android.widget.TextView;
import android.widget.Toast;

public class ViewSavedTapDance extends FragmentActivity{
	private static String TAG = "ViewSavedTapDance";
	
	private TapPagerAdapter pagerAdapter;
	
	private DanceDB db;	
	private Dance savedDance;
	
	private TextView fileName;
	private TextView moveNumbers;
	private Button deleteButton;
	private ViewPager imageSwap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_saved_tap_dance);
		
		deleteButton = (Button)findViewById(R.id.deleteButton);
		imageSwap = (ViewPager)findViewById(R.id.tap_image_view);
		fileName = (TextView) findViewById(R.id.danceName);
		moveNumbers = (TextView) findViewById(R.id.moveNumbers);
		
		db = DanceDB.getInstance(this);
		
		new ViewSavedTapDanceTask().execute();
		
		deleteButton.setOnClickListener(new DeleteButtonListener());
	}
	
	/*
	 * Listener for the delete button to maintain readability
	 */
	private class DeleteButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			Handler newThread = new Handler();
			newThread.post(new Runnable() {				
				@SuppressLint("InflateParams")
				@Override
				public void run() {
					Log.i(TAG, "Starting delete dialog");
					LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View v = li.inflate(R.layout.delete_dialog_layout, null, false);
					AlertDialog.Builder dialog = new AlertDialog.Builder(ViewSavedTapDance.this, R.style.Theme_Trans);
					dialog.setView(v);
					dialog.setTitle("Delete Saved Dance: " + savedDance.getName());
					dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).setNegativeButton("Yes", new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							db.deleteSavedDance(savedDance);
							Log.i(TAG, savedDance.getName() + " dance was deleted.");
							finish();
							Toast.makeText(ViewSavedTapDance.this, savedDance.getName() + " has been deleted.", Toast.LENGTH_SHORT).show();
						}
					});
					AlertDialog alertDialog = dialog.create();
					alertDialog.show();
				}
			});
		}		
	}

	/*
	 * Put into a task so as to free up the main thread
	 */
	private class ViewSavedTapDanceTask extends AsyncTask<Void, Void, Void>{

		/*
		 * getting the dance and starting the fragments for viewing
		 */
		@Override
		protected Void doInBackground(Void... params) {
			Bundle savedDanceIntent = getIntent().getExtras();
			try{
				String danceName = savedDanceIntent.getString("danceName");
				String danceID = savedDanceIntent.getString("danceID");
				savedDance = db.getDance(danceName, danceID);
				fileName.setText(savedDance.toString());
				
				List<TapMove> moves = savedDance.getMoves();
				moveNumbers.setText("Number of Moves: " + moves.size());
				Log.i(TAG, "Got saved dance: " + savedDance.getName());
				startFragments();
			} catch (Exception e){
				Log.e(TAG, "Intent error");
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
		
		/*
		 * method to start the fragments and put them into the appropriate adapter
		 */
		private void startFragments(){
			List<Fragment> fm = new Vector<Fragment>();
			List<TapMove> moves = savedDance.getMoves();
			for (int i = 0; i < moves.size(); ++i){
				Bundle args = new Bundle();
				args.putInt("Image", moves.get(i).getFilePath());
				args.putString("Name", moves.get(i).getName());  
				Fragment holdFragment = Fragment.instantiate(ViewSavedTapDance.this, TapImageFragment.class.getName());
				holdFragment.setArguments(args);
				fm.add(holdFragment);
			}
			pagerAdapter = new TapPagerAdapter(ViewSavedTapDance.this.getSupportFragmentManager(), fm);
			imageSwap.setAdapter(pagerAdapter);
		}
	}
}
