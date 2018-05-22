package com.lauruss.wedit;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class MyApplication extends Application{

	public static String BASE_API_URL ="http://wedit.dooble.mobi/api/";
	
	public static String FLAG_WEDDING_DATE = "wedding_date";
	//private static DatabaseHelper db;
	private static SharedPreferences pref;
	@Override
	public void onCreate() {
		Log.v("In MyApplication", "On create");
		/*if(db==null){
			db=new DatabaseHelper(getApplicationContext());
		}else if(db.isOpen()==false){
			db=new DatabaseHelper(getApplicationContext());
		}*/
		pref =  getApplicationContext().getSharedPreferences("Login", MODE_WORLD_WRITEABLE);
		super.onCreate();
	}
	public static SharedPreferences getSharedPreference(){
		Log.v("In MyApplication", "getDatabase()");
		return pref;
	}
	/*public static DatabaseHelper getDataBase(){
		Log.v("In MyApplication", "getDatabase()");
		return db;
	}
	
	
	public static void closeDataBase(){
		Log.v("In MyApplication", "in close db");
		db.closeDB();
	}*/
}
