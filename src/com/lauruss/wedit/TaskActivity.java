package com.lauruss.wedit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskActivity extends Activity{

	Button btnAddTask,btnAllTask,btnTaskToDo,btnTaskByCategory,btnback,btnhome;
	TextView tvTaskHeader;
	ListView lvTaskList;
	//	boolean ACTIVITY_FLAG_TASKACTIVITY = false;

	ArrayList<TaskDataClass> allTaskData;
	ArrayList<TaskDataClass> taskToDoData;
	ArrayList<TaskDataClass> taskByCatData;

	//CATaskListSingle adapterAllTask;
	//CATaskListSingle adapterTaskToDo;
	CASectionedTask adapterTaskByCat;
	CAAllTask adapterAllTask;
	CATaskList adapterTaskToDo;

	ProgressDialog pd;
	Intent intent;

	DateFormat formatterTaskDate = new SimpleDateFormat("yyyy-MM-dd");
	//DateFormat formatterTaskDate = new SimpleDateFormat("dd-MM-yyyy").format();
	Date currentDate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		//LoginActivity.FLAG_HOME=false;
		
		btnback=(Button)findViewById(R.id.task_btn_btnback);
		btnhome=(Button)findViewById(R.id.task_btn_btnhome);
		btnAddTask = (Button)findViewById(R.id.task_btn_add);
		btnAllTask = (Button)findViewById(R.id.task_btn_all);
		btnTaskByCategory = (Button)findViewById(R.id.task_btn_categories);
		btnTaskToDo = (Button)findViewById(R.id.task_btn_tasktodo);
		lvTaskList = (ListView)findViewById(R.id.task_lv);

		Calendar currentCal = Calendar.getInstance();
		currentDate = currentCal.getTime();

		btnAllTask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(CATaskList.aldeletetask.size()>0)
				  {
				   for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
				   {
				    DeleteTask delexe = new DeleteTask(TaskActivity.this);
				    delexe.execute(taskdeleteobj.getTaskId());
				   }
				  }
				btnAllTask.setBackgroundResource(R.drawable.task_btn_3_sel);
				btnTaskToDo.setBackgroundResource(R.drawable.task_btn_2);
				btnTaskByCategory.setBackgroundResource(R.drawable.task_btn_1);

				/*if(allTaskData == null || allTaskData.isEmpty()){
					if(isNetworkAvailable()){
						GetAllTask exe = new GetAllTask();
						exe.execute();
					}else{
						Toast.makeText(TaskActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
					}
				}else{
					adapterAllTask = new CAAllTask(TaskActivity.this, R.layout.row_task_home, allTaskData,false);
					lvTaskList.setAdapter(adapterAllTask);
				}*/
				if(isNetworkAvailable()){
					GetAllTask exe = new GetAllTask();
					exe.execute();
				}else{
					Toast.makeText(TaskActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
				}

			}
		});
		btnback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent=new Intent(TaskActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});

		btnhome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent=new Intent(TaskActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		btnTaskByCategory.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(CATaskList.aldeletetask.size()>0)
				  {
				   for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
				   {
				    DeleteTask delexe = new DeleteTask(TaskActivity.this);
				    delexe.execute(taskdeleteobj.getTaskId());
				   }
				  }
				btnAllTask.setBackgroundResource(R.drawable.task_btn_3);
				btnTaskToDo.setBackgroundResource(R.drawable.task_btn_2);
				btnTaskByCategory.setBackgroundResource(R.drawable.task_btn_1_sel);

				//adapterTaskByCat = new CASectionedTask(TaskActivity.this, R.layout.row_section_header, prepareItems());
				//lvTaskList.setAdapter(adapterTaskByCat);

				/*if(taskByCatData == null || taskByCatData.isEmpty()){
					if(isNetworkAvailable()){
						GetCategorywiseTask exe = new GetCategorywiseTask();
						exe.execute();
					}else {
						Toast.makeText(TaskActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
					}
				}else{
					adapterTaskByCat = new CASectionedTask(TaskActivity.this, R.layout.row_section_header, taskByCatData);
					lvTaskList.setAdapter(adapterTaskByCat);
				}*/
				if(isNetworkAvailable()){
					GetCategorywiseTask exe = new GetCategorywiseTask();
					exe.execute();
				}else {
					Toast.makeText(TaskActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btnTaskToDo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnAllTask.setBackgroundResource(R.drawable.task_btn_3);
				btnTaskToDo.setBackgroundResource(R.drawable.task_btn_2_sel);
				btnTaskByCategory.setBackgroundResource(R.drawable.task_btn_1);
				/*if(taskToDoData == null || taskToDoData.isEmpty()){
					if(isNetworkAvailable()){
						GetTaskToDo exe = new GetTaskToDo();
						exe.execute();
					}else {
						Toast.makeText(TaskActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
					}
				}else{
					adapterTaskToDo = new CATaskList(TaskActivity.this,R.layout.row_task_home, taskToDoData,false);
					lvTaskList.setAdapter(adapterTaskToDo);
				}*/
				if(isNetworkAvailable()){
					GetTaskToDo exe = new GetTaskToDo();
					exe.execute();
				}else {
					Toast.makeText(TaskActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
				}

			}
		});
		btnAddTask.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//		Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
				Bundle bn = new Bundle();
				bn.putBoolean(TaskViewActivity.FLAG_TASK_EDIT, false);
				Intent in = new Intent(TaskActivity.this,AddNewTaskActivity.class);

				in.putExtras(bn);
				startActivity(in);

			}
		});
		lvTaskList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HomeActivity.ACTIVITY_FLAG_HOME=false;
				//	AddNewTaskActivity.ACTIVITY_FLAG=true;
				TaskDataClass taskDate = (TaskDataClass)arg0.getAdapter().getItem(arg2);
				if(taskDate.getItemType() == 0){
					if(taskDate.getWeditPreTaskId() == 83){
						Intent in = new Intent(TaskActivity.this,UpdatePersonalInformationActivity.class);
						startActivity(in);
					}else{
						Intent in = new Intent(TaskActivity.this,TaskViewActivity.class);
						Bundle bn = new Bundle();
						bn.putInt(TaskViewActivity.FLAG_TASK_ID, taskDate.getTaskId());
						in.putExtras(bn);
						startActivity(in);
					}
				}
			}
		});

	}
	@Override
	protected void onResume() {
		if(CATaskList.aldeletetask.size()>0)
		{ 
			for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
			{
				DeleteTask delexe = new DeleteTask(TaskActivity.this);
				delexe.execute(taskdeleteobj.getTaskId());
			}
		}
		if(isNetworkAvailable()){
			allTaskData = null;
			taskToDoData = null;
			taskByCatData = null;
			btnAllTask.setBackgroundResource(R.drawable.task_btn_3_sel);
			btnTaskToDo.setBackgroundResource(R.drawable.task_btn_2);
			btnTaskByCategory.setBackgroundResource(R.drawable.task_btn_1);
			GetAllTask exe = new GetAllTask();
			exe.execute();
			//GetTaskToDo exe = new GetTaskToDo();
			//exe.execute();
		}else {
			Toast.makeText(TaskActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
		}
		super.onResume();
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	class GetCategorywiseTask extends AsyncTask<Void, Void, ArrayList<TaskDataClass>>{

		@Override
		protected void onPreExecute() {
			//	pd = ProgressDialog.show(TaskActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(TaskActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<TaskDataClass> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=taskscategoriesbycolor");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONArray jArray = new JSONArray(responseThis);

				ArrayList<TaskDataClass> tData = new ArrayList<TaskDataClass>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObjMain = jArray.getJSONObject(i);
					TaskDataClass HDate = new TaskDataClass();
					HDate.setType(TaskDataClass.SECTION);
					HDate.setTaskName(jObjMain.getString("wedittaskcategory_name"));
					HDate.setWeddingId(jObjMain.getInt("wedittaskcategory_id"));
					tData.add(HDate);
					JSONArray jArrTask = jObjMain.getJSONArray("tasks");
					int tLen = jArrTask.length();
					for(int j=0;j<tLen;j++){
						JSONArray jTaskArr = jArrTask.getJSONArray(j);
						int lenTask = jTaskArr.length();
						for(int k = 0; k<lenTask;k++){
							JSONObject jObj = jTaskArr.getJSONObject(k);


							TaskDataClass tObj = new TaskDataClass();
							boolean taskDone = jObj.getBoolean("taskdone");
							if(taskDone==false){
								tObj.setTaskDone(taskDone);

								tObj.setTaskId(jObj.getInt("id"));
								tObj.setTaskName(jObj.getString("name"));
								tObj.setWeddingId(jObj.getInt("weddingid"));
								tObj.setWeditPreTaskId(jObj.getInt("weditpretaskid"));
								tObj.setTaskComments(jObj.getString("comments"));
								tObj.setTaskEndDate(jObj.getString("enddate"));
								tObj.setTaskResponsible(jObj.getString("responsible"));
								tObj.setTaskStartDate(jObj.getString("startdate"));
								tObj.setCategoryId(jObj.getInt("categoryid"));
								tObj.setDaysToComplete(jObj.getInt("daystocomplete"));
								//tObj.setWeditPreTaskId(jObj.getInt("weditpretaskid"));

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

								tData.add(tObj);
							}
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
				taskByCatData = new ArrayList<TaskDataClass>();
				taskByCatData = result;
				adapterTaskByCat = new CASectionedTask(TaskActivity.this, R.layout.row_section_header, result);
				lvTaskList.setAdapter(adapterTaskByCat);
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	class GetTaskToDo extends AsyncTask<Void, Void, ArrayList<TaskDataClass>>{

		@Override
		protected void onPreExecute() {
			//	pd = ProgressDialog.show(TaskActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(TaskActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<TaskDataClass> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=taskstodobycolor");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
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

						//	ArrayList<TaskDataClass> tData = new ArrayList<TaskDataClass>();
						//		int len = jArray.length();
						/*for(int j = 0; j < len; i++){
					JSONObject jObj = jArray.getJSONObject(i);*/
						boolean taskDone = jObj.getBoolean("taskdone");
						if(taskDone == false){
							TaskDataClass tObj = new TaskDataClass();

							//	boolean taskDone = jObj.getBoolean("taskdone");
							//	TaskDataClass tObj = new TaskDataClass();
							tObj.setTaskId(jObj.getInt("id"));
							tObj.setTaskName(jObj.getString("name"));
							tObj.setWeddingId(jObj.getInt("weddingid"));
							tObj.setWeditPreTaskId(jObj.getInt("weditpretaskid"));
							tObj.setTaskComments(jObj.getString("comments"));
							tObj.setTaskEndDate(jObj.getString("enddate"));
							tObj.setTaskResponsible(jObj.getString("responsible"));
							tObj.setTaskStartDate(jObj.getString("startdate"));
							tObj.setCategoryId(jObj.getInt("categoryid"));
							tObj.setDaysToComplete(jObj.getInt("daystocomplete"));
							tObj.setTaskDone(jObj.getBoolean("taskdone"));

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
				taskToDoData = new ArrayList<TaskDataClass>();
				taskToDoData = result;
				adapterTaskToDo = new CATaskList(TaskActivity.this, R.layout.row_task_home, result,false);
				lvTaskList.setAdapter(adapterTaskToDo);
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	class GetAllTask extends AsyncTask<Void, Void, ArrayList<TaskDataClass>>{

		@Override
		protected void onPreExecute() {
			//	pd = ProgressDialog.show(TaskActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(TaskActivity.this, "", "אנא המתן");
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
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
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
						TaskDataClass tObj = new TaskDataClass();
						tObj.setTaskId(jObj.getInt("id"));
						tObj.setTaskName(jObj.getString("name"));
						tObj.setWeddingId(jObj.getInt("weddingid"));
						tObj.setWeditPreTaskId(jObj.getInt("weditpretaskid"));
						tObj.setTaskComments(jObj.getString("comments"));
						tObj.setTaskEndDate(jObj.getString("enddate"));
						tObj.setTaskResponsible(jObj.getString("responsible"));
						tObj.setTaskStartDate(jObj.getString("startdate"));
						tObj.setCategoryId(jObj.getInt("categoryid"));
						tObj.setDaysToComplete(jObj.getInt("daystocomplete"));
						tObj.setTaskDone(taskDone);

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

						tData.add(tObj);
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
				allTaskData = new ArrayList<TaskDataClass>();
				allTaskData = result;
				adapterAllTask = new CAAllTask(TaskActivity.this, R.layout.row_task_home, result,false);
				lvTaskList.setAdapter(adapterAllTask);
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}

}
