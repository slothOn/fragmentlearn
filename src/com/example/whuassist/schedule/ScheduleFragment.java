package com.example.whuassist.schedule;

import com.example.whuassist.R;
import com.example.whuassist.WhuHttpUtil;
import com.example.whuassist.WhuUtil;
import com.example.whuassist.R.layout;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ScheduleFragment extends Fragment {
	
	ProgressBar mprogress;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.schedule_fragment, container, false);
		mprogress.setVisibility(View.GONE);
		return v;
	}
	
	class DownloadScheduleTask extends AsyncTask<Void, Integer, Boolean>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mprogress.setVisibility(View.VISIBLE);
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				String rtxt=WhuHttpUtil.getInstance().reqwebinfo(
						"http://210.42.121.241//servlet/Svlt_QueryStuLsn?action=normalLsn", null);
				WhuUtil.courseSchedule.clear();
				WhuUtil.scheduleParse(rtxt);
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
				
			}else{
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_LONG).show();
			}
			//把服务器数据写回数据库
			saveSchedule2db();
			closeProgressDlg();
		}
	}
	public void saveSchedule2db(){
		
	}
}
