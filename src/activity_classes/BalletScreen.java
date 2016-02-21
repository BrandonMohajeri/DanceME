package activity_classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import com.danceme.R;

public class BalletScreen extends Activity{
	private static String TAG = "BalletScreen";
	
	private Button createButton;
	
	public void onCreate(Bundle savedInstanceState){ 
		Log.d(TAG, "Beginning OnCreate");
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.ballet_main_screen);
		
		createButton = (Button)findViewById(R.id.ballet_create_button);
		
		createButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				showDialog();
			}
		});	
	}
	
	@SuppressLint({ "NewApi", "InflateParams" })
	public void showDialog(){	
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("  Number of Moves");
		dialog.setCancelable(false);
		dialog.setNegativeButton("CREATE",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }  
        });    
	
	LayoutInflater li = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View v = li.inflate(R.layout.sub_layout_1, null, false); 
	
	final NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker1);
    np.setWrapSelectorWheel(false);
    np.setMaxValue(50);
	np.setMinValue(1);
	dialog.setView(v);

	AlertDialog alertDialog = dialog.create();
    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
	alertDialog.show();
	alertDialog.getWindow().setLayout(370, 330); //Controlling width and height.
	}
	
}
