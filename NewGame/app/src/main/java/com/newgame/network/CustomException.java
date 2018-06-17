package com.newgame.network;
/**
 * Handles the exception
 * 
 * @author siva.jonnalagadda
 * 
 */
public class CustomException extends Exception {

	private String displayMsg;// error message to display

	public CustomException(String displayMesg) {
		super("CustomException: " + displayMesg);
		this.displayMsg = displayMesg;

	}

	public String getDisplayMsg() {
		return displayMsg;
	}

	public void setDisplayMsg(String displayMsg) {
		this.displayMsg = displayMsg;
	}
}
