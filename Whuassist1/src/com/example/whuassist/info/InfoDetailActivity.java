package com.example.whuassist.info;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.whuassist.MyApplication;
import com.example.whuassist.R;
import com.example.whuassist.WhuUtil;

public class InfoDetailActivity extends Activity implements OnRefreshListener{
	String detailurl;
	WebView webview;
	SwipeRefreshLayout swipe;
	String webhtml;
	Toolbar mtoolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newsdetail);
	
		webview=(WebView) findViewById(R.id.id_newsdetail);
		swipe=(SwipeRefreshLayout) findViewById(R.id.ctnt_refresh);
		swipe.setOnRefreshListener(this);
		swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light, android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);
		/*
		mtoolbar=(Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mtoolbar);
		mtoolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {  
		    @Override  
		    public boolean onMenuItemClick(MenuItem item) {  
		        switch (item.getItemId()) {  
		        case R.id.action_settings:  
		            Toast.makeText(getApplicationContext(), "action_settings", 0).show();  
		            break;  
		        case R.id.action_share:  
		            Toast.makeText(getApplicationContext(), "action_share", 0).show();  
		            break;  
		        default:  
		            break;  
		        }  
		        return true;  
		    }  
		});  
		*/
		detailurl=getIntent().getStringExtra("DetailUrl");
				
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});
		
		swipe.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				swipe.setRefreshing(true);
			}
		});
		onRefresh();
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new DownloadNewsDetailTask().execute(detailurl);
	}
	
	class DownloadNewsDetailTask extends AsyncTask<String, Integer, Boolean>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpURLConnection conn=null;
			URL url;
			try {
				url = new URL(params[0]);
				conn=(HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(8000);
				conn.setReadTimeout(8000);
				InputStream in=conn.getInputStream();
				BufferedReader reader=new BufferedReader(new InputStreamReader(in,"gbk"));
				StringBuilder sb=new StringBuilder();
				String line;
				while((line=reader.readLine())!=null){
					sb.append(line);
				}
				webhtml=WhuUtil.parseNewsDetail(sb.toString());
				return true;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}finally{
				if(conn!=null){
					conn.disconnect();
				}
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
				webview.loadDataWithBaseURL(null, webhtml, "text/html", "gbk", null);
			}else{
				Toast.makeText(MyApplication.getWhuContext(), "ÍøÂç´íÎó", Toast.LENGTH_LONG).show();
			}
			
			swipe.setRefreshing(false);
		}
	}
	
}
