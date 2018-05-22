package com.lauruss.wedit;

public class TaskDataClass {

	int taskId=0,weddingId,catId, daysToComplete,weditpretaskid;
	String taskName,endDate,startDate,responsible,comments,categoryName;
	boolean taskDone = false,isSelected = false,ischecked=false;
	public static final int ITEM = 0;
	public static final int SECTION = 1;
	
	public static int FLAG_RED_COLOR  = 1,FLAG_ORANGE_COLOR  = 2,FLAG_GREEN_COLOR  = 3;
	int statusColor = 0;
	
	public int type = 0;
	public TaskDataClass() {
		// TODO Auto-generated constructor stub
	}
	
	public void setTaskId(int taskId){
		this.taskId = taskId;
	}
	public void setWeddingId(int weddingId){
		this.weddingId = weddingId;
	}
	public void setWeditPreTaskId(int weditPreTaskId){
		this.weditpretaskid = weditPreTaskId;
	}
	public void setTaskName(String taskName){
		this.taskName = taskName;
	}
	public void setCategoryId(int catId){
		this.catId = catId;
	}
	public void setTaskEndDate(String taskEndDate){
		this.endDate = taskEndDate;
	}
	public void setTaskStartDate(String taskStartDate){
		this.startDate = taskStartDate;
	}
	public void setTaskResponsible(String taskResponsible){
		this.responsible = taskResponsible;
	}
	public void setTaskComments(String taskComments){
		this.comments = taskComments;
	}
	public void setSelected(boolean selected){
		this.isSelected = selected;
	}
	public void setDaysToComplete(int daysToComplete){
		this.daysToComplete = daysToComplete;
	}
	public void setTaskDone(boolean taskDone){
		this.taskDone = taskDone;
	}
	public void setCategoryName(String catName){
		this.categoryName = catName;
	}
	public void setType(int TYPE){
		this.type = TYPE;
	}
	public void setStatusColor(int status_color){
		this.statusColor = status_color;
	}
	
	public int getTaskId(){
		return this.taskId;
	}
	public int getWeddingId(){
		return this.weddingId;
	}
	public int getWeditPreTaskId(){
		return this.weditpretaskid;
	}
	public String getTaskName(){
		return this.taskName;
	}
	public String getTaskEndDate(){
		return this.endDate;
	}
	public String getTaskStartDate(){
		return this.startDate;
	}
	public String getTaskResponsible(){
		return this.responsible;
	}
	public String getTaskComments(){
		return this.comments;
	}
	public boolean getIsSelected(){
		return this.isSelected;
	}
	public int getCatId(){
		return this.catId;
	}
	public int getDaysToComplete(){
		return this.daysToComplete;
	}
	public boolean getTaskDone(){
		return this.taskDone;
	}
	public String getCategoryName(){
		return this.categoryName;
	}
	public int getItemType(){
		return this.type;
	}
	public int getStatusColor(){
		return this.statusColor;
	}
	public boolean isChecked()
	 {
	  return this.ischecked;
	 }
	 public void setChecked(boolean ischecked)
	 {
	  this.ischecked=ischecked;
	 }
}
