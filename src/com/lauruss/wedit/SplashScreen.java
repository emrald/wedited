package com.lauruss.wedit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends Activity{
	
	SharedPreferences pref;
	private static final int SPLASH_DISPLAY_TIME = 3000; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		// pref =  getApplicationContext().getSharedPreferences(SignUpActivity.FLAG_WEDDING_ID,0);
		pref = MyApplication.getSharedPreference();
		
		 new Handler().postDelayed(new Runnable() {
				public void run() {
					int userid = pref.getInt(SignUpActivity.FLAG_WEDDING_ID, 0);
				//	Toast.makeText(getApplicationContext(), "ID"+userid, Toast.LENGTH_SHORT).show();
		    		if(userid!=0)
		    		{
		    			Intent intent=new Intent(SplashScreen.this,TabActivityMy.class);
		    			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    			startActivity(intent);
		    		}
		    		else
		    		{
		    			Intent mainIntent = new Intent(SplashScreen.this,LoginActivity.class);
		    			SplashScreen.this.startActivity(mainIntent);
		    		}
		    		/*SplashScreen.this.finish();				
					Intent mainIntent = new Intent(SplashScreen.this,
							LoginActivity.class);
					SplashScreen.this.startActivity(mainIntent);
*/
					SplashScreen.this.finish();				
					 
					overridePendingTransition(R.anim.mainfadein,
							R.anim.splashfadeout);
				}
			}, SPLASH_DISPLAY_TIME);
	}
	@Override
	public void onBackPressed() {
		
	}

}
