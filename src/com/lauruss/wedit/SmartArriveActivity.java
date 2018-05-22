package com.lauruss.wedit;

import java.io.File;
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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SmartArriveActivity extends Activity{
	
	Button btnBack,btnDone,btnClearFilter;
	CASmartArrive adapter;
	public static String FLAG_STR_BY_SMART = "by_smart_selected";
	public static int BY_EMAIL = 1;
	public static int BY_SMS = 2;
	int selectedMode = 0;
	String inviteString = "";
	String email_of_user;
	EditText etFilter;
	ProgressDialog pd;
	 int inviteid;
	ListView lvInviteList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smartarrive);
		
		btnBack = (Button)findViewById(R.id.smartarrive_btn_back);
		lvInviteList = (ListView)findViewById(R.id.smartarrive_lv);
		btnDone = (Button)findViewById(R.id.smartarrive_btn_done);
		etFilter = (EditText)findViewById(R.id.smartarrive_et_search_facebook);
		btnClearFilter = (Button)findViewById(R.id.smartarrive_btn_clearsearch_filter);
		
		Bundle bn = getIntent().getExtras();
		if(bn!= null){
		
		//	Toast.makeText(getApplicationContext(),id , Toast.LENGTH_SHORT).show();
			selectedMode = bn.getInt(FLAG_STR_BY_SMART);
		}
		GetCategorywiseTask exe = new GetCategorywiseTask();
		exe.execute();
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		btnClearFilter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etFilter.setText("");
				
			}
		});
		etFilter.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapter.filter(s.toString());
				
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
		btnDone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for(InviteDataClass list:CASmartArrive.selectedInviteList)
                {
     inviteid=list.getInviteId();
     break;
                }
				ArrayList<InviteDataClass> selectedInviteData = adapter.getSelectedInviteData();
				if(selectedMode == BY_EMAIL){
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.setType("plain/text");
					
					ArrayList<String> email = new ArrayList<String>();
					for(InviteDataClass dInvite : selectedInviteData){
						email.add(dInvite.getEmail());
						sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, email.toArray(new String[email.size()]));
					}
					
					sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedit invite List");
					sendIntent.putExtra(Intent.EXTRA_TEXT,  Html.fromHtml(inviteString) + "\n"+ "http://wedit.dooble.mobi/?invitedID="+ inviteid +"&wedID="+HomeActivity.weddingId);
				//	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));
					startActivity(sendIntent);
					finish();
					 /*InviteManagementActivity.ACTIVITY_FLAG_INVITE=true;
		        	 Intent i =new Intent(SmartArriveActivity.this,TabActivityMy.class);
		        	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			         startActivity(i);
			         finish();*/
				}else if(selectedMode == BY_SMS){
					String separator = "; ";


					 if(android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung")){
					    separator = ", ";
					 }
					 
					 try {
						 String number = "";
						 for(InviteDataClass dInvite : selectedInviteData){
							 	if(number.trim().equalsIgnoreCase(""))
							 		number = dInvite.getPhone();
							 	else
							 		number = number + separator + dInvite.getPhone();
							}
		                 Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		                 sendIntent.putExtra("address", number);
		                 sendIntent.putExtra("sms_body", inviteString + "\n"+ "http://wedit.dooble.mobi/?invitedID="+ inviteid +"&wedID="+HomeActivity.weddingId);
		                 sendIntent.setType("vnd.android-dir/mms-sms");
		                 startActivity(sendIntent);
		                 finish();
		                 /*InviteManagementActivity.ACTIVITY_FLAG_INVITE=true;
			        	 Intent i =new Intent(SmartArriveActivity.this,TabActivityMy.class);
			        	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				         startActivity(i);
				         finish();*/

		            } catch (Exception e) {
		                Toast.makeText(getApplicationContext(),
		                    "SMS faild, please try again later!",
		                    Toast.LENGTH_LONG).show();
		                e.printStackTrace();
		            }
				}	
			}
		});
		
	}
	class GetCategorywiseTask extends AsyncTask<Void, Void, ArrayList<InviteDataClass>>{

		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(SmartArriveActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(SmartArriveActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<InviteDataClass> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=getinvited");
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

				ArrayList<InviteDataClass> tData = new ArrayList<InviteDataClass>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObjMain = jArray.getJSONObject(i);
					/*JSONObject jObjMain = jArray.getJSONObject(i);
					InviteDataClass HDate = new InviteDataClass();
					HDate.setType(TaskDataClass.SECTION);
					HDate.setName(jObjMain.getString("weditusercategory_name"));
					HDate.setInviteId(jObjMain.getInt("weditusercategory_id"));
					tData.add(HDate);*/
					JSONArray jArrTask = jObjMain.getJSONArray("users");
					int tLen = jArrTask.length();
					for(int j=0;j<tLen;j++){
						JSONObject jObj = jArrTask.getJSONObject(j);
						InviteDataClass tObj = new InviteDataClass();
						tObj.setName(jObj.getString("name"));
						tObj.setInviteId(jObj.getInt("id"));
						tObj.setFamilyName(jObj.getString("fname"));
						tObj.setWeddingId(jObj.getInt("weddingid"));
						tObj.setNoOfInvites(jObj.getInt("numberofinvites"));
						tObj.setApprove(jObj.getInt("approve"));
						tObj.setAddress(jObj.getString("address"));
						tObj.setVegetarian(jObj.getInt("vegetarian"));
						tObj.setVegan(jObj.getInt("vegan"));
						tObj.setDisabled(jObj.getInt("disabled"));
						tObj.setChild(jObj.getInt("child"));
						tObj.setBabies(jObj.getInt("babys"));
						tObj.setInVitationSent(jObj.getBoolean("invitationsent"));
						tObj.setActuallyReached(jObj.getInt("acctuallyreached"));
						tObj.setGigt(jObj.getString("gift"));
						String phone = jObj.getString("phone"); 
						tObj.setPhone(phone);
						String email = jObj.getString("email"); 
						tObj.setEmail(email);
						
						if(selectedMode == BY_EMAIL){
							if(!email.trim().equalsIgnoreCase("")){
								tData.add(tObj);
							}
						}else if(selectedMode == BY_SMS){
							if(!phone.trim().equalsIgnoreCase("")){
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
		protected void onPostExecute(ArrayList<InviteDataClass> result) {
			if(result != null){
				
				adapter = new CASmartArrive(SmartArriveActivity.this, R.layout.row_smartarrive, result);
				lvInviteList.setAdapter(adapter);
			}
			pd.dismiss();
			
			GetInviteString exe = new GetInviteString();
			exe.execute();
			super.onPostExecute(result);
		}

	}
	class GetInviteString extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(SmartArriveActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(SmartArriveActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected JSONObject doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=smartinvitetext");
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
				JSONObject jO = new JSONObject(responseThis);
				return jO;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(JSONObject result) {
			if(result != null){
				try {
					if(selectedMode == BY_EMAIL){
						inviteString = result.getString("texthtml");
					}
					else if(selectedMode==BY_SMS){
					inviteString = result.getString("text");
					}
					/*String m = result.getString("message");
					id = m.substring(m.lastIndexOf(" ")+1);*/
				//	Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
					email_of_user = result.getString("weditusercategory_id");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	
}
