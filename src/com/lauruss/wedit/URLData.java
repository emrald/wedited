package com.lauruss.wedit;

public class URLData {
	int salt;
	String md5String;
	
	public URLData() {
		// TODO Auto-generated constructor stub
	}
	public void setSalt(int salt){
		this.salt = salt;
	}
	public void setMD5String(String str){
		this.md5String = str;
	}
	public int getSalt(){
		return this.salt;
	}
	public String getMD5String(){
		return this.md5String;
	}
	
	public URLData(int salt,String str) {
		this.salt = salt;
		this.md5String =str;
	}
}
