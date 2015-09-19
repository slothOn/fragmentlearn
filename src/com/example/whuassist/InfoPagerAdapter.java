package com.example.whuassist;

import java.util.ArrayList;
import java.util.List;

import com.example.whuassist.info.CulFragment;
import com.example.whuassist.info.NewsFragment;
import com.example.whuassist.info.NotfcFragment;
import com.example.whuassist.info.SciFragment;
import com.example.whuassist.info.TabFragment;

import android.app.Fragment;
import android.app.FragmentManager;

public class InfoPagerAdapter extends MyFragmentAdapter {
    public static String titles[]=new String[]{"新闻","通知","培养","科研"};
    public static List<Fragment> frgs;
	public InfoPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		if(frgs==null){
			frgs=new ArrayList<Fragment>();
			frgs.add(new NewsFragment());
			frgs.add(new NotfcFragment());
			frgs.add(new CulFragment());
			frgs.add(new SciFragment());
		}
		
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return frgs.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}
    @Override
    public CharSequence getPageTitle(int position) {
    	// TODO Auto-generated method stub
    	return titles[position];
    }
}
