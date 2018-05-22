package com.lauruss.wedit;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CATaskListSingle extends ArrayAdapter<TaskDataClass>{

	public LayoutInflater inflater;
	ArrayList<TaskDataClass> arrData;
	public CATaskListSingle(Context context, int textViewResourceId,
			ArrayList<TaskDataClass> objects) {
		super(context, textViewResourceId, objects);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arrData = objects;
	}
	public static class ViewHolder
	{	
		TextView txtViewTaskTitle;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if(convertView==null)
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.row_task, null);
			
			holder.txtViewTaskTitle = (TextView)convertView.findViewById(R.id.row_task_tv_title_task);
			
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		final TaskDataClass tData = arrData.get(position);
		holder.txtViewTaskTitle.setText(tData.getTaskName());
		//holder.rowView.setBackgroundColor(Color.RED);
		
		return convertView;
	}
}
