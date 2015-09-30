package com.example.whuassist.info;


import com.example.whuassist.R;
import com.example.whuassist.WhuUtil;
import com.example.whuassist.R.id;
import com.example.whuassist.R.layout;
import com.example.whuassist.db.InfoTableHelper;
import com.example.whuassist.info.NewsFragment.DownloadNewsTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class NotfcFragment extends Fragment implements OnRefreshListener
{
	SwipeRefreshLayout swipe;
	ListView mlist;
	//private ArrayAdapter<TitleModel> madapter;
	private TitleAdapter madapter;
	InfoTableHelper nth;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//madapter=new ArrayAdapter<TitleModel>(getActivity(), android.R.layout.simple_list_item_1,WhuUtil.notfctitle);
		madapter=new TitleAdapter(getActivity(), R.layout.layout_news_item, WhuUtil.notfctitle);
		nth=new InfoTableHelper(getActivity(), "ZihuanNews", null, 1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.layout_othrpage, null);
		swipe=(SwipeRefreshLayout)view.findViewById(R.id.list_refresh);
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
		swipe=(SwipeRefreshLayout) view.findViewById(R.id.list_refresh);
		mlist=(ListView) view.findViewById(R.id.id_infolist);
		
		mlist.setAdapter(madapter);
    	
    	if(WhuUtil.notfctitle.size()==0){
    		queryDataFromdb();
    	}
    	if(WhuUtil.notfctitle.size()==0){
    		updateNotfcFromServer();
    	}
    	
    	mlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), WhuUtil.notfctitle.get(position).txturl, Toast.LENGTH_LONG).show();
				Intent i=new Intent(getActivity(), InfoDetailActivity.class);
				i.putExtra("DetailUrl", WhuUtil.notfctitle.get(position).txturl);
				startActivity(i);
			}
		});
    	
		
		return view;
	}
	
	public void queryDataFromdb(){
		WhuUtil.notfctitle.clear();
		SQLiteDatabase sqd=nth.getWritableDatabase();
		Cursor cursor=sqd.rawQuery("select * from Notfc", null);
		if(cursor.moveToFirst()){
			do{
				String title=cursor.getString(cursor.getColumnIndex("title"));
				String date=cursor.getString(cursor.getColumnIndex("date"));
				String txturl=cursor.getString(cursor.getColumnIndex("txturl"));
				WhuUtil.notfctitle.add(new TitleModel(title, date, txturl));
				
			}while(cursor.moveToNext());
		}
		madapter.notifyDataSetChanged();
	}
	
	public void updateNotfcFromServer(){
		new DownloadNotfcTask().execute();
	}
	
	class DownloadNotfcTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//showProgressDlg();
			swipe.setRefreshing(true);
			WhuUtil.notfctitle.clear();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			WhuUtil.requestIndexpage();
			if(WhuUtil.notfctitle.size()!=0){
				return true;
			}else{
				queryDataFromdb();
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
				madapter.notifyDataSetChanged();
				saveNotfc2db();
			}else{
				Toast.makeText(getActivity().getApplicationContext(), "ÍøÂç´íÎó", Toast.LENGTH_LONG).show();
			}
			swipe.setRefreshing(false);
		}
		
	}
	
	public void saveNotfc2db(){
		SQLiteDatabase sqb=nth.getWritableDatabase();		
		sqb.beginTransaction();
		sqb.execSQL("delete from Notfc");
		for(TitleModel notfctitle:WhuUtil.notfctitle){
			sqb.execSQL("insert into Notfc(title,date,txturl) values(?,?,?)",
					new String[]{notfctitle.title,notfctitle.date,notfctitle.txturl});
		}
		sqb.setTransactionSuccessful();
		sqb.endTransaction();
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		updateNotfcFromServer();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		queryDataFromdb();
    	if(WhuUtil.notfctitle.size()==0){
    		updateNotfcFromServer();
    	}
    	madapter.notifyDataSetChanged();
	}
}
