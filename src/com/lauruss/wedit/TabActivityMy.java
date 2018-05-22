package com.lauruss.wedit;


import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

public class TabActivityMy extends TabActivity{
	static TabHost tabHost;
	static boolean FLAG_TAB=false;
//	ImageView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Animation translatebu2= AnimationUtils.loadAnimation(this, R.anim.login_anim);
		setContentView(R.layout.activity_tab);
		// image= (ImageView)findViewById(R.id.image);
		setTab();
		
		//Toast.makeText(getApplicationContext(), "TAB", Toast.LENGTH_SHORT).show();
		/*if(AddNewTaskActivity.ACTIVITY_FLAG==false || AddInvitation.ACTIVITY_FLAG_INVITE==false)
		{
			tabHost.setCurrentTab(2);
		}*/
		if(AddNewTaskActivity.ACTIVITY_FLAG)
		  {
		   AddNewTaskActivity.ACTIVITY_FLAG=false;
		   tabHost.setCurrentTab(4);
		  }
		else if(AddInvitation.ACTIVITY_FLAG_INVITE)
		{
			AddInvitation.ACTIVITY_FLAG_INVITE=false;
			tabHost.setCurrentTab(1);
		}
		/*else if(InviteManagementActivity.ACTIVITY_FLAG_INVITEMANAGEMENT==true)
		{
			InviteManagementActivity.ACTIVITY_FLAG_INVITEMANAGEMENT=false;
			tabHost.setCurrentTab(2);
		}*/
			
		/*else if(LoginActivity.FLAG_HOME==true)
		{
			 tabHost.setBackgroundResource(R.drawable.tab_home_unselect);
			 tabHost.setCurrentTab(2);
		}*/
		
		
		/*
		  else {
			  if(LoginActivity.FLAG_HOME==true)
				{
				//	addTab("home", R.drawable.tab_home_unselect, HomeActivity.class);
					tabHost.setCurrentTab(2);
					LoginActivity.FLAG_HOME=false;
				}*/
				else
				{
				//	if(LoginActivity.FLAG_HOME==false)
						tabHost.setCurrentTab(2);
				}
		  //}
		
		//tabHost.setCurrentTab(2);
		
	}
	public void setTab(){
		addTab("setting", R.drawable.tab_setting, SettingActivity.class);
		addTab("invitemanagement", R.drawable.tab_invitemanagemant, InviteManagementActivity.class);
		
		/*if (isNotLogin) {
		     tabHost.setCurrentTabByTag("Setting");
		}*/
		/* if(LoginActivity.FLAG_HOME==true)
		{
			*/	
			 addTab("home", R.drawable.tab_home, HomeActivity.class);
		
			/*	LoginActivity.FLAG_HOME=false;
			}
		 else{
				addTab("home", R.drawable.tab_home, HomeActivity.class);
		 	}*/
			//		Toast.makeText(getApplicationContext(), LoginActivity.FLAG_HOME+"", Toast.LENGTH_SHORT).show();
		addTab("temp", R.drawable.tab_temp, MainActivity.class);
		addTab("task", R.drawable.tab_task, TaskActivity.class);
		FLAG_TAB=true;
	}
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		
		tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	public void onTabChanged(String tabId) {
        if (tabId.equals("TAB 0")) {
              // Show your actual activity here
        } 
    }
}
