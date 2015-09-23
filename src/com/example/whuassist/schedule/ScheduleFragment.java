package com.example.whuassist.schedule;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.whuassist.MyPagerAdapter;
import com.example.whuassist.R;
import com.example.whuassist.WhuHttpUtil;
import com.example.whuassist.WhuUtil;

public class ScheduleFragment extends Fragment {
	
	List<Fragment> fraglist=new ArrayList<Fragment>();
	ViewPager mdayspager;
    MyPagerAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.schedule_fragment, container, false);
		mdayspager=(ViewPager) v.findViewById(R.id.id_dayspager);
		
		adapter=new MyPagerAdapter(getChildFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 25;
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				if(position>=fraglist.size()){
					fraglist.add(new DayTime(position));
				}
				return fraglist.get(position);
			}
		};
		mdayspager.setAdapter(adapter);
		mdayspager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		return v;
	}
	
	class DownloadScheduleTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String rtxt=WhuHttpUtil.getInstance().reqwebinfo(
						"http://210.42.121.241//servlet/Svlt_QueryStuLsn?action=normalLsn", null);
				WhuUtil.courseSchedule.clear();
				WhuUtil.scheduleParse(rtxt);
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				
			}else{
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_LONG).show();
			}
			//把服务器数据写回数据库
			saveSchedule2db();
			
		}
	}
	public void saveSchedule2db(){
		
	}
}
