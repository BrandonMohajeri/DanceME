package activity_classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;

import com.danceme.R;

import data_classes.Dance;
import data_classes.TapMove;
import database.DanceDB;
import fragment_classes.TapImageFragment;
import fragment_classes.TapPagerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewTapDance extends FragmentActivity {
	
	private static final String TAG = "ViewTapDance";
	
	private TapPagerAdapter pagerAdapter;
	private DanceDB db;
	
	private List<TapMove> moves = new ArrayList<TapMove>();
	
	private Dance currentDance;
	
	private Button saveButton;
	private Button shuffleButton;	
	private ViewPager imageSwap;
	private int numOfMoves = 0;
	
	private ProgressDialog progress;
	private boolean createdOriginal;
	/*
	 * opening the view and showing the progress bar while the moves are generated
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_tap_dance);
		
		saveButton = (Button)findViewById(R.id.saveTapDance);
		shuffleButton = (Button)findViewById(R.id.shuffleNewDance);
		imageSwap = (ViewPager)findViewById(R.id.tap_image_view);
		
		db = DanceDB.getInstance(this);
		
		numOfMoves = getIntent().getIntExtra("numOfMoves", 5);
		
		progress = ProgressDialog.show(ViewTapDance.this, "Generating Tap Dance", "Please wait...", true); //waiting for moves to generate
		
		new CreateMovesAndFragments().execute(numOfMoves); //generating moves
		
		saveButton.setOnClickListener(new SaveButtonListener());		
		shuffleButton.setOnClickListener(new ShuffleButtonListener());

	}
	
	public void onDestroy(){
		super.onDestroy();
	}

	/*
	 * simple savebutton listener class to maintain readability and host all necessary methods for saving
	 */
	private class SaveButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			showDialog();
		}
		
		/*
		 * to show the dialog and let the user choose whether or not to save their tap dance
		 */
		public void showDialog(){
			Handler newThread = new Handler();
			newThread.post(new Runnable(){
				@SuppressLint("InflateParams")
				public void run(){
					Log.i(TAG, "Starting show dialog");
					LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View v = li.inflate(R.layout.save_dialog_layout, null, false);
					AlertDialog.Builder dialog = new AlertDialog.Builder(ViewTapDance.this,R.style.Theme_Trans);
					dialog.setView(v);
					dialog.setTitle(" Sequence");  
					dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							final EditText hold = ((EditText)v.findViewById(R.id.saveModernEditText));
							currentDance.setId(UUID.randomUUID().toString()); //generating a random unique identifier
							currentDance.setName(hold.getText().toString());
							db.addTapDance(currentDance);
							Log.i(TAG, "Dance: " + currentDance.getName() + " saved.");
							finish(); //ending the activity
							Toast.makeText(ViewTapDance.this, currentDance.getName() + " has been saved.", Toast.LENGTH_SHORT).show();
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
	
	/*
	 * Simple listener for the shuffle button
	 */
	private class ShuffleButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			new CreateMovesAndFragments().execute(numOfMoves);
		}		
	}
	
	/*
	 * Most methods were put in here so as to maintain readability
	 * Put this is a separate thread to take the load off the main thread
	 */
	private class CreateMovesAndFragments extends AsyncTask<Integer, Void, Void>{
		@Override
		protected Void doInBackground(Integer... params) {
			int numOfMoves = params[0];
			
			moves = new ArrayList<TapMove>(db.getAllTapMoves());
			if (moves.isEmpty()){
				addMoves();
				moves = new ArrayList<TapMove>(db.getAllTapMoves());
			}
			
			Log.i(TAG, "# Moves: " + moves.size());
			
			createTapDance(numOfMoves);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			startFragments();
			progress.dismiss();
		}
		
		/*
		 * to add all the moves if they aren't already in the database
		 */
		public void addMoves(){
			Log.i(TAG, "Adding Moves");
			db.addTapMove(new TapMove("Ball", R.drawable.ball));
			db.addTapMove(new TapMove("Left Footed Wing", R.drawable.left_footed_wing));
			db.addTapMove(new TapMove("Brush", R.drawable.brush));
			db.addTapMove(new TapMove("Dig", R.drawable.dig));
			db.addTapMove(new TapMove("Changing Double Shuffle Pickups", R.drawable.changing_double_shuffle_pickups));
			db.addTapMove(new TapMove("Changing One Footed Wing", R.drawable.changing_one_footed_wing));
			db.addTapMove(new TapMove("Changing Shuffle Pickups", R.drawable.changing_shuffle_pickups));
			db.addTapMove(new TapMove("Closed Third", R.drawable.closed_third));
			db.addTapMove(new TapMove("Five Count Riff", R.drawable.five_count_riff));
			db.addTapMove(new TapMove("Flam", R.drawable.flam));
			db.addTapMove(new TapMove("Double Two Footed Pickup", R.drawable.double_two_footed_pickup));
			db.addTapMove(new TapMove("Heel Click", R.drawable.heel_click));
			db.addTapMove(new TapMove("Heel Drop", R.drawable.heel_drop));
			db.addTapMove(new TapMove("Left Shuffle Pickup", R.drawable.left_shuffle_pickup));
			db.addTapMove(new TapMove("Maxiford", R.drawable.maxiford));
			db.addTapMove(new TapMove("Open Third", R.drawable.open_third));
			db.addTapMove(new TapMove("Toe Third", R.drawable.toe_third));
			db.addTapMove(new TapMove("Toe Knock", R.drawable.toe_knock));
			db.addTapMove(new TapMove("Two Footed Wing", R.drawable.two_footed_wing));
			db.addTapMove(new TapMove("Scrape", R.drawable.scrape));
			db.addTapMove(new TapMove("Shuffle", R.drawable.shuffle));
			db.addTapMove(new TapMove("Spank", R.drawable.spank));
			db.addTapMove(new TapMove("To Click", R.drawable.to_click));
			db.addTapMove(new TapMove("Syncopated Pickup", R.drawable.syncopated_pickup));
			db.addTapMove(new TapMove("Two Footed Pickup", R.drawable.two_footed_pickup));
			db.addTapMove(new TapMove("Right Shuffle Pickup", R.drawable.right_shuffle_pickup));
			db.addTapMove(new TapMove("Left Footed Pickup", R.drawable.left_footed_pickup));
			db.addTapMove(new TapMove("Right Footed Pickup", R.drawable.right_footed_pickup));
			db.addTapMove(new TapMove("Right Footed Wing", R.drawable.right_footed_wing));
			Log.i(TAG, "Finishing adding moves to database");
		}
		
		/*
		 * randomizing and creating a new dance with the specified number of moves
		 */
		public void createTapDance(int numberOfMoves){
			if (numberOfMoves == 0){
				numberOfMoves = 5;
			}
			Log.i(TAG, "numberOfMoves: " + numberOfMoves);
			Random random = new Random();
			int previousMove = 0;
			int currentMove = 0;
			currentDance = new Dance();
			for (int i = 0; i < numberOfMoves; ++i){
				while (currentMove == previousMove){
					currentMove = random.nextInt(moves.size());
				}
				currentDance.addMove(moves.get(currentMove));
				previousMove = currentMove;
			}
			Log.i(TAG, "CreatedTapDance");
		}
		
		/* 
		 * to put all the fragments for each move together
		 */
		private void startFragments(){
			List<Fragment> fm = new Vector<Fragment>();
			for (int i = 0; i < currentDance.getMoves().size(); ++i){
				Bundle args = new Bundle();
				args.putInt("Image", currentDance.getMoves().get(i).getFilePath());
				args.putString("Name", currentDance.getMoves().get(i).getName());
				Fragment holdFragment = Fragment.instantiate(ViewTapDance.this, TapImageFragment.class.getName());
				holdFragment.setArguments(args);
				fm.add(holdFragment);	
			}
			if (createdOriginal){
				List<Fragment> holdFragment = ViewTapDance.this.getSupportFragmentManager().getFragments();
				int holdSize = holdFragment.size();
				FragmentTransaction x = ViewTapDance.this.getSupportFragmentManager().beginTransaction();
				for (int i = 0; i < holdSize; ++i){
					x.remove(holdFragment.get(i));
				}
				x.commit();
			}
			imageSwap = (ViewPager)findViewById(R.id.tap_image_view);					
			pagerAdapter = new TapPagerAdapter(ViewTapDance.this.getSupportFragmentManager(), fm);
			imageSwap.setAdapter(pagerAdapter);
			createdOriginal = true;
		}	
	}
}
