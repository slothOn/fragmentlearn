package com.example.whuassist.schedule;

import java.util.List;

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
	public Integer[] weektimestart;
	public Integer[] weektimeend;
	public String[] weekday;
	//起始课程节数
	public Integer[] daytimestart;
	//结束课程节数
	public Integer[] daytimeend;
	public String[] place;
	public String timetxt;
	public Schedulemodel(String id,String name,List<Integer> weektimestart,List<Integer> weektimeend,List<String> weekday,
			List<Integer> daytimestart, List<Integer> daytimeend, List<String> place,String timetxt){
		this.id=id;
		this.name=name;
		this.weekday=(String[])weekday.toArray(new String[0]);
		this.weektimestart=(Integer[])weektimestart.toArray(new Integer[0]);
		this.weektimeend=(Integer[])weektimeend.toArray(new Integer[0]);
		this.daytimestart=(Integer[])daytimestart.toArray(new Integer[0]);
		this.daytimeend=(Integer[])daytimeend.toArray(new Integer[0]);
		this.place=(String[])place.toArray(new String[0]);
		this.timetxt=timetxt;
	}
	
}
