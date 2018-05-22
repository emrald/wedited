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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity{
	public LayoutInflater inflater;
	ProgressDialog pd;
	RelativeLayout rlContact,rlUpdatePersonalInformation;
	LinearLayout llSettingDynamic;
	SharedPreferences pref;
//	int u_id;
	TextView logout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting);
		logout = (TextView)findViewById(R.id.tv_logoutlink);
		
		//pref = getSharedPreferences(SignUpActivity.FLAG_WEDDING_ID,0);
		pref = MyApplication.getSharedPreference();
		pref.getInt(SignUpActivity.FLAG_WEDDING_ID, 0);

	//	LoginActivity.FLAG_HOME=false;
		
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				 SharedPreferences.Editor preferencesEditor = pref.edit();
				 preferencesEditor.clear();
				 preferencesEditor.commit();		
				    
				 Intent intent=new Intent(SettingActivity.this,LoginActivity.class);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent);
				
			}
		});
		
		rlContact = (RelativeLayout)findViewById(R.id.setting_rl_contact);
		rlUpdatePersonalInformation = (RelativeLayout)findViewById(R.id.setting_rl_updatepersonalinformation);
		llSettingDynamic = (LinearLayout)findViewById(R.id.setting_ll_setting_dynamic);
		this.inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		rlContact.setOnClickListener(contactClickListner);
		rlUpdatePersonalInformation.setOnClickListener(updatePersonalClickListner);
		
		GetSettingButton exe = new GetSettingButton();
		exe.execute();
	}
	
	OnClickListener contactClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent in = new Intent(SettingActivity.this,ContactActivity.class);
			startActivity(in);
			
		}
	};
	OnClickListener updatePersonalClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent in = new Intent(SettingActivity.this,UpdatePersonalInformationActivity.class);
			startActivity(in);
			
		}
	};
	class GetSettingButton extends AsyncTask<Void, Void, ArrayList<SettingDynamicData>>{

		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(SettingActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(SettingActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<SettingDynamicData> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"pages?action=pageslist");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));*/

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONArray jArray = new JSONArray(responseThis);

				ArrayList<SettingDynamicData> sData = new ArrayList<SettingDynamicData>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObjMain = jArray.getJSONObject(i);
					SettingDynamicData sDData = new SettingDynamicData();
					sDData.setId(jObjMain.getInt("id"));
					sDData.setName(jObjMain.getString("name"));
					sDData.setButtonName(jObjMain.getString("buttonname"));
					sDData.setShortText(jObjMain.getString("shorttext"));
					
					JSONArray jArrURL = jObjMain.getJSONArray("url");
					if(jArrURL.length() > 0){
						JSONObject jObj = jArrURL.getJSONObject(0); 
						sDData.setURLId(jObj.getInt("url_id"));
						sDData.setURLPath("url_urlpath");
					}
					sData.add(sDData);
				}
				return sData;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<SettingDynamicData> result) {
			if(result != null){
				for(final SettingDynamicData sDData : result){
					View vSettingName = inflater.inflate(R.layout.row_task_arrow, null);
					TextView tvButtonNameName = (TextView)vSettingName.findViewById(R.id.row_task_arrow_tv_title);
					tvButtonNameName.setText(sDData.getButtonName());
					
					vSettingName.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							/*try {
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sDData.getURLPath()));
								startActivity(browserIntent);
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(SettingActivity.this, "No link found or wrong link.", Toast.LENGTH_SHORT).show();
							}*/
							Bundle bn = new Bundle();
							bn.putInt(SettingDetailActivity.FALG_PAGE_ID, sDData.getId());
							Intent in = new Intent(SettingActivity.this,SettingDetailActivity.class);
							in.putExtras(bn);
							startActivity(in);
							
						}
					});
					
					
					llSettingDynamic.addView(vSettingName);
					
					View line = new View(SettingActivity.this);
					line.setBackgroundResource(R.drawable.dash_line);
					
					LayoutParams buttonLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,2);
					buttonLayoutParams.setMargins(20, 0, 20, 0);
					//line.setLayoutParams(buttonLayoutParams);
					
					//ViewGroup.LayoutParams lParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 2);
					llSettingDynamic.addView(line ,buttonLayoutParams);
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	@Override
	 protected void onResume() {
	  // TODO Auto-generated method stub
	  
	  if(CATaskList.aldeletetask.size()>0)
	  { 
	   for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
	   {
	    DeleteTask delexe = new DeleteTask(SettingActivity.this);
	    delexe.execute(taskdeleteobj.getTaskId());
	   }
	  }
	  super.onResume();
	 }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	//	finish();
	}
}
