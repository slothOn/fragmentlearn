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
	private final static int GET_PAGEREPLY=0;
	private final static int GET_GPAFINISH=1;
	
	HttpResponse rsp;
	TextView GPAshow;
	ListView listview;
	Button btn_GPA;
	float GPA=0;
	boolean isGPAComputed;
	float creditAll=0;
	//ArrayAdapter<String> dataList;
	ArrayAdapter<Scoremodel> adapter;
	TextView titletext;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case GET_PAGEREPLY:
				//webtxt.setText((CharSequence) msg.obj);
				adapter.notifyDataSetChanged();
				break;
			case GET_GPAFINISH:
				//webtxt.setText((CharSequence) msg.obj);
				float g=(Float) msg.obj;
				GPAshow.setText(String.valueOf(g));
				break;
			default:
				break;
			}
		}
	};
	
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isgpacomputed", isGPAComputed);
		outState.putFloat("gpa", GPA);
	};
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		isGPAComputed=savedInstanceState.getBoolean("isgpacomputed");
		GPA=savedInstanceState.getFloat("gpa");
		if(isGPAComputed){
			GPAshow.setText(String.valueOf(GPA));
		}
	}
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
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
				GPA=0;
				creditAll=0;
			    for(int i=0;i<WhuUtil.courseScore.size();i++){
			    	Scoremodel scoremodel=WhuUtil.courseScore.get(i);
			    	creditAll+=scoremodel.credit;
			    	GPA+=scoremodel.getGPA()*scoremodel.credit;
			    }
			    if(creditAll!=0)
			    	GPA=GPA/creditAll;
			    Message msg=new Message();
			    msg.what=GET_GPAFINISH;
			    msg.obj=GPA;
			    handler.sendMessage(msg);
			    isGPAComputed=true;			    
			    
			    
			}
		});
		adapter=new ArrayAdapter<Scoremodel>(this,android.R.layout.simple_list_item_1,WhuUtil.courseScore);
		
		listview.setAdapter(adapter);
		WhuHttpUtil.getInstance().Loginweb("http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0", null, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String txt) {
				// TODO Auto-generated method stub
				Log.d("schedule", txt);
				WhuUtil.scoreParse(txt);
				Message msg=new Message();
				msg.what=GET_PAGEREPLY;
				handler.sendMessage(msg);
			}
			
			@Override
			public void onError(String txt) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
