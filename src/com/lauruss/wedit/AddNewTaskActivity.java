package com.lauruss.wedit;

import java.text.ParseException;
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
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AddNewTaskActivity extends Activity{

	Button btnBack,btnAddNewTask,btnDateToDo,btnDateToFinishTheTask,btnCategory,btnSave;
	EditText etNewTask,etPersonInChage,etComments,etDaysTillTask;
	//ToggleButton toggleSendToMyDiary,toggleSendMailToRemind,toggleSendSMSReminder;
	ToggleButton toggleSendToMyDiary,toggleSendMailToRemind;
	RelativeLayout rlDateToDo,rlDateToFinishTheTask,rlCategory;
	TextView tvDateToDo,tvDateToFinishTheTask,tvCategory;
	TextView tvHeader;
	//for do date
	private int year;
	private int month;
	private int day;
	private int eYear;
	private int eMonth;
	private int eDay;
	static final int START_DATE_DIALOG_ID = 1;
	static final int END_DATE_DIALOG_ID = 2;

	CACategoryList adapterCat;
	//ArrayList<CategoryData> catArrData;
	ProgressDialog pd;

	CategoryData selectedCatData;
	String startDate="",endDate = "";
	SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	List<NameValuePair> nameValueAddTaskPair;
	
	DateTime startDateJoda,endDateJoda;
	boolean FROM_TASK_EDIT = false;
	static boolean ACTIVITY_FLAG=false;
	int catId = 0 , taskId = 0;
	String taskName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnew_task);

		//HomeActivity.ACTIVITY_FLAG_HOME=false;
		
		btnBack = (Button)findViewById(R.id.addnewtask_btn_back);
		btnAddNewTask = (Button)findViewById(R.id.addnewtask_btn_addtask);
		etNewTask = (EditText)findViewById(R.id.addnewtask_et_taskname);
		etPersonInChage = (EditText)findViewById(R.id.addnewtask_et_personincharge);
		etComments = (EditText)findViewById(R.id.addnewtask_et_comments);
		toggleSendMailToRemind = (ToggleButton)findViewById(R.id.addnewtask_btn_sendmailtoremind);
		//toggleSendSMSReminder = (ToggleButton)findViewById(R.id.addnewtask_btn_sendsmsremainder);
		toggleSendToMyDiary = (ToggleButton)findViewById(R.id.addnewtask_btn_sendtomydiary);
		etDaysTillTask = (EditText)findViewById(R.id.addnewtask_et_daystillthetask);

		rlDateToDo = (RelativeLayout)findViewById(R.id.addnewtask_rl_datetodo);
		rlDateToFinishTheTask = (RelativeLayout)findViewById(R.id.addnewtask_rl_datetofinishthetask);
		rlCategory = (RelativeLayout)findViewById(R.id.addnewtask_rl_category);

		btnDateToDo = (Button)findViewById(R.id.addnewtask_btn_datetodo);
		btnDateToFinishTheTask = (Button)findViewById(R.id.addnewtask_btn_datetofinishthetask);
		btnCategory = (Button)findViewById(R.id.addnewtask_btn_category);
		 btnSave = (Button)findViewById(R.id.addnewtask_btn_save);
		tvDateToDo = (TextView)findViewById(R.id.addnewtask_tv_datetodo);
		tvDateToFinishTheTask = (TextView)findViewById(R.id.addnewtask_tv_datetofinishthetask);
		tvCategory = (TextView)findViewById(R.id.addnewtask_tv_category);
		tvHeader = (TextView)findViewById(R.id.addnewtask_tv_header);
		

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		eYear=c.get(Calendar.YEAR);;
		eMonth=c.get(Calendar.MONTH);
		eDay=c.get(Calendar.DAY_OF_MONTH);

		GetCategoryList exe = new GetCategoryList();
		exe.execute();

		Bundle bn = getIntent().getExtras();
		FROM_TASK_EDIT = bn.getBoolean(TaskViewActivity.FLAG_TASK_EDIT);
		if(FROM_TASK_EDIT){
			String jSon = bn.getString(TaskViewActivity.FLAG_TASK_DATA_STRING);
			try {
				JSONObject jObj = new JSONObject(jSon);
				taskId = jObj.getInt("id");
				String taskName = jObj.getString("name");
				catId = jObj.getInt("categoryid");
				String startDate = jObj.getString("startdate");
				int daysToComplete = jObj.getInt("daystocomplete");
				String endDate = jObj.getString("enddate");
				String responsible = jObj.getString("responsible");
				String comments = jObj.getString("comments");
				
				JSONArray jArrCat = jObj.getJSONArray("category");

				String catName = "";
				if(jArrCat.length() > 0){
					JSONObject jCat = jArrCat.getJSONObject(0);
					catName = jCat.getString("name");
				}
				etNewTask.setText(taskName);
				etDaysTillTask.setText(String.valueOf(daysToComplete));
				etPersonInChage.setText(responsible);
				etComments.setText(comments);

				selectedCatData = new CategoryData(catId, catName);
				tvCategory.setText(AddNewTaskActivity.this.getString(R.string.text_category)+" - "+selectedCatData.getCatName());
				this.startDate = startDate;
				this.endDate = endDate;
				tvDateToDo.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetodo)+" - "+this.startDate);
				tvDateToFinishTheTask.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetofinishtask)+" - "+this.endDate);
				tvHeader.setText(R.string.addtask_edit_header);
				
				startDateJoda = new DateTime(dateFormatter.parse(this.startDate).getTime());
				endDateJoda = new DateTime(dateFormatter.parse(this.endDate).getTime());
				
				boolean diary = jObj.getBoolean("senddiaryalert");
				toggleSendToMyDiary.setChecked(diary);
				
				boolean mailAlert = jObj.getBoolean("sendemailalert");
				toggleSendMailToRemind.setChecked(mailAlert);
				/*boolean smsAlert = jObj.getBoolean("sendsmsalert");
				toggleSendSMSReminder.setChecked(smsAlert);*/
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		rlDateToDo.setOnClickListener(dateToDoListner);
		tvDateToDo.setOnClickListener(dateToDoListner);
		btnDateToDo.setOnClickListener(dateToDoListner);
		rlDateToFinishTheTask.setOnClickListener(dateToFinishTaskListner);
		tvDateToFinishTheTask.setOnClickListener(dateToFinishTaskListner);
		btnDateToFinishTheTask.setOnClickListener(dateToFinishTaskListner);
		rlCategory.setOnClickListener(categoryClickListner);
		tvCategory.setOnClickListener(categoryClickListner);
		btnCategory.setOnClickListener(categoryClickListner);

		etDaysTillTask.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!s.toString().trim().equalsIgnoreCase("")){
					if(startDateJoda != null){
						endDateJoda = startDateJoda.plusDays(Integer.parseInt(s.toString()));
						//endDateJoda.plusDays(Integer.parseInt(s.toString()));
						Date endDatethis = new Date(endDateJoda.getMillis());
						endDate = dateFormatter.format(endDatethis);
						tvDateToFinishTheTask.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetofinishtask)+" - "+endDate);
						Log.e("EtTextChangeListner", "end date : "+endDate);
					}else{
						Toast.makeText(AddNewTaskActivity.this, "Please set start date first", Toast.LENGTH_SHORT).show();
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		btnAddNewTask.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				taskName = etNewTask.getText().toString().trim();
				String strDaysTillTask = etDaysTillTask.getText().toString().trim();
				String personInCharge = etPersonInChage.getText().toString().trim();
				String comments = etComments.getText().toString().trim();

				if(taskName != null && !taskName.equalsIgnoreCase("")){
					if(!startDate.equalsIgnoreCase("")){
						if(!strDaysTillTask.equalsIgnoreCase("") || !endDate.equalsIgnoreCase("")){
							if(selectedCatData != null){
								nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
								nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
								if(FROM_TASK_EDIT){
									nameValueAddTaskPair.add(new BasicNameValuePair("taskid", String.valueOf(taskId)));
								}
								nameValueAddTaskPair.add(new BasicNameValuePair("name", taskName));
								nameValueAddTaskPair.add(new BasicNameValuePair("startdate", startDate));
								nameValueAddTaskPair.add(new BasicNameValuePair("category", String.valueOf(selectedCatData.getCatId())));
								if(!strDaysTillTask.equalsIgnoreCase("")){
									nameValueAddTaskPair.add(new BasicNameValuePair("days", strDaysTillTask));
								}else{
									nameValueAddTaskPair.add(new BasicNameValuePair("enddate", endDate));
								}
								if(!personInCharge.equalsIgnoreCase("")){
									nameValueAddTaskPair.add(new BasicNameValuePair("responsible", personInCharge));
								}
								if(!comments.equalsIgnoreCase("")){
									nameValueAddTaskPair.add(new BasicNameValuePair("comment", comments));
								}
								if(toggleSendMailToRemind.isChecked()){
									nameValueAddTaskPair.add(new BasicNameValuePair("emailalert", "1"));
								}else{
									nameValueAddTaskPair.add(new BasicNameValuePair("emailalert", "0"));
								}
								/*if(toggleSendSMSReminder.isChecked()){
									nameValueAddTaskPair.add(new BasicNameValuePair("smsalert", "1"));
								}*/
								if(toggleSendToMyDiary.isChecked())
								{
									nameValueAddTaskPair.add(new BasicNameValuePair("diaryalert", "1"));
								}
								if(isNetworkAvailable()){
									AddNewTask exe = new AddNewTask();
									exe.execute();
									
								/*	ACTIVITY_FLAG=true;
							         Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
							         startActivity(i);
									*/
								}else{
									Toast.makeText(AddNewTaskActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
								}

							}else{
								Toast.makeText(AddNewTaskActivity.this, R.string.addtask_select_category, Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(AddNewTaskActivity.this, R.string.addtask_select_daystilltask_or_datetofinish, Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(AddNewTaskActivity.this, R.string.addtask_select_start_date, Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(AddNewTaskActivity.this, R.string.addtask_enter_task_name, Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {
			   
			   @Override
			   public void onClick(View v) {
			    // TODO Auto-generated method stub
			    taskName = etNewTask.getText().toString().trim();
			    String strDaysTillTask = etDaysTillTask.getText().toString().trim();
			    String personInCharge = etPersonInChage.getText().toString().trim();
			    String comments = etComments.getText().toString().trim();

			    if(taskName != null && !taskName.equalsIgnoreCase("")){
			     if(!startDate.equalsIgnoreCase("")){
			      if(!strDaysTillTask.equalsIgnoreCase("") || !endDate.equalsIgnoreCase("")){
			       if(selectedCatData != null){
			        nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
			        nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
			        if(FROM_TASK_EDIT){
			         nameValueAddTaskPair.add(new BasicNameValuePair("taskid", String.valueOf(taskId)));
			        }
			        nameValueAddTaskPair.add(new BasicNameValuePair("name", taskName));
			        nameValueAddTaskPair.add(new BasicNameValuePair("startdate", startDate));
			        nameValueAddTaskPair.add(new BasicNameValuePair("category", String.valueOf(selectedCatData.getCatId())));
			        if(!strDaysTillTask.equalsIgnoreCase("")){
			         nameValueAddTaskPair.add(new BasicNameValuePair("days", strDaysTillTask));
			        }else{
			         nameValueAddTaskPair.add(new BasicNameValuePair("enddate", endDate));
			        }
			        if(!personInCharge.equalsIgnoreCase("")){
			         nameValueAddTaskPair.add(new BasicNameValuePair("responsible", personInCharge));
			        }
			        if(!comments.equalsIgnoreCase("")){
			         nameValueAddTaskPair.add(new BasicNameValuePair("comment", comments));
			        }
			        if(toggleSendMailToRemind.isChecked()){
			         nameValueAddTaskPair.add(new BasicNameValuePair("emailalert", "1"));
			        }else{
			         nameValueAddTaskPair.add(new BasicNameValuePair("emailalert", "0"));
			        }
			        /*if(toggleSendSMSReminder.isChecked()){
			         nameValueAddTaskPair.add(new BasicNameValuePair("smsalert", "1"));
			        }*/
			        if(isNetworkAvailable()){
			         AddNewTask exe = new AddNewTask();
			         exe.execute();
			        /* ACTIVITY_FLAG=true;
			         Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
			         startActivity(i);*/
			        }else{
			         Toast.makeText(AddNewTaskActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
			        }

			       }else{
			        Toast.makeText(AddNewTaskActivity.this, R.string.addtask_select_category, Toast.LENGTH_SHORT).show();
			       }
			      }else{
			       Toast.makeText(AddNewTaskActivity.this, R.string.addtask_select_daystilltask_or_datetofinish, Toast.LENGTH_SHORT).show();
			      }
			     }else{
			      Toast.makeText(AddNewTaskActivity.this, R.string.addtask_select_start_date, Toast.LENGTH_SHORT).show();
			     }

			    }else{
			     Toast.makeText(AddNewTaskActivity.this, R.string.addtask_enter_task_name, Toast.LENGTH_SHORT).show();
			    }
			   }
			   
			  });
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	OnClickListener dateToDoListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog(START_DATE_DIALOG_ID);

		}
	};
	OnClickListener dateToFinishTaskListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showDialog(END_DATE_DIALOG_ID);

		}
	};
	OnClickListener categoryClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {

			AlertDialog.Builder diag = new AlertDialog.Builder(AddNewTaskActivity.this);
			// diag.setCancelable(true);
			
			diag.setAdapter(adapterCat, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedCatData = adapterCat.getItem(which);
				//	tvCategory.setText(AddNewTaskActivity.this.getString(R.string.text_category)+" - "+selectedCatData.getCatName());
					tvCategory.setText(selectedCatData.getCatName());
					dialog.dismiss();

				}
			});

		//	diag.setO
			diag.show();

		}
	};
	public void clearAllViews(){
		etNewTask.setText("");
		etDaysTillTask.setText("");
		etComments.setText("");
		etPersonInChage.setText("");
		startDate = "";
		endDate = "";
		selectedCatData = null;
		tvDateToFinishTheTask.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetofinishtask));
		tvDateToDo.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetodo));
		tvCategory.setText(AddNewTaskActivity.this.getString(R.string.text_category));
		toggleSendMailToRemind.setChecked(false);
	//	toggleSendSMSReminder.setChecked(false);
		toggleSendToMyDiary.setChecked(false);

	}
	private DatePickerDialog.OnDateSetListener datePickerDateToDoListener = new DatePickerDialog.OnDateSetListener()
	{

		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			Calendar c = Calendar.getInstance();
			c.set(year, month, day, 0, 0, 0);
			startDateJoda = new DateTime(c.getTimeInMillis());
			//Date sDate = new Date(year, month, day);
			startDate = dateFormatter.format(c.getTime());
			tvDateToDo.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetodo)+" - "+startDate);

		}
	};
	private DatePickerDialog.OnDateSetListener dateToFinishListener = new DatePickerDialog.OnDateSetListener()
	{

		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {

			if(D2MS(month, day, year) < D2MS(selectedMonth, selectedDay, selectedYear)){
				eYear = selectedYear;
				eMonth = selectedMonth;
				eDay = selectedDay;
				Calendar c = Calendar.getInstance();
				c.set(eYear, eMonth, eDay, 0, 0, 0);
				//Date sDate = new Date(eYear, eMonth, eDay);
				endDateJoda = new DateTime(c.getTimeInMillis());
				endDate = dateFormatter.format(c.getTime());
				tvDateToFinishTheTask.setText(AddNewTaskActivity.this.getString(R.string.addtask_datetofinishtask)+" - "+endDate);
				etDaysTillTask.setText(String.valueOf(Days.daysBetween(startDateJoda, endDateJoda).getDays()));
				Log.e("In end date lisner", "end date : "+endDate);
				//Log.e("In end date lisner", "In if");
			}else{
				Toast.makeText(AddNewTaskActivity.this, "End date can not be less than start date", Toast.LENGTH_SHORT).show();
				//Log.e("In end date lisner", "In else");
			}
		}
	};
	class AddNewTask extends AsyncTask<Void, Void, Integer>{

		int taskIdThis;
		String message;
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(AddNewTaskActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(AddNewTaskActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost;
				if(FROM_TASK_EDIT){
					//nameValueAddTaskPair.add(new BasicNameValuePair("taskid", String.valueOf(taskId)));
					httppost = new HttpPost(MyApplication.BASE_API_URL+"weditTasks?action=savetask");
				}else{
					httppost = new HttpPost(MyApplication.BASE_API_URL+"weditTasks?action=newtask");
				}
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

				httppost.setEntity(new UrlEncodedFormEntity(nameValueAddTaskPair,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONObject jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				if(status){
					if(FROM_TASK_EDIT){
						message = jObj.getString("message");
					}else
						taskIdThis = jObj.getInt("taskid");

					return 0;
				}else{
					message = jObj.getString("message");
					return 1;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return 2;	
			}

		}
		@SuppressLint("NewApi")
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 0){

				if(FROM_TASK_EDIT)
					Toast.makeText(AddNewTaskActivity.this, message, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(AddNewTaskActivity.this, R.string.addtask_task_inserted, Toast.LENGTH_SHORT).show();

				pd.dismiss();
				if(toggleSendToMyDiary.isChecked()){
					//toggleSendToMyDiary.setChecked(true);
					try {
						Date sd = dateFormatter.parse(startDate);
						long sMillis = sd.getTime();
						long eMillis;
						Date ed;
						String days = etDaysTillTask.getText().toString().trim(); 
						if(days.equalsIgnoreCase("")){
							ed = dateFormatter.parse(endDate);
							eMillis = ed.getTime();
						}else{
							Calendar c = Calendar.getInstance();
							c.setTime(sd);
							c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(days));
							eMillis = c.getTimeInMillis();

						}
						/*if (Build.VERSION.SDK_INT >= 14) {
							Intent intent = new Intent(Intent.ACTION_INSERT);
							intent.setData(Events.CONTENT_URI);
							intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, sMillis);
							intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eMillis);
							intent.putExtra(Events.TITLE, taskName);
							intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
							startActivity(intent);
						}*/
						if (Build.VERSION.SDK_INT >= 14) {
							Intent intent = new Intent(Intent.ACTION_INSERT)
							.setData(Events.CONTENT_URI)
							.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, sMillis)
							.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eMillis)
							.putExtra(Events.TITLE, taskName)
							.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
							startActivity(intent);
							//finish();
						}
						else {
							//Calendar cal = Calendar.getInstance();              
							Intent intent = new Intent(Intent.ACTION_EDIT);
							intent.setType("vnd.android.cursor.item/event");
							intent.putExtra("beginTime", sMillis);
							intent.putExtra("allDay", true);
							//intent.putExtra("rrule", "FREQ=YEARLY");
							intent.putExtra("endTime", eMillis);
							intent.putExtra("title", taskName);
							startActivity(intent);
						//	finish();
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					finish();
					
				}
				if(toggleSendToMyDiary.isChecked()==false)
				{
				if(HomeActivity.ACTIVITY_FLAG_HOME==true)
		         {
		        	 ACTIVITY_FLAG=false;
		        	 Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
		        //	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			         startActivity(i);
			      //   finish();
			         
		         }
		         else 
		         {
		        	 ACTIVITY_FLAG=true;
			         Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
			      //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			         startActivity(i);
			     //    finish();
		         }
				}
				/*else
				{
					if(HomeActivity.ACTIVITY_FLAG_HOME==true)
			         {
			        	 ACTIVITY_FLAG=false;
			        	 Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
			        //	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				         startActivity(i);
				      //   finish();
				         
			         }
			         else 
			         {
			        	 ACTIVITY_FLAG=true;
				         Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
				      //   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				         startActivity(i);
				     //    finish();
			         }
				}
				*/
				/*Intent in = new Intent(AddNewTaskActivity.this,TaskActivity.class);
				//in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(in);*/
				/*AddNewTaskActivity.this.finish();
		        android.os.Process.killProcess(android.os.Process.myPid());
		        System.exit(0);
				getParent().finish();
				  history.remove(history.size() - 1);
			        setContentView(history.get(history.size() - 1));*/
			//	Activity activity;
				/*TabActivityMy.tabHost.setCurrentTab(4);
				Intent intent = new Intent(AddNewTaskActivity.this, TabActivityMy.class);
				startActivity(intent);*/
				
		       /* if(HomeActivity.ACTIVITY_FLAG_HOME==true)
		         {
		        	 ACTIVITY_FLAG=false;
		        	 Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
		        	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			         startActivity(i);
			         
		         }
		         else 
		         {
		        	 ACTIVITY_FLAG=true;
			         Intent i =new Intent(AddNewTaskActivity.this,TabActivityMy.class);
			         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			         startActivity(i);
			     //    finish();
		         }*/
		     //    finish();
				
	//		finish();
				/*Intent intent = getIntent();
				  overridePendingTransition(0, 0);
				  intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				  finish();

				  overridePendingTransition(0, 0);
				  startActivity(intent);*/
			//	TabActivityMy.tabHost.setCurrentTab(4);
				//clearAllViews();
			}else if(result == 1){
				Toast.makeText(AddNewTaskActivity.this, message, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AddNewTaskActivity.this, "No Internet Connetion or error occured.", Toast.LENGTH_SHORT).show();
			}
			pd.dismiss();


			super.onPostExecute(result);
		}

	}
	
	public long D2MS(int month, int day, int year) { 
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month, day,00,00,00);

		//Log.e("time in millis is:", String.valueOf(c.getTimeInMillis()));
		return c.getTimeInMillis();  
	} 
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerDateToDoListener, 
					year, month,day);

		case END_DATE_DIALOG_ID:{
			return new DatePickerDialog(this, dateToFinishListener, 
					eYear, eMonth,eDay);
		}
		}
		return null;
	}
	class GetCategoryList extends AsyncTask<Void, Void, ArrayList<CategoryData>>{

		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(AddNewTaskActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(AddNewTaskActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<CategoryData> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditTaskCategory?action=getcategories");
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

				ArrayList<CategoryData> tData = new ArrayList<CategoryData>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObj = jArray.getJSONObject(i);
					CategoryData tObj = new CategoryData(jObj.getInt("id"),jObj.getString("name"));

					tData.add(tObj);
				}
				return tData;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<CategoryData> result) {
			if(result != null){
				adapterCat = new CACategoryList(AddNewTaskActivity.this, R.layout.row_task, result);
				//lvTask.setAdapter(adapter);
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	//	finish();
	}
}
