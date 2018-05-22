package com.lauruss.wedit;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CASectionedTask extends ArrayAdapter<TaskDataClass>{

	public LayoutInflater inflater;
	ArrayList<TaskDataClass> arrData;
	
	public CASectionedTask(Context context, int textViewResourceId,
			ArrayList<TaskDataClass> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arrData = objects;
	}
	public static class ViewHolder
	{	
		TextView txtViewTaskTitle;
		LinearLayout rowView;
		CheckBox cbsel;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_section_header, null);
			
			holder.txtViewTaskTitle = (TextView)convertView.findViewById(R.id.row_section_header_title);
			holder.rowView = (LinearLayout)convertView.findViewById(R.id.row_tasksection_view);
			 holder.cbsel=(CheckBox)convertView.findViewById(R.id.row_taskcheckbox);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		final TaskDataClass tData = arrData.get(position);
		holder.txtViewTaskTitle.setText(tData.getTaskName());
		
		if(tData.getItemType() == TaskDataClass.SECTION){
			convertView.setBackgroundResource(R.drawable.section_background);
			holder.txtViewTaskTitle.setTextColor(Color.WHITE);
			holder.cbsel.setVisibility(View.GONE);
			holder.rowView.setVisibility(View.GONE);
		}else{
			holder.cbsel.setVisibility(View.VISIBLE);
			holder.rowView.setVisibility(View.VISIBLE);
			convertView.setBackgroundColor(Color.TRANSPARENT);
			holder.txtViewTaskTitle.setTextColor(Color.BLACK);
			if(tData.getStatusColor() == TaskDataClass.FLAG_RED_COLOR){
				holder.rowView.setBackgroundColor(Color.RED);
			}else if(tData.getStatusColor() == TaskDataClass.FLAG_GREEN_COLOR){
				holder.rowView.setBackgroundColor(Color.GREEN);
			}else{
				holder.rowView.setBackgroundColor(Color.parseColor("#FFA500"));
			}
		}
		return convertView;
	}
	

}
