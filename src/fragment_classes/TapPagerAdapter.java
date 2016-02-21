package fragment_classes;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TapPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	
	public TapPagerAdapter(FragmentManager fm, List<Fragment> frag) {
		super(fm);
		this.fragments = frag;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
