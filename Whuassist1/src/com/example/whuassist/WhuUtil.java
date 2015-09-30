package com.example.whuassist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.example.whuassist.info.TitleModel;
import com.example.whuassist.schedule.Schedulemodel;
import com.example.whuassist.score.Scoremodel;
import com.example.whuassist.setting.AdminModel;

import android.content.Context;
import android.renderscript.Element;
import android.util.Log;

public class WhuUtil {
	//public static String name;
	public static AdminModel admin=new AdminModel();
	public static int weeknum=1;
	public static String[] picurl;
	public static ArrayList<Scoremodel> courseScore=new ArrayList<Scoremodel>();
	public static ArrayList<Schedulemodel> courseSchedule=new ArrayList<Schedulemodel>();
	public static ArrayList<TitleModel> newstitle=new ArrayList<TitleModel>();
	public static ArrayList<TitleModel> notfctitle=new ArrayList<TitleModel>();
	public static ArrayList<TitleModel> cultitle=new ArrayList<TitleModel>();
	public static ArrayList<TitleModel> scititle=new ArrayList<TitleModel>();
	
	public static void saveToFile(Context c,String txt,String name) throws IOException{
		OutputStream out=c.openFileOutput(name, Context.MODE_PRIVATE);
		BufferedWriter bfw=new BufferedWriter(new OutputStreamWriter(out));
		bfw.write(txt);
		if(bfw!=null)
			bfw.close();
	}
	public static void scoreParse(String html){
		
		Document doc=Jsoup.parse(html);
		Elements tables=doc.getElementsByTag("table");
		Elements tr=tables.get(0).getElementsByTag("tr");
		for(int i=1;i<tr.size();i++){
			org.jsoup.nodes.Element e=tr.get(i);
			Scoremodel course=new Scoremodel();
			Elements td=e.getElementsByTag("td");
			course.id=td.get(0).text();
			course.name=td.get(1).text();
			course.credit=Float.parseFloat(td.get(3).text());
			if(!("".equals(td.get(9).text())))
	        course.score=Float.parseFloat(td.get(9).text());
			else
				break;
	        courseScore.add(course);
		}
	}
	/*
	 * 解析课表
	 */
	public static void scheduleParse(String html){
		Document doc=Jsoup.parse(html);
		org.jsoup.nodes.Element lestable=doc.select("table").first();
		Elements lessons=lestable.getElementsByTag("tr");
		for(int i=1;i<lessons.size();i++){
			Elements lestd=lessons.get(i).getElementsByTag("td");
			String id=lestd.get(0).text();
			String name=lestd.get(1).text();
			/*课表展示功能暂时可不用
			String stype=lestd.get(2).text();
			String school=lestd.get(4).text();
			String teacher=lestd.get(5).text();
			String major=lestd.get(6).text();
			float credit=Float.valueOf(lestd.get(7).text());
			int totaltime=Integer.valueOf(lestd.get(8).text());
			*/
			String timetxt=lestd.get(9).select("div").first().text();
			//System.out.println(timetxt);
			if("".equals(timetxt)){
				continue;
			}else{
				courseSchedule.add(parse2Schedulemodel(id, name, timetxt));
			}
			
			
		}
	}
	/*
	 * 专门解析课程信息
	 */
	public static Schedulemodel parse2Schedulemodel(String id ,String name, String timetxt){
		String[] timestr=timetxt.split(" ");
		ArrayList<String> weekday=new ArrayList<String>();
        ArrayList<Integer> weektimestart=new ArrayList<Integer>();
        ArrayList<Integer> weektimeend=new ArrayList<Integer>();
        ArrayList<Integer> daytimestart=new ArrayList<Integer>();
        ArrayList<Integer> daytimeend=new ArrayList<Integer>();
        ArrayList<String> place=new ArrayList<String>();
        
		for(int j=0;j<timestr.length;j=j+2){
			//处理周数
			 String sw[]=timestr[j].split(":");
			 weekday.add(sw[0]);
			 String[] swk=sw[1].split(",");
			 String[] swkse=swk[0].split("-");
			 int wstart=Integer.valueOf(swkse[0]);
			 int wend=Integer.valueOf(swkse[1].substring(0, swkse[1].length()-1));
			 weektimestart.add(wstart);weektimeend.add(wend);
			 //处理节数和地点
			 String sd[]=timestr[j+1].split(",");
			 String sdy[]=sd[0].split("-");
			 int dstart=Integer.valueOf(sdy[0]);
			 int dend=Integer.valueOf(sdy[1].substring(0,sdy[1].length()-1));
			 String splace=sd[1]+","+sd[2];
			 place.add(splace);
			 daytimestart.add(dstart);
			 daytimeend.add(dend);
		}
		return new Schedulemodel(id, name, weektimestart,weektimeend,weekday,daytimestart,daytimeend,place, timetxt);
	}
	
