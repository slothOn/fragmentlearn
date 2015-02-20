package com.example.whuassist;

import com.viewpagerindicator.TabPageIndicator;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NewsFragment extends Fragment {
	ViewPager viewpager;
	TabPageIndicator tabpageindicator;
	NewsPagerAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.news_frag,container,false);
		adapter=new NewsPagerAdapter(getFragmentManager());
		viewpager=(ViewPager) v.findViewById(R.id.id_viewpager);
		viewpager.setAdapter(adapter);
		tabpageindicator=(TabPageIndicator) v.findViewById(R.id.id_pageindicator);
		tabpageindicator.setViewPager(viewpager, 0);
		return v;
	}
}
