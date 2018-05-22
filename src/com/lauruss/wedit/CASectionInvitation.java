package com.lauruss.wedit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import com.lauruss.wedit.CASectionedTask.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CASectionInvitation extends ArrayAdapter<InviteDataClass>{

	public LayoutInflater inflater;
	ArrayList<InviteDataClass> arrData;
	Activity aContex;
	AlertDialog.Builder alertDialog,alertDialog1;
	ArrayList<InviteDataClass> arrDataFilter;
	ProgressDialog pd;
	Dialog dialog;
	public CASectionInvitation(Activity context, int textViewResourceId,
			ArrayList<InviteDataClass> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arrData = objects;
		this.aContex = context;
		arrDataFilter = new ArrayList<InviteDataClass>();
		arrDataFilter.addAll(arrData);
		
	}
	public CASectionInvitation getThisAdapter(){
		return this;
	}
	public static class ViewHolder
	{	
		TextView txtViewInviteName;
		TextView txtViewNos;
		ImageView ivThumb;
		//Button btnTemp;
	}
	public ArrayList<InviteDataClass> getInviteData(){
		return this.arrData;
	}
	public void clearSelection(){
		this.arrDataFilter.clear();
		for(InviteDataClass cData : arrData){
			cData.setSelected(false);
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_invite_section, null);
			
			holder.txtViewInviteName = (TextView)convertView.findViewById(R.id.rowinvite_tv_title);
			holder.txtViewNos = (TextView)convertView.findViewById(R.id.rowinvite_tv_nos);
			holder.ivThumb = (ImageView)convertView.findViewById(R.id.rowinvite_iv_thumb);
			//holder.btnTemp = (Button)convertView.findViewById(R.id.rowinvite_btn_temp);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
			holder.ivThumb.setOnClickListener(null);
			
			//holder.btnTemp.setOnClickListener(null);
		}
		
		final InviteDataClass dClass = arrData.get(position);
		try {
			holder.txtViewInviteName.setText(dClass.getName()+" "+dClass.getFamilyName());
		} catch (Exception e) {
			e.printStackTrace();
			holder.txtViewInviteName.setText(dClass.getName());
		}
		
		if(dClass.getItemType() == TaskDataClass.SECTION){
			holder.ivThumb.setVisibility(View.INVISIBLE);
			holder.txtViewNos.setVisibility(View.INVISIBLE);
			convertView.setBackgroundResource(R.drawable.section_background);
			holder.txtViewInviteName.setTextColor(Color.WHITE);
		}else{
			convertView.setBackgroundColor(Color.WHITE);
			holder.txtViewInviteName.setTextColor(Color.BLACK);
			holder.ivThumb.setVisibility(View.VISIBLE);
			holder.txtViewNos.setVisibility(View.VISIBLE);
			
		}
		if(dClass.getInVitationSent()==true){
			holder.ivThumb.setImageResource(R.drawable.thumb_green);
			holder.txtViewNos.setText(dClass.getApprove()+"/"+dClass.getNoOfInvites());
		}
		
			else{
			holder.ivThumb.setImageResource(R.drawable.thumb_grey);
			holder.txtViewNos.setText(dClass.getApprove()+"/"+dClass.getNoOfInvites());
			//holder.txtViewNos.setText(String.valueOf(dClass.getNoOfInvites()));
		}
		/*if(dClass.getApprove() > 0){
			holder.ivThumb.setImageResource(R.drawable.thumb_green);
			holder.txtViewNos.setText(dClass.getApprove()+"/"+dClass.getNoOfInvites());
		}
		
			else{
			holder.ivThumb.setImageResource(R.drawable.thumb_grey);
			holder.txtViewNos.setText(String.valueOf(dClass.getNoOfInvites()));
		}*/
		holder.txtViewNos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog = new AlertDialog.Builder(aContex).setTitle(R.string.invite_thumb_dialog_title);
				
				final EditText etApprove = new EditText(aContex);
				etApprove.setGravity(Gravity.RIGHT);
			//	etApprove.setText(" ");
				etApprove.setSingleLine();
				etApprove.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
			//	etApprove.setInputType(InputType.TYPE_CLASS_NUMBER);
				etApprove.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
			//	etApprove.setText(String.valueOf(dClass.getApprove()));
				alertDialog.setView(etApprove);
				alertDialog.setPositiveButton(R.string.invite_thumb_dialog_update, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(!etApprove.getText().toString().trim().equalsIgnoreCase("")){
							if(Integer.parseInt(etApprove.getText().toString().trim()) > dClass.getNoOfInvites()
									){
								
								alertDialog1 = new AlertDialog.Builder(aContex).setTitle("מספר האורחים שהזנת גדול ממספר המוזמנים");
								alertDialog1.setPositiveButton("אישור", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										UpdateApprove exe = new UpdateApprove();
										exe.execute(new Integer[]{dClass.getInviteId(),Integer.parseInt(etApprove.getText().toString())});
										// TODO Auto-generated method stub
										//Toast.makeText(aContex, "מספר האורחים שהזנת גדול ממספר המוזמנים", Toast.LENGTH_SHORT).show();
									}
							});
								alertDialog1.show();
								/*dialog = new Dialog(getContext());	
								((Activity) dialog).requestWindowFeature(Window.FEATURE_NO_TITLE);
								((Activity) dialog).setContentView(R.layout.dialog_layout);*/
							//	Button cancel = (Button)convertView.findViewById(id)(R.id.btn_done);
							//	Toast.makeText(aContex, "מספר האורחים שהזנת גדול ממספר המוזמנים", Toast.LENGTH_SHORT).show();
							}
							/*else if(Integer.parseInt(etApprove.getText().toString().trim()) == 0)
							{
								UpdateApprove exe = new UpdateApprove();
								exe.execute(new Integer[]{dClass.getInviteId(),Integer.parseInt(etApprove.getText().toString())});
							}*/
								else{
								UpdateApprove exe = new UpdateApprove();
								exe.execute(new Integer[]{dClass.getInviteId(),Integer.parseInt(etApprove.getText().toString())});
							//	Toast.makeText(aContex, R.string.invite_thumb_dialog_error_msg, Toast.LENGTH_SHORT).show();
							}
							
						}else{
							Toast.makeText(aContex, "Please enter value.", Toast.LENGTH_SHORT).show();
						}
						
					}
				}).setNegativeButton(R.string.invite_thumb_dialog_cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				alertDialog.show();
				
			
			}
		});
		holder.ivThumb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog = new AlertDialog.Builder(aContex).setTitle(R.string.invite_thumb_dialog_title);
				final EditText etApprove = new EditText(aContex);
				etApprove.setGravity(Gravity.RIGHT);
				etApprove.setSingleLine();
			//	etApprove.setFilters(new InputFilter[] {new InputFilter.LengthFilter(3)});
				etApprove.setInputType(InputType.TYPE_CLASS_NUMBER);
				etApprove.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
			//	etApprove.setText(String.valueOf(dClass.getApprove()));
				alertDialog.setView(etApprove);
				alertDialog.setPositiveButton(R.string.invite_thumb_dialog_update, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if(!etApprove.getText().toString().trim().equalsIgnoreCase("")){
							if(Integer.parseInt(etApprove.getText().toString().trim()) > dClass.getNoOfInvites()
									){
								
								alertDialog1 = new AlertDialog.Builder(aContex).setTitle("מספר האורחים שהזנת גדול ממספר המוזמנים");
								alertDialog1.setPositiveButton("אישור", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										UpdateApprove exe = new UpdateApprove();
										exe.execute(new Integer[]{dClass.getInviteId(),Integer.parseInt(etApprove.getText().toString())});
										// TODO Auto-generated method stub
									//	Toast.makeText(aContex, "מספר האורחים שהזנת גדול ממספר המוזמנים", Toast.LENGTH_SHORT).show();
									}
							});
								alertDialog1.show();
								//Toast.makeText(aContex, "מספר האורחים שהזנת גדול ממספר המוזמנים", Toast.LENGTH_SHORT).show();
							}
							
							/*else if(Integer.parseInt(etApprove.getText().toString().trim()) == 0)
							{
								UpdateApprove exe = new UpdateApprove();
								exe.execute(new Integer[]{dClass.getInviteId(),Integer.parseInt(etApprove.getText().toString())});
							}*/
							else{
								UpdateApprove exe = new UpdateApprove();
								exe.execute(new Integer[]{dClass.getInviteId(),Integer.parseInt(etApprove.getText().toString())});
							//	Toast.makeText(aContex, R.string.invite_thumb_dialog_error_msg, Toast.LENGTH_SHORT).show();
							}
							
						}else{
							Toast.makeText(aContex, "Please enter value.", Toast.LENGTH_SHORT).show();
						}
						
					}
				}).setNegativeButton(R.string.invite_thumb_dialog_cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
					}
				});
				alertDialog.show();
				
			}
		});
		/*holder.ivThumb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});*/
		return convertView;
	}
	class UpdateApprove extends AsyncTask<Integer, Void, ArrayList<InviteDataClass>>{

		int taskIdThis;
		String message ="";
		@Override
		protected void onPreExecute() {
			//pd = ProgressDialog.show(aContex, "", "טוען נתונים");
			pd = ProgressDialog.show(aContex, "", "אנא המתן");
			super.onPreExecute();
		}
		@Override
		protected ArrayList<InviteDataClass> doInBackground(Integer... params) {
			try {
				List<NameValuePair> nameValueAddTaskPair = new ArrayList<NameValuePair>(2);
				nameValueAddTaskPair.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
				nameValueAddTaskPair.add(new BasicNameValuePair("invitedID", String.valueOf(params[0])));
				nameValueAddTaskPair.add(new BasicNameValuePair("reached", "1"));
				nameValueAddTaskPair.add(new BasicNameValuePair("approve", String.valueOf(params[1])));

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=invitedapprove");

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
				if(status){
					httpclient = new DefaultHttpClient();
					httppost = new HttpPost(MyApplication.BASE_API_URL+"weditusers?action=getinvited");
					httpParameters = httppost.getParams();

					HttpConnectionParams.setConnectionTimeout(httpParameters,
							timeoutConnection);

					HttpConnectionParams
					.setSoTimeout(httpParameters, timeoutSocket);

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("weddingID", String.valueOf(HomeActivity.weddingId)));
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					response = httpclient.execute(httppost);
					responseThis = EntityUtils.toString(response.getEntity()).trim();
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
							jObj = jArrTask.getJSONObject(j);
							InviteDataClass tObj = new InviteDataClass();
							tObj.setName(jObj.getString("name"));
							//tObj.setEmail(jObj.getString("email"));
							tObj.setInviteId(jObj.getInt("id"));
							tObj.setFamilyName(jObj.getString("fname"));
							tObj.setWeddingId(jObj.getInt("weddingid"));
							tObj.setNoOfInvites(jObj.getInt("numberofinvites"));
							tObj.setApprove(jObj.getInt("approve"));
							tObj.setInVitationSent(jObj.getBoolean("invitationsent"));

							tData.add(tObj);
						}

					}
					return tData;
				}else{
					message = jObj.getString("message");
				
					return null;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return null;	
			}

		}
		@Override
		protected void onPostExecute(ArrayList<InviteDataClass> result) {

			if(result != null){
				arrData.clear();
				arrData.addAll(result);
				getThisAdapter().notifyDataSetChanged();
			}else{
				if(message.trim().equalsIgnoreCase(""))
					Toast.makeText(aContex, "No internet or error occured", Toast.LENGTH_SHORT).show();
				else{
					Toast.makeText(aContex, message, Toast.LENGTH_SHORT).show();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}
	}
	
	public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrData.clear();
        if (charText.length() == 0) {
            arrData.addAll(arrDataFilter);
        } else {
            for (InviteDataClass cMDC : arrDataFilter) {
                if(cMDC.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                	arrData.add(cMDC);
                }
            }
        }
        notifyDataSetChanged();
    }
	/*public CASectionInvitation getThisAdapter1(){
		return this;
	}*/
}
