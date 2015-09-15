package com.example.whuassist;

import java.util.ArrayList;

import org.apache.http.HttpResponse;

import com.example.whuassist.db.ScoreTableHelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
	private ScoreTableHelper sdbhelper;
	
	TextView GPAshow;
	ListView listview;
	Button btn_GPA;
	ProgressDialog progressdlg;
	float GPA=0;
	
	boolean isGPAComputed;
	float creditAll=0;
	ArrayAdapter<Scoremodel> adapter;
	TextView titletext;
	SwipeRefreshLayout swiperefresh;
	@Override
    public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
    	super.onAttach(activity);
    	//数据库不存在则新建
		sdbhelper=new ScoreTableHelper(getActivity(),"WHU"+MainActivity.Account+".db",null,1);
    	adapter=new ArrayAdapter<Scoremodel>(activity,
    			android.R.layout.simple_list_item_1,WhuUtil.courseScore);
    };
   
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.score_fragment, null);
		
		swiperefresh=(SwipeRefreshLayout) v.findViewById(R.id.id_swiperefresh);
		swiperefresh.setOnRefreshListener(this);
		swiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
		
		listview=(ListView)v.findViewById(R.id.score_list);
		btn_GPA=(Button)v.findViewById(R.id.btn_compute);
		GPAshow=(TextView)v.findViewById(R.id.show_score);
		swiperefresh.setOnRefreshListener( this);
        btn_GPA.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GPA=0;
				creditAll=0;
				ArrayList<String> namelist=new ArrayList<String>();
			    for(int i=0;i<WhuUtil.courseScore.size();i++){
			    	Scoremodel scoremodel=WhuUtil.courseScore.get(i);
			    	if(scoremodel.score<60)
			    		continue;
			    	if(namelist.lastIndexOf(scoremodel.name)!=-1){
			    		int j=namelist.lastIndexOf(scoremodel.name);
			    		Scoremodel prescore=WhuUtil.courseScore.get(j);
			    		creditAll-=prescore.credit;
			    		GPA-=prescore.getGPA();
			    		
			    	}
			    	namelist.add(scoremodel.name);
			    	creditAll+=scoremodel.credit;
			    	GPA+=scoremodel.getGPA()*scoremodel.credit;
			    }
			    if(creditAll!=0)
			    	GPA=GPA/creditAll;
			    GPAshow.setText(String.valueOf(GPA));
			    isGPAComputed=true;			        
			}
		});
        
		listview.setAdapter(adapter);
		
		if(WhuUtil.courseScore.size()==0){
			queryScoreData();
		}
	
		return v;
	}
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isgpacomputed", isGPAComputed);
		outState.putFloat("gpa", GPA);
	}
	
	//保存到数据库
	public void saveScore2db(){
		SQLiteDatabase sdb=sdbhelper.getWritableDatabase();
		//开启事务
		sdb.beginTransaction();
		sdb.execSQL("delete from Score");
		for(Scoremodel cscore:WhuUtil.courseScore){
			sdb.execSQL("insert into Score(id,name,credit,score) values(?,?,?,?)",
					new String[]{cscore.id,cscore.name,String.valueOf(cscore.credit),String.valueOf(cscore.score)});
			
		}
		sdb.setTransactionSuccessful();
		sdb.endTransaction();
	}
	
	//从数据库获取成绩
	public void queryScoreFromdb(){
		SQLiteDatabase sdb=sdbhelper.getWritableDatabase();
		Cursor cursor=sdb.rawQuery("select * from Score", null);
		if(cursor.moveToFirst()){
			do {
				String id=cursor.getString(cursor.getColumnIndex("id"));
				String name=cursor.getString(cursor.getColumnIndex("name"));
				float credit=cursor.getFloat(cursor.getColumnIndex("credit"));
				float score=cursor.getFloat(cursor.getColumnIndex("score"));
				WhuUtil.courseScore.add(new Scoremodel(id,name,credit,score));
			} while (cursor.moveToNext());
		}
		adapter.notifyDataSetChanged();
	}
	//从服务器获取数据
	public void queryScoreFromSever(){
		new DownloadScoreTask().execute();
	}
	//获取数据
	public void queryScoreData(){
		try {
			queryScoreFromdb();
			if(WhuUtil.courseScore.size()==0){
				queryScoreFromSever();
			}
		} catch (Exception e) {
			// TODO: handle exception
			queryScoreFromSever();
		}
		
	}
	class DownloadScoreTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//showProgressDlg();
			swiperefresh.setRefreshing(true);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String rtxt=WhuHttpUtil.getInstance().reqwebinfo(
						"http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0", null);
				WhuUtil.courseScore.clear();
				WhuUtil.scoreParse(rtxt);
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
				adapter.notifyDataSetChanged();
			}else{
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_LONG).show();
			}
			//把服务器数据写回数据库
			saveScore2db();
			//closeProgressDlg();
			swiperefresh.setRefreshing(false);
		}
		
	}
	//刷新状态
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		queryScoreFromSever();
	}

}	
	