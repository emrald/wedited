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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContactActivity extends Activity{

	ProgressDialog pd;
	Button btnBack,btnSend,btnSubject;
	TextView tvSubject;
	EditText etName,etCellularPhone,etEmail,etFreeText;
	RelativeLayout rlSubject;
	SharedPreferences prefemail,prefcell;
	 String prefstremail;
	CACategoryList adapterCat;
	CategoryData selectedCatData;
	
	String name = "",email = "",cell = "",text = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		btnBack = (Button)findViewById(R.id.contact_btn_back);
		btnSend =(Button)findViewById(R.id.contact_btn_send);
		
		etName = (EditText)findViewById(R.id.contact_et_name);
		etCellularPhone = (EditText)findViewById(R.id.contact_et_cellularphone);
		etEmail = (EditText)findViewById(R.id.contact_et_email);
		etFreeText = (EditText)findViewById(R.id.contact_et_freetext);
		
		btnSubject = (Button)findViewById(R.id.contact_btn_subject);
		rlSubject = (RelativeLayout)findViewById(R.id.contact_rl_subject);
		tvSubject = (TextView)findViewById(R.id.contact_tv_subject);
		
		  prefemail=getSharedPreferences("Login",MODE_WORLD_WRITEABLE);
		  prefstremail=prefemail.getString(SignUpActivity.FLAG_USER_EMAIL,"");
		  
		  etEmail.setText(prefstremail);
		
		GetSubjectList exe = new GetSubjectList();
		exe.execute();
		
		 prefcell=getSharedPreferences("cellinfo",0);
         String strcell=prefcell.getString("cell","");
         String strfname=prefcell.getString("fname","");
         
         etCellularPhone.setText(strcell);
         etName.setText(strfname);
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		btnSubject.setOnClickListener(topicClickListner);
		rlSubject.setOnClickListener(topicClickListner);
		tvSubject.setOnClickListener(topicClickListner);
		
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name = etName.getText().toString().trim();
				email = etEmail.getText().toString().trim();
				cell = etCellularPhone.getText().toString().trim();
				text = etFreeText.getText().toString().trim();
				
				if(name != null && !name.equalsIgnoreCase("")){
					if(email != null && !email.equalsIgnoreCase("")){
						if(isEmailValid(email)){
							if(cell != null && !cell.equalsIgnoreCase("")){
								if(isPhoneValid(cell) == false){
									Toast.makeText(ContactActivity.this, "Please enter valid phone.", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							if(text != null && !text.equalsIgnoreCase("")){
								if(text.length() > 4000){
									Toast.makeText(ContactActivity.this, "Text lenght should be less than 4000.", Toast.LENGTH_SHORT).show();
									return;
								}
							}
							ContactUs exe = new ContactUs();
							exe.execute();
							
						}else{
							Toast.makeText(ContactActivity.this, "Please enter valid email.", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(ContactActivity.this, "Please enter email.", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ContactActivity.this, "Please enter name.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	OnClickListener topicClickListner = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AlertDialog.Builder diag = new AlertDialog.Builder(ContactActivity.this);
			diag.setAdapter(adapterCat, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedCatData = adapterCat.getItem(which);
					tvSubject.setText(ContactActivity.this.getString(R.string.contact_subject)+" - "+selectedCatData.getCatName());
					dialog.dismiss();

				}
			});

			diag.show();
			
		}
	};
	
	class ContactUs extends AsyncTask<Void, Void, Integer>{
		String message = "";
		int weddingId = 0;
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(ContactActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(ContactActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"contactus?action=contac");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				if(!cell.equalsIgnoreCase("")){
					nameValuePairs.add(new BasicNameValuePair("cell", cell));
				}if(!text.equalsIgnoreCase("")){
					nameValuePairs.add(new BasicNameValuePair("text", text));
				}
				
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("email", email));
				if(selectedCatData != null){
					nameValuePairs.add(new BasicNameValuePair("subject", String.valueOf(selectedCatData.getCatId())));
				}
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity());
				Log.e("API response", responseThis);
				JSONObject json = new JSONObject(responseThis);
				boolean status = json.getBoolean("success");
				if(status){
					//message = json.getString("message");
					return 0;
				}
				else {
					message = json.getString("message");
					return 1;

				}
				

			} catch (Exception e) {
				e.printStackTrace();
				return 2;

			}

		}
		@Override
		protected void onPostExecute(Integer result) {
			pd.dismiss();
			if(result == 0){
				Toast.makeText(ContactActivity.this, "your request has been send.", Toast.LENGTH_SHORT).show();
			//	finish();
				Intent intent = new Intent(ContactActivity.this, TabActivityMy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(intent);
				finish();
			}else if(result == 2){
				Toast.makeText(ContactActivity.this, "Error occured.", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(ContactActivity.this, message, Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(result);
		}

	}
	/*public void clearAllViews(){
		etName.setText("");
		etEmail.setText("");
		etCellularPhone.setText("");
		etFreeText.setText("");
		selectedCatData = null;
	}*/
	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	public static boolean isPhoneValid(CharSequence phoneNumber) {
		return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
	}
	class GetSubjectList extends AsyncTask<Void, Void, ArrayList<CategoryData>>{

		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(ContactActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(ContactActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<CategoryData> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"contactus?action=getcontactsubjects");
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
				adapterCat = new CACategoryList(ContactActivity.this, R.layout.row_task, result);
				//lvTask.setAdapter(adapter);
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
}
