package com.example.whuassist.service;

import com.example.whuassist.HttpCallbackListener;
import com.example.whuassist.WhuHttpUtil;
import com.example.whuassist.WhuUtil;
import com.example.whuassist.receiver.AutoUpdateReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class AutoUpdateService extends Service {
	private AutoUpdateBinder mBinder=new AutoUpdateBinder();
	class AutoUpdateBinder extends Binder{
		/*
		 * 和activity通信
		 */
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		long anHour=60*60*1000;
		long triggerTime=SystemClock.elapsedRealtime()+anHour;
		
		if(checkNetworkConnection()){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					WhuHttpUtil.getInstance().Loginweb("http://210.42.121.241/servlet/Svlt_QueryStuScore?year=0&term=&learnType=&scoreFlag=0", null, new HttpCallbackListener() {
						
						@Override
						public void onFinish(String txt) {
							// TODO Auto-generated method stub
							Log.d("schedule", txt);
							WhuUtil.scoreParse(txt);
							
						}
						
						@Override
						public void onError(String txt) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}).start();
		}
		
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i=new Intent(this,AutoUpdateReceiver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_NO_CREATE);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	public boolean checkNetworkConnection(){
		//检测当前网络状态
		ConnectivityManager manager=
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=manager.getActiveNetworkInfo();
		if(info!=null&&info.isAvailable()){
			return true;
		}
		return false;
	}
}
