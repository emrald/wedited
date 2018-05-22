package com.lauruss.wedit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity{

	EditText etEmail,etPassword;
	Button btnConnect,btnFacebookConnect;
	TextView tvSignUp,tvForgotPassword;

	String email = "";
	String pass = "";

	ProgressDialog pd;
	//	private Facebook facebook;
	//	private AsyncFacebookRunner mAsyncRunner;
	public static Activity LOGIN_ACTIVITY;

	String FACEBOOK_ACCESS_TOKEN = "";
	boolean FACEBOOK_LOGIN = false;
	static boolean FLAG_HOME=false;
	SharedPreferences pref;
	//	String APP_ID= "163592167165863" ;
	ImageView ivLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Animation translatebu= AnimationUtils.loadAnimation(this, R.anim.login_anim);
		//		Animation translatebu2= AnimationUtils.loadAnimation(this, R.anim.login_anim);
		ivLogo = (ImageView)findViewById(R.id.login_logo);
		//	facebook = new Facebook(APP_ID);
		//       mAsyncRunner = new AsyncFacebookRunner(facebook);

		//   String access_token = facebook.getAccessToken();
		//	final OpenRequest open = new OpenRequest(this);
		/*open.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
        open.setPermissions(Arrays.asList(new String[]{"email", "publish_actions", "user_birthday", "user_hometown"}));
        open.setCallback(getApplicationContext());
        Session s = new Session(this);
        s.openForPublish(open);*/

		pref = MyApplication.getSharedPreference();
		//	pref =  getApplicationContext().getSharedPreferences(SignUpActivity.FLAG_WEDDING_ID,0);
		int weddingId = pref.getInt(SignUpActivity.FLAG_WEDDING_ID, 0);
		Log.e("weding_id", String.valueOf(weddingId));
		if(weddingId != 0){// && MainActivity.MAINLOGIN_FLAG==false){
			//Toast.makeText(getApplicationContext(), "Toast", Toast.LENGTH_SHORT).show();
			FLAG_HOME=true;

			Intent in = new Intent(LoginActivity.this,SplashLogoActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(in);

			finish();
		}

		//	 MainActivity.MAINLOGIN_FLAG=false;
		LOGIN_ACTIVITY = this;

		etEmail = (EditText)findViewById(R.id.login_et_email);
		etPassword = (EditText)findViewById(R.id.login_et_password);
		btnConnect = (Button)findViewById(R.id.login_btn_connect);
		btnFacebookConnect = (Button)findViewById(R.id.login_btn_facebook_connect);
		tvSignUp = (TextView)findViewById(R.id.login_tv_signup);
		tvForgotPassword = (TextView)findViewById(R.id.login_tv_forgot_password);

		tvSignUp.setText(Html.fromHtml(getString(R.string.text_signup)));
		tvForgotPassword.setText(Html.fromHtml(getString(R.string.text_forgot_password)));


		translatebu.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				etEmail.setVisibility(View.VISIBLE);
				etPassword.setVisibility(View.VISIBLE);
				btnConnect.setVisibility(View.VISIBLE);
				btnFacebookConnect.setVisibility(View.VISIBLE);
				tvSignUp.setVisibility(View.VISIBLE);
				tvForgotPassword.setVisibility(View.VISIBLE);

			}
		});
		ivLogo.startAnimation(translatebu);
		/*try {
		    PackageInfo info = getPackageManager().getPackageInfo(
		            "com.lauruss.wedit", PackageManager.GET_SIGNATURES);
		    for (Signature signature : info.signatures) {
		        MessageDigest md = MessageDigest.getInstance("SHA");
		        md.update(signature.toByteArray());
		        Log.e("MY KEY HASH:",
		                Base64.encodeToString(md.digest(), Base64.DEFAULT));
		    }
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		{"success":true,"weddingid":35,"userid":105}	*/
		btnConnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				email = etEmail.getText().toString().trim();
				pass = etPassword.getText().toString().trim();

				if(!email.equalsIgnoreCase("") && !pass.equalsIgnoreCase("")){

					if(isNetworkAvailable()){
						String url = MyApplication.BASE_API_URL+"users?action=login";
						//Log.e("Login url", url);
						SignInExe exe = new SignInExe();
						exe.execute(url);

					}else{
						Toast.makeText(LoginActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(LoginActivity.this, "Please enter user name and password", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnFacebookConnect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {


				Session s = new Session(LoginActivity.this);
				Session.setActiveSession(s);
				Session.OpenRequest request = new Session.OpenRequest(LoginActivity.this);

				//request.setPermissions(Arrays.asList("basic_info","email","read_friendlists")); 
				request.setPermissions(Arrays.asList("basic_info","user_photos","user_videos","offline_access","user_checkins","friends_checkins","email","user_location","user_name"));
				s.openForRead(request);// authButton.setReadPermissions

				//request.setCallback( /* your callback here */// );
				/*request.setPermissions(Arrays.asList("basic_info","user_photos","user_videos","offline_access","user_checkins","friends_checkins","email","user_location","user_name"));
			//	request.setPermissions(Arrays.asList("offline_access","publish_stream","email","user_birthday"));
				request.setPermissions(Arrays.asList("user_photos","user_videos","offline_access","user_checkins","friends_checkins","email","user_location"));
				s.openForRead(request);
				request.setPermissions(Arrays.asList("user_photos","user_videos","offline_access","user_checkins","friends_checkins","email","user_location"));
				s.openForRead(request);

			//	 request.setPermissions(Arrays.asList(new String[]{"email", "publish_actions", "user_birthday", "user_hometown"}));
				 s.openForRead(request);*/
				//	 request.setCallback((Session.StatusCallback)LoginActivity.this);
				//	 s.openForPublish(request);
				/*      Session s = new Session(this);
			//        s.openForPublish(request);

				 List<String> permissions = new ArrayList<String>();
			        permissions.add("publish_stream");
			        permissions.add("user_likes");
			        permissions.add("email");
			        permissions.add("user_birthday");
			        request.setPermissions(permissions);

			        Session session = new Session(LoginActivity.this);//.build();
			        Session.setActiveSession(session);
			        session.openForPublish(request);

				// start Facebook Login*/
				Session.openActiveSession(LoginActivity.this, true, new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							FACEBOOK_ACCESS_TOKEN = session.getAccessToken();
							Log.e("Access", FACEBOOK_ACCESS_TOKEN);
							//	http://wedit.dooble.mobi/api/users?action=facebooklogin
							String url = MyApplication.BASE_API_URL+"users?action=facebooklogin";
							//Log.e("Login url", url);
							SignInExe exe = new SignInExe();
							FACEBOOK_LOGIN = true;
							exe.execute(new String[]{url,FACEBOOK_ACCESS_TOKEN});
							Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

								@Override
								public void onCompleted(GraphUser user,
										Response response) {

									//	String email;
									try {
										email = user.getInnerJSONObject().getString("email");
										Log.e("User  "+user.getProperty("gender"), user.getName()+" First "+user.getFirstName()+" last name:"+user.getLastName()+" username : "+	user.getUsername()+" "+user.getProperty("email"));
										Intent in = new Intent(LoginActivity.this,TabActivityMy.class);
										finish();
										startActivity(in);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

							});


						}
					}
				});

			}
		});
		/*btnFacebookConnect.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		            loginToFacebook();
		        }
		});*/
		/*}
		});*/
		tvSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent in = new Intent(LoginActivity.this,SignUpActivity.class);
				startActivity(in);

			}
		});
		tvForgotPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://wedit.dooble.mobi/action?type=ResetPassword"));
					startActivity(browserIntent);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(LoginActivity.this, "No link found or wrong link.", Toast.LENGTH_SHORT).show();
				}

			}
		});


		/*@SuppressWarnings("deprecation")
	public void loginToFacebook() {
	    pref = getPreferences(MODE_PRIVATE);
	    String access_token = pref.getString("access_token", null);
	    long expires = pref.getLong("access_expires", 0);

	    if (access_token != null) {
	        facebook.setAccessToken(access_token);
	    }

	    if (expires != 0) {
	        facebook.setAccessExpires(expires);
	    }

	    if (!facebook.isSessionValid()) {
	        facebook.authorize(this,
	                new String[] { "email", "publish_stream" },
	                new DialogListener() {

	                    @Override
	                    public void onCancel() {
	                        // Function to handle cancel event
	                    }

	                    @Override
	                    public void onComplete(Bundle values) {
	                        // Function to handle complete event
	                        // Edit Preferences and update facebook acess_token
	                        SharedPreferences.Editor editor = pref.edit();
	                        editor.putString("access_token",
	                                facebook.getAccessToken());
	                        editor.putLong("access_expires",
	                                facebook.getAccessExpires());
	                        editor.commit();
	                    }

						@Override
						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub

						}



	                });*/

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	private class SignInExe extends AsyncTask<String, Void, Integer>{
		String message = "";
		int weddingId = 0,userId = 0;
		@Override
		protected void onPreExecute() {
			//	pd = ProgressDialog.show(LoginActivity.this, "", "טוען נתונים");
			pd = ProgressDialog.show(LoginActivity.this, "", "אנא המתן");
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
				List<NameValuePair> nameValuePairs;
				if(FACEBOOK_LOGIN){
					nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("accessToken", params[1]));
				}else{
					nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("Username", email));
					nameValuePairs.add(new BasicNameValuePair("Password", pass));
				}

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity());
				Log.e("API response", responseThis);
				JSONObject json = new JSONObject(responseThis);

				boolean status = json.getBoolean("success");
				if(status){
					if(FACEBOOK_LOGIN){
						weddingId = json.getInt("weddingid");
						//userId = json.getInt("userid");
					}else{
						weddingId = json.getInt("weddingid");
						userId = json.getInt("userid");
					}

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
			super.onPostExecute(result);
			pd.dismiss();
			FACEBOOK_LOGIN = false;
			if(result == 0){
				try {
					SharedPreferences.Editor editor = pref.edit();
					//	Editor editor = MyApplication.getSharedPreference().edit();
					editor.putInt(SignUpActivity.FLAG_WEDDING_ID, weddingId);
					editor.putInt(SignUpActivity.FLAG_USER_ID, userId);
					editor.putString(SignUpActivity.FLAG_USER_EMAIL, email);
					editor.commit();

					Toast.makeText(LoginActivity.this, "Successfully Login.", Toast.LENGTH_SHORT).show();

					Intent in = new Intent(LoginActivity.this,TabActivityMy.class);
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);

					//	finish();

				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}else if(result == 1){
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(LoginActivity.this, "No internet connection or Some error occured.", Toast.LENGTH_SHORT).show();
			}
		}

	}
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
}
