package com.lauruss.wedit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity{
	TextView tvMonth,tvDays;
	ListView lvTask;
	CATaskList adapter;
	//ImageView ivComingSoon;

	public static int weddingId = 0,userIdWedit=0;
	ProgressDialog pd;
	DateFormat formatterResponseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	DateFormat formatterTaskDate = new SimpleDateFormat("yyyy-MM-dd");
	//Date weddingDate , currentDate;
	DateTime startDate,endDate;
	String strenddate,strstartdate;
	Date currentDate,taskDate;
	Calendar currentCal,weddingCal;
	boolean SET_WEDDING_DATE = true;
	boolean COMINGSOON_CLICKED = false;
	static boolean ACTIVITY_FLAG_HOME=false;
	SharedPreferences date;
	ImageView iv_home;
	int datecnt;
	RelativeLayout relative_home;

	static final int START_DATE_DIALOG_ID = 1;

	String weddingDate="";
	Calendar currentC , calendarCurrent;
	private int year;
	private int month;
	private int day;

	private int cYear;
	private int cMonth;
	private int cDay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		//	TabActivityMy.tabHost.setCurrentTab(2);
		iv_home = (ImageView)findViewById(R.id.image_home);
		date=getSharedPreferences("date",0);
		datecnt = date.getInt("wedDate", 0);
		
		currentC = Calendar.getInstance();

		calendarCurrent = Calendar.getInstance();
		cYear = calendarCurrent.get(Calendar.YEAR);
		cMonth = calendarCurrent.get(Calendar.MONTH);
		cDay = calendarCurrent.get(Calendar.DAY_OF_MONTH);

		if(LoginActivity.FLAG_HOME==true)
		{
			iv_home.setVisibility(View.INVISIBLE);
		}
		else
		{
			iv_home.setVisibility(View.VISIBLE);
		}
		//imbtndob.setOnClickListener(dateToDoListner);
		// signup_tv_datetodo.setOnClickListener(dateToDoListner);
		iv_home.setOnClickListener(weddingDateClickListner);
		
		SharedPreferences pref1 = MyApplication.getSharedPreference();
		  long dateMillis = pref1.getLong(MyApplication.FLAG_WEDDING_DATE, 0);
		
		TabActivityMy.FLAG_TAB=false;
		 //LoginActivity.FLAG_HOME=false;
		 relative_home = (RelativeLayout)findViewById(R.id.home_iv_top_banner);

		 tvMonth = (TextView)findViewById(R.id.home_tv_top_month);
		 tvDays = (TextView)findViewById(R.id.home_tv_top_days);

		 lvTask = (ListView)findViewById(R.id.home_lv);
		 //	ivComingSoon = (ImageView)findViewById(R.id.home_iv_comingsoon);
		 //ivComingSoon.setVisibility(8);
		 /*if(LoginActivity.FLAG_HOME==true)
		{
			ivComingSoon.setVisibility(0);
		}
		else
		{
			ivComingSoon.setVisibility(8);
		}*/

		 SharedPreferences pref = MyApplication.getSharedPreference();
		 weddingId = pref.getInt(SignUpActivity.FLAG_WEDDING_ID, 0);
		 userIdWedit = pref.getInt(SignUpActivity.FLAG_USER_ID, 0);

		 try {
			 currentCal = Calendar.getInstance();
			 currentDate = currentCal.getTime();
			 startDate = new DateTime(currentCal.get(Calendar.YEAR), (currentCal.get(Calendar.MONTH)+1), currentCal.get(Calendar.DAY_OF_MONTH), currentCal.get(Calendar.HOUR), currentCal.get(Calendar.MINUTE));

		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 formatterResponseDate.setTimeZone(TimeZone.getTimeZone("GMT"));

		 lvTask.setOnItemClickListener(new OnItemClickListener() {

			 @Override
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					 long arg3) {
				 ACTIVITY_FLAG_HOME=true;
				 TaskDataClass taskDate = (TaskDataClass)arg0.getAdapter().getItem(arg2);
				 if(taskDate.getItemType() == 0){
					 if(taskDate.getWeditPreTaskId() == 83){
						 Intent in = new Intent(HomeActivity.this,UpdatePersonalInformationActivity.class);
						 startActivity(in);
					 }else{
						 Intent in = new Intent(HomeActivity.this,TaskViewActivity.class);
						 Bundle bn = new Bundle();
						 bn.putInt(TaskViewActivity.FLAG_TASK_ID, taskDate.getTaskId());
						 in.putExtras(bn);
						 startActivity(in);
					 }

				 }

			 }
		 });
		 /*ivComingSoon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				COMINGSOON_CLICKED = true;
				ivComingSoon.setVisibility(View.GONE);
				if(isNetworkAvailable()){
					GetWeddingDate exeD = new GetWeddingDate();
					exeD.execute();

				}else {
					Toast.makeText(HomeActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
				}
			}
		});*/


	}
	OnClickListener weddingDateClickListner = new OnClickListener() {
		  
		  @Override
		  public void onClick(View v) {
		   showDialog(START_DATE_DIALOG_ID);
		  
		  }
		 };
		 private DatePickerDialog.OnDateSetListener weddingDateListener = new DatePickerDialog.OnDateSetListener()
		 {

		  public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

		   if(D2MS(cMonth, cDay, cYear) < D2MS(selectedMonth, selectedDay, selectedYear)){
		    currentC.set(selectedYear, selectedMonth, selectedDay);
		    weddingDate = formatterTaskDate.format(currentC.getTime());
		//    tvWeddingDate.setText(weddingDate+"-"+getString(R.string.updatepersonal_dateofwedding));
		    Log.e("date", weddingDate);
		   }else{
		    Toast.makeText(HomeActivity.this, "wedding date can not be in past.", Toast.LENGTH_SHORT).show();
		    //Log.e("In end date lisner", "In else");
		   }
		  }
		 };
		 public long D2MS(int month, int day, int year) { 
			  Calendar c = Calendar.getInstance();
			  c.clear();
			  c.set(year, month, day,00,00,00);

			  return c.getTimeInMillis();  
			 } 
			 @Override
			 protected Dialog onCreateDialog(int id) {
			  return new DatePickerDialog(this, weddingDateListener, 
			    year, month,day);
			 }
	@Override
	protected void onResume() {
		//	if(COMINGSOON_CLICKED){
		/*if(LoginActivity.FLAG_HOME==true)
		{
			ivComingSoon.setVisibility(0);
		}
		else
		{
			ivComingSoon.setVisibility(8);
		}*/
		//ivComingSoon.setVisibility(0);
		if(CATaskList.ACTIVITY_FLAG_TASKACTIVITY==true)
		{
			if(CATaskList.aldeletetask.size()>0)
			{ 
				for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
				{
					DeleteTask delexe = new DeleteTask(HomeActivity.this);
					delexe.execute(taskdeleteobj.getTaskId());
				}
			}

		}

		if(isNetworkAvailable()){
			if(LoginActivity.FLAG_HOME==true){

				GetWeddingDate exeD = new GetWeddingDate();
				exeD.execute();}
			else
			{

				GetWeddingDate exeD = new GetWeddingDate();
				exeD.execute();
			}


			/*if(SET_WEDDING_DATE){

				}else{
					GetAllTask getTask = new GetAllTask();
					getTask.execute();
				}*/
		}else {
			Toast.makeText(HomeActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
			//}
		}

		super.onResume();
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	class GetAllTask extends AsyncTask<Void, Void, ArrayList<TaskDataClass>>{

		@Override
		protected void onPreExecute() {
			if(pd.isShowing() == false){
				pd.show();
			}
			super.onPreExecute();
		}
		@Override
		protected ArrayList<TaskDataClass> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=tasksbycolor");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(weddingId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONArray jArray = new JSONArray(responseThis);

				ArrayList<TaskDataClass> tData = new ArrayList<TaskDataClass>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONArray jArrData = jArray.getJSONArray(i);
					int jArrLen = jArrData.length();
					for(int j = 0; j< jArrLen; j++){
						JSONObject jObj = jArrData.getJSONObject(j);

						boolean taskDone = jObj.getBoolean("taskdone");
						if(taskDone == false){
							TaskDataClass tObj = new TaskDataClass();
							String startDate = jObj.getString("startdate");
							String endDate = jObj.getString("enddate");
							Date thisEndDate = formatterTaskDate.parse(endDate);
							Date thisStartDate = formatterTaskDate.parse(startDate);

							if(thisStartDate.after(currentDate)){
								tObj.setStatusColor(TaskDataClass.FLAG_GREEN_COLOR);

							}else if(thisEndDate.before(currentDate)){
								tObj.setStatusColor(TaskDataClass.FLAG_RED_COLOR);
							}else{
								tObj.setStatusColor(TaskDataClass.FLAG_ORANGE_COLOR);
							}

							tObj.setTaskId(jObj.getInt("id"));
							tObj.setTaskName(jObj.getString("name"));
							tObj.setWeddingId(jObj.getInt("weddingid"));
							tObj.setWeditPreTaskId(jObj.getInt("weditpretaskid"));
							tObj.setTaskComments(jObj.getString("comments"));
							tObj.setTaskEndDate(endDate);
							tObj.setTaskResponsible(jObj.getString("responsible"));
							tObj.setTaskStartDate(startDate);
							tObj.setCategoryId(jObj.getInt("categoryid"));
							tObj.setDaysToComplete(jObj.getInt("daystocomplete"));
							tObj.setTaskDone(taskDone);
							tData.add(tObj);
						}

					}
				}
				return tData;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<TaskDataClass> result) {
			if(result != null){
				adapter = new CATaskList(HomeActivity.this, R.layout.row_task_home, result,true);
				lvTask.setAdapter(adapter);
			}else{
				Toast.makeText(HomeActivity.this, "No internet or error occured", Toast.LENGTH_SHORT).show();
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	/*public void deleteTask(int taskId){
		DeleteTask exe = new DeleteTask();
		exe.execute(taskId);
	}*/

	class GetWeddingDate extends AsyncTask<Void, Void, Integer>{

		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(HomeActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=getweddingdate");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(weddingId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				responseThis = responseThis.replace("\"", "").trim();
				Log.e("API response", responseThis.trim());

				weddingCal = Calendar.getInstance();
				weddingCal.clear();
				weddingCal.setTime(formatterResponseDate.parse(responseThis.trim()));
				endDate = new DateTime(weddingCal.get(Calendar.YEAR), (weddingCal.get(Calendar.MONTH)+1), weddingCal.get(Calendar.DAY_OF_MONTH), weddingCal.get(Calendar.HOUR), weddingCal.get(Calendar.MINUTE));

				return 0;
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}

		}
		@Override
		protected void onPostExecute(Integer result) {

			if(result == 1){
				Toast.makeText(HomeActivity.this, "No internet or error occured", Toast.LENGTH_SHORT).show();
			}else{
				LocalDate localStart = new LocalDate(currentCal.get(Calendar.YEAR), (currentCal.get(Calendar.MONTH)+1), currentCal.get(Calendar.DAY_OF_MONTH));
				LocalDate localEnd = new LocalDate(weddingCal.get(Calendar.YEAR), (weddingCal.get(Calendar.MONTH)+1), weddingCal.get(Calendar.DAY_OF_MONTH));
				//Period period = new Period(startDate, endDate , PeriodType.yearMonthDayTime());
				Period period = new Period(localStart, localEnd , PeriodType.yearMonthDay());
				int year = period.getYears();
				int month = period.getMonths();
				if(year>0)
					month = month + (year * 12);

				tvMonth.setText(String.valueOf(month));
				tvDays.setText(String.valueOf(period.getDays()));
				SET_WEDDING_DATE = false;
				if(datecnt==0)
				{
					iv_home.setVisibility(View.VISIBLE);
				}
				else
				{
					iv_home.setVisibility(View.INVISIBLE);
				}
			}
			GetAllTask getTask = new GetAllTask();
			getTask.execute();
			super.onPostExecute(result);
		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//finish();
	}

	
	class SetWeddingDate extends AsyncTask<Void, Void, Boolean>{

		String message = "";
		@Override
		protected void onPreExecute() {
			pd = ProgressDialog.show(HomeActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=setweddingdate");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(weddingId)));
				nameValuePairs.add(new BasicNameValuePair("weddingdate", String.valueOf(weddingDate)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				responseThis = responseThis.replace("\"", "").trim();
				Log.e("API response", responseThis.trim());

				JSONObject jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				message = jObj.getString("message");
				if(status==true)
				{
					date=getSharedPreferences("date", 0);
					SharedPreferences.Editor editor=date.edit();
					editor.putInt("wedDate",1);
					//     editor.putString("fname",jObjBride.getString("fname"));
					editor.commit();

					return status;
				}

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}
		@Override
		protected void onPostExecute(Boolean result) {

			if(result == false){
				Toast.makeText(HomeActivity.this, "No internet or error occured", Toast.LENGTH_SHORT).show();
			}else{
				GetWeddingDate getdate = new GetWeddingDate();
				getdate.execute();
			}
			/*GetAllTask getTask = new GetAllTask();
					getTask.execute();*/
			super.onPostExecute(result);
		}

	}

}
