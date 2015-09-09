package com.example.whuassist;

public class Scoremodel {
	//private String id;
	public static int COURSE_GONGBI=0;
	public static int COURSE_ZHUANBI=1;
	public static int COURSE_ZHUANXUAN=2;
	public static int COURSE_GONGXUAN=3;
	public String id;
	public String name;
	public int type;
	public float credit=0;
	//private String tchName;
	//private String schName;
	//private String note;
	//private String year;
	//private String term;
	public float score=0;
	public Scoremodel() {
		// TODO Auto-generated constructor stub
	}
	public Scoremodel(String id,String name,float credit,float score) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.name=name;
		this.credit=credit;
		this.score=score;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name+" "+String.valueOf(credit)+"  "+String.valueOf(score);
	}
	public float getGPA(){
		float result=0;
		if(score>=90)
			result=(float) 4.0;
		else if(score>=85)
			result=(float) 3.7;
		else if(score>=82)
			result=(float) 3.3;
		else if(score>=78)
			result=(float) 3.0;
		else if(score>=75)
			result=(float) 2.7;
		else if(score>=72)
			result=(float) 2.3;
		else if(score>=68)
			result=(float) 2.0;
		else if(score>=64)
			result=(float) 1.5;
		else if(score>=60)
			result=(float) 1.0;
		return result;
	}
}
