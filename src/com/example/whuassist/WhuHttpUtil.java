package com.example.whuassist;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WhuHttpUtil {
	private WhuHttpUtil(){
		client=new DefaultHttpClient();
	}
	private static WhuHttpUtil whuhttputil;
	public static WhuHttpUtil getInstance(){
		if(whuhttputil==null)
			whuhttputil=new WhuHttpUtil();
		return whuhttputil;
	}
	static HttpClient client;
	static CookieStore cookies;
	public static String COOKIE="";
	public Bitmap getcode() throws Exception{
		HttpGet httpGet = new HttpGet("http://210.42.121.241/servlet/GenImg");
		HttpResponse httpResponse = client.execute(httpGet);
	
		COOKIE = ((AbstractHttpClient) client).getCookieStore().getCookies().get(0).getValue();
		byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity()); 
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
		return bitmap; 
	}
	public void Loginweb(final String address,final String id,final String pwd,final String xdvfb,final HttpCallbackListener listener){
//		final String address="http://210.42.121.241/servlet/Login";
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//String responsetxt;
				HttpPost post=new HttpPost(address);
				List<NameValuePair> params=new ArrayList<NameValuePair>();
				
				params.add(new BasicNameValuePair("id", id));
				params.add(new BasicNameValuePair("pwd", pwd));
				params.add(new BasicNameValuePair("xdvfb", xdvfb));
				try {
					HttpResponse response;
					UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"utf-8");
					post.setEntity(entity);
					post.setHeader("Cookie", "JESSIONID="+COOKIE);
					Log.d("whuhttputil", COOKIE);
					response=client.execute(post);
					if(response.getStatusLine().getStatusCode()==200){
//						HttpEntity entityback=response.getEntity();
//						responsetxt=EntityUtils.toString(entityback,"utf-8");
//						Log.d("whu", responsetxt);
						StringBuffer sb = new StringBuffer();
                        HttpEntity entityback = response.getEntity();
                        InputStream is = entityback.getContent();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
                        String data = "";
                        while ((data = br.readLine()) != null) {
                                sb.append(data);
                        }
                        Log.d("whu", sb.toString());
                        // 此时result中就是登陆后的页面的HTML的源代码
					}
					listener.onFinish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listener.onError();
				}
			}
		}).start();
	}
}
