package com.example.whuassist;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class NewsFragment extends Fragment
{	
	public HzontalGallery myGallery;
	public RadioGroup gallerypoints;
	public HzontalGalleryAdapter gadapter;
	RadioButton[] rbtns;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.layout_imgswitcher, container, false);
		
		myGallery=(HzontalGallery) v.findViewById(R.id.hgallery);
    	gallerypoints=(RadioGroup) v.findViewById(R.id.gallaryrgroup);
    	int[] res={R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
    	gadapter=new HzontalGalleryAdapter(res, getActivity());
    	myGallery.setAdapter(gadapter);
    	//����radiobtn
    	rbtns=new RadioButton[res.length];
    	LinearLayout layout;
    	for(int i=0;i<rbtns.length;i++){
    		layout=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.gallery_icon_layout, null);
    		rbtns[i]=(RadioButton) layout.findViewById(R.id.id_radiobtn);
    		rbtns[i].setId(i);
    		int wh=Tool.dp2px(getActivity(), 10);
    		RadioGroup.LayoutParams params=new RadioGroup.LayoutParams(wh, wh);
    		params.setMargins(4, 0, 4, 0);
    		rbtns[i].setLayoutParams(params);
    		rbtns[i].setClickable(false);
    		layout.removeView(rbtns[i]);
    		gallerypoints.addView(rbtns[i]);
    	}
    	addEvn();
		return v;
	}

	 @Override
	public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	autogallery();
    }
		
  //����¼�
  	void addEvn(){
  		myGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

  			@Override
  			public void onItemSelected(AdapterView<?> arg0, View arg1,
  					int arg2, long arg3) {
  				// TODO Auto-generated method stub
  				gallerypoints.check(rbtns[arg2%rbtns.length].getId());
  			}

  			@Override
  			public void onNothingSelected(AdapterView<?> arg0) {
  				// TODO Auto-generated method stub
  				
  			}
  		});
  	}
    /** չʾͼ��������ʵ��չʾͼ�л� */
	final Handler handler_gallery = new Handler() {
		public void handleMessage(Message msg) {
			/* �Զ�����Ļ���µĶ��� */
			MotionEvent e1 = MotionEvent.obtain(SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
					89.333336f, 265.33334f, 0);
			/* �Զ�����Ļ�ſ��Ķ��� */
			MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
					SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
					300.0f, 238.00003f, 0);
			//���ûŪ���ס�����
			myGallery.onFling(e2, e1, -800, 0);
			/* ��gallery��Ӱ��ºͷſ��Ķ�����ʵ���Զ����� */
			super.handleMessage(msg);
		}
	};
	
	private void autogallery() {
		/* ���ö�ʱ����ÿ5���Զ��л�չʾͼ */
		Timer time = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message m = new Message();
				handler_gallery.sendMessage(m);
			}
		};
		time.schedule(task, 8000, 5000);
	}
}
