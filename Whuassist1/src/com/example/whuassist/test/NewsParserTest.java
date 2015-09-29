package com.example.whuassist.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsParserTest {
	public static void main(String[] args){
		File f=new File("E:\\³ÌÐò2\\android_work1\\indexzihuan.txt");
		StringBuilder sb=new StringBuilder();
		String line;
		try{
			FileInputStream input=new FileInputStream(f);
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			while((line=reader.readLine())!=null){
				sb.append(line);
			}
			
			Document doc=Jsoup.parse(sb.toString());
			Elements tables=doc.getElementsByTag("table");
			for(Element e:tables){
				System.out.println(e.text());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
