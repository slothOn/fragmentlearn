package com.example.whuassist.info;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EntityUtils;

import com.example.whuassist.MyApplication;
import com.example.whuassist.R;
import com.example.whuassist.Tool;
import com.example.whuassist.WhuHttpUtil;
import com.example.whuassist.WhuUtil;
import com.example.whuassist.R.drawable;
import com.example.whuassist.R.id;
import com.example.whuassist.R.layout;
import com.example.whuassist.db.InfoTableHelper;
import com.example.whuassist.db.ScoreTableHelper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class NewsFragment extends Fragment implements OnRefreshListener
{	
	public HzontalGallery myGallery;
	public RadioGroup gallerypoints;
	public Bitmap[] bmps;
	public HzontalGalleryAdapter gadapter;
	private SwipeRefreshLayout swipe;
	private RadioButton[] rbtns;
	private ListView newslist;
	//private ArrayAdapter<TitleModel> nadapter;
	private TitleAdapter nadapter;
	InfoTableHelper nth;
	
	//ArrayList<NewsTitleModel> test=new ArrayList<NewsTitleModel>();
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//test.add(new NewsTitleModel("test","test","test"));test.add(new NewsTitleModel("test","test","test"));
		//nadapter=new ArrayAdapter<TitleModel>(getActivity(), android.R.layout.simple_list_item_1,WhuUtil.newstitle);
		nadapter=new TitleAdapter(getActivity(), R.layout.layout_news_item, WhuUtil.newstitle);
		nth=new InfoTableHelper(getActivity(), "ZihuanNews", null, 1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.layout_newspage, null);
		myGallery=(HzontalGallery) v.findViewById(R.id.hgallery);
		newslist=(ListView) v.findViewById(R.id.news_list);
		
		swipe=(SwipeRefreshLayout) v.findViewById(R.id.news_refresh);
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
		//int[] res={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
		
		bmps=readPicFromFile();
		
		if(bmps!=null){			
			gadapter=new HzontalGalleryAdapter(bmps, getActivity());
		}else{
			gadapter = new HzontalGalleryAdapter(null, getActivity());
			updateNewsFromServer();
		}
		
    	gallerypoints=(RadioGroup) v.findViewById(R.id.gallaryrgroup);
    	
    	myGallery.setAdapter(gadapter);
    	//设置radiobtn
    	rbtns=new RadioButton[3];
    	LinearLayout layout;
    	for(int i=0;i<rbtns.length;i++){
    		layout=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.gallery_icon_layout, null);
    		rbtns[i]=(RadioButton) layout.findViewById(R.id.id_radiobtn);
    		rbtns[i].setId(i);
    		int wh=Tool.dp2px(getActivity(), 10);
    		RadioGroup.LayoutParams params=new RadioGroup.LayoutParams(wh, wh);
    		params.setMargins(4, 0, 4, 0);
    		rbtns[i].setLayoutParams(params);
    		rbtns[i].setClickable(false);
    		layout.removeView(rbtns[i]);
    		gallerypoints.addView(rbtns[i]);
    	}
    	addEvn();
    	newslist.setAdapter(nadapter);
    	
    	queryNewsData();
    	newslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), WhuUtil.newstitle.get(position).txturl, Toast.LENGTH_LONG).show();
				Intent i=new Intent(getActivity(), InfoDetailActivity.class);
				i.putExtra("DetailUrl", WhuUtil.newstitle.get(position).txturl);
				startActivity(i);
			}
		});
    	autogallery();
		return v;
	}
	
	public void queryNewsData(){
		try {
			queryDataFromdb();
			if(WhuUtil.newstitle.size()==0){
				updateNewsFromServer();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			updateNewsFromServer();
		}
	}
	
	public void queryDataFromdb(){
		WhuUtil.newstitle.clear();
		SQLiteDatabase sqd=nth.getWritableDatabase();
		Cursor cursor=sqd.rawQuery("select * from News", null);
		if(cursor.moveToFirst()){
			do{
				String title=cursor.getString(cursor.getColumnIndex("title"));
				String date=cursor.getString(cursor.getColumnIndex("date"));
				String txturl=cursor.getString(cursor.getColumnIndex("txturl"));
				WhuUtil.newstitle.add(new TitleModel(title, date, txturl));
				
			}while(cursor.moveToNext());
		}
		nadapter.notifyDataSetChanged();
	}
	/*
	 @Override
	public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	queryDataFromdb();
    	if(WhuUtil.newstitle.size()==0){
    		updateNewsFromServer();
    	}
    	
    	nadapter.notifyDataSetChanged();
    	autogallery();
    }
	*/	
  //添加事件
  	void addEvn(){
  		myGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

  			@Override
  			public void onItemSelected(AdapterView<?> arg0, View arg1,
  					int arg2, long arg3) {
  				// TODO Auto-generated method stub
  				gallerypoints.check(rbtns[arg2%rbtns.length].getId());
  			}

  			@Override
  			public void onNothingSelected(AdapterView<?> arg0) {
  				// TODO Auto-generated method stub
  				
  			}
  		});
  	}
    /** 展示图控制器，实现展示图切换 */
	final Handler handler_gallery = new Handler() {
		public void handleMessage(Message msg) {
			/* 自定义屏幕按下的动作 */
			MotionEvent e1 = MotionEvent.obtain(SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
					89.333336f, 265.33334f, 0);
			/* 自定义屏幕放开的动作 */
			MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
					300.0f, 238.00003f, 0);
			//这个没弄明白。。。
			myGallery.onFling(e2, e1, -800, 0);
			/* 给gallery添加按下和放开的动作，实现自动滑动 */
			super.handleMessage(msg);
		}
	};
	
	private void autogallery() {
		/* 设置定时器，每5秒自动切换展示图 */
		Timer time = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message m = new Message();
				handler_gallery.sendMessage(m);
			}
		};
		time.schedule(task, 8000, 8000);
	}
	
	public void updateNewsFromServer(){
		swipe.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				swipe.setRefreshing(true);
			}
		});
		onRefresh();
	}
	
	class DownloadNewsTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//showProgressDlg();
			//swipe.setRefreshing(true);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			boolean t=requestIndexpage();
			bmps=readPicFromFile();
			
			if(bmps!=null){				
				return t;
			}
			return false;
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
				gadapter.bmps=bmps;
				gadapter.notifyDataSetChanged();
				nadapter.notifyDataSetChanged();
				saveNews2db();
			}else{
				Toast.makeText(MyApplication.getWhuContext(), "刷新新闻错误", Toast.LENGTH_LONG).show();
			}
			swipe.setRefreshing(false);
		}
		
	}
	
	public boolean requestIndexpage(){
		boolean t=WhuUtil.requestIndexpage();
		if(t){
			savePicToFile(WhuUtil.picurl);
			return t;
		}else{
			return false;
		}
	}
	
	public Bitmap[] readPicFromFile(){
		try {
			Bitmap[] bmps=new Bitmap[3];
			for(int i=0;i<3;i++){
				InputStream input=null;
				input=getActivity().openFileInput("newspic"+i);
				byte[] bytes=new byte[256*1024];
				int len=input.read(bytes);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, len); 
				bmps[i]=bitmap; 
			}
			return bmps;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public void savePicToFile(String[] picurl){
		for(int i=0;i<picurl.length;i++){
			FileOutputStream out=null;
			try {
				URL purl=new URL(picurl[i]);
				HttpURLConnection piccon=(HttpURLConnection) purl.openConnection();
				piccon.setDoInput(true);
				//piccon.setDoOutput(true);
				piccon.setRequestMethod("GET");
				InputStream input=piccon.getInputStream();
				
				out=getActivity().openFileOutput("newspic"+i, Context.MODE_PRIVATE);
				byte[] b=new byte[256*1024];
				int len;
				while((len=input.read(b))!=-1){
					if(out!=null){
						out.write(b,0,len);
					}
				}			
				if(input!=null){
					input.close();
				}
				if(out!=null){
					out.close();
				}
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
	}
	
	public void saveNews2db(){
		SQLiteDatabase sqb=nth.getWritableDatabase();		
		sqb.beginTransaction();
		sqb.execSQL("delete from News");
		for(TitleModel newstitle:WhuUtil.newstitle){
			sqb.execSQL("insert into News(title,date,txturl) values(?,?,?)",
					new String[]{newstitle.title,newstitle.date,newstitle.txturl});
		}
		sqb.setTransactionSuccessful();
		sqb.endTransaction();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new DownloadNewsTask().execute();
	}
	
}
