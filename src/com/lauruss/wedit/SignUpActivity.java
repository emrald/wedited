package com.lauruss.wedit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends Activity{
	Button btnBack,btnSignup;
	EditText etBrideMail,etGroomMail,etPassword,etConfirmPassword;
	CheckBox chbDisclaimer;
	TextView tvDisclaimer;
	ProgressDialog pd;

	public static String FLAG_WEDDING_ID = "wedding_id";
	public static String FLAG_USER_ID = "user_id";
	public static String FLAG_USER_EMAIL = "user_email";
	String brideMail = "";
	String groomMail = "";
	String password = "";
	String congirmPass = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		btnBack = (Button)findViewById(R.id.signup_btn_back);
		btnSignup = (Button)findViewById(R.id.signup_btn_connect);
		etBrideMail = (EditText)findViewById(R.id.signup_et_bride_email);
		etGroomMail = (EditText)findViewById(R.id.signup_et_groom_email);
		etPassword = (EditText)findViewById(R.id.signup_et_password);
		etConfirmPassword = (EditText)findViewById(R.id.signup_et_confirm_password);
		chbDisclaimer = (CheckBox)findViewById(R.id.signup_chb_disclaimer);
		tvDisclaimer = (TextView)findViewById(R.id.signup_tv_disclaimer);

		tvDisclaimer.setText(Html.fromHtml(getString(R.string.text_disclaimer)));


		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//(brideMail != null && !brideMail.equalsIgnoreCase("")) || (groomMail != null && !groomMail.equalsIgnoreCase(""))
		
		btnSignup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				brideMail = etBrideMail.getText().toString().trim();
				groomMail = etGroomMail.getText().toString().trim();
				password = etPassword.getText().toString().trim();
				congirmPass = etConfirmPassword.getText().toString().trim();

				if(password != null && !password.equalsIgnoreCase("") && congirmPass != null && !congirmPass.equalsIgnoreCase("")){
					if((brideMail != null && !brideMail.equalsIgnoreCase("")) || (groomMail != null && !groomMail.equalsIgnoreCase(""))){
						if(password.equals(congirmPass)){
							if(isEmailValid(brideMail) || isEmailValid(groomMail)){
								if(chbDisclaimer.isChecked()){
									if(password.length() > 5){
										if(password.equals(congirmPass)){
											if(isNetworkAvailable()){
												SignUP exe = new SignUP();
												exe.execute(MyApplication.BASE_API_URL+"users?action=registerbrideandgroom");
											}else{
												Toast.makeText(SignUpActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
											}
										}else{
											Toast.makeText(SignUpActivity.this, R.string.signup_password_confirmpassword_not_match, Toast.LENGTH_SHORT).show();
										}
									}else{
										Toast.makeText(SignUpActivity.this, R.string.signup_password_lenth, Toast.LENGTH_SHORT).show();
									}
								}else{
									Toast.makeText(SignUpActivity.this, R.string.signup_accept_terms, Toast.LENGTH_SHORT).show();
								}
							}else{
								Toast.makeText(SignUpActivity.this, R.string.signup_not_valid_email, Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(SignUpActivity.this, R.string.signup_password_not_match, Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(SignUpActivity.this, "Please enter bride or groom email address.", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(SignUpActivity.this, R.string.text_enter_all_fields, Toast.LENGTH_SHORT).show();
				}

			}
		});
		tvDisclaimer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bn = new Bundle();
				bn.putInt(SettingDetailActivity.FALG_PAGE_ID, 19);
				Intent in = new Intent(SignUpActivity.this,SettingDetailActivity.class);
				in.putExtras(bn);
				startActivity(in);

			}
		});
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	class SignUP extends AsyncTask<String, Void, Integer>{
		String message = "";
		int weddingId = 0;
		@Override
		protected void onPreExecute() {
	//		pd = ProgressDialog.show(SignUpActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(SignUpActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(String... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(params[0]);
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				if(!brideMail.equalsIgnoreCase("")){
					nameValuePairs.add(new BasicNameValuePair("bride", brideMail));
				}if(!groomMail.equalsIgnoreCase("")){
					nameValuePairs.add(new BasicNameValuePair("groom", groomMail));
				}
				
				nameValuePairs.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("confirmpassword", congirmPass));
				nameValuePairs.add(new BasicNameValuePair("rules", "1"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity());
				Log.e("API response", responseThis);
				JSONObject json = new JSONObject(responseThis);
				boolean status = json.getBoolean("success");
				if(status){
					weddingId = json.getInt("weddingid");
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
				Editor editor = MyApplication.getSharedPreference().edit();
				editor.putInt(FLAG_WEDDING_ID, weddingId);
				if(!brideMail.equalsIgnoreCase("")){
					editor.putString(FLAG_USER_EMAIL, brideMail);
				}else{
					editor.putString(FLAG_USER_EMAIL, groomMail);
				}
				editor.commit();
				Intent in = new Intent(SignUpActivity.this,TabActivityMy.class);
				finish();
				LoginActivity.LOGIN_ACTIVITY.finish();
				startActivity(in);
			}else if(result == 1){
				Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(SignUpActivity.this, "Please check internet connection or error occured.", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}

}
