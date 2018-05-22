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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

class DeleteTask extends AsyncTask<Integer, Void, ArrayList<TaskDataClass>>{

	int taskIdThis;
	String message ="";
	DateFormat formatterTaskDate = new SimpleDateFormat("yyyy-MM-dd");
	ProgressDialog pd;
	Date currentDate = Calendar.getInstance().getTime();
	Context context;
	static boolean FLAG_DELETE = false;
	public DeleteTask(Context context) {
	      this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		pd = ProgressDialog.show(context, "", "אנא המתן");
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<TaskDataClass> doInBackground(Integer... params) {
		try {
			List<NameValuePair> nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
			nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
			nameValueAddTaskPair.add(new BasicNameValuePair("taskid", String.valueOf(params[0])));
			nameValueAddTaskPair.add(new BasicNameValuePair("done", "1"));

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
	//	 FLAG_DELETE = true;
		if(result != null){
			CATaskList.arrData.clear();
			CATaskList.arrData.addAll(result);
		}else{
			if(message.trim().equalsIgnoreCase(""))
				Toast.makeText(context, "No internet or error occured", Toast.LENGTH_SHORT).show();
			else{
				Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
			}
		}
		pd.dismiss();
		super.onPostExecute(result);
	}
}