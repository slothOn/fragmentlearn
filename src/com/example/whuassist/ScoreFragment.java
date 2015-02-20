package com.example.whuassist;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class ScoreFragment extends Fragment {
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
	@Override
    public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
    	super.onAttach(activity);
    	adapter=new ArrayAdapter<Scoremodel>(activity,
    			android.R.layout.simple_list_item_1,WhuUtil.courseScore);
    };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.score_fragment, null);
		listview=(ListView)v.findViewById(R.id.score_list);
		btn_GPA=(Button)v.findViewById(R.id.btn_compute);
		GPAshow=(TextView)v.findViewById(R.id.show_score);
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
		return v;
	}
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isgpacomputed", isGPAComputed);
		outState.putFloat("gpa", GPA);
	};
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		//ÏÈcreateÔÙrestore
//		super.onRestoreInstanceState(savedInstanceState);
//		isGPAComputed=savedInstanceState.getBoolean("isgpacomputed");
//		GPA=savedInstanceState.getFloat("gpa");
//		if(isGPAComputed){
//			GPAshow.setText(String.valueOf(GPA));
//		}
//	}
}
