package com.example.whuassist.setting;

import com.example.whuassist.R;
import com.example.whuassist.R.layout;
import com.example.whuassist.WhuUtil;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends Fragment {
	TextView nametxt;
	TextView stunumtxt;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.setting_fragment, container, false);
		nametxt=(TextView) v.findViewById(R.id.id_adminname);
		stunumtxt=(TextView) v.findViewById(R.id.id_adminnum);
		nametxt.setText(WhuUtil.admin.name);
		stunumtxt.setText(WhuUtil.admin.stunum);
		return v;
	}
}
