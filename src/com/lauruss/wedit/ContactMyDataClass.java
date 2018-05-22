package com.lauruss.wedit;

public class ContactMyDataClass {
	
	int contactId;
	String contactName,contactPhone,contactemail,contactfamilyname;
	boolean selected = false;
	
	public ContactMyDataClass() {
		// TODO Auto-generated constructor stub
	}
	public void setContactId(int contactId){
		this.contactId = contactId;
	}
	public void setContactName(String contactName){
		this.contactName = contactName;
	}
	public void setContactfamilyname(String contactfamilyname){
		this.contactfamilyname = contactfamilyname;
	}
	public void setContactPhone(String contactPhone){
		this.contactPhone = contactPhone;
	}
	public void setContactEmail(String contactemail){
		this.contactemail = contactemail;
	}
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	public int getContactId(){
		return this.contactId;
	}
	public String getContactName(){
		return this.contactName;
	}
	public String getContactfamilyname(){
		return this.contactfamilyname;
	}
	public String getContactPhone(){
		return this.contactPhone;
	}
	public String getContactEmail(){
		return this.contactemail;
	}
	public boolean isSelected(){
		return this.selected;
	}
	
	public ContactMyDataClass(int contactId,String contactName,String contactPhone,String contactemail){
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.contactemail = contactemail;
	}
	public ContactMyDataClass(int contactId,String contactName,String contactPhone,String contactemail,String contactfamilyname){
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.contactemail = contactemail;
		this.contactfamilyname = contactfamilyname;
	}
	public ContactMyDataClass(int contactId,String contactName){
		this.contactId = contactId;
		this.contactName = contactName;
	}

}
