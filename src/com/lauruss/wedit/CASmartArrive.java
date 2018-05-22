package com.lauruss.wedit;

import java.util.ArrayList;
import java.util.Locale;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CASmartArrive extends ArrayAdapter<InviteDataClass>{

	public LayoutInflater inflater;
	ArrayList<InviteDataClass> arrData;
	ArrayList<InviteDataClass> arrDataFilter;
	AlertDialog.Builder alertDialog;
	ProgressDialog pd;
	static ArrayList<InviteDataClass> selectedInviteList ;
	
	public CASmartArrive(Activity context, int textViewResourceId,
			ArrayList<InviteDataClass> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arrData = objects;
		selectedInviteList = new ArrayList<InviteDataClass>();
		
		arrDataFilter = new ArrayList<InviteDataClass>();
		arrDataFilter.addAll(arrData);
	}
	public CASmartArrive getThisAdapter(){
		return this;
	}
	public static class ViewHolder
	{	
		TextView txtViewInviteName;
		/*TextView txtViewNos;
		ImageView ivThumb;*/
		CheckBox chbSelected;
		//Button btnTemp;
	}
	public ArrayList<InviteDataClass> getSelectedInviteData(){
		
		
		return this.selectedInviteList;
	}
	public ArrayList<InviteDataClass> getInviteData(){
		return this.arrData;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_smartarrive, null);
			
			holder.txtViewInviteName = (TextView)convertView.findViewById(R.id.rowsmart_tv_title);
	//		holder.txtViewNos = (TextView)convertView.findViewById(R.id.rowsmart_tv_nos);
	//		holder.ivThumb = (ImageView)convertView.findViewById(R.id.rowsmart_iv_thumb);
			holder.chbSelected = (CheckBox)convertView.findViewById(R.id.rowsmart_chb);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		final InviteDataClass dClass = arrData.get(position);
		
		holder.txtViewInviteName.setText(dClass.getName()+" "+dClass.getFamilyName());
		
		
		if(dClass.getApprove() > 0){
	//		holder.ivThumb.setImageResource(R.drawable.thumb_green);
	//		holder.txtViewNos.setText(dClass.getApprove()+"/"+dClass.getNoOfInvites());
		}else{
		//	holder.ivThumb.setImageResource(R.drawable.thumb_grey);
		//	holder.txtViewNos.setText(String.valueOf(dClass.getNoOfInvites()));
		}
		
		dClass.setSelected(true);
		holder.txtViewInviteName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!holder.chbSelected.isChecked()){
				holder.chbSelected.setChecked(true);}
				else if(holder.chbSelected.isChecked())
				{
					holder.chbSelected.setChecked(false);
				}
			}
		});
		holder.chbSelected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				dClass.setSelected(isChecked);
				if(isChecked){
					selectedInviteList.add(dClass);
				}else{
					selectedInviteList.remove(dClass);
				}
			}

		});
		holder.chbSelected.setChecked(dClass.isSelected());
		return convertView;
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

}
