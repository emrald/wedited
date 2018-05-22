package com.lauruss.wedit;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button btnback,btnhome;
	Intent intent;
	static boolean MAIN_HOMEFLAG,MAINLOGIN_FLAG,FLAG=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FLAG=true;
		/*btnback=(Button)findViewById(R.id.activity_main_btnback);
		btnhome=(Button)findViewById(R.id.activity_main_btnhome);
		
		btnback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MAINLOGIN_FLAG=true;
				intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});
		
		btnhome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent=new Intent(MainActivity.this,TabActivityMy.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				
				intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
			}
		});*/
	}
	@Override
	 protected void onResume() {
	  // TODO Auto-generated method stub
	  
	  if(CATaskList.aldeletetask.size()>0)
	  { 
	   for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
	   {
	    DeleteTask delexe = new DeleteTask(MainActivity.this);
	    delexe.execute(taskdeleteobj.getTaskId());
	   }
	  }
	  super.onResume();
	 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
