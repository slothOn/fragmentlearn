package com.example.whuassist.info;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.whuassist.R;
import com.example.whuassist.WhuUtil;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.Toolbar;

public class InfoDetailActivity extends Activity implements OnRefreshListener{
	String detailurl;
	WebView webview;
	SwipeRefreshLayout swipe;
	String webhtml;
	
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
		
		detailurl=getIntent().getStringExtra("DetailUrl");
				
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});
		
		new DownloadNewsDetailTask().execute(detailurl);
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
			swipe.setRefreshing(true);
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
				Toast.makeText(getApplicationContext(), "ÍøÂç´íÎó", Toast.LENGTH_LONG).show();
			}
			swipe.setRefreshing(false);
		}
	}
	
}
