package com.lauruss.wedit;

public class CategoryData {
	
	int catId;
	String catName;
	
	public CategoryData(){
		
	}
	
	public void setCatId(int catId){
		this.catId = catId;
	}
	public void setCatName(String catName){
		this.catName = catName;
	}
	public int getCatId(){
		return this.catId;
	}
	public String getCatName(){
		return this.catName;
	}
	
	public CategoryData(int catId, String catName){
		this.catId = catId;
		this.catName = catName;
	}

}
