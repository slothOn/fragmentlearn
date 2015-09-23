package com.example.whuassist.schedule;

import com.example.whuassist.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DayTime extends Fragment{
	
	int mposition;
	public DayTime(int p){
		mposition=p;
	}
	ProgressBar mprogress;	
	TextView text;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.schedule_day, null);
		text=(TextView) v.findViewById(R.id.id_weeknum);
		text.setText("µÚ"+mposition+"ÖÜ");
		return v;
	}
}
