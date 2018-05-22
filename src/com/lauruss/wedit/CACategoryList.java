package com.lauruss.wedit;

import java.util.ArrayList;

import com.lauruss.wedit.CATaskListSingle.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CACategoryList extends ArrayAdapter<CategoryData>{
	
	public LayoutInflater inflater;
	static int catid;
	ArrayList<CategoryData> arrData;
	public CACategoryList(Context context, int textViewResourceId,
			ArrayList<CategoryData> objects) {
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
		final CategoryData cData = arrData.get(position);
		holder.txtViewTaskTitle.setText(cData.getCatName());
		catid=arrData.get(0).getCatId();
		return convertView;
	}

}
