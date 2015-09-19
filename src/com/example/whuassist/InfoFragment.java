package com.example.whuassist;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whuassist.info.CulFragment;
import com.example.whuassist.info.NewsFragment;
import com.example.whuassist.info.NotfcFragment;
import com.example.whuassist.info.SciFragment;
import com.viewpagerindicator.TabPageIndicator;

public class InfoFragment extends Fragment {
	public String titles[]=new String[]{"新闻","通知","培养","科研"};
	public List<Fragment> frgs=new ArrayList<Fragment>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		frgs.add(new NewsFragment());
		frgs.add(new NotfcFragment());
		frgs.add(new CulFragment());
		frgs.add(new SciFragment());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.news_frag, container, false);
		
		ViewPager viewpager=(ViewPager) v.findViewById(R.id.id_viewpager);
		TabPageIndicator tabpageindicator=(TabPageIndicator) v.findViewById(R.id.id_pageindicator);
		//fragment嵌套调用时要用
		InfoPagerAdapter adapter=new InfoPagerAdapter(getChildFragmentManager());
		adapter.notifyDataSetChanged();
		viewpager.setAdapter(adapter);
		tabpageindicator.setViewPager(viewpager);
		return v;
	}
	
	class InfoPagerAdapter extends MyFragmentAdapter {
	    
	    
		public InfoPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			
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
}

