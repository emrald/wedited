package com.lauruss.wedit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashLogoActivity extends Activity{
	ImageView ivlogo;
	EditText etEmail,etPassword;
	Button btnConnect,btnFacebookConnect;
	TextView tvSignUp,tvForgotPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashlogo);
		etEmail = (EditText)findViewById(R.id.login_et_email);
		etPassword = (EditText)findViewById(R.id.login_et_password);
		ivlogo = (ImageView)findViewById(R.id.login_logo);
		btnConnect = (Button)findViewById(R.id.login_btn_connect);
		btnFacebookConnect = (Button)findViewById(R.id.login_btn_facebook_connect);
		tvSignUp = (TextView)findViewById(R.id.login_tv_signup);
		tvForgotPassword = (TextView)findViewById(R.id.login_tv_forgot_password);
		Animation translatebu2= AnimationUtils.loadAnimation(this, R.anim.login_anim);

		translatebu2.setAnimationListener(new AnimationListener() {

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
				etEmail.setVisibility(View.INVISIBLE);
				etPassword.setVisibility(View.INVISIBLE);
				btnConnect.setVisibility(View.INVISIBLE);
				btnFacebookConnect.setVisibility(View.INVISIBLE);
				tvSignUp.setVisibility(View.INVISIBLE);
				tvForgotPassword.setVisibility(View.INVISIBLE);
				
				Intent in = new Intent(SplashLogoActivity.this,TabActivityMy.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				
				finish();

			}
		});

		ivlogo.startAnimation(translatebu2);
	}
}
