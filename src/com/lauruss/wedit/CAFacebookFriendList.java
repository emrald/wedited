package com.lauruss.wedit;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CAFacebookFriendList extends ArrayAdapter<FaceBookFriendDataClass>{

	ArrayList<FaceBookFriendDataClass> arrData;
	ArrayList<FaceBookFriendDataClass> arrDataFilter;
	ArrayList<FaceBookFriendDataClass> selectedContact ;
	
	public LayoutInflater inflater;
	public CAFacebookFriendList(Context context, int textViewResourceId,
			ArrayList<FaceBookFriendDataClass> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		selectedContact = new ArrayList<FaceBookFriendDataClass>();
		this.arrData = objects;
		arrDataFilter = new ArrayList<FaceBookFriendDataClass>();
		arrDataFilter.addAll(arrData);
	}
	public static class ViewHolder
	{	
		TextView txtViewName;
		CheckBox chbSelected;
	}
	public ArrayList<FaceBookFriendDataClass> getSelectedContactData(){
		
		/*for(ContactMyDataClass ccon : arrData){
			if(ccon.isSelected()){
				selectedContact.add(ccon);
			}
		}*/
		return this.selectedContact;
	}
	public void clearSelection(){
		this.selectedContact.clear();
		for(FaceBookFriendDataClass cData : arrData){
			cData.setSelected(false);
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_contactlist, null);
			
			holder.txtViewName = (TextView)convertView.findViewById(R.id.rowcontact_tv_name);
			holder.chbSelected = (CheckBox)convertView.findViewById(R.id.rowcontact_chb);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		final FaceBookFriendDataClass cData = this.arrData.get(position);
		holder.txtViewName.setText(cData.getFbUser().getName());
		holder.txtViewName.setOnClickListener(new OnClickListener() {
			
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
				cData.setSelected(isChecked);
				if(isChecked){
					selectedContact.add(cData);
				}else{
					selectedContact.remove(cData);
				}
				
			}
		});
		
		holder.chbSelected.setChecked(cData.isSelected());
		return convertView;
	}
	public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrData.clear();
        if (charText.length() == 0) {
            arrData.addAll(arrDataFilter);
        } else {
            for (FaceBookFriendDataClass cMDC : arrDataFilter) {
                if(cMDC.getFbUser().getName().toLowerCase(Locale.getDefault()).contains(charText)){
                	arrData.add(cMDC);
                }
            }
        }
        notifyDataSetChanged();
    }

}
