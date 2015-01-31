package com.example.whuassist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ScheduleActivity extends Activity {
	private TextView webtxt;
	HttpResponse rsp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		webtxt=(TextView) findViewById(R.id.schedule_code);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client=new DefaultHttpClient();
				HttpPost post=new HttpPost("http://210.42.121.241/stu/stu_score_parent.jsp");
				post.setHeader("Cookie", "JESSIONID="+WhuHttpUtil.COOKIE);
				
				try {
					rsp=client.execute(post);
					if(rsp.getStatusLine().getStatusCode()==200){
//						HttpEntity e=rsp.getEntity();
//						String txt=EntityUtils.toString(e);
//						Log.d("schedule", txt);
						Log.d("schedule", WhuHttpUtil.COOKIE);
						StringBuffer sb = new StringBuffer();
                        HttpEntity entityback = rsp.getEntity();
                        InputStream is = entityback.getContent();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
                        String data = "";
                        while ((data = br.readLine()) != null) {
                                sb.append(data);
                        }
                        Log.d("schedule", sb.toString());
					}
				
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}
}
