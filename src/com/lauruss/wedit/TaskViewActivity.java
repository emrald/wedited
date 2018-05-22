package com.lauruss.wedit;

import java.util.ArrayList;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskViewActivity extends Activity{

	public static String FLAG_TASK_ID = "task_id",FLAG_TASK_DATA_STRING  = "task_dat_string" , FLAG_TASK_EDIT = "from_task_edit";
	
	int taskId = 0,catId = 0;
	Button btnBack,btnEdit,btnDelete;
	TextView tvHeader;
	LinearLayout llTaskLink,llTaskDetail;
	public LayoutInflater inflater;
	JSONObject jObj;
	ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taskview);
		
		btnBack = (Button)findViewById(R.id.taskview_btn_back);
		btnEdit = (Button)findViewById(R.id.taskview_btn_edit);
		btnDelete = (Button)findViewById(R.id.taskview_btn_delete);
		tvHeader = (TextView)findViewById(R.id.taskview_tv_header);
		llTaskLink = (LinearLayout)findViewById(R.id.taskview_ll_tasklinks);
		llTaskDetail = (LinearLayout)findViewById(R.id.taskview_ll_task);
		llTaskLink.requestLayout();
		llTaskDetail.requestLayout();
		//llTaskLink.setLayoutParams(new FrameLayout.LayoutParams(30,60));
		
		Bundle bn = getIntent().getExtras();
		
		taskId = bn.getInt(FLAG_TASK_ID);
		if(taskId == 0){
			Toast.makeText(this, "Task id not found.", Toast.LENGTH_SHORT).show();
			finish();
		}
		
		this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(jObj != null){
					Bundle bn = new Bundle();
					bn.putString(FLAG_TASK_DATA_STRING, jObj.toString());
					bn.putBoolean(FLAG_TASK_EDIT, true);
					Intent in = new Intent(TaskViewActivity.this,AddNewTaskActivity.class);
				//	Intent in = new Intent(TaskViewActivity.this,TaskViewActivity.class);
					in.putExtras(bn);
					startActivity(in);
					
				}
				
			}
		});
		btnDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DeleteTask exe = new DeleteTask();
				exe.execute();
				
			}
		});
	}
	@Override
	protected void onResume() {
		llTaskDetail.removeAllViews();
		llTaskLink.removeAllViews();
		GetTaskDetail exe = new GetTaskDetail();
		exe.execute();
		super.onResume();
	}
	class DeleteTask extends AsyncTask<Void, Void, Integer>{
		String message;
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(TaskViewActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(TaskViewActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditTasks?action=deletetask");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				nameValuePairs.add(new BasicNameValuePair("taskid", String.valueOf(taskId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				
				jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				if(status){
					message = jObj.getString("message");
					return 0;
				}else{
					return 1;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return 2;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 0){
				Toast.makeText(TaskViewActivity.this, R.string.task_deleted, Toast.LENGTH_SHORT).show();
				finish();
			}else if(result == 1){
				Toast.makeText(TaskViewActivity.this, "Task not deleted", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(TaskViewActivity.this, "No Internet Connetion or error occured.", Toast.LENGTH_SHORT).show();
			}
			pd.dismiss();
			super.onPostExecute(result);
		}
		
	}
	class GetTaskDetail extends AsyncTask<Void, Void, Integer>{
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(TaskViewActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(TaskViewActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditTasks?action=gettask");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				nameValuePairs.add(new BasicNameValuePair("taskid", String.valueOf(taskId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				
				jObj = new JSONObject(responseThis);
				
				return 0;
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 0){
				try {
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
					
					View vTaskName = inflater.inflate(R.layout.row_task, null);
					TextView tvTaskName = (TextView)vTaskName.findViewById(R.id.row_task_tv_title_task);
					tvTaskName.setText(TaskViewActivity.this.getString(R.string.addtask_taskname)+": "+taskName);
					llTaskDetail.addView(vTaskName);
					
					View vStartDate = inflater.inflate(R.layout.row_task, null);
					TextView tvStartDate = (TextView)vStartDate.findViewById(R.id.row_task_tv_title_task);
					tvStartDate.setText(TaskViewActivity.this.getString(R.string.text_startdate)+": "+startDate);
					llTaskDetail.addView(vStartDate);
					
					View vDaysToComplete = inflater.inflate(R.layout.row_task, null);
					TextView tvDaysToComplete = (TextView)vDaysToComplete.findViewById(R.id.row_task_tv_title_task);
					tvDaysToComplete.setText(TaskViewActivity.this.getString(R.string.text_days_to_complete)+": "+String.valueOf(daysToComplete));
					llTaskDetail.addView(vDaysToComplete);
					
					View vEndDate = inflater.inflate(R.layout.row_task, null);
					TextView tvEndDate = (TextView)vEndDate.findViewById(R.id.row_task_tv_title_task);
					tvEndDate.setText(TaskViewActivity.this.getString(R.string.taskview_enddate)+": "+endDate);
					llTaskDetail.addView(vEndDate);
					
					View vResponsible = inflater.inflate(R.layout.row_task, null);
					TextView tvResonsible = (TextView)vResponsible.findViewById(R.id.row_task_tv_title_task);
					tvResonsible.setText(TaskViewActivity.this.getString(R.string.text_responsible)+": "+responsible);
					llTaskDetail.addView(vResponsible);
					
					View vTaskCategory = inflater.inflate(R.layout.row_task, null);
					TextView tvTaskCategory = (TextView)vTaskCategory.findViewById(R.id.row_task_tv_title_task);
					tvTaskCategory.setText(TaskViewActivity.this.getString(R.string.text_task_category)+": "+catName);
					llTaskDetail.addView(vTaskCategory);
					
					View vComments = inflater.inflate(R.layout.row_task, null);
					TextView tvComments = (TextView)vComments.findViewById(R.id.row_task_tv_title_task);
					tvComments.setText(TaskViewActivity.this.getString(R.string.text_comments)+": "+comments);
					llTaskDetail.addView(vComments);

					
					JSONArray arrLinks = jObj.getJSONArray("links");
					int len = arrLinks.length();
					if(len>0){
						for(int i = 0; i<len; i++){
							JSONObject jLink = arrLinks.getJSONObject(i);
							String name = jLink.getString("name");
							final String link = jLink.getString("link");
							final int pageid = jLink.getInt("id");
							
							View linkView = inflater.inflate(R.layout.row_task_arrow, null);
							TextView tvName = (TextView)linkView.findViewById(R.id.row_task_arrow_tv_title);
							tvName.setText(name);
							
							linkView.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									if(URLUtil.isValidUrl(link)){
										try {
										//	Toast.makeText(getApplicationContext(), "Link", Toast.LENGTH_SHORT).show();
											Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
											startActivity(browserIntent);
										} catch (Exception e) {
											e.printStackTrace();
											Toast.makeText(TaskViewActivity.this, "No link found or wrong link.", Toast.LENGTH_SHORT).show();
										}
									}else{
										
										Bundle bn = new Bundle();
										bn.putInt(SettingDetailActivity.FALG_PAGE_ID, pageid);
										//Toast.makeText(getApplicationContext(), "Link"+pageid, Toast.LENGTH_SHORT).show();
										//bn.putInt(, link);
										Intent in = new Intent(TaskViewActivity.this,SettingDetailActivity.class);
										in.putExtras(bn);
										startActivity(in);
									}
								}
							});
							llTaskLink.addView(linkView);
						}
					}else{
						llTaskLink.setVisibility(View.GONE);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Toast.makeText(TaskViewActivity.this, "Please check internet connection or error occured.", Toast.LENGTH_SHORT).show();
			}
			pd.dismiss();
			super.onPostExecute(result);
		}
		
	}

	
}
