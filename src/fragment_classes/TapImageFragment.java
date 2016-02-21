package fragment_classes;

import com.danceme.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * ImageFragment class for Tap Dances
 * This is used for the view screen of tap dances
 */
public class TapImageFragment extends Fragment {
	private static String TAG = "TapImageFragment";
	
	@SuppressWarnings("unused")
	private ViewGroup myViewGroup;
	private LinearLayout linear;
	private TextView viewDanceName;
	private ImageView danceImage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup image, Bundle os){
		if (image == null){
			return null;
		}
		Bundle args = getArguments();
		myViewGroup = image;
		linear = (LinearLayout)inflater.inflate(R.layout.view_pager_layout, image, false);
		danceImage = (ImageView)linear.findViewById(R.id.tap_image_view);
		viewDanceName = (TextView)linear.findViewById(R.id.dance_name);
		viewDanceName.setSelected(true);

		try{
			danceImage.setImageResource(args.getInt("Image"));
			viewDanceName.setText(args.getString("Name"));
		}
		catch (Exception e){
			Log.e(TAG,"Unable to get photo/name");
		}
		return linear;
	}
}
