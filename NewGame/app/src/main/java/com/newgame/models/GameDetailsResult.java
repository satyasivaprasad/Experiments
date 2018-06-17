package com.newgame.models;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameDetailsResult {

	@Expose
	private Integer GameID;
	@Expose
	private String Name;
	@Expose
	private String Note;
	@Expose
	private Boolean Active;
	

	public Boolean getActive() {
		return Active;
	}

	public void setActive(Boolean active) {
		Active = active;
	}

	/**
	 * 
	 * @return The GameID
	 */
	public Integer getGameID() {
		return GameID;
	}

	/**
	 * 
	 * @param GameID
	 *            The GameID
	 */
	public void setGameID(Integer GameID) {
		this.GameID = GameID;
	}

	/**
	 * 
	 * @return The Name
	 */
	public String getName() {
		return Name;
	}

	/**
	 * 
	 * @param Name
	 *            The Name
	 */
	public void setName(String Name) {
		this.Name = Name;
	}

	/**
	 * 
	 * @return The Note
	 */
	public String getNote() {
		return Note;
	}

	/**
	 * 
	 * @param Note
	 *            The Note
	 */
	public void setNote(String Note) {
		this.Note = Note;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}