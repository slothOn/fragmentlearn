package com.example.whuassist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class HzontalGalleryAdapter extends BaseAdapter{
	private int[] res;
	private Context context;
	public HzontalGalleryAdapter(int[] res, Context c) {
		// TODO Auto-generated constructor stub
		this.res=res;
		context=c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return res[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView img=new ImageView(context);
		img.setBackgroundResource(res[position%res.length]);
		img.setScaleType(ScaleType.FIT_XY);
		img.setLayoutParams(new Gallery.LayoutParams(
				Gallery.LayoutParams.MATCH_PARENT,Gallery.LayoutParams.WRAP_CONTENT));
		return img;
	}

}
