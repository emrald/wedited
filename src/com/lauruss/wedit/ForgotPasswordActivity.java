package com.lauruss.wedit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity{
	
	Button btnBack,btnSend;
	EditText etEmail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);
		
		btnBack = (Button)findViewById(R.id.forgotpassword_btn_back);
		btnSend = (Button)findViewById(R.id.forgotpassword_btn_send);
		etEmail = (EditText)findViewById(R.id.forgotpassword_et_email);
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = etEmail.getText().toString().trim();
				if(email != null && !email.equalsIgnoreCase("")){
					if(SignUpActivity.isEmailValid(email)){
						
					}else{
						Toast.makeText(ForgotPasswordActivity.this, R.string.signup_not_valid_email, Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(ForgotPasswordActivity.this, R.string.forgotpassword_pleaseenteremail, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

}
