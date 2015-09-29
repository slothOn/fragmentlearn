package com.example.whuassist.info;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class HzontalGallery extends Gallery {
	//以下三货有啥区别？？？自定义view待学习啊
	public HzontalGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public HzontalGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public HzontalGallery(Context context,AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	private boolean isScrollingLeft(MotionEvent e1,MotionEvent e2){
		return e1.getX()<e2.getX();
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int keyEvent;
		if(isScrollingLeft(e1, e2)){
			keyEvent=KeyEvent.KEYCODE_DPAD_LEFT;
		}else{
			keyEvent=KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(keyEvent, null);
		return true;
	}


}
