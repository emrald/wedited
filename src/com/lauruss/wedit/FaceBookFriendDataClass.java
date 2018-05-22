package com.lauruss.wedit;

import com.facebook.model.GraphUser;

public class FaceBookFriendDataClass {

	private GraphUser fbUser;
	private boolean selected = false;
	
	public GraphUser getFbUser() {
		return fbUser;
	}
	
	public void setFbUser(GraphUser fbUser) {
		this.fbUser = fbUser;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public FaceBookFriendDataClass(GraphUser fbUser, boolean selected) {
		this.fbUser = fbUser;
		this.selected = selected;
	}
	
}
