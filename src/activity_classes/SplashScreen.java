package activity_classes;

import web_classes.ParseData;

import com.danceme.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class SplashScreen extends Activity {
	private static String TAG = "SplashScreen";
	
	private ProgressBar spinner;
	
	private String quoteToPass = "";
	
	public void onCreate(Bundle savedInstanceState){
		Log.d(TAG, "Beginning OnCreate");
		super.onCreate(savedInstanceState);   
			
		setContentView(R.layout.splash_screen);
		
		spinner = (ProgressBar)findViewById(R.id.spinnerProgress);
		spinner.setVisibility(View.VISIBLE);
		
		//checking if the network is available
		if (isNetworkAvailable())
			new ProgressBarTask().execute();
		else{
			try{
				new Handler().postDelayed(new Runnable(){
					@Override
					public void run(){
						Log.d(TAG, "Opening Main Screen");
						Intent newIntent = new Intent(SplashScreen.this, MainScreen.class);
						newIntent.putExtra("Quote", "DanceME created at Chapman University");
						startActivity(newIntent);
						finish(); //removing the splash screen from the acitivity stack
					}
				}, 2000);
				  
			} catch (Exception e){
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
	}
	
	/*
	 * Checking if the network is available
	 * This was references from stackoverflow
	 */
	private boolean isNetworkAvailable(){
		ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	/*
	 * To show a small spinning progress bar while a quote is parsed, then opening the main screen once the quote is grabbed
	 */
	private class ProgressBarTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {			
			ParseData p = new ParseData();
			quoteToPass = p.getQuote();			
			return quoteToPass;
		}
		  
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d(TAG, "Opening Main Screen");
			Intent newIntent = new Intent(SplashScreen.this, MainScreen.class);
			newIntent.putExtra("Quote", result);
			startActivity(newIntent);
		}
		
	}
}
