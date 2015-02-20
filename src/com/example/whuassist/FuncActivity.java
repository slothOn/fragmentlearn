package com.example.whuassist;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class FuncActivity extends Activity implements OnClickListener{
	 private LinearLayout weixin;
	 private LinearLayout frd;
	 private LinearLayout addr;
	 private LinearLayout setting;
	 private ImageButton weixinimg;
	 private ImageButton frdimg;
	 private ImageButton addrimg;
	 private ImageButton settingimg;
	 private Fragment weixinfrg;
	 private Fragment frdfrg;
	 private Fragment addrfrg;
	 private Fragment settingfrg;
	 FragmentManager manager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.frag_activity);
		initview();
		initevent();
	}
	public void initview(){
		weixin=(LinearLayout) findViewById(R.id.id_tabweixin);
		frd=(LinearLayout) findViewById(R.id.id_tabfrd);
		addr=(LinearLayout) findViewById(R.id.id_tabaddress);
		setting=(LinearLayout) findViewById(R.id.id_tabsettings);
		weixinimg=(ImageButton) findViewById(R.id.id_tabweixinimg);
		frdimg=(ImageButton) findViewById(R.id.id_tabfrdimg);
		addrimg=(ImageButton) findViewById(R.id.id_tabaddressimg);
		settingimg=(ImageButton) findViewById(R.id.id_tabsettingsimg);
		
		weixinfrg=new NewsFragment();
		frdfrg=new ScoreFragment();
		addrfrg=new WeixinFragment();
		settingfrg=new WeixinFragment();
		
		manager=getFragmentManager();
		FragmentTransaction transaction=manager.beginTransaction();
		transaction.replace(R.id.id_fraglayout, weixinfrg);
		transaction.commit();
	}
	public void initevent(){
		weixin.setOnClickListener(this);
		frd.setOnClickListener(this);
		addr.setOnClickListener(this);
		setting.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		resetimg();
		FragmentTransaction tr=manager.beginTransaction();
		switch (arg0.getId()) {
		case R.id.id_tabweixin:
			tr.replace(R.id.id_fraglayout, weixinfrg);
			weixinimg.setImageResource(R.drawable.tab_weixin_pressed);
			break;
        case R.id.id_tabfrd:
			tr.replace(R.id.id_fraglayout, frdfrg);
			frdimg.setImageResource(R.drawable.tab_find_frd_pressed);
			break;
        case R.id.id_tabaddress:
			tr.replace(R.id.id_fraglayout, addrfrg);
			addrimg.setImageResource(R.drawable.tab_address_pressed);
			break;
        case R.id.id_tabsettings:
			tr.replace(R.id.id_fraglayout, settingfrg);
			settingimg.setImageResource(R.drawable.tab_settings_pressed);
			break;
		default:
			break;
		}
		tr.commit();
	}
	private void resetimg() {
		// TODO Auto-generated method stub
		weixinimg.setImageResource(R.drawable.tab_weixin_normal);
		frdimg.setImageResource(R.drawable.tab_find_frd_normal);
		addrimg.setImageResource(R.drawable.tab_address_normal);
		settingimg.setImageResource(R.drawable.tab_settings_normal);
	}
}
