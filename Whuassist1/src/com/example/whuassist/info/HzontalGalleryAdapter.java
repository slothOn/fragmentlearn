package com.example.whuassist.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class HzontalGalleryAdapter extends BaseAdapter{
	private Context context;
	public Bitmap[] bmps;
	
	public HzontalGalleryAdapter(Bitmap[] bmps, Context c) {
		// TODO Auto-generated constructor stub
		this.bmps=bmps;
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
		if(bmps!=null){
			return bmps[position];
		}else
			return null;
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
		if(bmps!=null){
			img.setImageBitmap(bmps[position%bmps.length]);	
		}
		img.setScaleType(ScaleType.CENTER_CROP);
		img.setLayoutParams(new Gallery.LayoutParams(
				Gallery.LayoutParams.MATCH_PARENT,Gallery.LayoutParams.WRAP_CONTENT));
		return img;
	}

}
