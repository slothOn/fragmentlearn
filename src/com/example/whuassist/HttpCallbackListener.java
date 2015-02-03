package com.example.whuassist;

public interface HttpCallbackListener {
	public void onFinish(String txt);
	public void onError(String txt);
}
