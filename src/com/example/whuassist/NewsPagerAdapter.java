package com.example.whuassist;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;

public class NewsPagerAdapter extends MyFragmentAdapter {
    public static String titles[]=new String[]{"推荐","新闻","通知","招聘"};
    public static List<Fragment> frgs=new ArrayList<Fragment>();
	public NewsPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
		frgs.add(new TabFragment(0));
		frgs.add(new TabFragment(1));
		frgs.add(new TabFragment(2));
		frgs.add(new TabFragment(3));
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
