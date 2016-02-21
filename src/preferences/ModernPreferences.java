package preferences;

import com.danceme.R;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

/*
 * Preferences screen for Modern Moves
 */
public class ModernPreferences extends PreferenceActivity{
	
	private final static String TAG = "ModernPreferences";
	
	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new ModernPreferencesFragment()).commit(); //fragment for preferences
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		PreferenceManager.setDefaultValues(ModernPreferences.this, R.layout.modern_preference_screen_layout, true);
		preferences = PreferenceManager.getDefaultSharedPreferences(ModernPreferences.this);
		if (preferences.getBoolean("direction_up", true)){
			Log.e(TAG, "it got direction_up");
		}
	}
	
	/*
	 * To have the back button in the action bar
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:{
				NavUtils.navigateUpFromSameTask(ModernPreferences.this);
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * required fragment for preferences
	 */
	private static class ModernPreferencesFragment extends PreferenceFragment{
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.layout.modern_preference_screen_layout);
			Log.i(TAG, "Modern preferences loaded from resource.");
		}
	}
	
	public void onDestroy(){
		Log.i(TAG, "OnDestroy");
		super.onDestroy();
	}
}
