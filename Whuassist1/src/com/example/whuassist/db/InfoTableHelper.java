package com.example.whuassist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class InfoTableHelper extends SQLiteOpenHelper {
	
	public final static String CREATE_NEWS="create table News ("+
		     "title text primary key, "+"date text, "+"txturl text)";
	public final static String CREATE_NOTFC="create table Notfc ("+
		     "title text primary key, "+"date text, "+"txturl text)";
	public final static String CREATE_CUL="create table Cul ("+
		     "title text primary key, "+"date text, "+"txturl text)";
	public final static String CREATE_SCI="create table Sci ("+
		     "title text primary key, "+"date text, "+"txturl text)";
	public InfoTableHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_NEWS);
		db.execSQL(CREATE_NOTFC);
		db.execSQL(CREATE_CUL);
		db.execSQL(CREATE_SCI);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
