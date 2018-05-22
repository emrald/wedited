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

import com.lauruss.wedit.CAContactList.ViewHolder;
import com.lauruss.wedit.HomeActivity.GetAllTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CAAllTask extends ArrayAdapter<TaskDataClass>{

	public LayoutInflater inflater;
	ArrayList<TaskDataClass> arrData;
	//ArrayList<TaskDataClass> selectedTaskData;
	Activity activity;
	boolean FROM_HOME_ACTIVITY = true;
	ProgressDialog pd;
	DateFormat formatterTaskDate = new SimpleDateFormat("yyyy-MM-dd");
	Date currentDate;
	public CAAllTask getThisAdapter(){
		return this;
	}
	public CAAllTask(Activity context, int textViewResourceId,
			ArrayList<TaskDataClass> objects,boolean FROM_HOME_ACTIVITY) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arrData = objects;
		//selectedTaskData= new ArrayList<TaskDataClass>();
		activity = context;
		this.FROM_HOME_ACTIVITY = FROM_HOME_ACTIVITY;
		currentDate = Calendar.getInstance().getTime();
	}
	public static class ViewHolder
	{	
		TextView txtViewTaskTitle;
		CheckBox chbSelected;
		LinearLayout rowView;
	}
	/*public ArrayList<TaskDataClass> getSelectedTaskData(){

		for(ContactMyDataClass ccon : arrData){
			if(ccon.isSelected()){
				selectedContact.add(ccon);
			}
		}
		return this.selectedTaskData;
	}*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_task_home, null);

			holder.txtViewTaskTitle = (TextView)convertView.findViewById(R.id.row_task_tv_title);
			holder.chbSelected = (CheckBox)convertView.findViewById(R.id.row_task_chb);
			holder.rowView = (LinearLayout)convertView.findViewById(R.id.row_task_view);

			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
			holder.chbSelected.setOnCheckedChangeListener(null);
		}
		final TaskDataClass tData = arrData.get(position);
		holder.txtViewTaskTitle.setText(tData.getTaskName());
		if(tData.getStatusColor() == TaskDataClass.FLAG_RED_COLOR){
			holder.rowView.setBackgroundColor(Color.RED);
		}else if(tData.getStatusColor() == TaskDataClass.FLAG_GREEN_COLOR){
			holder.rowView.setBackgroundColor(Color.GREEN);
		}else{
			holder.rowView.setBackgroundColor(Color.parseColor("#FFA500"));
		}
		if(tData.getTaskDone()){
			holder.chbSelected.setChecked(true);
		}else{
			holder.chbSelected.setChecked(false);
		}
		
		/*if(tData.getTaskDone()){
			holder.chbSelected.setChecked(true);
			holder.rowView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}*/

		//holder.rowView.setBackgroundColor(Color.RED);
		holder.chbSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(tData.getTaskId()>0){
					tData.setSelected(isChecked);
					DeleteTask exe = new DeleteTask();
					if(isChecked){
						exe.execute(tData.getTaskId(),1);
					}else{
						exe.execute(tData.getTaskId(),0);
					}
				}
			}

		});

		//holder.chbSelected.setChecked(tData.getIsSelected());
		return convertView;
	}
	class DeleteTask extends AsyncTask<Integer, Void, ArrayList<TaskDataClass>>{

		int taskIdThis;
		String message ="";
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(activity, "", "טוען נתונים");
			pd = ProgressDialog.show(activity, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<TaskDataClass> doInBackground(Integer... params) {
			try {
				List<NameValuePair> nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
				nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				nameValueAddTaskPair.add(new BasicNameValuePair("taskid", String.valueOf(params[0])));
				nameValueAddTaskPair.add(new BasicNameValuePair("done", String.valueOf(params[1])));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=taskdone");

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
					httpclient = new DefaultHttpClient();
					httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=getalltasks");
					httpParameters = httppost.getParams();

					HttpConnectionParams.setConnectionTimeout(httpParameters,
							timeoutConnection);

					HttpConnectionParams
					.setSoTimeout(httpParameters, timeoutSocket);

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					response = httpclient.execute(httppost);
					responseThis = EntityUtils.toString(response.getEntity()).trim();
					JSONArray jArray = new JSONArray(responseThis);

					ArrayList<TaskDataClass> tData = new ArrayList<TaskDataClass>();
					int len = jArray.length();
					for(int i = 0; i < len; i++){
						jObj = jArray.getJSONObject(i);
						boolean taskDone = jObj.getBoolean("taskdone");
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
					return tData;
				}else{
					message = jObj.getString("message");
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<TaskDataClass> result) {

			if(result != null){
				arrData.clear();
				arrData.addAll(result);
				getThisAdapter().notifyDataSetChanged();
			}else{
				if(message.trim().equalsIgnoreCase(""))
					Toast.makeText(activity, "No internet or error occured", Toast.LENGTH_SHORT).show();
				else{
					Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}
	}
	/*class GetAllTask extends AsyncTask<Void, Void, ArrayList<TaskDataClass>>{

		@Override
		protected ArrayList<TaskDataClass> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditwedding?action=getalltasks");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONArray jArray = new JSONArray(responseThis);

				ArrayList<TaskDataClass> tData = new ArrayList<TaskDataClass>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObj = jArray.getJSONObject(i);
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
				return tData;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<TaskDataClass> result) {

			if(result != null){
				arrData = result;
				getThisAdapter().notifyDataSetChanged();
			}else{
				Toast.makeText(activity, "No internet or error occured", Toast.LENGTH_SHORT).show();
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}*/

}
