package activity_classes;

import com.danceme.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent; 
import android.os.Bundle;
import android.util.Log; 
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
  
public class MainScreen extends Activity {
	private static String TAG = "MainScreenClass";
	
	private Button tapButton;  
	private Button modernButton;
	private Button balletButton;     
	private TextView quote;
	
	public void onCreate(Bundle savedInstanceState){
		Log.i(TAG, "Beginning OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen_layout); 
		
		tapButton = (Button)findViewById(R.id.OpenTapScreen);
		modernButton = (Button)findViewById(R.id.OpenModernScreen);
		balletButton = (Button)findViewById(R.id.OpenBalletScreen);
		quote = (TextView)findViewById(R.id.Quote);
		
		quote.setText(getIntent().getStringExtra("Quote"));
		
		tapButton.getBackground().setAlpha(200);
		modernButton.getBackground().setAlpha(200);
		balletButton.getBackground().setAlpha(200);

		
		balletButton.setOnClickListener(new BalletButtonListener());
		modernButton.setOnClickListener(new ModernButtonListener());
		tapButton.setOnClickListener(new TapButtonListener());	 
	}
	
	/*
	 * When the user hits the back button it will go home instead of back to the splash screen
	 */
	@Override
	public void onBackPressed(){
		Log.d(TAG, "Back Button Pushed");
		Intent goHomeIntent = new Intent(Intent.ACTION_MAIN);
		goHomeIntent.addCategory(Intent.CATEGORY_HOME);
		goHomeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(goHomeIntent);
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.universal_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * To show the user the about page in a simple dialog
	 */
	@SuppressLint("InflateParams")
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.about_button:{  
				LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View v = li.inflate(R.layout.delete_dialog_layout, null, false);
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainScreen.this, R.style.Theme_Trans);
				dialog.setTitle("About DanceME")
				.setView(v)
				.setMessage("DanceME is an app created for dancers by Johnny Green, William Minniear, and Brandon Mohajeri "
						+ "as part of the Chapman University CPSC370 Final Project. "
						+ "\nWe hope you enjoy it as much as we enjoyed making it!")
						.setPositiveButton("Back", new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				AlertDialog alertDialog = dialog.create();
				alertDialog.show();
				return true;
			}
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	private class BalletButtonListener implements Button.OnClickListener{
		@SuppressLint("InflateParams")
		@Override
		public void onClick(View v) {
			LayoutInflater li = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View view = li.inflate(R.layout.delete_dialog_layout, null, false);
			AlertDialog.Builder dialog = new AlertDialog.Builder(MainScreen.this,R.style.Theme_Trans);
			dialog.setView(view);
			dialog.setTitle("Coming Soon!"); 
			dialog.setMessage("Ballet functionality coming soon in 2015!");
			dialog.setPositiveButton("Back", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel(); 
				}
			});
			AlertDialog alertDialog = dialog.create();
			alertDialog.show();
//			Intent nextScreen = new Intent(MainScreen.this, BalletScreen.class);
//			nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);				
//			startActivity(nextScreen);
		}		
	}
	
	private class ModernButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			Intent nextScreen = new Intent(MainScreen.this, ModernScreen.class);
			nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);				
			startActivity(nextScreen);
		}
	}
	
	private class TapButtonListener implements Button.OnClickListener{
		@Override
		public void onClick(View v) {
			Intent nextScreen = new Intent(MainScreen.this, TapScreen.class);
			nextScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);				
			startActivity(nextScreen);
		}
	}
}
