package com.example.whuassist;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleActivity extends Activity {
	//private TextView webtxt;
	HttpResponse rsp;
	TextView GPAshow;
	ListView listview;
	Button btn_GPA;
	float GPA=0;
	float creditAll=0;
	//ArrayAdapter<String> dataList;
	ArrayAdapter<Scoremodel> adapter;
	TextView titletext;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				//webtxt.setText((CharSequence) msg.obj);
				adapter.notifyDataSetChanged();
				break;
			case 1:
				//webtxt.setText((CharSequence) msg.obj);
				float g=(Float) msg.obj;
				GPAshow.setText(String.valueOf(g));
				break;
			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.schedule_activity);
		
		listview=(ListView) findViewById(R.id.score_list);
		btn_GPA=(Button) findViewById(R.id.btn_compute);
		GPAshow=(TextView) findViewById(R.id.show_score);
		titletext=(TextView) findViewById(R.id.title_text1);
		titletext.setText("³É¼¨");
		btn_GPA.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			    for(int i=0;i<WhuUtil.courseScore.size();i++){
			    	Scoremodel scoremodel=WhuUtil.courseScore.get(i);
			    	creditAll+=scoremodel.credit;
			    	GPA+=scoremodel.getGPA()*scoremodel.credit;
			    }
			    if(creditAll!=0)
			    	GPA=GPA/creditAll;
			    Message msg=new Message();
			    msg.what=1;
			    msg.obj=GPA;
			    handler.sendMessage(msg);
			}
		});
		adapter=new ArrayAdapter<Scoremodel>(this,android.R.layout.simple_list_item_1,WhuUtil.courseScore);
		//webtxt=(TextView) findViewById(R.id.schedule_code);
		listview.setAdapter(adapter);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpClient client=new DefaultHttpClient();
				//HttpPost post=new HttpPost("http://210.42.121.241/stu/stu_score_parent.jsp");
				//HttpPost post=new HttpPost("http://210.42.121.241/servlet/Svlt_QueryStuLsn?action=queryStuLsn");
				Date t=new Date();
				//HttpPost post=new HttpPost("http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0&t="+t.toString());
				HttpPost post=new HttpPost("http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0");
				post.setHeader("Cookie", "JSESSIONID="+WhuHttpUtil.COOKIE);
				
				try {
					rsp=client.execute(post);
					if(rsp.getStatusLine().getStatusCode()==200){
						HttpEntity e=rsp.getEntity();
						String txt=EntityUtils.toString(e);
						Log.d("schedule", txt);
						WhuUtil.scoreParse(txt);
						Message msg=new Message();
						msg.what=0;
						//msg.obj=WhuUtil.courseScore;
						handler.sendMessage(msg);
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
