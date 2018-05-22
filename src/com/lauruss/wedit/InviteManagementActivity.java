package com.lauruss.wedit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Boolean;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.RawContacts;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

public class InviteManagementActivity extends Activity{

	public static int weditusercategory_id = 0;
	Button btnAddNew,btnImportFromFacebook,btnImportFromContact,btnAllInvite,btnback,btnhome;
	ProgressDialog pd;
	CAContactList adapterContactList;
	CASectionInvitation adapterInviteByCat;
	CAFacebookFriendList adapterFacebookFriend;
	//int inviteid;
	ListView lvContactList;
	static ArrayList<String> arrgift;
	static ArrayList<String> arremail;
	static ArrayList<String> arrfamname;
	static ArrayList<String> arrname;
	static ArrayList<String> arrphone;
	static ArrayList<String> arraddress;
	static ArrayList<Integer> arrnoofinvites;
	static ArrayList<Integer> arrnoaproved;
	static ArrayList<Integer> arrinviteid;
	String lastWord="";
	Intent intent;
	 boolean FLAG_ALL_INVITEEMPTY=false;
	 RelativeLayout rlinviteemptytext;
	
	RelativeLayout rlSearchInvite,rlSearchContact,rlSearchFacebook;
	Button btnClearInvite,btnClearContact,btnClearFacebook;
	EditText etSearchInvite,etSearchContact,etSearchFacebook;


	Button btnExportContact,btnExportInvite,btnExportFacebook,btnSmartArriveButton;

	ArrayList<InviteDataClass> inviteByCatData;
	ArrayList<ContactMyDataClass> cArray;
	ArrayList<FaceBookFriendDataClass> fbDataArr = new ArrayList<FaceBookFriendDataClass>();

	public String EXTERNAL_STORAGE_PATH = "";
	public String EXPORT_CONTACT_FILE_NAME = "invitedContact.xls";
	String userMail = "";

	public boolean FLAG_FROM_FACEBOOK = false;
	public static String FLAG_INVITE_EDIT = "from_invite_edit", FLAG_INVITE_DATA = "invite_data";
	
