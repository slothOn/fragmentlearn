package com.example.whuassist.schedule;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.whuassist.MainActivity;
import com.example.whuassist.MyApplication;
import com.example.whuassist.MyPagerAdapter;
import com.example.whuassist.R;
import com.example.whuassist.WhuHttpUtil;
import com.example.whuassist.WhuUtil;
import com.example.whuassist.db.ScheduleTableHelper;
import com.example.whuassist.db.ScoreTableHelper;
import com.example.whuassist.score.Scoremodel;

public class ScheduleFragment extends Fragment {
	
	List<Fragment> fraglist=new ArrayList<Fragment>();
	ViewPager mdayspager;
    MyPagerAdapter adapter;
    ScheduleTableHelper sdbhelper;
    ProgressBar prgbar;
    Spinner weektip;
    
    @Override
    public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
    	super.onAttach(activity);
    	//数据库不存在则新建
		sdbhelper=new ScheduleTableHelper(getActivity(),"WHU"+MainActivity.Account+"schedule.db",null,1);
    };
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.schedule_fragment, container, false);
		mdayspager=(ViewPager) v.findViewById(R.id.id_dayspager);
		prgbar=(ProgressBar) v.findViewById(R.id.schedule_progressbar);
		weektip=(Spinner) v.findViewById(R.id.id_weeknum);
		weektip.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mdayspager.setCurrentItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		adapter=new MyPagerAdapter(getChildFragmentManager()) {
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return 25;
			}
			
			@Override
			public Fragment getItem(int position) {
				// TODO Auto-generated method stub
				while(position>=fraglist.size()){
					fraglist.add(new DayTimeFragment(fraglist.size()));
				}
				return fraglist.get(position);
			}
		};
		mdayspager.setAdapter(adapter);
		mdayspager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				weektip.setSelection(arg0);
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
		
		if(WhuUtil.courseSchedule.size()==0){
			queryScheduleData();
		}

		return v;
	}
	
	public void queryScheduleData(){
		try {
			queryScheduleFromdb();
			if(WhuUtil.courseSchedule.size()==0){
				queryScheduleFromServer();
			}
		} catch (Exception e) {
			// TODO: handle exception
			queryScheduleFromServer();
		}
	}
	
	public void queryScheduleFromdb(){
		//从数据库中获取课表
		SQLiteDatabase sdb=sdbhelper.getWritableDatabase();
		Cursor cursor=sdb.rawQuery("select * from Schedule", null);
		if(cursor.moveToFirst()){
			do {			
				String id=cursor.getString(cursor.getColumnIndex("id"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String timetxt=cursor.getString(cursor.getColumnIndex("time"));
				WhuUtil.courseSchedule.add(WhuUtil.parse2Schedulemodel(id, name, timetxt));
				
			} while (cursor.moveToNext());
		}
		mdayspager.setCurrentItem(WhuUtil.weeknum-1);
		weektip.setSelection(WhuUtil.weeknum-1);
		adapter.notifyDataSetChanged();
	}
	
	public void queryScheduleFromServer(){
		//从服务器获取课表
		new DownloadScheduleTask().execute();
	}
	
	class DownloadScheduleTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			prgbar.setVisibility(View.VISIBLE);
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
				mdayspager.setCurrentItem(WhuUtil.weeknum-1);
				weektip.setSelection(WhuUtil.weeknum-1);
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(MyApplication.getWhuContext(), "网络错误", Toast.LENGTH_LONG).show();
			}
			//把服务器数据写回数据库
			saveSchedule2db();
			prgbar.setVisibility(View.GONE);
		}
	}
	public void saveSchedule2db(){
		//课表信息存回数据库
		SQLiteDatabase sdb=sdbhelper.getWritableDatabase();
		//开启事务
		sdb.beginTransaction();
		sdb.execSQL("delete from Schedule");
		for(Schedulemodel cschedule:WhuUtil.courseSchedule){
			sdb.execSQL("insert into Schedule(id,name,time) values(?,?,?)",
					new String[]{cschedule.id,cschedule.name,cschedule.timetxt});
			
		}
		sdb.setTransactionSuccessful();
		sdb.endTransaction();
	}
}
