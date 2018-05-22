package com.lauruss.wedit;


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
import org.json.JSONObject;

import com.lauruss.wedit.AddNewTaskActivity.AddNewTask;
import com.lauruss.wedit.AddNewTaskActivity.GetCategoryList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus.NmeaListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AddInvitation extends Activity{

	Button btnBack,btnSave,btnSaveBottom,btndelete;
	//EditText etName,etFamilyName,etEmai,etAddress,etGift;
	EditText etName,etFamilyName,etEmai,etAddress,etGift;
	
	CheckBox chbInvitationhasBennSent;
	RelativeLayout rlCategory;
	TextView tvCategory,tvHeader;
	Button btnCategory;
	EditText etNoofInvites,etConfirmedArrival,etVegetarian,etVegan,etNoOfDisabled,etNoOfChild,etNoOfBabies,etActuallyReached,etPhone;
	static boolean ACTIVITY_FLAG_INVITE=false;
	CACategoryList adapterCat;
	ArrayList<CategoryData> catArrData;
	ProgressDialog pd;
	String name_array[];
	CategoryData selectedCatData = new CategoryData();

	List<NameValuePair> nameValueAddTaskPair;

	boolean FROM_INVITE_EDIT = false;
	
	
	int inviteId = 0,catId=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addinvitation);

		btnBack = (Button)findViewById(R.id.addinvite_btn_back);
		btnSave = (Button)findViewById(R.id.addinvite_btn_save);
		btnSaveBottom = (Button)findViewById(R.id.addinvite_btn_save_bottom);
		btndelete = (Button)findViewById(R.id.addinvite_btn_delete);
		
		etName = (EditText)findViewById(R.id.addinvite_et_name);
		etFamilyName = (EditText)findViewById(R.id.addinvite_et_familyname);
		etEmai = (EditText)findViewById(R.id.addinvite_et_email);
	//	etAddress = (EditText)findViewById(R.id.addinvite_et_address);
		etGift = (EditText)findViewById(R.id.addinvite_et_gift);
		etPhone = (EditText)findViewById(R.id.addinvite_et_phone);
		chbInvitationhasBennSent = (CheckBox)findViewById(R.id.addinvite_chb_invitationhasbeensent);

		rlCategory = (RelativeLayout)findViewById(R.id.addinvite_rl_category);

		etNoofInvites = (EditText)findViewById(R.id.addinvite_et_noofinvites);
		etConfirmedArrival = (EditText)findViewById(R.id.addinvite_et_confirmarrival);
		btnCategory = (Button)findViewById(R.id.addinvite_btn_category);
		etVegetarian = (EditText)findViewById(R.id.addinvite_et_vegetarian);
		etVegan = (EditText)findViewById(R.id.addinvite_et_vegan);
		etNoOfDisabled = (EditText)findViewById(R.id.addinvite_et_noofdisabled);
		etNoOfChild = (EditText)findViewById(R.id.addinvite_et_noofchild);
		etNoOfBabies = (EditText)findViewById(R.id.addinvite_et_noofbabies);
		etActuallyReached = (EditText)findViewById(R.id.addinvite_et_actuallyreached);

		tvCategory = (TextView)findViewById(R.id.addinvite_tv_category);

		tvHeader = (TextView)findViewById(R.id.addinvite_tv_header);
		
		btnSave.setOnClickListener(btnSaveClickListner);
		btnSaveBottom.setOnClickListener(btnSaveClickListner);

		btndelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				DeleteTask exe = new DeleteTask();
				exe.execute();
			}
		});
		
		if(isNetworkAvailable()){
			GetCategoryList exe = new GetCategoryList();
			exe.execute();
		}else{
			Toast.makeText(AddInvitation.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
		}
		String firstname,lastname;
		Intent in = getIntent();
		Bundle bn = in.getExtras();
		FROM_INVITE_EDIT = bn.getBoolean(InviteManagementActivity.FLAG_INVITE_EDIT);
		if(FROM_INVITE_EDIT){
			InviteDataClass dCalss = (InviteDataClass)in.getSerializableExtra(InviteManagementActivity.FLAG_INVITE_DATA);
			inviteId = dCalss.getInviteId();
			catId = dCalss.getUserCatId();
			
			firstname = dCalss.getName();
		
			name_array = firstname.split(" ");
			if(firstname.contains(" "))
			{
				
				etName.setText(name_array[1]+"");
				etFamilyName.setText(name_array[0]+"");
		//		Toast.makeText(getApplicationContext(), dCalss.getName(), Toast.LENGTH_SHORT).show();
			}
			else 
			{
				etFamilyName.setText(dCalss.getName());
				etName.setText(dCalss.getFamilyName());
			}
			
	//		selectedCatData = new CategoryData(catId, selectedCatData.getCatName());
			
			etNoofInvites.setText(String.valueOf(dCalss.getNoOfInvites()));
			etConfirmedArrival.setText(String.valueOf(dCalss.getApprove()));
			etEmai.setText(dCalss.getEmail());
			//Toast.makeText(getApplicationContext(), dCalss.getEmail(), Toast.LENGTH_SHORT).show();
			//etAddress.setText(dCalss.getAddress());
			etVegetarian.setText(String.valueOf(dCalss.getVegetarian()));
			etVegan.setText(String.valueOf(dCalss.getVegan()));
			etNoOfDisabled.setText(String.valueOf(dCalss.getDisabled()));
			etNoOfChild.setText(String.valueOf(dCalss.getChild()));
			etNoOfBabies.setText(String.valueOf(dCalss.getBabies()));
			chbInvitationhasBennSent.setChecked(dCalss.getInVitationSent());
			etActuallyReached.setText(String.valueOf(dCalss.getActuallyReached()));
			etGift.setText(dCalss.getGigt());
			etPhone.setText(dCalss.getPhone());
			 tvCategory.setText(selectedCatData.getCatName());
			/*selectedCatData = null;
			selectedCatData = new CategoryData();*/
		//	 tvCategory.setText(AddInvitation.this.getString(R.string.text_category)+" - "+selectedCatData.getCatName());
	//		tvCategory.setText(dCalss.get);ss
			/*inviteId = dCalss.getInviteId();
			   etName.setText(dCalss.getName());
			   etFamilyName.setText(dCalss.getFamilyName());
			   etNoofInvites.setText(String.valueOf(dCalss.getNoOfInvites()));
			   etConfirmedArrival.setText(String.valueOf(dCalss.getApprove()));
			   etEmai.setText(dCalss.getEmail());
			   //Toast.makeText(getApplicationContext(), dCalss.getEmail(), Toast.LENGTH_SHORT).show();
			   //etAddress.setText(dCalss.getAddress());
			   etVegetarian.setText(String.valueOf(dCalss.getVegetarian()));
			   etVegan.setText(String.valueOf(dCalss.getVegan()));
			   etNoOfDisabled.setText(String.valueOf(dCalss.getDisabled()));
			   etNoOfChild.setText(String.valueOf(dCalss.getChild()));
			   etNoOfBabies.setText(String.valueOf(dCalss.getBabies()));
			   chbInvitationhasBennSent.setChecked(dCalss.getInVitationSent());
			   etActuallyReached.setText(String.valueOf(dCalss.getActuallyReached()));
			   etGift.setText(dCalss.getGigt());
			   etPhone.setText(dCalss.getPhone());
			//   tvCategory.setText(dCalss.getInviteSpnCatName()+"R");
			   selectedCatData = null;
			   selectedCatData = new CategoryData();*/
			/*if(dCalss.getUserCatId()+""==null)
			   {
			    selectedCatData.setCatId(CACategoryList.catid);
			    
			   }
			   else{
			   selectedCatData.setCatId(dCalss.getUserCatId());
			   }*/
			//selectedCatData.setCatId(dCalss.getUserCatId());
			
		//	tvHeader.setText(R.string.editinvite_title);
			tvHeader.setText(dCalss.getWeditUserCatName());
		//	Toast.makeText(getApplicationContext(),selectedCatData.getCatName()+"", Toast.LENGTH_SHORT).show();
			
		}
		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		rlCategory.setOnClickListener(categoryClickListner);
		tvCategory.setOnClickListener(categoryClickListner);
		btnCategory.setOnClickListener(categoryClickListner);
	}
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	OnClickListener btnSaveClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String name = etFamilyName.getText().toString().trim();
			String fName = etName.getText().toString().trim();
			String noOfInvites = etNoofInvites.getText().toString().trim();
			String confirmApprival = etConfirmedArrival.getText().toString().trim();
			String email = etEmai.getText().toString().trim();
		//	String address = etAddress.getText().toString().trim();
			String vagetarian = etVegetarian.getText().toString().trim();
			String vegan = etVegan.getText().toString().trim();
			String noOfDisabled = etNoOfDisabled.getText().toString().trim();
			String noOfChild = etNoOfChild.getText().toString().trim();
			String noOdBabies = etNoOfBabies.getText().toString().trim();
			String actuallyReached = etActuallyReached.getText().toString().trim();
			String gift = etGift.getText().toString().trim();
			String phone = etPhone.getText().toString();
			 if((name != null && !name.equalsIgnoreCase("") || fName != null  && !fName.equalsIgnoreCase("")))
				 {
				  if(noOfInvites != null && !noOfInvites.equalsIgnoreCase("")){
				 
			
			/*if(name != null && !name.equalsIgnoreCase("")){
				if(selectedCatData != null){*/
					if(email != null && !email.equalsIgnoreCase("")){
						if(!SignUpActivity.isEmailValid(email)){
							Toast.makeText(AddInvitation.this, R.string.signup_not_valid_email, Toast.LENGTH_SHORT).show();
							return;
						}
					}
					/*nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
					nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
					nameValueAddTaskPair.add(new BasicNameValuePair("wedituserid", String.valueOf(inviteId)));
					nameValueAddTaskPair.add(new BasicNameValuePair("name", name));
					nameValueAddTaskPair.add(new BasicNameValuePair("usercategoryId", String.valueOf(selectedCatData.getCatId())));

					if(fName != null  && !fName.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("fname", fName));
					}
					if(noOfInvites != null && !noOfInvites.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("numberofinvites", noOfInvites));
					}
					if(confirmApprival != null && !confirmApprival.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("approve", confirmApprival));
					}
					if(email != null && !email.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("email", email));
					}
					if(address != null && !address.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("address", address));
					}
					if(vagetarian != null  && !vagetarian.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("vegetarian", vagetarian));
					}
					if(vegan != null && !vegan.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("vegan", vegan));
					}
					if(noOfChild != null && !noOfChild.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("child", noOfChild));
					}
					if(noOdBabies != null && !noOdBabies.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("babys", noOdBabies));
					}
					if(noOfDisabled != null && !noOfDisabled.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("disabled", noOfDisabled));
					}
					if(actuallyReached != null && !actuallyReached.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("acctuallyreached", actuallyReached));
					}
					if(chbInvitationhasBennSent.isChecked()){
						nameValueAddTaskPair.add(new BasicNameValuePair("invitationsent", "1"));
					}else{
						nameValueAddTaskPair.add(new BasicNameValuePair("invitationsent", "0"));
					}
					if(gift!= null && !gift.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("gift",gift));
					}
					if(phone!= null && !phone.equalsIgnoreCase("")){
						nameValueAddTaskPair.add(new BasicNameValuePair("phone",phone));
					}*/
					nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
				     nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				     nameValueAddTaskPair.add(new BasicNameValuePair("wedituserid", String.valueOf(inviteId)));
				     nameValueAddTaskPair.add(new BasicNameValuePair("name", name));
				     
				     nameValueAddTaskPair.add(new BasicNameValuePair("usercategoryId", String.valueOf(selectedCatData.getCatId())));
				     nameValueAddTaskPair.add(new BasicNameValuePair("fname", fName));
				     nameValueAddTaskPair.add(new BasicNameValuePair("numberofinvites", noOfInvites));
				     /*if(fName != null  && !fName.equalsIgnoreCase("")){
				      nameValueAddTaskPair.add(new BasicNameValuePair("fname", fName));
				     }
				     if(noOfInvites != null && !noOfInvites.equalsIgnoreCase("")){
				      nameValueAddTaskPair.add(new BasicNameValuePair("numberofinvites", noOfInvites));
				     }*/
				     nameValueAddTaskPair.add(new BasicNameValuePair("approve", confirmApprival));
				     nameValueAddTaskPair.add(new BasicNameValuePair("email", email));
				 //    nameValueAddTaskPair.add(new BasicNameValuePair("address", address));
				     nameValueAddTaskPair.add(new BasicNameValuePair("vegetarian", vagetarian));
				     nameValueAddTaskPair.add(new BasicNameValuePair("vegan", vegan));
				     nameValueAddTaskPair.add(new BasicNameValuePair("child", noOfChild));
				     nameValueAddTaskPair.add(new BasicNameValuePair("babys", noOdBabies));
				     nameValueAddTaskPair.add(new BasicNameValuePair("disabled", noOfDisabled));
				     nameValueAddTaskPair.add(new BasicNameValuePair("acctuallyreached", actuallyReached));
				     nameValueAddTaskPair.add(new BasicNameValuePair("gift",gift));
				     nameValueAddTaskPair.add(new BasicNameValuePair("phone",phone));
				     
				     tvCategory.setText(selectedCatData.getCatName());
				 //    tvCategory.setText(tv);
				//     Toast.makeText(getApplicationContext(), tv+"", Toast.LENGTH_LONG).show();
				     if(chbInvitationhasBennSent.isChecked()){
				      nameValueAddTaskPair.add(new BasicNameValuePair("invitationsent", "1"));
				     }else{
				      nameValueAddTaskPair.add(new BasicNameValuePair("invitationsent", "0"));
				     }
					if(isNetworkAvailable()){
						AddNewInvitation exe = new AddNewInvitation();
						exe.execute();
					}else{
						Toast.makeText(AddInvitation.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
					}

				}else{
					Toast.makeText(AddInvitation.this, "Add invites", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(AddInvitation.this, "Add name", Toast.LENGTH_SHORT).show();
			}

		}
	};
	class AddNewInvitation extends AsyncTask<Void, Void, Integer>{

		String message;
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(AddInvitation.this, "", "×˜×•×¢×Ÿ × ×ª×•× ×™×�");
			pd = ProgressDialog.show(AddInvitation.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost;
				if(FROM_INVITE_EDIT){
					httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=editvited");
				}else{
					httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=newinvited");
				}
				
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

				httppost.setEntity(new UrlEncodedFormEntity(nameValueAddTaskPair,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONObject jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				if(FROM_INVITE_EDIT)
					message = jObj.getString("message"); 
					
				if(status){
					return 0;
					
				}else{
					return 1;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return 2;	
			}

		}
		@Override
		protected void onPostExecute(Integer result){
		//	Intent in;
			if(result == 0){
				if(FROM_INVITE_EDIT)	
					Toast.makeText(AddInvitation.this, message, Toast.LENGTH_SHORT).show();
					
				else
					Toast.makeText(AddInvitation.this, R.string.addtask_task_inserted, Toast.LENGTH_SHORT).show();
				/*in = new Intent(AddInvitation.this,InviteManagementActivity.class);
				startActivity(in);*/
				//finish();
				
				/*ACTIVITY_FLAG_INVITE=true;
		         Intent i =new Intent(AddInvitation.this,TabActivityMy.class);
		         startActivity(i);*/
		         //finish();
			}else if(result == 1){
				if(FROM_INVITE_EDIT)
					Toast.makeText(AddInvitation.this, message, Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(AddInvitation.this, "Invitation not added.", Toast.LENGTH_SHORT).show();
				
			/*	in = new Intent(AddInvitation.this,InviteManagementActivity.class);
				startActivity(in);*/
			}else{
				Toast.makeText(AddInvitation.this, "No Internet Connetion or error occured.", Toast.LENGTH_SHORT).show();
			}
			ACTIVITY_FLAG_INVITE=true;
	         Intent i =new Intent(AddInvitation.this,TabActivityMy.class);
	         startActivity(i);
	         finish();
			pd.dismiss();

			super.onPostExecute(result);
		}

	}
	/*public void clearAllViews(){
		etName.setText("");
		etFamilyName.setText("");
		etEmai.setText("");
		etAddress.setText("");
		etGift.setText("");

		etNoofInvites.setText("");
		etConfirmedArrival.setText("");
		etVegetarian.setText("");
		etVegan.setText("");
		etNoOfDisabled.setText("");
		etNoOfChild.setText("");
		etNoOfBabies.setText("");
		etActuallyReached.setText("");

		chbInvitationhasBennSent.setChecked(false);

		selectedCatData = null;
		tvCategory.setText(AddInvitation.this.getString(R.string.text_category));
	}*/
	OnClickListener categoryClickListner = new OnClickListener() {

		@Override
		public void onClick(View v) {

			AlertDialog.Builder diag = new AlertDialog.Builder(AddInvitation.this);
			
			diag.setAdapter(adapterCat, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					selectedCatData = adapterCat.getItem(which);
				//	tvCategory.setText(AddInvitation.this.getString(R.string.text_category)+" - "+selectedCatData.getCatName());
					tvCategory.setText(selectedCatData.getCatName());
				//	tv=selectedCatData.getCatName();
					dialog.dismiss();

				}
			});

			diag.show();

		}
	};
	class GetCategoryList extends AsyncTask<Void, Void, ArrayList<CategoryData>>{

		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(AddInvitation.this, "", "×˜×•×¢×Ÿ × ×ª×•× ×™×�");
			pd = ProgressDialog.show(AddInvitation.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<CategoryData> doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=getusercategories");
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
			//	Log.e("NAME", responseThis);
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
				adapterCat = new CACategoryList(AddInvitation.this, R.layout.row_task, result);
				//lvTask.setAdapter(adapter);
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	
	 class DeleteTask extends AsyncTask<Void, Void, Integer>{
		  String message;
		  JSONObject jObj;
		  @Override
		  protected void onPreExecute() {
		  // pd = ProgressDialog.show(TaskViewActivity.this, "", "×˜×•×¢×Ÿ × ×ª×•× ×™×�");
		   pd = ProgressDialog.show(AddInvitation.this, "", "אנא המתן");
		   super.onPreExecute();
		  }
		  @Override
		  protected Integer doInBackground(Void... params) {
		   try {
		    HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=deleteinvited");
		    HttpParams httpParameters = httppost.getParams();

		    int timeoutConnection = 10000;
		    HttpConnectionParams.setConnectionTimeout(httpParameters,
		      timeoutConnection);

		    int timeoutSocket = 10000;
		    HttpConnectionParams
		    .setSoTimeout(httpParameters, timeoutSocket);

		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
		    nameValuePairs.add(new BasicNameValuePair("InvitedID", String.valueOf(inviteId)));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		    //httppost.setEntity(new UrlEncodedFormEntity(nameValueAddTaskPair,HTTP.UTF_8));

		    HttpResponse response = httpclient.execute(httppost);
		    String responseThis = EntityUtils.toString(response.getEntity()).trim();
		    Log.e("Response", responseThis);
		    jObj = new JSONObject(responseThis);
		    
		    
		    boolean status = jObj.getBoolean("success");
		    if(status){
		     message = jObj.getString("message");
		     return 0;
		    }else{
		     return 1;
		    }
		    
		   } catch (Exception e) {
		    e.printStackTrace();
		    return 2;
		   }
		  }
		  @Override
		  protected void onPostExecute(Integer result) {
		   if(result == 0){
		    Toast.makeText(AddInvitation.this,message, Toast.LENGTH_SHORT).show();
		   // Toast.makeText(getApplicationContext(), ""+inviteId, Toast.LENGTH_SHORT).show();
		    /* String lastWord = message.substring(message.lastIndexOf(" "));
		     Toast.makeText(getApplicationContext(), lastWord, Toast.LENGTH_SHORT).show();*/
		    finish();
		   }else if(result == 1){
		    Toast.makeText(AddInvitation.this, "Task not deleted", Toast.LENGTH_SHORT).show();
		   }else{
		    Toast.makeText(AddInvitation.this, "No Internet Connetion or error occured.", Toast.LENGTH_SHORT).show();
		   }
		   pd.dismiss();
		   super.onPostExecute(result);
		  }
		  
		 }
}