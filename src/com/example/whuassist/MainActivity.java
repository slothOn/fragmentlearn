package com.example.whuassist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private EditText nameedit;
    private EditText passwordedit;
    private EditText verifykey;
    private Button loginbtn;
    public ImageView imgview;
    public String accout;
    public String password;
    public String key;
    
    HttpCallbackListener listener;
    WhuHttpUtil httputil;
    
    private Handler handler=new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		switch (msg.what) {
			case 0:
				imgview.setImageBitmap((Bitmap) msg.obj);
				break;

			default:
				break;
			}
    	}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        nameedit=(EditText) findViewById(R.id.accout);
        passwordedit=(EditText) findViewById(R.id.password);
        loginbtn=(Button) findViewById(R.id.btn_login);
        verifykey=(EditText)findViewById(R.id.verify_key);
        imgview=(ImageView) findViewById(R.id.login_image);
        httputil=WhuHttpUtil.getInstance();
        new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Bitmap bmp=httputil.getcode();
					Message msg=new Message();
					msg.obj=bmp;
					msg.what=0;
					handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
        
        loginbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				accout=nameedit.getText().toString();
				password=passwordedit.getText().toString();
				key=verifykey.getText().toString();
				httputil.Loginweb("http://210.42.121.241/servlet/Login",accout, password, key,new HttpCallbackListener() {
					
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						Log.d("login", "successfully");
						Intent i=new Intent(MainActivity.this,ScheduleActivity.class);
						startActivity(i);
					}
					
					@Override
					public void onError() {
						// TODO Auto-generated method stub
						Log.d("login", "unsuccessfully");
					}
				});
			}
		});
    }
}
