package com.example.whuassist.schedule;

public class Schedulemodel {
	public String id;
	public String name;
	/*课表展示功能暂时不需要
	public int stype;
	public String school;
	public String teacher;
	public String major;
	public float credit;
	public int totaltime;
	*/
	public String time;
	public Schedulemodel(String id,String name,String time){
		this.id=id;
		this.name=name;
		this.time=time;
	}
}
