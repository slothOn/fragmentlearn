package com.example.whuassist.info;

public class TitleModel {
	public String title;
	public String date;
	public String txturl;
	public TitleModel() {
		// TODO Auto-generated constructor stub	
	}
	public TitleModel(String title,String date,String txturl) {
		// TODO Auto-generated constructor stub
		this.title=title;
		this.date=date;
		this.txturl=txturl;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title+"  "+date;
	}
}
