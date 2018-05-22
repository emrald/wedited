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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePersonalInformationActivity extends Activity{
	//for do date
	private int year;
	private int month;
	private int day;
	
	private int cYear;
	private int cMonth;
	private int cDay;
	
	static final int START_DATE_DIALOG_ID = 1;
	ProgressDialog pd;
	

	String strBrideName,strGroomName,strBrideEmail,strGroomEmail,strBridePhone,strGroomPhone,strBrideAddress;
	Button btnBack,btnUpdateInformation,btnWeddingDate,btnUpdateInformationsave;
	EditText etBrideName,etGroomName,etBrideEmail,etGroomEmail,etBridePhone,etGroomPhone,etBrideAddress;
	RelativeLayout rlWeddingDate;
	TextView tvWeddingDate;
	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	Calendar currentC , calendarCurrent;
	 SharedPreferences sprefcell,isDate;
	String weddingDate = "";
	
	List<NameValuePair> nameValuePairs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatepersonalinformation);

		btnBack = (Button)findViewById(R.id.updatepersonal_btn_back);
		btnUpdateInformation = (Button)findViewById(R.id.updatepersonal_btn_updateinformation);
		btnUpdateInformationsave = (Button)findViewById(R.id.updatepersonal_btn_save);
		etBrideName = (EditText)findViewById(R.id.updatepersonal_et_bride_name);
		etGroomName = (EditText)findViewById(R.id.updatepersonal_et_groom_name);
		etBrideEmail = (EditText)findViewById(R.id.updatepersonal_et_bride_emai);
		etGroomEmail = (EditText)findViewById(R.id.updatepersonal_et_groom_emai);
		etBridePhone = (EditText)findViewById(R.id.updatepersonal_et_bride_phone);
		etGroomPhone = (EditText)findViewById(R.id.updatepersonal_et_groom_phone);
		etBrideAddress = (EditText)findViewById(R.id.updatepersonal_et_bride_address);
		rlWeddingDate = (RelativeLayout)findViewById(R.id.updatepersonal_rl_dateofwedding);
		tvWeddingDate = (TextView)findViewById(R.id.updatepersonal_tv_dateofwedding);
		btnWeddingDate = (Button)findViewById(R.id.updatepersonal_btn_dateofwedding);
		
		
		currentC = Calendar.getInstance();
		
		calendarCurrent = Calendar.getInstance();
		cYear = calendarCurrent.get(Calendar.YEAR);
		cMonth = calendarCurrent.get(Calendar.MONTH);
		cDay = calendarCurrent.get(Calendar.DAY_OF_MONTH);
		
		rlWeddingDate.setOnClickListener(weddingDateClickListner);
		tvWeddingDate.setOnClickListener(weddingDateClickListner);
		btnWeddingDate.setOnClickListener(weddingDateClickListner);
		
		SharedPreferences pref = MyApplication.getSharedPreference();
		long dateMillis = pref.getLong(MyApplication.FLAG_WEDDING_DATE, 0);
		/*if(dateMillis != 0){
			currentC.setTimeInMillis(dateMillis);
			weddingDate = dateFormatter.format(currentC.getTime());
			tvWeddingDate.setText(weddingDate+"-"+getString(R.string.updatepersonal_dateofwedding));
		}*/
		GetUserInfo exe = new GetUserInfo();
		exe.execute();
		
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		btnUpdateInformation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(calendarCurrent.before(currentC)){
					if(validateInsertedData()){
						SharedPreferences.Editor prefEditor = MyApplication.getSharedPreference().edit();
						prefEditor.putLong(MyApplication.FLAG_WEDDING_DATE, currentC.getTimeInMillis());
						prefEditor.commit();
						UpdatePersonalInfo exe = new UpdatePersonalInfo();
						exe.execute();
					}
				}else{
					Toast.makeText(UpdatePersonalInformationActivity.this, "wedding date can not be in past.\nPlease select date again.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		btnUpdateInformationsave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(calendarCurrent.before(currentC)){
					if(validateInsertedData()){
						SharedPreferences.Editor prefEditor = MyApplication.getSharedPreference().edit();
						prefEditor.putLong(MyApplication.FLAG_WEDDING_DATE, currentC.getTimeInMillis());
						prefEditor.commit();
						UpdatePersonalInfo exe = new UpdatePersonalInfo();
						exe.execute();
					}
				}else{
					Toast.makeText(UpdatePersonalInformationActivity.this, "wedding date can not be in past.\nPlease select date again.", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
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
				weddingDate = dateFormatter.format(currentC.getTime());
				tvWeddingDate.setText(weddingDate+"-"+getString(R.string.updatepersonal_dateofwedding));
			}else{
				Toast.makeText(UpdatePersonalInformationActivity.this, "wedding date can not be in past.", Toast.LENGTH_SHORT).show();
				//Log.e("In end date lisner", "In else");
			}
		}
	};
	class GetUserInfo extends AsyncTask<Void, Void, String>{

		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(UpdatePersonalInformationActivity.this, "", "×˜×•×¢×Ÿ × ×ª×•× ×™×�");
			pd = ProgressDialog.show(UpdatePersonalInformationActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"users?action=getallinformation");
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
				
				return responseThis;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(String result) {
			if(result != null && !result.trim().equalsIgnoreCase("")){
				try {
					JSONArray jObjArray = new JSONArray(result);
					JSONObject jObj = jObjArray.getJSONObject(0);
					
					weddingDate = jObj.getString("wedding_date");
					
					JSONArray jArrGroom = jObj.getJSONArray("groom");
					JSONArray jArrBride = jObj.getJSONArray("bride");
					
					JSONObject jObjGroom = jArrGroom.getJSONObject(0);
					JSONObject jObjBride = jArrBride.getJSONObject(0);
					
					etBrideEmail.setText(jObjBride.getString("username"));
					etGroomEmail.setText(jObjGroom.getString("username"));
					
					etBridePhone.setText(jObjBride.getString("cell"));
					etGroomPhone.setText(jObjGroom.getString("cell"));
					
					etBrideName.setText(jObjBride.getString("fname")+" "+jObjBride.getString("lname"));
					etGroomName.setText(jObjGroom.getString("fname")+" "+jObjGroom.getString("lname"));
					tvWeddingDate.setText(weddingDate+"-"+getString(R.string.updatepersonal_dateofwedding));
					currentC.setTime(dateFormatter.parse(weddingDate));
					day = currentC.get(Calendar.DAY_OF_MONTH);
					month = currentC.get(Calendar.MONTH);
					year = currentC.get(Calendar.YEAR);
					
					sprefcell=getSharedPreferences("cellinfo", 0);
				     SharedPreferences.Editor editor=sprefcell.edit();
				        editor.putString("cell",jObjBride.getString("cell"));
				        editor.putString("fname",jObjBride.getString("fname"));
				        editor.commit();
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	class UpdatePersonalInfo extends AsyncTask<Void, Void, Boolean>{
		String message="";
		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(UpdatePersonalInformationActivity.this, "", "×˜×•×¢×Ÿ × ×ª×•× ×™×�");
			pd = ProgressDialog.show(UpdatePersonalInformationActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"users?action=updatedetails");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONObject jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				message = jObj.getString("message");
				if(status==true)
				{
					isDate=getSharedPreferences("date", 0);
				     SharedPreferences.Editor editor=isDate.edit();
				        editor.putInt("wedDate",1);
				   //     editor.putString("fname",jObjBride.getString("fname"));
				        editor.commit();
					
				}
				return status;
			} catch (Exception e) {
				e.printStackTrace();
				return false;	
			}

		}
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(UpdatePersonalInformationActivity.this, message, Toast.LENGTH_SHORT).show();
				finish();
			}else{
				if(message.equalsIgnoreCase("")){
					Toast.makeText(UpdatePersonalInformationActivity.this, "Error occured or no internet coonection.", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(UpdatePersonalInformationActivity.this, message, Toast.LENGTH_SHORT).show();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	public boolean validateInsertedData(){
		strBrideName = etBrideName.getText().toString().trim();
		strGroomName = etGroomName.getText().toString().trim();
		strBrideEmail = etBrideEmail.getText().toString().trim();
		strGroomEmail = etGroomEmail.getText().toString().trim();
		strBridePhone = etBridePhone.getText().toString().trim();
		strGroomPhone = etGroomPhone.getText().toString().trim();
		strBrideAddress = etBrideAddress.getText().toString().trim();
		
		
		nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
		if(strBrideName != null && !strBrideName.equalsIgnoreCase("")){
			nameValuePairs.add(new BasicNameValuePair("brideName", strBrideName));
		}
		if(strGroomName != null && !strGroomName.equalsIgnoreCase("")){
			nameValuePairs.add(new BasicNameValuePair("groomname", strGroomName));
		}
		if(strBrideEmail != null && !strBrideEmail.equalsIgnoreCase("")){
			if(isEmailValid(strBrideEmail)){
				nameValuePairs.add(new BasicNameValuePair("brideemail", strBrideEmail));
			}else{
				Toast.makeText(UpdatePersonalInformationActivity.this, "Please enter valid bride email.", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(strGroomEmail != null && !strGroomEmail.equalsIgnoreCase("")){
			if(isEmailValid(strGroomEmail)){
				nameValuePairs.add(new BasicNameValuePair("groomemail", strGroomEmail));
			}else{
				Toast.makeText(UpdatePersonalInformationActivity.this, "Please enter valid Groom email.", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(strBridePhone != null && !strBridePhone.equalsIgnoreCase("")){
			if(isPhoneValid(strBridePhone)){
				nameValuePairs.add(new BasicNameValuePair("bridephone", strBridePhone));
			}else{
				Toast.makeText(UpdatePersonalInformationActivity.this, "Please enter valid bride Phone.", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(strGroomPhone != null && !strGroomPhone.equalsIgnoreCase("")){
			if(isPhoneValid(strGroomPhone)){
				nameValuePairs.add(new BasicNameValuePair("groomphone", strGroomPhone));
			}else{
				Toast.makeText(UpdatePersonalInformationActivity.this, "Please enter valid groom Phone.", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(strBrideAddress != null && !strBrideAddress.equalsIgnoreCase("")){
			if(strBrideAddress.length() <= 400){
				nameValuePairs.add(new BasicNameValuePair("address", strBrideAddress));
			}else{
				Toast.makeText(UpdatePersonalInformationActivity.this, "Please enter address in 400 character", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		if(!weddingDate.equalsIgnoreCase("")){
			nameValuePairs.add(new BasicNameValuePair("weddingDate", weddingDate));
		}
		
		return true;
		
	}
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
	boolean isEmailValid(CharSequence email) {
		   return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	boolean isPhoneValid(CharSequence phone) {
		   return android.util.Patterns.PHONE.matcher(phone).matches();
	}

}
