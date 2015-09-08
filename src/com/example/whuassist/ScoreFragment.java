package com.example.whuassist;

import java.util.ArrayList;

import org.apache.http.HttpResponse;

import com.example.whuassist.db.ScoreTableHelper;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import android.widget.Toast;

public class ScoreFragment extends Fragment {
	private ScoreTableHelper sdbhelper;
	
	HttpResponse rsp;
	TextView GPAshow;
	ListView listview;
	Button btn_GPA;
	ProgressDialog progressdlg;
	float GPA=0;
	
	boolean isGPAComputed;
	float creditAll=0;
	//ArrayAdapter<String> dataList;
	ArrayAdapter<Scoremodel> adapter;
	TextView titletext;
	
	@Override
    public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
    	super.onAttach(activity);
    	adapter=new ArrayAdapter<Scoremodel>(activity,
    			android.R.layout.simple_list_item_1,WhuUtil.courseScore);
    };
    public void showProgressDlg(){
    	if(progressdlg==null){
    		progressdlg=new ProgressDialog(getActivity());
    		progressdlg.setMessage("正在加载");
    		progressdlg.setCanceledOnTouchOutside(false);
    	}
    	progressdlg.show();
    }
    public void closeProgressDlg(){
    	if(progressdlg!=null){
    		progressdlg.dismiss();
    	}
    }
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
			new DownloadScoreTask().execute();
		}
		return v;
	}
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isgpacomputed", isGPAComputed);
		outState.putFloat("gpa", GPA);
	}
	
	
	public void saveScore2db(){
		//数据库不存在则新建
		sdbhelper=new ScoreTableHelper(getActivity(),"WHU"+MainActivity.Account+".db",null,1);
		sdbhelper.getWritableDatabase();
	}
	
	
	class DownloadScoreTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			showProgressDlg();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String rtxt=WhuHttpUtil.getInstance().reqwebinfo(
						"http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0", null);
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
			saveScore2db();
			closeProgressDlg();
		}
	}

}	
	