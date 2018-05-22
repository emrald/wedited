package com.lauruss.wedit;

import java.io.Serializable;

public class InviteDataClass implements Serializable{

	int inviteId,weddingId,userCatId,numberOfInvites,approve,vagetarian,vegan,disabled,child,babies,actuallyReached,type = 0;
	String name = "", fName = "",email = "",address="",gift="",catName="", phone = "";
	boolean invitationSent,selected=false;
	
	
	public InviteDataClass() {
		// TODO Auto-generated constructor stub
	}
	
	public void setInviteId(int inviteId){
		this.inviteId = inviteId;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setFamilyName(String fName){
		this.fName = fName;
	}
	public void setWeddingId(int weddingId){
		this.weddingId = weddingId;
	}
	public void setUserCatId(int userCatId){
		this.userCatId = userCatId;
	}
	public void setNoOfInvites(int noOfInvites){
		this.numberOfInvites = noOfInvites;
	}
	public void setApprove(int approve){
		this.approve = approve;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public void setVegetarian(int vegetarian){
		this.vagetarian = vegetarian;
	}
	public void setVegan(int vegan){
		this.vegan = vegan;
	}
	public void setDisabled(int disabled){
		this.disabled = disabled;
	}
	public void setChild(int child){
		this.child = child;
	}
	public void setBabies(int babies){
		this.babies = babies;
	}
	public void setInVitationSent(boolean invitationSent){
		this.invitationSent = invitationSent;
	}
	public void setActuallyReached(int actuallyReached){
		this.actuallyReached = actuallyReached;
	}
	public void setGigt(String gift){
		this.gift = gift;
	}
	public void setWeditUserCatName(String catName){
		this.catName = catName;
	}
	public void setPhone(String phone){
		this.phone = phone;
	}
	public void setType(int TYPE){
		this.type = TYPE;
	}
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	public int getInviteId(){
		return this.inviteId;
	}
	public String getName(){
		return this.name;
	}
	public String getFamilyName(){
		return this.fName;
	}
	public int getWeddingId(){
		return this.weddingId;
	}
	public int getUserCatId(){
		return this.userCatId;
	}
	public int getNoOfInvites(){
		return this.numberOfInvites;
	}
	public int getApprove(){
		return this.approve;
	}
	public String getEmail(){
		return this.email ;
	}
	public String getAddress(){
		return this.address;
	}
	public int getVegetarian(){
		return this.vagetarian ;
	}
	public int getVegan(){
		return this.vegan;
	}
	public int getDisabled(){
		return this.disabled;
	}
	public int getChild(){
		return this.child;
	}
	public int getBabies(){
		return this.babies ;
	}
	public boolean getInVitationSent(){
		return this.invitationSent;
	}
	public int getActuallyReached(){
		return this.actuallyReached ;
	}
	public String getGigt(){
		return this.gift;
	}
	public String getWeditUserCatName(){
		return this.catName;
	}
	public String getPhone(){
		return this.phone;
	}
	public int getItemType(){
		return this.type;
	}
}

