package com.example.whuassist.info;

import java.util.List;

import com.example.whuassist.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TitleAdapter extends ArrayAdapter<TitleModel> {
	private int rsrcid;
	private Context mcontext;
	public TitleAdapter(Context context, int resource, List<TitleModel> objects) {
		super(context, resource, objects);
		rsrcid=resource;
		mcontext=context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TitleModel title=getItem(position);
		View v;
		ViewHolder vholder;
		if(convertView==null){
			v=LayoutInflater.from(mcontext).inflate(rsrcid, null);
			vholder=new ViewHolder();
			vholder.titletext=(TextView) v.findViewById(R.id.news_title);
			vholder.datetext=(TextView) v.findViewById(R.id.news_date);
			v.setTag(vholder);
		}else{
			v=convertView;
			vholder=(ViewHolder) v.getTag();
		}
		vholder.titletext.setText(title.title);
		vholder.datetext.setText(title.date);	
		return v;
	}
	
	class ViewHolder{
		TextView titletext;
		TextView datetext;
	}
}