	public static boolean isLogin(String html){
		Document doc=Jsoup.parse(html);
		org.jsoup.nodes.Element nameelement=doc.getElementById("nameLable");
		org.jsoup.nodes.Element weeknumele=doc.getElementById("showOrHide");
		if(nameelement==null)
			return false;
		else{
			admin.name=nameelement.text();
			String weeknumtxt=weeknumele.text();
			int a=weeknumtxt.indexOf("教");
			weeknum=Integer.valueOf(weeknumtxt.substring(1, a));
			return true;	
		}
	}
	
	public static boolean requestIndexpage(){
		HttpURLConnection conn=null;
		URL url;
		try {
			url = new URL("http://sres.whu.edu.cn/");
			conn=(HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(8000);
			conn.setReadTimeout(8000);
			InputStream in=conn.getInputStream();
			BufferedReader reader=new BufferedReader(new InputStreamReader(in,"gbk"));
			StringBuilder sb=new StringBuilder();
			String line;
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
			WhuUtil.parseUrl(sb.toString());
			for(int i=0;i<picurl.length;i++){
				picurl[i]="http://sres.whu.edu.cn/"+picurl[i];
			}
			
			return true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if(conn!=null){
				conn.disconnect();
			}
		}
	}
	
	public static void parseUrl(String html){

		Document doc=Jsoup.parse(html);
		Elements jss=doc.getElementsByTag("script");
		//解析图片
		org.jsoup.nodes.Element js=jss.get(5);
		Pattern mpatrn=Pattern.compile("uploadfile/ImgFile/\\d{4}-\\d{2}/Img\\d+.jpg");
		Matcher matcher=mpatrn.matcher(js.toString());
	    ArrayList<String> as=new ArrayList<String>();
		while(matcher.find()){
	    	as.add(matcher.group());
	    }
		Log.d("picurl", ""+as.size());
		picurl=new String[as.size()];
		for(int i=0;i<as.size();i++){
			picurl[i]=as.get(i);
		}
		//解析news
		newstitle.clear();
		org.jsoup.nodes.Element newsele=doc.getElementsByTag("table").get(18).getElementsByTag("tbody").get(0);
		Elements newstable=newsele.getElementsByTag("table");
		for(org.jsoup.nodes.Element enews:newstable){
			String t=enews.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("title");
			String h="http://sres.whu.edu.cn/"+enews.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("href");
			String d=enews.getElementsByTag("td").get(2).text();
			TitleModel ntm=new TitleModel(t,d,h);
			newstitle.add(ntm);
		}
		//解析notification
		notfctitle.clear();
		org.jsoup.nodes.Element notfcele=doc.getElementsByTag("table").get(38).getElementsByTag("tbody").get(0);
		Elements notfctable=notfcele.getElementsByTag("table");
		for(org.jsoup.nodes.Element enotfc:notfctable){
			String t=enotfc.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("title");
			String h="http://sres.whu.edu.cn/"+enotfc.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("href");
			String d=enotfc.getElementsByTag("td").get(2).text();
			TitleModel ntm=new TitleModel(t,d,h);
			notfctitle.add(ntm);
		}
		//解析cultivation
		cultitle.clear();
		org.jsoup.nodes.Element culele=doc.getElementsByTag("table").get(8).getElementsByTag("tbody").get(0);
		Elements cultable=culele.getElementsByTag("table");
		for(org.jsoup.nodes.Element ecul:cultable){
			String t=ecul.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("title");
			String h="http://sres.whu.edu.cn/"+ecul.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("href");
			String d=ecul.getElementsByTag("td").get(2).text();
			TitleModel ntm=new TitleModel(t,d,h);
			cultitle.add(ntm);
		}
		//解析science
		scititle.clear();
		org.jsoup.nodes.Element sciele=doc.getElementsByTag("table").get(62).getElementsByTag("tbody").get(0);
		Elements scitable=sciele.getElementsByTag("table");
		for(org.jsoup.nodes.Element esci:scitable){
			String t=esci.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("title");
			String h="http://sres.whu.edu.cn/"+esci.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attributes().get("href");
			String d=esci.getElementsByTag("td").get(2).text();
			TitleModel ntm=new TitleModel(t,d,h);
			scititle.add(ntm);
		}
		
	}
	
	public static String parseNewsDetail(String html){
		Document doc=Jsoup.parse(html);
		org.jsoup.nodes.Element head=doc.getElementsByTag("head").get(0);
		org.jsoup.nodes.Element ctnt=doc.getElementsByTag("table").get(9);
		Elements as=ctnt.getElementsByTag("a");
		Elements imgs=ctnt.getElementsByTag("img");
		for(org.jsoup.nodes.Element img:imgs){
			String src=img.attr("src");
			img.attr("src", "http://sres.whu.edu.cn/"+src);
			img.attr("style","width:100%;height:auto;margin:0 auto;display:block;");
		}
		for(org.jsoup.nodes.Element a:as){
			String href=a.attr("href");
			a.attr("href", "http://sres.whu.edu.cn/"+href);
		}
		String ctntstr=ctnt.toString();
		ctntstr="<html>"+head.toString()+"<body>"+ctntstr+"</body>"+"</html>";
		return ctntstr;
	}
	
	
}
