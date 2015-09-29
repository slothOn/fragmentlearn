package com.example.whuassist.schedule;

import java.util.ArrayList;

import com.example.whuassist.R;
import com.example.whuassist.Tool;
import com.example.whuassist.WhuUtil;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DayTimeFragment extends Fragment{
	
	public final static String[] LESCOLORS={"#FFB6C1", "#DA70D6", "#6495ED", "#90EE90", "#D3D3D3"
		, "#F0E68C", "#A8DFF4", "#DDBCEE", "#DE3E992", "#FFE3A0", "#FFAFAF"};
	
	int mposition;
	public DayTimeFragment(int p){
		mposition=p;
	}
	ProgressBar mprogress;	
	AbsoluteLayout abs;
	
	ArrayList<Integer> thisweekclass=new ArrayList<Integer>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.schedule_day, null);
		abs=(AbsoluteLayout) v.findViewById(R.id.id_leslayout);
		
		for(int i=0;i<WhuUtil.courseSchedule.size();i++){
			Schedulemodel sm=WhuUtil.courseSchedule.get(i);
			if(isInThisweek(sm)){
				//处理每门课的显示
				processDisplay(sm,i);
			}
		}
		
		return v;
	}
	
	public void processDisplay(Schedulemodel sm,int index){
		for(int i=0;i<sm.weekday.length;i++){
			int daystart=sm.daytimestart[i];
			int dayend=sm.daytimeend[i];
			int weekday=0;
			switch (sm.weekday[i]) {
			case "周日":
				weekday=0;
				break;
			case "周一":
				weekday=1;
				break;
			case "周二":
				weekday=2;
				break;
			case "周三":
				weekday=3;
				break;
			case "周四":
				weekday=4;
				break;
			case "周五":
				weekday=5;
				break;
			case "周六":
				weekday=6;
				break;
			default:
				break;
			}
			int width=Tool.dp2px(getActivity(), 100);
			int height=Tool.dp2px(getActivity(),(dayend-daystart+1)*50);
			int y=Tool.dp2px(getActivity(),(daystart-1)*50);
			int x=Tool.dp2px(getActivity(),weekday*100);
			Button btn=new Button(getActivity());
			//btn.setBackgroundColor(LESCOLORS[i%LESCOLORS.length]);
			btn.setBackgroundColor(Color.parseColor(LESCOLORS[index%LESCOLORS.length]));
			btn.setText(sm.name+","+sm.place[i]);
			AbsoluteLayout.LayoutParams params=new AbsoluteLayout.LayoutParams(width, height, x, y);
			btn.setLayoutParams(params);
			abs.addView(btn);
		}
		
	}
	
	public boolean isInThisweek(Schedulemodel s){
		if(s.weektimestart[0]<=mposition&&mposition<=s.weektimeend[0]){
			return true;
		}else{
			return false;
		}
	}
}