	RelativeLayout rlHeader;
	List<NameValuePair> contactListNameValuePairs,facebookFriendNameValuePairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invitemanagement);
		
		 rlinviteemptytext=(RelativeLayout)findViewById(R.id.invite_rl_emptylist);
		
		 btnback=(Button)findViewById(R.id.invite_btn_btnback);
		  btnhome=(Button)findViewById(R.id.invite_btn_btnhome);
		btnAddNew = (Button)findViewById(R.id.invite_btn_add);
		btnAllInvite = (Button)findViewById(R.id.invite_btn_allinvite);
		btnImportFromContact = (Button)findViewById(R.id.invite_btn_importcontact);
		btnImportFromFacebook = (Button)findViewById(R.id.invite_btn_importfacebookfriend);
		lvContactList = (ListView)findViewById(R.id.invite_lv_contactList);

		rlSearchInvite = (RelativeLayout)findViewById(R.id.invite_rl_main_search);
		rlSearchContact = (RelativeLayout)findViewById(R.id.invite_rl_main_search_contact);
		rlSearchFacebook = (RelativeLayout)findViewById(R.id.invite_rl_main_search_facebook);

		btnClearInvite = (Button)findViewById(R.id.invite_btn_clearsearch);
		btnClearContact = (Button)findViewById(R.id.invite_btn_clearsearch_contact);
		btnClearFacebook = (Button)findViewById(R.id.invite_btn_clearsearch_facebook);

		etSearchInvite = (EditText)findViewById(R.id.invite_et_search);
		etSearchContact = (EditText)findViewById(R.id.invite_et_search_contact);
		etSearchFacebook = (EditText)findViewById(R.id.invite_et_search_facebook);

		btnExportContact = (Button)findViewById(R.id.invite_btn_export_contact);
		btnExportInvite = (Button)findViewById(R.id.invite_btn_exportinvite);
		btnExportFacebook = (Button)findViewById(R.id.invite_btn_export_facebook);
		btnSmartArriveButton = (Button)findViewById(R.id.invite_btn_smartarrivebtn);
		File f = new File(Environment.getExternalStorageDirectory()+"/wedit/");
		if(!f.exists()){
			f.mkdirs();
		}

		EXTERNAL_STORAGE_PATH = f.getAbsolutePath();
		SharedPreferences pref = MyApplication.getSharedPreference();
		userMail = pref.getString(SignUpActivity.FLAG_USER_EMAIL, "");
		rlHeader = (RelativeLayout)findViewById(R.id.invite_rl_top);
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
		}*/
		rlHeader.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
					etSearchInvite.clearFocus();
				return false;
			}
		});
		btnback.setOnClickListener(new View.OnClickListener() {
			   
			   @Override
			   public void onClick(View v) {
			    // TODO Auto-generated method stub
				   intent=new Intent(InviteManagementActivity.this,LoginActivity.class);
				    startActivity(intent);
			   }
			  });
			  
			  btnhome.setOnClickListener(new View.OnClickListener() {
			   
			   @Override
			   public void onClick(View v) {
			    // TODO Auto-generated method stub
			    intent=new Intent(InviteManagementActivity.this,LoginActivity.class);
			    startActivity(intent);
			   }
			  });
		btnSmartArriveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Bundle bn = new Bundle();
				final Intent in = new Intent(InviteManagementActivity.this,SmartArriveActivity.class);
				
				
				AlertDialog.Builder diag = new AlertDialog.Builder(InviteManagementActivity.this);
				diag.setTitle(R.string.smartarrive_diaglog_title);
				
				diag.setPositiveButton(R.string.smartarrive_diaglog_byemail, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						bn.putInt(SmartArriveActivity.FLAG_STR_BY_SMART, SmartArriveActivity.BY_EMAIL);
						bn.putString("inviteid", lastWord);
						in.putExtras(bn);
						startActivity(in);
					}
				});
				diag.setNeutralButton(R.string.smartarrive_diaglog_bysms, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						bn.putInt(SmartArriveActivity.FLAG_STR_BY_SMART, SmartArriveActivity.BY_SMS);
						bn.putString("inviteid", lastWord);
					
						in.putExtras(bn);
						startActivity(in);
					}
				});
				diag.setNegativeButton(R.string.smartarrive_diaglog_cancel, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				
				diag.show();
				
			}
		});
		lvContactList.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
					etSearchInvite.clearFocus();
				return false;
			}
		});
		lvContactList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(rlSearchInvite.isShown()){
					InviteDataClass mClass = (InviteDataClass)arg0.getAdapter().getItem(arg2);
				//	mClass.getEmail();
					//inviteByCatData.get(index)
				//	Toast.makeText(getApplicationContext(),String.valueOf(mClass.getEmail())+"", Toast.LENGTH_SHORT).show();
					if(mClass.type != TaskDataClass.SECTION){
						Bundle bn = new Bundle();
						bn.putBoolean(FLAG_INVITE_EDIT, true);
						
						bn.putSerializable(FLAG_INVITE_DATA, mClass);
					//	Toast.makeText(getApplicationContext(), mClass.getWeditUserCatName()+"", Toast.LENGTH_SHORT).show();
						Intent in = new Intent(InviteManagementActivity.this,AddInvitation.class);
						in.putExtras(bn);
						
						startActivity(in);
					}
				}
				
			}
		});
		etSearchInvite.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER);
		etSearchInvite.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if(!etSearchInvite.getText().toString().trim().equalsIgnoreCase("")){
					//Toast.makeText(InviteManagementActivity.this, etSearchInvite.getText().toString(), Toast.LENGTH_SHORT).show();
					InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		            etSearchInvite.clearFocus();
					GetSearchCategorywiseTask exe = new GetSearchCategorywiseTask();
					exe.execute(etSearchInvite.getText().toString().trim());
					return true;
				}else{
					return false;
				}


			}
		});
		etSearchInvite.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					btnExportInvite.setVisibility(View.GONE);
					btnSmartArriveButton.setVisibility(View.GONE);
				}else{
					btnExportInvite.setVisibility(View.VISIBLE);
					btnSmartArriveButton.setVisibility(View.VISIBLE);
				}
				
			}
		});
		btnExportFacebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					ArrayList<FaceBookFriendDataClass> data = adapterFacebookFriend.getSelectedContactData();
					facebookFriendNameValuePairs = new ArrayList<NameValuePair>(2);
					facebookFriendNameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
					
					int len = data.size();
					
					for(int i = 0;i<len;i++){
						GraphUser gUser = data.get(i).getFbUser();
						
						facebookFriendNameValuePairs.add(new BasicNameValuePair("invite["+i+"][name]", gUser.getName()));
						facebookFriendNameValuePairs.add(new BasicNameValuePair("invite["+i+"][fname]", gUser.getFirstName()));
					
					}
					ImportFacebookFriendList exe = new ImportFacebookFriendList();
					exe.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnExportContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					contactListNameValuePairs = new ArrayList<NameValuePair>(2);
					contactListNameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
					
					ArrayList<ContactMyDataClass> data = adapterContactList.getSelectedContactData();
					int len = data.size();
					for(int i = 0;i<len;i++){
						ContactMyDataClass cData = data.get(i);
						
					//	contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][name]", displayname[0]+""));
					//	contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][fname]", displayname[1]+""));
				//		String displayname[]=cData.getContactName().split(" ");
						
					//	contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][fname]", cData.getContactfamilyname()));
						contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][name]", cData.getContactName()));
						
						String phone = cData.getContactPhone();
						String email = cData.getContactEmail();
						
					//	String family = cData.getContactfanmilyname();
						phone = phone.replaceAll("[()]", "");
						phone = phone.replaceAll(" ", "");
						
						
						contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][phone]", phone));
						contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][email]", email));
					//	contactListNameValuePairs.add(new BasicNameValuePair("invite["+i+"][family]", family));
						
					}
					ImportContactList exe = new ImportContactList();
					exe.execute();
					
					/*Intent in = new Intent(InviteManagementActivity.this,InviteManagementActivity.class);
					startActivity(in);*/

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		btnAllInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FLAG_FROM_FACEBOOK = false;
				btnAllInvite.setBackgroundResource(R.drawable.in_btn_3_sel);
				btnImportFromContact.setBackgroundResource(R.drawable.in_btn_2);
				btnImportFromFacebook.setBackgroundResource(R.drawable.in_btn_1);
				rlSearchContact.setVisibility(View.INVISIBLE);
				rlSearchFacebook.setVisibility(View.INVISIBLE);
				rlSearchInvite.setVisibility(View.VISIBLE);
				FLAG_ALL_INVITEEMPTY=true;
				if(isNetworkAvailable()){
					GetCategorywiseTask exe = new GetCategorywiseTask();
					exe.execute();}
				else {
					Toast.makeText(InviteManagementActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
				}

			}
		});
		btnImportFromContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				FLAG_FROM_FACEBOOK = false;
				btnAllInvite.setBackgroundResource(R.drawable.in_btn_3);
				btnImportFromContact.setBackgroundResource(R.drawable.in_btn_2_sel);
				btnImportFromFacebook.setBackgroundResource(R.drawable.in_btn_1);
				rlSearchContact.setVisibility(View.VISIBLE);
				rlSearchInvite.setVisibility(View.INVISIBLE);
				rlSearchFacebook.setVisibility(View.INVISIBLE);

				FLAG_ALL_INVITEEMPTY=false;
			    rlinviteemptytext.setVisibility(8);
			    lvContactList.setVisibility(0);
				if(cArray == null || cArray.isEmpty()){
					GetContactList getCList = new GetContactList();
					getCList.execute();
				}else{
					adapterContactList = new CAContactList(InviteManagementActivity.this, R.layout.row_invite_section, cArray);
					lvContactList.setAdapter(adapterContactList);
				}

			}
		});
		btnImportFromFacebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnAllInvite.setBackgroundResource(R.drawable.in_btn_3);
				btnImportFromContact.setBackgroundResource(R.drawable.in_btn_2);
				btnImportFromFacebook.setBackgroundResource(R.drawable.in_btn_1_sel);
				rlSearchContact.setVisibility(View.INVISIBLE);
				rlSearchInvite.setVisibility(View.INVISIBLE);
				rlSearchFacebook.setVisibility(View.VISIBLE);
				FLAG_FROM_FACEBOOK = true;
				FLAG_ALL_INVITEEMPTY=false;
			    rlinviteemptytext.setVisibility(8);
			    lvContactList.setVisibility(0);
				if(fbDataArr.isEmpty()){
					 Session s = new Session(InviteManagementActivity.this);
					Session.setActiveSession(s);
					Session.OpenRequest request = new Session.OpenRequest(InviteManagementActivity.this);
					request.setPermissions(Arrays.asList("basic_info","user_photos","user_videos","offline_access","user_checkins","friends_checkins","email","user_location"));
					//request.setPermissions(Arrays.asList("basic_info","email","read_friendlists"));
					//request.setCallback( /* your callback here */ );
					s.openForRead(request);

					// start Facebook Login
					Session.openActiveSession(InviteManagementActivity.this, true, new Session.StatusCallback() {

						// callback when session changes state
						@Override
						public void call(Session session, SessionState state, Exception exception) {
							if (session.isOpened()) {
								requestFacebookFriends(session);

							}else{
								//Toast.makeText(InviteManagementActivity.this, "Facebook not connected", Toast.LENGTH_SHORT).show();
								FLAG_FROM_FACEBOOK = false;
							}
						}
					});
				}else{
					FLAG_FROM_FACEBOOK = false;
					adapterFacebookFriend = new CAFacebookFriendList(InviteManagementActivity.this, R.layout.row_contactlist, fbDataArr);
					lvContactList.setAdapter(adapterFacebookFriend);
				}


			}
		});
		btnAddNew.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bn = new Bundle();
				bn.putBoolean(FLAG_INVITE_EDIT, false);
				
				Intent in = new Intent(InviteManagementActivity.this,AddInvitation.class);
				in.putExtras(bn);
				startActivity(in);

			}
		});
		btnClearInvite.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				etSearchInvite.setText("");
				

			}
		});
		btnClearContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				etSearchContact.setText("");
				GetContactList getCList = new GetContactList();
				getCList.execute();
			}
		});
		btnClearFacebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				etSearchFacebook.setText("");
				Session s = new Session(InviteManagementActivity.this);
				Session.setActiveSession(s);
				Session.OpenRequest request = new Session.OpenRequest(InviteManagementActivity.this);
				request.setPermissions(Arrays.asList("basic_info","user_photos","user_videos","offline_access","user_checkins","friends_checkins","email","user_location"));
				//request.setPermissions(Arrays.asList("basic_info","email","read_friendlists"));
				//request.setCallback( /* your callback here */ );
				s.openForRead(request);

				// start Facebook Login
				Session.openActiveSession(InviteManagementActivity.this, true, new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							requestFacebookFriends(session);

						}else{
							//Toast.makeText(InviteManagementActivity.this, "Facebook not connected", Toast.LENGTH_SHORT).show();
							FLAG_FROM_FACEBOOK = false;
						}
					}
				});

			}
		});
		etSearchContact.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapterContactList.filter(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		etSearchInvite.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapterInviteByCat.filter(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		etSearchFacebook.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				adapterFacebookFriend.filter(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		btnExportInvite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					ExportInvite exe = new ExportInvite();
					exe.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	class ExportInvite extends AsyncTask<Void, Void, Integer>{

		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã‹Å“Ãƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â¢Ãƒâ€”Ã…Â¸ Ãƒâ€”Ã‚Â Ãƒâ€”Ã‚ÂªÃƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â Ãƒâ€”Ã¢â€žÂ¢Ãƒâ€”Ã¯Â¿Â½");
			pd = ProgressDialog.show(InviteManagementActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				ArrayList<InviteDataClass> data = adapterInviteByCat.getInviteData();
			    
				File f = new File(EXTERNAL_STORAGE_PATH+"/"+EXPORT_CONTACT_FILE_NAME);
				
				WritableWorkbook workBook = Workbook.createWorkbook(f); 
				workBook.createSheet("Invite List", 0);
				WritableSheet excelSheet = workBook.getSheet(0);
				
			    
			    int len = data.size();
			    Label l;
			    
			    l = new Label(0, 0, "CATEGORY");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(1, 0, "NAME");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(2, 0, "FAMILY NAME");
	    		excelSheet.addCell(l);
	    		l = new Label(3, 0, "NO OF INVITES");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(4, 0, "APPROVE");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(5, 0, "PHONE");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(6, 0, "EMAIL");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(7, 0, "ADDRESS");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(8, 0, "VAGETARIAN");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(9, 0, "VAGAN");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(10, 0, "NO OF DISABLED");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(11, 0, "NO OF CHILD");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(12, 0, "NO OF BABIES");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(13, 0, "INVITATION SENT");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(14, 0, "ACTUALLY REACHED");
	    		excelSheet.addCell(l);
	    		
	    		l = new Label(15, 0, "GIFT");
	    		excelSheet.addCell(l);
	    		
			    
			    String cat = "";
			    for(int i = 1; i <=  len; i++){
					InviteDataClass inviteData = data.get(i-1);
			    	
			    	if(inviteData.getItemType() == TaskDataClass.SECTION){
			    		cat = inviteData.getName();
						//dataThis.add(new String[] {inviteData.getName()});
					}
						
						l = new Label(0, i, inviteData.getName());
			    		excelSheet.addCell(l);
						
						l = new Label(1, i, inviteData.getName());
			    		excelSheet.addCell(l);
			    		l = new Label(2, i, inviteData.getFamilyName());
			    		excelSheet.addCell(l);
			    		
			    		Number num = new Number(3, i, inviteData.getNoOfInvites());
			    		excelSheet.addCell(num);
			    		
			    		num = new Number(4, i, inviteData.getApprove());
			    		excelSheet.addCell(num);
			    		
			    		l = new Label(5, i, inviteData.getEmail());
			    		excelSheet.addCell(l);
			    		
			    		l = new Label(6, i, inviteData.getAddress());
			    		excelSheet.addCell(l);
			    		
			    		num = new Number(7, i, inviteData.getVegetarian());
			    		excelSheet.addCell(num);
			    		
			    		num = new Number(8, i, inviteData.getVegan());
			    		excelSheet.addCell(num);
			    		
			    		num = new Number(9, i, inviteData.getDisabled());
			    		excelSheet.addCell(num);
			    		
			    		num = new Number(10, i, inviteData.getChild());
			    		excelSheet.addCell(num);
			    		
			    		num = new Number(11, i, inviteData.getBabies());
			    		excelSheet.addCell(num);
			    		
			    		Boolean bool = new Boolean(12, i, inviteData.getInVitationSent());
			    		excelSheet.addCell(bool);
			    		
			    		num = new Number(13, i, inviteData.getActuallyReached());
			    		excelSheet.addCell(num);
			    		
			    		l = new Label(14, i, inviteData.getGigt());
			    		excelSheet.addCell(l);
			    		
			    		l = new Label(15, i, inviteData.getPhone());
			    		excelSheet.addCell(l);
					
				}
			    workBook.write();
			    workBook.close();
			    return 0;
			} catch (Exception e) {
				e.printStackTrace();
				return 1;
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			pd.dismiss();
			if(result == 0){
				Intent sendIntent = new Intent(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {userMail});
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Wedit invite List");
				//sendIntent.putExtra(Intent.EXTRA_CC, "Wedit invite List");
			//	sendIntent.putExtra(Intent.EXTRA_TEXT, );
				
				sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(EXTERNAL_STORAGE_PATH+"/"+EXPORT_CONTACT_FILE_NAME)));
				sendIntent.setType("application/vnd.ms-excel");
				startActivity(sendIntent);
			}else{
				Toast.makeText(InviteManagementActivity.this, "Error occured in export please try again.", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
		
	}
	
	private void requestFacebookFriends(Session session) {
		Request.executeMyFriendsRequestAsync(session,
				new Request.GraphUserListCallback() {

			@Override
			public void onCompleted(List<GraphUser> users,
					Response response) {
				List<GraphUser> friends = getResults(response);
				fbDataArr = new ArrayList<FaceBookFriendDataClass>();
				for(GraphUser user : friends){
					//Log.e("friend name ", user.getName());
					fbDataArr.add(new FaceBookFriendDataClass(user, false));
				}
				adapterFacebookFriend = new CAFacebookFriendList(InviteManagementActivity.this, R.layout.row_contactlist, fbDataArr);
				lvContactList.setAdapter(adapterFacebookFriend);
				FLAG_FROM_FACEBOOK = false;
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession()
		.onActivityResult(this, requestCode, resultCode, data);
	}
	/*private Request createRequest(Session session) {
	    Request request = Request.newGraphPathRequest(session, "me/friends", null);

	    Set<String> fields = new HashSet<String>();
	    String[] requiredFields = new String[] { "id", "name", "picture",
	            "installed" };
	    fields.addAll(Arrays.asList(requiredFields));

	    Bundle parameters = request.getParameters();
	    parameters.putString("fields", TextUtils.join(",", fields));
	    request.setParameters(parameters);

	    return request;
	}
	private void requestMyAppFacebookFriends(Session session) {
	    Request friendsRequest = createRequest(session);
	    friendsRequest.setCallback(new Request.Callback() {

	        @Override
	        public void onCompleted(Response response) {
	            List<GraphUser> friends = getResults(response);
	            for(GraphUser user : friends){
					 Log.e("friend name ", user.getName());
				 }
	        }
	    });
	    friendsRequest.executeAsync();
	}*/
	private List<GraphUser> getResults(Response response) {
		GraphMultiResult multiResult = response
				.getGraphObjectAs(GraphMultiResult.class);
		GraphObjectList<GraphObject> data = multiResult.getData();
		return data.castToListOf(GraphUser.class);
	}
	@Override
	protected void onResume() {
		if(CATaskList.aldeletetask.size()>0)
		  {
		   for(TaskDataClass taskdeleteobj:CATaskList.aldeletetask)
		   {
		    DeleteTask delexe = new DeleteTask(InviteManagementActivity.this);
		    delexe.execute(taskdeleteobj.getTaskId());
		   }
		  }
		if(FLAG_FROM_FACEBOOK == false){


			if(isNetworkAvailable()){
				inviteByCatData = null;
				btnAllInvite.setBackgroundResource(R.drawable.in_btn_3_sel);
				btnImportFromContact.setBackgroundResource(R.drawable.in_btn_2);
				btnImportFromFacebook.setBackgroundResource(R.drawable.in_btn_1);
				rlSearchContact.setVisibility(View.INVISIBLE);
				rlSearchInvite.setVisibility(View.VISIBLE);
				FLAG_ALL_INVITEEMPTY=true;
				GetCategorywiseTask exe = new GetCategorywiseTask();
				exe.execute();
			}else{
				Toast.makeText(InviteManagementActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
			}
		}
		super.onResume();
	}
	class GetSearchCategorywiseTask extends AsyncTask<String, Void, ArrayList<InviteDataClass>>{

		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã‹Å“Ãƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â¢Ãƒâ€”Ã…Â¸ Ãƒâ€”Ã‚Â Ãƒâ€”Ã‚ÂªÃƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â Ãƒâ€”Ã¢â€žÂ¢Ãƒâ€”Ã¯Â¿Â½");
			//pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã¯Â¿Â½Ãƒâ€”Ã‚Â Ãƒâ€”Ã¯Â¿Â½ Ãƒâ€”Ã¢â‚¬ï¿½Ãƒâ€”Ã…Â¾Ãƒâ€”Ã‚ÂªÃƒâ€”Ã…Â¸");
			pd = ProgressDialog.show(InviteManagementActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<InviteDataClass> doInBackground(String... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=searchinvited");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				nameValuePairs.add(new BasicNameValuePair("name", params[0]));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONArray jArray = new JSONArray(responseThis);

				ArrayList<InviteDataClass> tData = new ArrayList<InviteDataClass>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObjMain = jArray.getJSONObject(i);
					InviteDataClass HDate = new InviteDataClass();
					HDate.setType(TaskDataClass.SECTION);
					HDate.setName(jObjMain.getString("weditusercategory_name"));
					HDate.setInviteId(jObjMain.getInt("weditusercategory_id"));
					tData.add(HDate);
					JSONArray jArrTask = jObjMain.getJSONArray("users");
					int tLen = jArrTask.length();
					for(int j=0;j<tLen;j++){
						JSONObject jObj = jArrTask.getJSONObject(j);
						InviteDataClass tObj = new InviteDataClass();
						tObj.setName(jObj.getString("name"));
						tObj.setInviteId(jObj.getInt("id"));
						tObj.setFamilyName(jObj.getString("fname"));
						tObj.setWeddingId(jObj.getInt("weddingid"));
						tObj.setNoOfInvites(jObj.getInt("numberofinvites"));
						tObj.setApprove(jObj.getInt("approve"));
						tObj.setInVitationSent(jObj.getBoolean("invitationsent"));
					//	tObj.setInVitationSent(true);

						tData.add(tObj);
					}

				}
				return tData;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<InviteDataClass> result) {
			if(result != null){
				inviteByCatData = new ArrayList<InviteDataClass>();
				inviteByCatData = result;
				adapterInviteByCat = new CASectionInvitation(InviteManagementActivity.this, R.layout.row_invite_section, result);
				//sortAscending ();
				/* List<String> sortedMonthsList = Arrays.asList(lvContactList.toString());
				    Collections.sort(sortedMonthsList);
				    Log.e("Contact Array", sortedMonthsList+"");*/
				//Collections.sort(lvContactList, ALPHABETICAL_ORDER1);
				
				lvContactList.setAdapter(adapterInviteByCat);
				
				
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	/*private void sortAscending () {
	    List<String> sortedMonthsList = Arrays.asList(lvContactList.toString());
	    Collections.sort(sortedMonthsList);

	    lvContactList = (ListView) sortedMonthsList;
	}*/
	/*Comparator<String> ALPHABETICAL_ORDER1 = new Comparator<String>()

			 { 

			       public int compare(String object1, String object2)
			 {

			 int res = String.CASE_INSENSITIVE_ORDER.compare(object1.toString(), object2.toString()); 

			return res;

			 }
			 }; */
	class GetCategorywiseTask extends AsyncTask<Void, Void, ArrayList<InviteDataClass>>{

		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã‹Å“Ãƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â¢Ãƒâ€”Ã…Â¸ Ãƒâ€”Ã‚Â Ãƒâ€”Ã‚ÂªÃƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â Ãƒâ€”Ã¢â€žÂ¢Ãƒâ€”Ã¯Â¿Â½");
		//	pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ã—ï¿½Ã—Â Ã—ï¿½ Ã—â€�Ã—Å¾Ã—ÂªÃ—Å¸");
			pd = ProgressDialog.show(InviteManagementActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<InviteDataClass> doInBackground(Void... params) {
			String message="";
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=getinvited");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);
				InviteDataClass tnmobj = null;
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONArray jArray = new JSONArray(responseThis);
				

				ArrayList<InviteDataClass> tData = new ArrayList<InviteDataClass>();
				ArrayList<InviteDataClass> taskdata=new ArrayList<InviteDataClass>();
				int len = jArray.length();
				for(int i = 0; i < len; i++){
					JSONObject jObjMain = jArray.getJSONObject(i);
					InviteDataClass HDate = new InviteDataClass();
					HDate.setType(TaskDataClass.SECTION);
					HDate.setName(jObjMain.getString("weditusercategory_name"));
					HDate.setInviteId(jObjMain.getInt("weditusercategory_id"));
					tData.add(HDate);
					taskdata.add(HDate);
					JSONArray jArrTask = jObjMain.getJSONArray("users");
					int tLen = jArrTask.length();
					arremail=new ArrayList<String>();
					arrgift=new ArrayList<String>();
					arrfamname=new ArrayList<String>();
					arrname=new ArrayList<String>();
					arrphone=new ArrayList<String>();
					arraddress=new ArrayList<String>();
					arrnoofinvites=new ArrayList<Integer>();
					arrnoaproved=new ArrayList<Integer>();
					arrinviteid=new ArrayList<Integer>();
					for(int j=0;j<tLen;j++){
						JSONObject jObj = jArrTask.getJSONObject(j);
						InviteDataClass tObj = new InviteDataClass();
						tObj.setName(jObj.getString("name"));
						tObj.setInviteId(jObj.getInt("id"));
						//inviteid=jObj.getInt("id");
						tObj.setFamilyName(jObj.getString("fname"));
						tObj.setWeddingId(jObj.getInt("weddingid"));
						tObj.setNoOfInvites(jObj.getInt("numberofinvites"));
						tObj.setApprove(jObj.getInt("approve"));
						tObj.setEmail(jObj.getString("email"));
						
						tObj.setAddress(jObj.getString("address"));
						tObj.setVegetarian(jObj.getInt("vegetarian"));
						tObj.setVegan(jObj.getInt("vegan"));
						tObj.setDisabled(jObj.getInt("disabled"));
						tObj.setChild(jObj.getInt("child"));
						tObj.setBabies(jObj.getInt("babys"));
						
					//	tObj.setInVitationSent(true);
						tObj.setInVitationSent(jObj.getBoolean("invitationsent"));
						tObj.setActuallyReached(jObj.getInt("acctuallyreached"));
						tObj.setGigt(jObj.getString("gift"));
						tObj.setPhone(jObj.getString("phone"));
						
						tData.add(tObj);
						arremail.add(tObj.getEmail());
						arrfamname.add(tObj.getFamilyName());
						arrname.add(tObj.getName());// + " " + tObj.getFamilyName());
						arrnoofinvites.add(tObj.getNoOfInvites());
						arrnoaproved.add(tObj.getApprove());
						arrinviteid.add(tObj.getInviteId());
						arrphone.add(tObj.getPhone());
						arraddress.add(tObj.getAddress());
						arrgift.add(tObj.getGigt());
					//	arrname.add(tObj.getNoOfInvites()+"");
					}
					Collections.sort(arrname);
					int cnt=0;
					for(String string:arrname)
					{
						//Toast.makeText(InviteManagementActivity.this,string,Toast.LENGTH_SHORT).show();
						tnmobj=new InviteDataClass();
						tnmobj.setName(string);
						tnmobj.setFamilyName(arrfamname.get(cnt));
						tnmobj.setNoOfInvites(arrnoofinvites.get(cnt));
						tnmobj.setApprove(arrnoaproved.get(cnt));
						tnmobj.setInviteId(arrinviteid.get(cnt));
						tnmobj.setEmail(arremail.get(cnt));
						tnmobj.setPhone(arrphone.get(cnt));
						tnmobj.setAddress(arraddress.get(cnt));
						tnmobj.setGigt(arrgift.get(cnt));
						cnt++;
						
						/*tnmobj.setNoOfInvites(tnmobj.getNoOfInvites());
						tnmobj.setNoOfInvites(tnmobj.getNoOfInvites());
						tnmobj.setNoOfInvites(tnmobj.getNoOfInvites());
						tnmobj.setNoOfInvites(tnmobj.getNoOfInvites());
						tnmobj.setNoOfInvites(tnmobj.getNoOfInvites());
						*/
						taskdata.add(tnmobj);
						
						Log.e("sorting",string);
					}

				}
			
				return tData;
				//return taskdata;

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<InviteDataClass> result) {
			if(result != null){
				if(result.size()>0 || FLAG_ALL_INVITEEMPTY==false)
			    {
			     inviteByCatData = new ArrayList<InviteDataClass>();
			     inviteByCatData = result;
			     adapterInviteByCat = new CASectionInvitation(InviteManagementActivity.this, R.layout.row_invite_section, result);
			     /*Collections.sort(result, new Comparator<String>() {
			           @Override
			           public int compare(String s1, String s2) {
			               return s1.compareToIgnoreCase(s2);
			           }
			        });*/
			     // lvContactList.setSelectionFromTop(0, 0);
			     lvContactList.setAdapter(adapterInviteByCat);
			     rlinviteemptytext.setVisibility(8);
			     lvContactList.setVisibility(0);
			     /* String[] stringsArray = new String[result.size()];
			        result.toArray(stringsArray);
			        Arrays.sort(stringsArray);*/
			    }
			    else
			    {
			     rlinviteemptytext.setVisibility(0);
			     lvContactList.setVisibility(8);
			    }
			}
			pd.dismiss();
			super.onPostExecute(result);
		}

	}
	/*public abstract class NumericAndAlphabetSectionizer implements Sectionizer<InviteDataClass> {

	    @Override
	    public String getSectionTitleForItem() {
	        return instance.getName().toUpperCase().substring(0, 1);
	    }
	}*/
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager 
		= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	class GetContactList extends AsyncTask<Void, Void, ArrayList<ContactMyDataClass>>{

		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã‹Å“Ãƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â¢Ãƒâ€”Ã…Â¸ Ãƒâ€”Ã‚Â Ãƒâ€”Ã‚ÂªÃƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â Ãƒâ€”Ã¢â€žÂ¢Ãƒâ€”Ã¯Â¿Â½");
		//	pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã¯Â¿Â½Ãƒâ€”Ã‚Â Ãƒâ€”Ã¯Â¿Â½ Ãƒâ€”Ã¢â‚¬ï¿½Ãƒâ€”Ã…Â¾Ãƒâ€”Ã‚ÂªÃƒâ€”Ã…Â¸");
			pd = ProgressDialog.show(InviteManagementActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<ContactMyDataClass> doInBackground(Void... params) {
			try {
				ContactProviderClass cProvider = new ContactProviderClass();
				cArray = cProvider.getAllPhoneContact(InviteManagementActivity.this);
		//
		//		Toast.makeText(InviteManagementActivity.this, cArray+"", Toast.LENGTH_SHORT).show();
				return cArray;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		@Override
		protected void onPostExecute(ArrayList<ContactMyDataClass> result) {
			pd.dismiss();
			if(result !=  null){
				adapterContactList = new CAContactList(InviteManagementActivity.this, R.layout.row_contactlist, cArray);
				lvContactList.setAdapter(adapterContactList);

			}else{
				Toast.makeText(InviteManagementActivity.this, "Exception occure", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

	}
	class ImportFacebookFriendList extends AsyncTask<Void, Void, Integer>{

		String message="";
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã‹Å“Ãƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â¢Ãƒâ€”Ã…Â¸ Ãƒâ€”Ã‚Â Ãƒâ€”Ã‚ÂªÃƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â Ãƒâ€”Ã¢â€žÂ¢Ãƒâ€”Ã¯Â¿Â½");
			pd = ProgressDialog.show(InviteManagementActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=insertinvited");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				
				httppost.setEntity(new UrlEncodedFormEntity(facebookFriendNameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				JSONObject jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				message = jObj.getString("message");
				if(status)
					return 0;
				else
					return 1;

			} catch (Exception e) {
				e.printStackTrace();
				return 2;	
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			pd.dismiss();
			if(result == 0){
				
				Toast.makeText(InviteManagementActivity.this, message, Toast.LENGTH_SHORT).show();
				adapterFacebookFriend.clearSelection();
				contactListNameValuePairs = new ArrayList<NameValuePair>(2);
				if(isNetworkAvailable()){
					inviteByCatData = null;
					btnAllInvite.setBackgroundResource(R.drawable.in_btn_3_sel);
					btnImportFromContact.setBackgroundResource(R.drawable.in_btn_2);
					btnImportFromFacebook.setBackgroundResource(R.drawable.in_btn_1);
					rlSearchContact.setVisibility(View.INVISIBLE);
					rlSearchInvite.setVisibility(View.VISIBLE);
					GetCategorywiseTask exe = new GetCategorywiseTask();
					exe.execute();
				}else{
					Toast.makeText(InviteManagementActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
				}
			}else if(result == 1){
				Toast.makeText(InviteManagementActivity.this, message, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(InviteManagementActivity.this, "Contact not imported error occured.", Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(result);
		}

	}
	class ImportContactList extends AsyncTask<Void, Void, Integer>{

		String message="";
		@Override
		protected void onPreExecute() {
		//	pd = ProgressDialog.show(InviteManagementActivity.this, "", "Ãƒâ€”Ã‹Å“Ãƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â¢Ãƒâ€”Ã…Â¸ Ãƒâ€”Ã‚Â Ãƒâ€”Ã‚ÂªÃƒâ€”Ã¢â‚¬Â¢Ãƒâ€”Ã‚Â Ãƒâ€”Ã¢â€žÂ¢Ãƒâ€”Ã¯Â¿Â½");
			pd = ProgressDialog.show(InviteManagementActivity.this, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=insertinvited");
				HttpParams httpParameters = httppost.getParams();

				int timeoutConnection = 10000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);

				int timeoutSocket = 10000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				
				httppost.setEntity(new UrlEncodedFormEntity(contactListNameValuePairs,HTTP.UTF_8));

				HttpResponse response = httpclient.execute(httppost);
				String responseThis = EntityUtils.toString(response.getEntity()).trim();
				Log.e("RESPONSE", responseThis);
				JSONObject jObj = new JSONObject(responseThis);
				boolean status = jObj.getBoolean("success");
				message = jObj.getString("message");
				if(status)
					return 0;
				else
					return 1;

			} catch (Exception e) {
				e.printStackTrace();
				return 2;	
			}
		}
		@Override
		protected void onPostExecute(Integer result) {
			pd.dismiss();
			if(result == 0){
				
				Toast.makeText(InviteManagementActivity.this, message, Toast.LENGTH_SHORT).show();
			//	Log.e("IDTUYTYUfsdfsdsf", message);
				  lastWord = message.substring(message.lastIndexOf(" ")+1);
				adapterContactList.clearSelection();
				contactListNameValuePairs = new ArrayList<NameValuePair>(2);
				if(isNetworkAvailable()){
					inviteByCatData = null;
					btnAllInvite.setBackgroundResource(R.drawable.in_btn_3_sel);
					btnImportFromContact.setBackgroundResource(R.drawable.in_btn_2);
					btnImportFromFacebook.setBackgroundResource(R.drawable.in_btn_1);
					rlSearchContact.setVisibility(View.INVISIBLE);
					rlSearchInvite.setVisibility(View.VISIBLE);
					GetCategorywiseTask exe = new GetCategorywiseTask();
					exe.execute();
				}else{
					Toast.makeText(InviteManagementActivity.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
				}
			}else if(result == 1){
				Toast.makeText(InviteManagementActivity.this, message, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(InviteManagementActivity.this, "Contact not imported error occured.", Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(result);
		}

	}
}
