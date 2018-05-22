package com.lauruss.wedit;

public class SettingDynamicData {

	int id = 0 , urlId = 0;
	String name = "", shortText = "", buttonName = "" , url_path = "";
	
	public void setId(int id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setShortText(String shortText){
		this.shortText = shortText;
	}
	public void setButtonName(String btnText){
		this.buttonName = btnText;
	}
	public void setURLId(int urlId){
		this.urlId = urlId;
	}
	public void setURLPath(String urlpath){
		this.url_path = urlpath;
	}
	
	public int getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
	public String getShortText(){
		return this.shortText;
	}
	public String getButtonName(){
		return this.buttonName;
	}
	public int getURLId(){
		return this.urlId;
	}
	public String getURLPath(){
		return this.url_path;
	}
}
