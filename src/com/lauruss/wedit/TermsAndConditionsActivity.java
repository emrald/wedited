package com.lauruss.wedit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TermsAndConditionsActivity extends Activity{
	
	
	Button btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_termsandconditions);
		
		btnBack = (Button)findViewById(R.id.termsandconditions_btn_back);
		
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

}
