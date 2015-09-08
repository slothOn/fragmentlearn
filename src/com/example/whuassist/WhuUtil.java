package com.example.whuassist;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.content.Context;
import android.renderscript.Element;

public class WhuUtil {
	public static ArrayList<Scoremodel> courseScore=new ArrayList<Scoremodel>();
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
			course.name=td.get(1).text();
			course.credit=Float.parseFloat(td.get(3).text());
			if(!("".equals(td.get(9).text())))
	        course.score=Float.parseFloat(td.get(9).text());
			else
				break;
	        courseScore.add(course);
		}
	}
	public static boolean isLogin(String html){
		Document doc=Jsoup.parse(html);
		org.jsoup.nodes.Element nameelement=doc.getElementById("nameLable");
		if(nameelement==null)
			return false;
		else
			return true;
	}
}
