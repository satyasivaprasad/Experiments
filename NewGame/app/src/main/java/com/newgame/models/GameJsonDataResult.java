package com.newgame.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameJsonDataResult implements Serializable{

	@Expose
	private String BracketEndTime;
	@Expose
	private String BracketTime;
	@Expose
	private String CPanaTime;
	@Expose
	private String CloseEndTime;
	@Expose
	private String CloseTime;
	@Expose
	private String CpanaEndTime;
	@Expose
	private String GameID;
	@Expose
	private String Name;
	@Expose
	private String Note;
	@Expose
	private String OPanaTime;
	@Expose
	private String OpanaEndTime;
	@Expose
	private String OpenEndTime;
	@Expose
	private String OpenTime;

	/**
	 * 
	 * @return The BracketEndTime
	 */
	public String getBracketEndTime() {
		return BracketEndTime;
	}

	/**
	 * 
	 * @param BracketEndTime
	 *            The BracketEndTime
	 */
	public void setBracketEndTime(String BracketEndTime) {
		this.BracketEndTime = BracketEndTime;
	}

	/**
	 * 
	 * @return The BracketTime
	 */
	public String getBracketTime() {
		return BracketTime;
	}

	/**
	 * 
	 * @param BracketTime
	 *            The BracketTime
	 */
	public void setBracketTime(String BracketTime) {
		this.BracketTime = BracketTime;
	}

	/**
	 * 
	 * @return The CPanaTime
	 */
	public String getCPanaTime() {
		return CPanaTime;
	}

	/**
	 * 
	 * @param CPanaTime
	 *            The CPanaTime
	 */
	public void setCPanaTime(String CPanaTime) {
		this.CPanaTime = CPanaTime;
	}

	/**
	 * 
	 * @return The CloseEndTime
	 */
	public String getCloseEndTime() {
		return CloseEndTime;
	}

	/**
	 * 
	 * @param CloseEndTime
	 *            The CloseEndTime
	 */
	public void setCloseEndTime(String CloseEndTime) {
		this.CloseEndTime = CloseEndTime;
	}

	/**
	 * 
	 * @return The CloseTime
	 */
	public String getCloseTime() {
		return CloseTime;
	}

	/**
	 * 
	 * @param CloseTime
	 *            The CloseTime
	 */
	public void setCloseTime(String CloseTime) {
		this.CloseTime = CloseTime;
	}

	/**
	 * 
	 * @return The CpanaEndTime
	 */
	public String getCpanaEndTime() {
		return CpanaEndTime;
	}

	/**
	 * 
	 * @param CpanaEndTime
	 *            The CpanaEndTime
	 */
	public void setCpanaEndTime(String CpanaEndTime) {
		this.CpanaEndTime = CpanaEndTime;
	}

	/**
	 * 
	 * @return The GameID
	 */
	public String getGameID() {
		return GameID;
	}

	/**
	 * 
	 * @param GameID
	 *            The GameID
	 */
	public void setGameID(String GameID) {
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

	/**
	 * 
	 * @return The OPanaTime
	 */
	public String getOPanaTime() {
		return OPanaTime;
	}

	/**
	 * 
	 * @param OPanaTime
	 *            The OPanaTime
	 */
	public void setOPanaTime(String OPanaTime) {
		this.OPanaTime = OPanaTime;
	}

	/**
	 * 
	 * @return The OpanaEndTime
	 */
	public String getOpanaEndTime() {
		return OpanaEndTime;
	}

	/**
	 * 
	 * @param OpanaEndTime
	 *            The OpanaEndTime
	 */
	public void setOpanaEndTime(String OpanaEndTime) {
		this.OpanaEndTime = OpanaEndTime;
	}

	/**
	 * 
	 * @return The OpenEndTime
	 */
	public String getOpenEndTime() {
		return OpenEndTime;
	}

	/**
	 * 
	 * @param OpenEndTime
	 *            The OpenEndTime
	 */
	public void setOpenEndTime(String OpenEndTime) {
		this.OpenEndTime = OpenEndTime;
	}

	/**
	 * 
	 * @return The OpenTime
	 */
	public String getOpenTime() {
		return OpenTime;
	}

	/**
	 * 
	 * @param OpenTime
	 *            The OpenTime
	 */
	public void setOpenTime(String OpenTime) {
		this.OpenTime = OpenTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
